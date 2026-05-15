const admin = require("firebase-admin");
const serviceAccount = require("./service-account.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

async function updateExistingBadges() {
  console.log("Updating existing badges based on new thresholds (5 reviews, 1 follower)...");
  
  const usersSnap = await db.collection("usuarios").get();
  let updatedCount = 0;

  for (const userDoc of usersSnap.docs) {
    const userData = userDoc.data();
    const userId = userDoc.id;
    const updates = {};

    // Check Influencer (> 0 followers)
    if (!userData.isInfluencer && (userData.followersCount || 0) >= 1) {
      updates.isInfluencer = true;
    }

    // Check Top Reviewer (> 4 reviews)
    if (!userData.isTopReviewer) {
      const reviewsSnap = await db.collection("reviews").where("usuarioId", "==", userId).get();
      if (reviewsSnap.size >= 5) {
        updates.isTopReviewer = true;
      }
    }

    if (Object.keys(updates).length > 0) {
      console.log(`Updating user ${userData.nombre} (${userId}):`, updates);
      await userDoc.ref.update(updates);
      updatedCount++;
    }
  }

  console.log(`Finished. Updated ${updatedCount} users.`);
}

updateExistingBadges().then(() => process.exit(0));
