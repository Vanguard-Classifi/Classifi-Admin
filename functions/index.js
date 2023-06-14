/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const {onRequest} = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const {initializeApp} = require("firebase-admin/app");
const {getFirestore} = require("firebase-admin/firestore");

initializeApp();

exports.addmessage = onRequest(async (request, response) => {
    const original  = request.query.text; 
    const writeResult = await getFirestore()
        .collection("messages")
        .add({original: original});
    response.json({result: `Message with ID: ${writeResult.id} added successfully!!!. And the value of the message is ${original}`});
});

exports.makeuppercase = onDocumentCreated("/messages/{documentId}", (event) => {
    const original  = event.data.data().original; 

    logger.log("Uppercasing,", event.params.documentId, original);
    const uppercase = original.toUpperCase();

    return event.data.ref.set({uppercase}, {merge:true});
})

