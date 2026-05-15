const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const { onDocumentUpdated } = require("firebase-functions/v2/firestore");
const { setGlobalOptions } = require("firebase-functions/v2");
const admin = require("firebase-admin");

admin.initializeApp();

// Establecer región por defecto para las functions
setGlobalOptions({ region: "us-central1" });

/**
 * Función 1: Notificación Push al dar Like.
 * Se dispara cuando se crea un documento en la subcolección de likes de un review.
 */
exports.sendLikeNotification = onDocumentCreated("reviews/{reviewId}/likes/{userId}", async (event) => {
    const reviewId = event.params.reviewId;
    const likerUserId = event.params.userId;

    console.log(`Usuario ${likerUserId} le dio like al review ${reviewId}`);

    // 1. Obtener los datos del review para saber quién es el dueño
    const reviewSnap = await admin.firestore().collection("reviews").doc(reviewId).get();
    if (!reviewSnap.exists) {
        console.log("El review ya no existe.");
        return null;
    }
    
    const reviewData = reviewSnap.data();
    const ownerId = reviewData.usuarioId;
    const reviewTitle = reviewData.articuloNombre || "un artículo";

    // Evitar notificación si el usuario le da like a su propia reseña
    if (ownerId === likerUserId) {
        console.log("El usuario le dio like a su propia reseña, no se envía notificación.");
        return null;
    }

    // 2. Obtener los datos de quien dio like
    const likerSnap = await admin.firestore().collection("usuarios").doc(likerUserId).get();
    const likerName = likerSnap.exists ? likerSnap.data().nombre : "Alguien";
    const likerAvatar = likerSnap.exists ? (likerSnap.data().avatarUrl || "") : "";

    // 3. Guardar notificación en Firestore (para la pantalla de notificaciones)
    await admin.firestore()
        .collection("notifications")
        .doc(ownerId)
        .collection("items")
        .add({
            type: "like",
            userName: likerName,
            action: `le dio like a tu reseña en ${reviewTitle}`,
            time: "Ahora",
            avatarUrl: likerAvatar,
            createdAt: Date.now()
        });

    // 4. Obtener FCM token del dueño para push notification
    const ownerSnap = await admin.firestore().collection("usuarios").doc(ownerId).get();
    if (!ownerSnap.exists) {
        console.log("El dueño del review no existe.");
        return null;
    }

    const ownerData = ownerSnap.data();
    const fcmToken = ownerData.fcmToken;

    if (!fcmToken) {
        console.log("El dueño del review no tiene un fcmToken registrado.");
        return null;
    }

    // 5. Enviar push notification
    const message = {
        notification: {
            title: "¡Nuevo Like! 👍",
            body: `${likerName} le dio like a tu reseña en ${reviewTitle}.`
        },
        token: fcmToken
    };

    try {
        const response = await admin.messaging().send(message);
        console.log("Notificación enviada exitosamente:", response);
    } catch (error) {
        console.error("Error enviando la notificación:", error);
    }

    return null;
});

/**
 * Función 2: Integridad de datos (Adicional 0.3).
 * Actualiza el campo "usuarioNombre" en todas las reseñas del usuario si este cambia su nombre en el perfil.
 */
exports.updateUserNameInReviews = onDocumentUpdated("usuarios/{userId}", async (event) => {
    const userId = event.params.userId;
    const beforeData = event.data.before.data();
    const afterData = event.data.after.data();

    // Comprobar si el nombre realmente cambió
    if (beforeData.nombre === afterData.nombre) {
        return null;
    }

    const newName = afterData.nombre;
    console.log(`El usuario ${userId} cambió su nombre a ${newName}. Actualizando reviews...`);

    const db = admin.firestore();
    const reviewsRef = db.collection("reviews");
    
    try {
        // Consultar todas las reseñas escritas por este usuario
        const snapshot = await reviewsRef.where("usuarioId", "==", userId).get();

        if (snapshot.empty) {
            console.log("No se encontraron reseñas para actualizar.");
            return null;
        }

        // Usar un batch para actualizar todas las reseñas de forma atómica (límite de 500 ops por batch)
        let batch = db.batch();
        let count = 0;

        snapshot.docs.forEach((doc) => {
            batch.update(doc.ref, { usuarioNombre: newName });
            count++;

            // Si llegamos a 490 (límite es 500), comiteamos y creamos un batch nuevo
            if (count === 490) {
                batch.commit();
                batch = db.batch();
                count = 0;
            }
        });

        // Comitear las operaciones restantes
        if (count > 0) {
            await batch.commit();
        }

        console.log(`Se actualizaron exitosamente ${snapshot.size} reseñas.`);
    } catch (error) {
        console.error("Error actualizando las reseñas del usuario:", error);
    }

    return null;
});

/**
 * Función 3: Notificación Push al recibir un Follow.
 * Se dispara cuando se crea un documento en la subcolección followers de un usuario.
 */
exports.sendFollowNotification = onDocumentCreated("usuarios/{targetUserId}/followers/{followerUserId}", async (event) => {
    const targetUserId = event.params.targetUserId;
    const followerUserId = event.params.followerUserId;

    console.log(`Usuario ${followerUserId} empezó a seguir a ${targetUserId}`);

    // Evitar auto-follow (por si acaso)
    if (targetUserId === followerUserId) return null;

    // 1. Obtener los datos de quien empezó a seguir
    const followerSnap = await admin.firestore().collection("usuarios").doc(followerUserId).get();
    const followerName = followerSnap.exists ? followerSnap.data().nombre : "Alguien";
    const followerAvatar = followerSnap.exists ? (followerSnap.data().avatarUrl || "") : "";

    // 2. Guardar notificación en Firestore (para la pantalla de notificaciones dentro de la app)
    await admin.firestore()
        .collection("notifications")
        .doc(targetUserId)
        .collection("items")
        .add({
            type: "follow",
            userName: followerName,
            action: `empezó a seguirte`,
            time: "Ahora",
            avatarUrl: followerAvatar,
            createdAt: Date.now()
        });

    // 3. Obtener FCM token del targetUser para push notification
    const targetSnap = await admin.firestore().collection("usuarios").doc(targetUserId).get();
    if (!targetSnap.exists) {
        console.log("El usuario target no existe.");
        return null;
    }

    const targetData = targetSnap.data();
    const fcmToken = targetData.fcmToken;

    if (!fcmToken) {
        console.log("El usuario target no tiene un fcmToken registrado.");
        return null;
    }

    // 4. Enviar push notification tipo WhatsApp
    const message = {
        notification: {
            title: "¡Nuevo Seguidor! 👤",
            body: `${followerName} empezó a seguirte.`
        },
        token: fcmToken
    };

    try {
        const response = await admin.messaging().send(message);
        console.log("Notificación de follow enviada exitosamente:", response);
    } catch (error) {
        console.error("Error enviando la notificación de follow:", error);
    }

    return null;
});

/**
 * Función 4: Gamificación - Top Reseñador
 * Se dispara al crear una reseña. Cuenta las reseñas y si >= 10, asigna isTopReviewer = true.
 */
exports.checkTopReviewerBadge = onDocumentCreated("reviews/{reviewId}", async (event) => {
    const reviewData = event.data.data();
    if (!reviewData || !reviewData.usuarioId) return null;

    const userId = reviewData.usuarioId;
    const db = admin.firestore();

    try {
        // Verificar si ya tiene el badge
        const userRef = db.collection("usuarios").doc(userId);
        const userSnap = await userRef.get();
        if (!userSnap.exists) return null;
        
        const userData = userSnap.data();
        if (userData.isTopReviewer) {
            // Ya es top reviewer, no hacemos nada extra
            return null;
        }

        // Contar reseñas
        const snapshot = await db.collection("reviews").where("usuarioId", "==", userId).get();
        if (snapshot.size >= 5) {
            console.log(`Otorgando insignia Top Reseñador a ${userId} (${snapshot.size} reseñas)`);
            await userRef.update({ isTopReviewer: true });
        }
    } catch (error) {
        console.error("Error comprobando badge de top reviewer:", error);
    }

    return null;
});

/**
 * Función 5: Gamificación - Influencer
 * Revisa cuando un usuario se actualiza, si followersCount >= 100, asigna isInfluencer = true.
 */
exports.checkInfluencerBadge = onDocumentUpdated("usuarios/{userId}", async (event) => {
    const afterData = event.data.after.data();
    const userId = event.params.userId;

    if (!afterData) return null;

    // Si ya lo tiene, ignorar
    if (afterData.isInfluencer) return null;

    if (afterData.followersCount >= 1) {
        console.log(`Otorgando insignia Influencer a ${userId} (${afterData.followersCount} seguidores)`);
        await admin.firestore().collection("usuarios").doc(userId).update({ isInfluencer: true });
    }

    return null;
});
