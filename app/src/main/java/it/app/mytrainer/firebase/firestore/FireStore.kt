package it.app.mytrainer.firebase.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.app.mytrainer.firebase.storage.Storage
import it.app.mytrainer.models.*

class FireStore {

    private val db = Firebase.firestore
    private val TAG = "FIRESTORE"
    private val COLLECTIONATHLETE = "Athletes"
    private val COLLECTIONTRAINER = "Trainers"
    private val COLLECTIONEXERCISES = "Exercises"

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

    fun createListExerciseIta() {
        val listExercises = arrayListOf(
            ObjSearchExercise("Petto", "Cavi alti"),
            ObjSearchExercise("Petto", "Cavi bassi"),
            ObjSearchExercise("Petto", "Croci panca piana"),
            ObjSearchExercise("Petto", "Croci cavi panca piana"),
            ObjSearchExercise("Petto", "Croci inclinata 45"),
            ObjSearchExercise("Petto", "Distensioni panca piana"),
            ObjSearchExercise("Petto", "Distensioni inclinata 45"),
            ObjSearchExercise("Petto", "Panca piana"),
            ObjSearchExercise("Petto", "Chest press"),
            ObjSearchExercise("Petto", "Cheste press inclinata"),
            ObjSearchExercise("Petto", "Panca inclinata"),
            ObjSearchExercise("Petto", "Pectoral machine"),
            ObjSearchExercise("Petto", "Croci TRX"),
            ObjSearchExercise("Petto", "Flessioni"),
            ObjSearchExercise("Petto", "Dips (Petto)"),
            ObjSearchExercise("Petto", "Pullover"),
            ObjSearchExercise("Petto", "Dips anelli (Petto)"),
            ObjSearchExercise("Petto", "Flessioni diamond (Petto)"),
            ObjSearchExercise("Petto", "Panca declinata manubri"),
            ObjSearchExercise("Petto", "Panca declinata bilanciere"),
            ObjSearchExercise("Petto", ""),
            ObjSearchExercise("Petto", ""),

            ObjSearchExercise("Gambe", "Squat"),
            ObjSearchExercise("Gambe", "Leg extensions"),
            ObjSearchExercise("Gambe", "Affondi corpo libero"),
            ObjSearchExercise("Gambe", "Affondi maubri"),
            ObjSearchExercise("Gambe", "Affondi bilanciere"),
            ObjSearchExercise("Gambe", "Pistol squat"),
            ObjSearchExercise("Gambe", "Step up libero (Gambe)"),
            ObjSearchExercise("Gambe", "Step up manubri (Gambe)"),
            ObjSearchExercise("Gambe", "Box jump"),
            ObjSearchExercise("Gambe", "Pressa"),
            ObjSearchExercise("Gambe", "Pressa inclinata 45"),
            ObjSearchExercise("Gambe", "Pressa 90"),
            ObjSearchExercise("Gambe", "Pressa verticale"),
            ObjSearchExercise("Gambe", "Hack squat"),
            ObjSearchExercise("Gambe", "Pistol squat kettlebell"),
            ObjSearchExercise("Gambe", "Hypetrust"),
            ObjSearchExercise("Gambe", "Sumo squat"),
            ObjSearchExercise("Gambe", "Stacco (Gambe)"),
            ObjSearchExercise("Gambe", "Adduttori machine"),
            ObjSearchExercise("Gambe", "Affondo inverso"),
            ObjSearchExercise("Gambe", "Adduttori elastico"),
            ObjSearchExercise("Gambe", "Squat frontale"),
            ObjSearchExercise("Gambe", "Jump squat (Gambe)"),
            ObjSearchExercise("Gambe", ""),
            ObjSearchExercise("Gambe", ""),
            ObjSearchExercise("Gambe", ""),

            ObjSearchExercise("Bicipiti", "Curl manubri"),
            ObjSearchExercise("Bicipiti", "Curl bilanciere"),
            ObjSearchExercise("Bicipiti", "Hammer manubri"),
            ObjSearchExercise("Bicipiti", "Curl concentrato"),
            ObjSearchExercise("Bicipiti", "Curl cavi"),
            ObjSearchExercise("Bicipiti", "Curl cavi corda"),
            ObjSearchExercise("Bicipiti", "Curl elastici"),
            ObjSearchExercise("Bicipiti", "Hammer elastici"),
            ObjSearchExercise("Bicipiti", "Trazioni (Bicipiti)"),
            ObjSearchExercise("Bicipiti", "Spider curl"),
            ObjSearchExercise("Bicipiti", "Curl panca 45"),
            ObjSearchExercise("Bicipiti", "Zottman Curl"),
            ObjSearchExercise("Bicipiti", "Curl bilanciere ez"),
            ObjSearchExercise("Bicipiti", "Curl TRX"),
            ObjSearchExercise("Bicipiti", ""),
            ObjSearchExercise("Bicipiti", ""),
            ObjSearchExercise("Bicipiti", ""),

            ObjSearchExercise("Dorso", "Stacchi regular"),
            ObjSearchExercise("Dorso", "Stacchi sumo"),
            ObjSearchExercise("Dorso", "Stacchi rumeni"),
            ObjSearchExercise("Dorso", "Lat machine"),
            ObjSearchExercise("Dorso", "Lat machine T-Bar"),
            ObjSearchExercise("Dorso", "Rematore manubri"),
            ObjSearchExercise("Dorso", "Rematore cavi"),
            ObjSearchExercise("Dorso", "Trazioni (Dorso)"),
            ObjSearchExercise("Dorso", "Rematore bilanciere"),
            ObjSearchExercise("Dorso", "Rematore su panca"),
            ObjSearchExercise("Dorso", "Rematore bilanciere presa stretta"),
            ObjSearchExercise("Dorso", "Push-down corda"),
            ObjSearchExercise("Dorso", "Butterflies (Dorso)"),
            ObjSearchExercise("Dorso", "Rematore manubri singolo"),
            ObjSearchExercise("Dorso", "Rematore manubri invers"),
            ObjSearchExercise("Dorso", "Rematore bilanciere presa stretta"),
            ObjSearchExercise("Dorso", "Rematore renegade"),
            ObjSearchExercise("Dorso", "Rematore TRX"),
            ObjSearchExercise("Dorso", "Hyperextension"),
            ObjSearchExercise("Dorso", "Rematore T-Bar"),

            ObjSearchExercise("Tricipiti", "Panca piana presa stretta"),
            ObjSearchExercise("Tricipiti", "Panca inclinata presa stretta"),
            ObjSearchExercise("Tricipiti", "French press bilanciere"),
            ObjSearchExercise("Tricipiti", "Tricipiti cavi"),
            ObjSearchExercise("Tricipiti", "French press cavi"),
            ObjSearchExercise("Tricipiti", "Tricipiti cavi con corda"),
            ObjSearchExercise("Tricipiti", "Tricipiti TRX"),
            ObjSearchExercise("Tricipiti", "Dips (Tricipiti)"),
            ObjSearchExercise("Tricipiti", "Flessioni diamonds"),
            ObjSearchExercise("Tricipiti", "Tricipiti alla panca"),
            ObjSearchExercise("Tricipiti", "Press manubri"),
            ObjSearchExercise("Tricipiti", "Flessioni tiger (Tricipiti)"),
            ObjSearchExercise("Tricipiti", "French press manubri"),
            ObjSearchExercise("Tricipiti", "Flessioni diamond rialzo (Tricipiti)"),
            ObjSearchExercise("Tricipiti", ""),

            ObjSearchExercise("Spalle", "Alzate laterali"),
            ObjSearchExercise("Spalle", "Alzate frontali"),
            ObjSearchExercise("Spalle", "Spinte manubri"),
            ObjSearchExercise("Spalle", "Arnold con manubri"),
            ObjSearchExercise("Spalle", "Tirate al mento manubri"),
            ObjSearchExercise("Spalle", "Tirate al mento bilanciere"),
            ObjSearchExercise("Spalle", "Alzate laterali cavi"),
            ObjSearchExercise("Spalle", "Alzate frontali cavi"),
            ObjSearchExercise("Spalle", "Military press bilanciere"),
            ObjSearchExercise("Spalle", "Alzate frontali disco"),
            ObjSearchExercise("Spalle", "Butterfly (Spalle)"),
            ObjSearchExercise("Spalle", "Alzate frontali elastici"),
            ObjSearchExercise("Spalle", "Alzate laterali elastici"),
            ObjSearchExercise("Spalle", "Pressa spalle macchinario"),
            ObjSearchExercise("Spalle", ""),
            ObjSearchExercise("Spalle", ""),

            ObjSearchExercise("Polpacci", "Polpacci su disco"),
            ObjSearchExercise("Polpacci", "Calf machine"),
            ObjSearchExercise("Polpacci", "Camminata punta di piedi"),
            ObjSearchExercise("Polpacci", "Jump squat (Polpacci)"),
            ObjSearchExercise("Polpacci", "Alzate in punta di pidedi bilanciere"),
            ObjSearchExercise("Polpacci", "Alzate in punta di pidedi manubri"),
            ObjSearchExercise("Polpacci", ""),
            ObjSearchExercise("Polpacci", ""),

            ObjSearchExercise("Addominali", "Crunch"),
            ObjSearchExercise("Addominali", "Bicicletta (Addominali)"),
            ObjSearchExercise("Addominali", "Addominali laterali"),
            ObjSearchExercise("Addominali", "Plank"),
            ObjSearchExercise("Addominali", "Plank laterale"),
            ObjSearchExercise("Addominali", "Plank con salto"),
            ObjSearchExercise("Addominali", "Sit-up"),
            ObjSearchExercise("Addominali", "Super-man plank"),
            ObjSearchExercise("Addominali", "Plank con rotazione bacino"),
            ObjSearchExercise("Addominali", "Mountain-climber"),

            ObjSearchExercise("Cardio", "Tapis-roulant"),
            ObjSearchExercise("Cardio", "Cyclette"),
            ObjSearchExercise("Cardio", "Corda"),
            ObjSearchExercise("Cardio", "Ellittica"),
            ObjSearchExercise("Cardio", "Step-up (Cardio)"),
            ObjSearchExercise("Cardio", "Corsa"),
            ObjSearchExercise("Cardio", "Jogging"),
            ObjSearchExercise("Cardio", "Spinning"),
            ObjSearchExercise("Cardio", "Vogatore"),
            ObjSearchExercise("Cardio", "Marcia"),
            ObjSearchExercise("Cardio", "Skip"),
            ObjSearchExercise("Cardio", "Corsa calciata"),
            ObjSearchExercise("Cardio", ""),
            ObjSearchExercise("Cardio", ""),
            ObjSearchExercise("Cardio", ""),
            ObjSearchExercise("Cardio", "")
        )

        db.collection(COLLECTIONEXERCISES).document("ITA")
            .update("ListaEsercizi", listExercises)
            .addOnSuccessListener {
                Log.d(TAG, "Storing document exercises: success")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Storing document exercises: failed", e)
            }

    }

    fun createListExerciseEng() {
        val listExercises = arrayListOf(
            ObjSearchExercise("Chest", "High cables"),
            ObjSearchExercise("Chest", "Bass cables"),
            ObjSearchExercise("Chest", "Flat bench crosses"),
            ObjSearchExercise("Chest", "Cables flat bench crosses"),
            ObjSearchExercise("Chest", "Inclined bench crosses 45"),
            ObjSearchExercise("Chest", "Flat bench press"),
            ObjSearchExercise("Chest", "Inclined bench press 45"),
            ObjSearchExercise("Chest", "Flat bench press barbell"),
            ObjSearchExercise("Chest", "Chest press"),
            ObjSearchExercise("Chest", "Inclined chest press"),
            ObjSearchExercise("Chest", "Inclined bench press barbell"),
            ObjSearchExercise("Chest", "Pectoral machine"),
            ObjSearchExercise("Chest", "TRX crosses"),
            ObjSearchExercise("Chest", "Push-ups"),
            ObjSearchExercise("Chest", "Dips (Chest)"),
            ObjSearchExercise("Chest", "Pullover"),
            ObjSearchExercise("Chest", "Dips rings (Chest)"),
            ObjSearchExercise("Chest", "Diamonds push-ups (Chest)"),
            ObjSearchExercise("Chest", "Barbell decline bench"),
            ObjSearchExercise("Chest", "Dumbbells decline bench"),
            ObjSearchExercise("Chest", ""),
            ObjSearchExercise("Chest", ""),

            ObjSearchExercise("Legs", "Squat"),
            ObjSearchExercise("Legs", "Leg extensions"),
            ObjSearchExercise("Legs", "Bodyweight lunges"),
            ObjSearchExercise("Legs", "Barbell lunges"),
            ObjSearchExercise("Legs", "Dumbbells lunges"),
            ObjSearchExercise("Legs", "Pistol squat"),
            ObjSearchExercise("Legs", "Step up bodyweight (Legs)"),
            ObjSearchExercise("Legs", "Step up dumbbells (Legs)"),
            ObjSearchExercise("Legs", "Box jump"),
            ObjSearchExercise("Legs", "Press"),
            ObjSearchExercise("Legs", "Inclined press 45"),
            ObjSearchExercise("Legs", "90 press"),
            ObjSearchExercise("Legs", "Vertical press"),
            ObjSearchExercise("Legs", "Hack squat"),
            ObjSearchExercise("Legs", "Pistol squat kettlebell"),
            ObjSearchExercise("Legs", "Hypetrust"),
            ObjSearchExercise("Legs", "Sumo squat"),
            ObjSearchExercise("Legs", "Dead-lift (Legs)"),
            ObjSearchExercise("Legs", "Abductors machine"),
            ObjSearchExercise("Legs", "Reverse lunge"),
            ObjSearchExercise("Legs", "Abductors elastic"),
            ObjSearchExercise("Legs", "Frontal squat"),
            ObjSearchExercise("Legs", "Jump squat (Legs)"),
            ObjSearchExercise("Legs", ""),
            ObjSearchExercise("Legs", ""),
            ObjSearchExercise("Legs", ""),

            ObjSearchExercise("Biceps", "Curl barbell"),
            ObjSearchExercise("Biceps", "Curl dumbbells"),
            ObjSearchExercise("Biceps", "Hammer dumbbells"),
            ObjSearchExercise("Biceps", "Concentrated curl"),
            ObjSearchExercise("Biceps", "Cables curl"),
            ObjSearchExercise("Biceps", "Cables rope curl"),
            ObjSearchExercise("Biceps", "Curl elastic"),
            ObjSearchExercise("Biceps", "Hammer elastic"),
            ObjSearchExercise("Biceps", "Pull-ups (Biceps)"),
            ObjSearchExercise("Biceps", "Spider curl"),
            ObjSearchExercise("Biceps", "Curl bench 45"),
            ObjSearchExercise("Biceps", "Zottman Curl"),
            ObjSearchExercise("Biceps", "Curl ez barbell"),
            ObjSearchExercise("Biceps", "Curl TRX"),
            ObjSearchExercise("Biceps", ""),
            ObjSearchExercise("Biceps", ""),
            ObjSearchExercise("Biceps", ""),

            ObjSearchExercise("Back", "Regular dead-lift"),
            ObjSearchExercise("Back", "Sumo dead-lift"),
            ObjSearchExercise("Back", "Romanian dead-lift"),
            ObjSearchExercise("Back", "Lat machine"),
            ObjSearchExercise("Back", "Lat machine T-Bar"),
            ObjSearchExercise("Back", "Dumbbells row"),
            ObjSearchExercise("Back", "Cable row"),
            ObjSearchExercise("Back", "Pull-ups (Back)"),
            ObjSearchExercise("Back", "Barbell row"),
            ObjSearchExercise("Back", "Bench row"),
            ObjSearchExercise("Back", "Close grip barbell rowing machine"),
            ObjSearchExercise("Back", "Push-down rope"),
            ObjSearchExercise("Back", "Butterflies (Back)"),
            ObjSearchExercise("Back", "Single dumbbell row"),
            ObjSearchExercise("Back", "Inverted dumbbells rowing"),
            ObjSearchExercise("Back", "Close grip barbell rowing machine"),
            ObjSearchExercise("Back", "Renegade oarsman"),
            ObjSearchExercise("Back", "TRX oarsman"),
            ObjSearchExercise("Back", "Hyperextension"),
            ObjSearchExercise("Back", "T-Bar oarsman"),

            ObjSearchExercise("Triceps", "Close grip flat bench"),
            ObjSearchExercise("Triceps", "Close grip incline bench"),
            ObjSearchExercise("Triceps", "French press barbell"),
            ObjSearchExercise("Triceps", "Rope triceps"),
            ObjSearchExercise("Triceps", "French press cables"),
            ObjSearchExercise("Triceps", "Cable triceps with rope"),
            ObjSearchExercise("Triceps", "TRX triceps"),
            ObjSearchExercise("Triceps", "Dips (Triceps)"),
            ObjSearchExercise("Triceps", "Diamonds push-ups"),
            ObjSearchExercise("Triceps", "Bench press triceps"),
            ObjSearchExercise("Triceps", "Press dumbbells"),
            ObjSearchExercise("Triceps", "Tiger push-ups (Triceps)"),
            ObjSearchExercise("Triceps", "French press dumbbells"),
            ObjSearchExercise("Triceps", "Diamond Raise Push-ups (Triceps)"),
            ObjSearchExercise("Triceps", ""),

            ObjSearchExercise("Shoulders", "Lateral raises"),
            ObjSearchExercise("Shoulders", "Front raises"),
            ObjSearchExercise("Shoulders", "Dumbbell thrusts"),
            ObjSearchExercise("Shoulders", "Arnold"),
            ObjSearchExercise("Shoulders", "Chin pull-up dumbbell"),
            ObjSearchExercise("Shoulders", "Chin pull-up barbell"),
            ObjSearchExercise("Shoulders", "Lateral cable raises"),
            ObjSearchExercise("Shoulders", "Cables front raises"),
            ObjSearchExercise("Shoulders", "Military press barbell"),
            ObjSearchExercise("Shoulders", "Front disc raises"),
            ObjSearchExercise("Shoulders", "Butterfly (Shoulders)"),
            ObjSearchExercise("Shoulders", "Elastic front lifts"),
            ObjSearchExercise("Shoulders", "Elastic side raises"),
            ObjSearchExercise("Shoulders", "Machine shoulders press"),
            ObjSearchExercise("Shoulders", ""),
            ObjSearchExercise("Shoulders", ""),

            ObjSearchExercise("Calves", "Calves on disc"),
            ObjSearchExercise("Calves", "Calf machine"),
            ObjSearchExercise("Calves", "Tiptoe walking"),
            ObjSearchExercise("Calves", "Jump squat (Calves)"),
            ObjSearchExercise("Calves", "Tiptoe up the barbell"),
            ObjSearchExercise("Calves", "Raise toe of dumbbells"),
            ObjSearchExercise("Calves", ""),
            ObjSearchExercise("Calves", ""),

            ObjSearchExercise("Abdominals", "Crunch"),
            ObjSearchExercise("Abdominals", "Bicycle (Abs)"),
            ObjSearchExercise("Abdominals", "Lateral abs"),
            ObjSearchExercise("Abdominals", "Plank"),
            ObjSearchExercise("Abdominals", "Side plank"),
            ObjSearchExercise("Abdominals", "Jump plank"),
            ObjSearchExercise("Abdominals", "Sit-up"),
            ObjSearchExercise("Abdominals", "Super-man plank"),
            ObjSearchExercise("Abdominals", "Plank with pelvis rotation"),
            ObjSearchExercise("Abdominals", "Mountain-climber"),

            ObjSearchExercise("Cardio", "Tapis-Roulant"),
            ObjSearchExercise("Cardio", "Cyclette"),
            ObjSearchExercise("Cardio", "Rope"),
            ObjSearchExercise("Cardio", "Elliptical"),
            ObjSearchExercise("Cardio", "Step-up (Cardio)"),
            ObjSearchExercise("Cardio", "Running"),
            ObjSearchExercise("Cardio", "Jogging"),
            ObjSearchExercise("Cardio", "Spinning"),
            ObjSearchExercise("Cardio", "Rowing machine"),
            ObjSearchExercise("Cardio", "March"),
            ObjSearchExercise("Cardio", "Skip"),
            ObjSearchExercise("Cardio", "Run kicked"),
            ObjSearchExercise("Cardio", ""),
            ObjSearchExercise("Cardio", ""),
            ObjSearchExercise("Cardio", ""),
            ObjSearchExercise("Cardio", "")
        )

        db.collection(COLLECTIONEXERCISES).document("ENG")
            .update("ListExercises", listExercises)
            .addOnSuccessListener {
                Log.d(TAG, "Storing document exercises: success")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Storing document exercises: failed", e)
            }
    }

    @Suppress("UNCHECKED_CAST")
    fun downloadAvailableExerciseEng(callback: (ArrayList<ObjSearchExercise>) -> Unit) {
        db.collection(COLLECTIONEXERCISES).document("ENG").get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "Downloading document exercises: success")
                val doc = document.data?.get("ListExercises") as ArrayList<HashMap<String, String>>
                Log.d(TAG, "$doc")
                val listExercise = ArrayList<ObjSearchExercise>()

                doc.forEach { exercise ->
                    exercise.forEach { (key, value /*value -> muscle,name*/) ->
                        Log.d("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", "$key     $value")
                        if(key == "muscle"){
                            listExercise.add(ObjSearchExercise(value, null))
                        }else{
                            listExercise[listExercise.size-1].nameExercise = value
                        }
                        callback(listExercise)
                    }
                }

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Downloading document exercises: failed", e)
            }
    }

    @Suppress("UNCHECKED_CAST")
    fun downloadAvailableExerciseIta(callback: (ArrayList<ObjSearchExercise>) -> Unit) {
        db.collection(COLLECTIONEXERCISES).document("ITA").get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "Downloading document exercises: success")
                val doc = document.data?.get("ListaEsercizi") as ArrayList<HashMap<String, String>>
                val listExercise = ArrayList<ObjSearchExercise>()

                doc.forEach { exercise ->
                    exercise.forEach { (key, value /*value -> muscle,name*/) ->
                        if(key == "muscle"){
                            listExercise.add(ObjSearchExercise(value, null))
                        }else{
                            listExercise[listExercise.size-1].nameExercise = value
                        }
                        callback(listExercise)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Downloading document exercises: failed", e)
            }
    }
}