require("dotenv").config();
const admin = require("firebase-admin");
const { faker } = require("@faker-js/faker");

// Inicializar la app usando credenciales por defecto (ADC o emulador si FIRESTORE_EMULATOR_HOST está configurado)
admin.initializeApp({
  projectId: process.env.GCLOUD_PROJECT || "demo-condorapp",
});

const db = admin.firestore();

async function seedDatabase() {
  console.log("Iniciando la generación de datos falsos...");
  console.log(
    "Modo:",
    process.env.FIRESTORE_EMULATOR_HOST ? "Emulador Local" : "Producción (Real)"
  );

  try {
    const usuariosRef = db.collection("usuarios");
    const articulosRef = db.collection("articulos");
    
    let batch = db.batch();
    let batchCount = 0;
    
    // 1. Generar Usuarios
    const numUsuarios = faker.number.int({ min: 10, max: 20 });
    const userIds = [];
    console.log(`Generando ${numUsuarios} usuarios...`);

    for (let i = 0; i < numUsuarios; i++) {
      const userId = faker.string.uuid();
      userIds.push(userId);
      const userDoc = usuariosRef.doc(userId);
      
      const userData = {
        id: userId,
        nombre: faker.person.fullName(),
        correo: faker.internet.email(),
        avatarUrl: faker.image.avatar(),
        fcmToken: faker.string.alphanumeric(32),
        fechaCreacion: faker.date.past().getTime(),
      };
      
      batch.set(userDoc, userData);
      batchCount++;
      if (batchCount === 490) {
        await batch.commit();
        batch = db.batch();
        batchCount = 0;
      }
    }

    // 2. Generar Artículos (asociados a esos usuarios)
    const numArticulos = faker.number.int({ min: 20, max: 30 });
    console.log(`Generando ${numArticulos} artículos...`);

    for (let i = 0; i < numArticulos; i++) {
      const articuloId = faker.string.uuid();
      const articuloDoc = articulosRef.doc(articuloId);
      
      // Elegir un usuario aleatorio como autor
      const autorId = faker.helpers.arrayElement(userIds);

      const articuloData = {
        id: articuloId,
        titulo: faker.lorem.sentence(5),
        contenido: faker.lorem.paragraphs(3),
        imagenUrl: faker.image.urlLoremFlickr({ category: 'technology' }),
        autorId: autorId,
        fechaPublicacion: faker.date.recent().getTime(),
        likes: faker.number.int({ min: 0, max: 100 })
      };
      
      batch.set(articuloDoc, articuloData);
      batchCount++;
      if (batchCount === 490) {
        await batch.commit();
        batch = db.batch();
        batchCount = 0;
      }
    }

    // Comitear las operaciones restantes
    if (batchCount > 0) {
      await batch.commit();
    }

    console.log("¡Carga de datos falsos completada exitosamente!");
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
