package it.app.mytrainer.firebase.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.app.mytrainer.models.Athlete
import it.app.mytrainer.models.Trainer

class FireStore {
    private val TAG = "FIRESTORE [DEBUG]"
    private val db = Firebase.firestore
    private val COLLECTIONATHLETE = "athletes"
    private val COLLECTIONTRAINER = "trainers"

    fun saveAthlete(documentId: String, callback: (Boolean) -> Unit) {
        db.collection(COLLECTIONATHLETE).document(documentId)
            .set(Athlete.getHashMap())
            .addOnSuccessListener {
                Log.d(TAG, "The storing of the document has done")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "The storing of the document has failed", e)
                callback(false)
            }
    }

    fun saveTrainer(documentId: String, callback: (Boolean) -> Unit) {
        db.collection(COLLECTIONTRAINER).document(documentId)
            .set(Trainer.getHashMap())
            .addOnSuccessListener {
                Log.d(TAG, "The storing of the document has done")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "The storing of the document has failed", e)
                callback(false)
            }
    }

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
}