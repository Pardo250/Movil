const admin = require("firebase-admin");
const serviceAccount = require("./service-account.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

async function checkBadges() {
  const usersSnap = await db.collection("usuarios").get();
  console.log(`Checking ${usersSnap.size} users...`);
  
  usersSnap.forEach(doc => {
    const data = doc.data();
    if (data.isTopReviewer || data.isInfluencer) {
      console.log(`User ${data.nombre} (${doc.id}): isTopReviewer=${data.isTopReviewer}, isInfluencer=${data.isInfluencer}, followersCount=${data.followersCount}`);
    }
  });

  const reviewsSnap = await db.collection("reviews").get();
  console.log(`Total reviews in DB: ${reviewsSnap.size}`);
}

checkBadges().then(() => process.exit(0));
