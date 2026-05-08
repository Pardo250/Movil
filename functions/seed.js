require("dotenv").config();
const admin = require("firebase-admin");
const { faker } = require("@faker-js/faker");

// Inicializar la app usando credenciales por defecto (ADC o emulador si FIRESTORE_EMULATOR_HOST está configurado)
admin.initializeApp({
  projectId: process.env.GCLOUD_PROJECT || "demo-condorapp",
});

const db = admin.firestore();

/**
 * Genera datos falsos para las 3 entidades principales de CondorApp:
 *   - Usuarios (colección "usuarios")
 *   - Artículos (colección "articulos")
 *   - Reviews  (colección "reviews")
 *
 * Puede ejecutarse contra el emulador local:
 *   FIRESTORE_EMULATOR_HOST=localhost:8080 node seed.js
 *
 * O contra Firebase real (producción):
 *   GCLOUD_PROJECT=tu-project-id node seed.js
 */
async function seedDatabase() {
  console.log("Iniciando la generación de datos falsos...");
  console.log(
    "Modo:",
    process.env.FIRESTORE_EMULATOR_HOST ? "Emulador Local" : "Producción (Real)"
  );

  try {
    const usuariosRef = db.collection("usuarios");
    const articulosRef = db.collection("articulos");
    const reviewsRef = db.collection("reviews");

    let batch = db.batch();
    let batchCount = 0;

    const commitIfNeeded = async () => {
      batchCount++;
      if (batchCount >= 490) {
        await batch.commit();
        batch = db.batch();
        batchCount = 0;
      }
    };

    // ─── 1. Generar Usuarios ───────────────────────────────────
    const numUsuarios = faker.number.int({ min: 10, max: 20 });
    const userIds = [];
    const userNames = [];
    console.log(`Generando ${numUsuarios} usuarios...`);

    const tipos = ["Lugar", "Museo", "Playa", "Montaña", "Parque", "Ciudad", "Restaurante"];

    for (let i = 0; i < numUsuarios; i++) {
      const userId = faker.string.uuid();
      const nombre = faker.person.fullName();
      userIds.push(userId);
      userNames.push(nombre);
      const userDoc = usuariosRef.doc(userId);

      const userData = {
        id: userId,
        nombre: nombre,
        email: faker.internet.email(),
        username: "@" + faker.internet.userName().toLowerCase(),
        bio: faker.lorem.sentence(),
        avatarUrl: faker.image.avatar(),
        followersCount: 0,
        followingCount: 0,
        fcmToken: faker.string.alphanumeric(32),
        savedArticles: [],
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };

      batch.set(userDoc, userData);
      await commitIfNeeded();
    }

    // ─── 2. Generar Artículos ──────────────────────────────────
    const numArticulos = faker.number.int({ min: 15, max: 25 });
    const articuloIds = [];
    console.log(`Generando ${numArticulos} artículos...`);

    for (let i = 0; i < numArticulos; i++) {
      const articuloId = faker.string.uuid();
      articuloIds.push(articuloId);
      const articuloDoc = articulosRef.doc(articuloId);

      const articuloData = {
        id: articuloId,
        titulo: faker.lorem.sentence(4),
        descripcion: faker.lorem.paragraphs(2),
        tipo: faker.helpers.arrayElement(tipos),
        imagenUrl: faker.image.urlLoremFlickr({ category: "nature" }),
      };

      batch.set(articuloDoc, articuloData);
      await commitIfNeeded();
    }

    // ─── 3. Generar Reviews ────────────────────────────────────
    const numReviews = faker.number.int({ min: 20, max: 40 });
    console.log(`Generando ${numReviews} reviews...`);

    for (let i = 0; i < numReviews; i++) {
      const reviewDoc = reviewsRef.doc();
      const userIndex = faker.number.int({ min: 0, max: userIds.length - 1 });
      const articuloIndex = faker.number.int({ min: 0, max: articuloIds.length - 1 });

      const reviewData = {
        contenido: faker.lorem.sentences(2),
        calificacion: faker.number.int({ min: 1, max: 5 }),
        usuarioId: userIds[userIndex],
        articuloId: articuloIds[articuloIndex],
        usuarioNombre: userNames[userIndex],
        articuloTitulo: "",
        likesCount: faker.number.int({ min: 0, max: 20 }),
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };

      batch.set(reviewDoc, reviewData);
      await commitIfNeeded();
    }

    // ─── 4. Generar relaciones de Follow ───────────────────────
    console.log("Generando relaciones de follow...");
    for (let i = 0; i < userIds.length; i++) {
      const numFollows = faker.number.int({ min: 1, max: Math.min(5, userIds.length - 1) });
      const targets = faker.helpers
        .shuffle(userIds.filter((id) => id !== userIds[i]))
        .slice(0, numFollows);

      for (const targetId of targets) {
        const followingRef = usuariosRef.doc(userIds[i]).collection("following").doc(targetId);
        const followerRef = usuariosRef.doc(targetId).collection("followers").doc(userIds[i]);
        batch.set(followingRef, { timestamp: admin.firestore.FieldValue.serverTimestamp() });
        batch.set(followerRef, { timestamp: admin.firestore.FieldValue.serverTimestamp() });
        await commitIfNeeded();

        // Actualizar contadores
        batch.update(usuariosRef.doc(userIds[i]), {
          followingCount: admin.firestore.FieldValue.increment(1),
        });
        batch.update(usuariosRef.doc(targetId), {
          followersCount: admin.firestore.FieldValue.increment(1),
        });
        await commitIfNeeded();
      }
    }

    // Comitear las operaciones restantes
    if (batchCount > 0) {
      await batch.commit();
    }

    console.log("¡Carga de datos falsos completada exitosamente!");
    console.log(`  → ${numUsuarios} usuarios`);
    console.log(`  → ${numArticulos} artículos`);
    console.log(`  → ${numReviews} reviews`);
    console.log(`  → Relaciones de follow generadas`);
  } catch (error) {
    console.error("Error al generar datos:", error);
  }
}

seedDatabase()
  .then(() => process.exit(0))
  .catch((err) => {
    console.error(err);
    process.exit(1);
  });
