package it.app.mytrainer.firebase.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.app.mytrainer.models.Athlete
import it.app.mytrainer.models.Trainer

class FireStore {

    private val TAG = "FIRESTORE"
    private val db = Firebase.firestore
    private val COLLECTIONATHLETE = "Athletes"
    private val COLLECTIONTRAINER = "Trainers"

    //UTILITIES
    fun findType(currentUserId: String, callback: (Int) -> Unit) {
        db.collection(COLLECTIONATHLETE).document(currentUserId).get()
            .addOnSuccessListener { documentAthlete ->
                if (documentAthlete.data != null) {
                    //Find the user athlete
                    Log.d(TAG, "Find the user, it's an athlete: ${documentAthlete.data}")
                    callback(1)
                } else {
                    db.collection(COLLECTIONTRAINER).document(currentUserId).get()
                        .addOnSuccessListener { documentTrainer ->
                            if (documentTrainer.data != null) {
                                //Find the user athlete
                                Log.d(TAG, "Find the user, it's a trainer: ${documentTrainer.data}")
                                callback(0)
                            } else {
                                //User not found, doesn't match to auth
                                Log.d(TAG, "User not found")
                                callback(-1)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Failed connection", e)
                            callback(-1)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Failed connection", e)
                callback(-1)
            }
    }

    //FUN FOR THE PROFILE OF THE TRAINER
    fun saveTrainer(documentId: String, callback: (Boolean) -> Unit) {
        db.collection(COLLECTIONTRAINER).document(documentId).set(Trainer.getHashMap())
            .addOnSuccessListener {
                Log.d(TAG, "The storing of the document has done")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "The storing of the document has failed", e)
                callback(false)
            }
    }

    fun getTrainer(currentUserId: String, callback: (Map<String, Any>?) -> Unit) {
        db.collection(COLLECTIONTRAINER).document(currentUserId).get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "DocumentSnapshot trainer data: ${document.data}")
                callback(document.data)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Get trainer failed with ", e)
            }
    }

    fun updateTrainer(currentUserId: String, gym: String, specialization: String) {
        db.collection(COLLECTIONTRAINER).document(currentUserId).update("Gym", gym)
        db.collection(COLLECTIONTRAINER).document(currentUserId)
            .update("Specialization", specialization)
    }

    fun setSchedule(userId: String, schedule: HashMap<String, HashMap<String, ArrayList<String>>>) {
        db.collection(COLLECTIONATHLETE).document(userId).update("Schedule", schedule)

    }

    //FUN FOR THE OPTION IN THE MENU
    fun deleteTrainer(currentUserId: String) {
        db.collection(COLLECTIONTRAINER).document(currentUserId).delete().addOnSuccessListener {
            Log.d(TAG, "Trainer delete: success")
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "Trainer delete: fail", e)
            }
    }

    //FUN FOR THE PROFILE OF THE ATHLETE
    fun saveAthlete(documentId: String, callback: (Boolean) -> Unit) {
        db.collection(COLLECTIONATHLETE).document(documentId).set(Athlete.getHashMap())
            .addOnSuccessListener {
                Log.d(TAG, "The storing of the document: success")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "The storing of the document: failed", e)
                callback(false)
            }
    }

    fun getAthlete(currentUserId: String, callback: (Map<String, Any>?) -> Unit) {
        db.collection(COLLECTIONATHLETE).document(currentUserId).get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "Get athlete: susses\nDocumentSnapshot athlete: ${document.data}")
                callback(document.data)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Get athlete: failed", e)
            }
    }

    fun updateAthlete(
        currentUserId: String,
        height: String,
        weight: String,
        typeOfWo: String,
        goal: String,
        level: String,
        numOfWO: String,
        listCheckBox: ArrayList<String>,
    ) {
        db.collection(COLLECTIONATHLETE).document(currentUserId).update("Height", height)
        db.collection(COLLECTIONATHLETE).document(currentUserId).update("Weight", weight)
        db.collection(COLLECTIONATHLETE).document(currentUserId).update("TypeOfWO", typeOfWo)
        db.collection(COLLECTIONATHLETE).document(currentUserId).update("Goal", goal)
        db.collection(COLLECTIONATHLETE).document(currentUserId).update("Level", level)
        db.collection(COLLECTIONATHLETE).document(currentUserId).update("DaysOfWorkout", numOfWO)
        db.collection(COLLECTIONATHLETE).document(currentUserId).update("Equipment", listCheckBox)
    }

    //FUN FOR THE OPTION IN THE MENU
    fun deleteAthlete(currentUserId: String) {
        db.collection(COLLECTIONATHLETE).document(currentUserId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Athlete delete: success")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Athlete delete: fail", e)
            }
    }
}