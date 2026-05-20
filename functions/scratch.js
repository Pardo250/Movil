const { Firestore } = require("@google-cloud/firestore");

async function check(projectId) {
  const db = new Firestore({
    projectId,
    host: "localhost:8080",
    ssl: false
  });
  const snapshot = await db.collection("usuarios").get();
  console.log(`Project ${projectId}: ${snapshot.size} usuarios`);
}

check("condorapp-b9bbd").catch(console.error);
check("demo-condorapp").catch(console.error);
