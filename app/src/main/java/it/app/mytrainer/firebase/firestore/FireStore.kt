package it.app.mytrainer.firebase.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.app.mytrainer.firebase.storage.Storage
import it.app.mytrainer.models.MapAthlete
import it.app.mytrainer.models.MapTrainer
import it.app.mytrainer.models.ObjAthlete
import it.app.mytrainer.models.ObjSchedule

class FireStore {

    private val db = Firebase.firestore
    private val TAG = "FIRESTORE"
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
        db.collection(COLLECTIONTRAINER).document(documentId).set(MapTrainer.getHashMap())
            .addOnSuccessListener {
                Log.d(TAG, "Storing document: success")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Storing document: failed", e)
                callback(false)
            }
    }

    fun getTrainer(currentUserId: String, callback: (Map<String, Any>?) -> Unit) {
        db.collection(COLLECTIONTRAINER).document(currentUserId).get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "Get trainer: success -> data: ${document.data}")
                callback(document.data)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Get trainer: failed", e)
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

    fun schedule(userId: String, schedule: ObjSchedule) {
        db.collection(COLLECTIONATHLETE).document(userId).update("Schedule", schedule)
    }

    //FUN FOR THE OPTION IN THE MENU
    fun deleteTrainer(currentUserId: String) {
        db.collection(COLLECTIONTRAINER).document(currentUserId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Trainer delete: success")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Trainer delete: failed", e)
            }
    }

    //FUN FOR THE PROFILE OF THE ATHLETE
    fun saveAthlete(documentId: String, callback: (Boolean) -> Unit) {
        db.collection(COLLECTIONATHLETE).document(documentId).set(MapAthlete.getHashMap())
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
                Log.d(TAG, "Get athlete: success -> data: ${document.data}")
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
                Log.w(TAG, "Athlete delete: failed", e)
            }
    }

    // To fill the recycleView in the home of the trainer
    @Suppress("UNCHECKED_CAST")
    fun getAllAthlete(callback: (ArrayList<ObjAthlete>, Boolean) -> Unit) {
        val listAthlete = ArrayList<ObjAthlete>()
        var athleteId: String

        db.collection(COLLECTIONATHLETE).get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "Getting documents: success")
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val doc = document.data
                    athleteId = doc["AthleteId"].toString()
                    Storage.getPhotoUrl(athleteId) { uri ->

                        var url = ""
                        if (uri != null) {
                            url = uri.toString()
                        }

                        listAthlete.add(ObjAthlete(
                            doc["Name"].toString(),
                            doc["Surname"].toString(),
                            doc["BirthDate"].toString(),
                            doc["Height"].toString(),
                            doc["Weight"].toString(),
                            doc["TypeOfWO"].toString(),
                            doc["Goal"].toString(),
                            doc["Level"].toString(),
                            doc["DaysOfWorkout"].toString(),
                            doc["Equipment"] as ArrayList<String>,
                            athleteId,
                            url))

                        callback(listAthlete, true)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Getting documents: failed", e)
                callback(listAthlete, false)
            }
    }
}