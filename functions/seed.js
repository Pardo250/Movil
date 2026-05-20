const path = require("path");
require("dotenv").config({ path: path.join(__dirname, ".env.seed") });
const admin = require("firebase-admin");
const { faker } = require("@faker-js/faker");
const { Firestore } = require("@google-cloud/firestore");

const emulatorHost = process.env.FIRESTORE_EMULATOR_HOST || "localhost:8080";
const modeArg = process.argv.includes("--both") ? "both" : (process.env.FIRESTORE_EMULATOR_HOST ? "emulator" : "real");

// Eliminar la variable global para poder instanciar clientes de producción explícitamente sin que apunten al emulador
delete process.env.FIRESTORE_EMULATOR_HOST;

// Inicializar la app usando credenciales por defecto
admin.initializeApp({
  projectId: process.env.GCLOUD_PROJECT || "demo-condorapp",
});

const databases = [];

if (modeArg === "both") {
  databases.push({ name: "Producción (Real)", db: admin.firestore() });
  databases.push({ name: "Emulador Local", db: new Firestore({
    projectId: process.env.GCLOUD_PROJECT || "demo-condorapp",
    host: emulatorHost,
    ssl: false
  }) });
} else if (modeArg === "emulator") {
  databases.push({ name: "Emulador Local", db: new Firestore({
    projectId: process.env.GCLOUD_PROJECT || "demo-condorapp",
    host: emulatorHost,
    ssl: false
  }) });
} else {
  databases.push({ name: "Producción (Real)", db: admin.firestore() });
}

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
 *
 * O contra ambos simultáneamente:
 *   node seed.js --both
 */
async function seedDatabase() {
  console.log("Iniciando la generación de datos falsos...");
  console.log(
    "Modo:",
    modeArg === "both" ? "Ambos (Emulador Local y Producción Real)" : (modeArg === "emulator" ? "Emulador Local" : "Producción (Real)")
  );

  try {
    const dbs = databases.map(d => ({
        db: d.db,
        name: d.name,
        usuariosRef: d.db.collection("usuarios"),
        articulosRef: d.db.collection("articulos"),
        reviewsRef: d.db.collection("reviews"),
        batch: d.db.batch(),
        batchCount: 0
    }));

    const commitIfNeeded = async () => {
      for (const d of dbs) {
        d.batchCount++;
        if (d.batchCount >= 490) {
          await d.batch.commit();
          d.batch = d.db.batch();
          d.batchCount = 0;
        }
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

      for (const d of dbs) {
        const userDoc = d.usuariosRef.doc(userId);
        d.batch.set(userDoc, userData);
      }
      await commitIfNeeded();
    }

    // ─── 2. Generar Artículos ──────────────────────────────────
    const numArticulos = faker.number.int({ min: 15, max: 25 });
    const articuloIds = [];
    console.log(`Generando ${numArticulos} artículos...`);

    for (let i = 0; i < numArticulos; i++) {
      const articuloId = faker.string.uuid();
      articuloIds.push(articuloId);

      const articuloData = {
        id: articuloId,
        titulo: faker.lorem.sentence(4),
        descripcion: faker.lorem.paragraphs(2),
        tipo: faker.helpers.arrayElement(tipos),
        imagenUrl: faker.image.urlLoremFlickr({ category: "nature" }),
      };

      for (const d of dbs) {
        const articuloDoc = d.articulosRef.doc(articuloId);
        d.batch.set(articuloDoc, articuloData);
      }
      await commitIfNeeded();
    }

    // ─── 3. Generar Reviews ────────────────────────────────────
    const numReviews = faker.number.int({ min: 20, max: 40 });
    console.log(`Generando ${numReviews} reviews...`);

    for (let i = 0; i < numReviews; i++) {
      const reviewId = faker.string.uuid();
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

      for (const d of dbs) {
        const reviewDoc = d.reviewsRef.doc(reviewId);
        d.batch.set(reviewDoc, reviewData);
      }
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
        for (const d of dbs) {
          const followingRef = d.usuariosRef.doc(userIds[i]).collection("following").doc(targetId);
          const followerRef = d.usuariosRef.doc(targetId).collection("followers").doc(userIds[i]);
          d.batch.set(followingRef, { timestamp: admin.firestore.FieldValue.serverTimestamp() });
          d.batch.set(followerRef, { timestamp: admin.firestore.FieldValue.serverTimestamp() });
        }
        await commitIfNeeded();

        // Actualizar contadores
        for (const d of dbs) {
          d.batch.update(d.usuariosRef.doc(userIds[i]), {
            followingCount: admin.firestore.FieldValue.increment(1),
          });
          d.batch.update(d.usuariosRef.doc(targetId), {
            followersCount: admin.firestore.FieldValue.increment(1),
          });
        }
        await commitIfNeeded();
      }
    }

    // Comitear las operaciones restantes
    for (const d of dbs) {
      if (d.batchCount > 0) {
        await d.batch.commit();
      }
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
