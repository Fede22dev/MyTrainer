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

    @Suppress("UNCHECKED_CAST")
    fun updateSchedule(athleteId: String, trainerId: String, dayOfWo: ObjDayOfWo) {
        getAthlete(athleteId) { athlete ->
            val schedule = athlete?.get("Schedule")

            if (schedule != "") {
                schedule as HashMap<String, ArrayList<Any>>
                schedule["listOfDays"]?.add(dayOfWo)

                db.collection(COLLECTIONATHLETE).document(athleteId).update("Schedule", schedule)

            } else {

                val firstWrite = ObjSchedule(ArrayList())
                firstWrite.listOfDays.add(dayOfWo)

                db.collection(COLLECTIONATHLETE).document(athleteId).update("Schedule", firstWrite)
                db.collection(COLLECTIONATHLETE).document(athleteId).update("TrainerId", trainerId)
            }
        }
    }

    fun checkIdTrainer(athleteId: String, trainerId: String, callback: (Boolean) -> Unit) {
        getAthlete(athleteId) { athlete ->
            val schedule = athlete?.get("Schedule")

            if (schedule != "") {
                val trainerIdFireStore = athlete?.get("TrainerId").toString()

                if (trainerIdFireStore == trainerId) {
                    callback(true)
                } else {
                    callback(false)
                }
            } else {
                callback(true)
            }
        }
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

    @Suppress("UNCHECKED_CAST")
    fun getNameDayScheduleAthlete(athleteId: String, callback: (ArrayList<String>) -> Unit) {
        getAthlete(athleteId) { athlete ->
            val schedule = athlete?.get("Schedule")

            val listOfDays = ArrayList<String>()

            if (schedule != "") {
                schedule as HashMap<String, ArrayList<HashMap<String, Any>>>
                schedule["listOfDays"]?.forEach { hashMapDay ->
                    listOfDays.add(hashMapDay["nameOfDay"].toString())
                    callback(listOfDays)
                }
            } else {
                listOfDays.add("")
                callback(listOfDays)
            }
        }
    }

    fun createListExerciseIta() {
        val listExercises = arrayListOf(
            ObjSearchExercise("Petto", "cavi alti"),
            ObjSearchExercise("Petto", "cavi bassi"),
            ObjSearchExercise("Petto", "croci panca piana"),
            ObjSearchExercise("Petto", "croci cavi panca piana"),
            ObjSearchExercise("Petto", "croci inclinata 45"),
            ObjSearchExercise("Petto", "distensioni panca piana"),
            ObjSearchExercise("Petto", "distensioni inclinata 45"),
            ObjSearchExercise("Petto", "panca piana"),
            ObjSearchExercise("Petto", "chest press"),
            ObjSearchExercise("Petto", "cheste press inclinata"),
            ObjSearchExercise("Petto", "panca inclinata"),
            ObjSearchExercise("Petto", "pectoral machine"),
            ObjSearchExercise("Petto", "croci TRX"),
            ObjSearchExercise("Petto", "flessioni"),
            ObjSearchExercise("Petto", "dips (Petto)"),
            ObjSearchExercise("Petto", "pullover"),
            ObjSearchExercise("Petto", "dips anelli (Petto)"),
            ObjSearchExercise("Petto", "flessioni diamond (Petto)"),
            ObjSearchExercise("Petto", "panca declinata manubri"),
            ObjSearchExercise("Petto", "panca declinata bilanciere"),
            ObjSearchExercise("Petto", ""),
            ObjSearchExercise("Petto", ""),

            ObjSearchExercise("Gambe", "squat"),
            ObjSearchExercise("Gambe", "leg extensions"),
            ObjSearchExercise("Gambe", "affondi corpo libero"),
            ObjSearchExercise("Gambe", "affondi maubri"),
            ObjSearchExercise("Gambe", "affondi bilanciere"),
            ObjSearchExercise("Gambe", "pistol squat"),
            ObjSearchExercise("Gambe", "step up libero (Gambe)"),
            ObjSearchExercise("Gambe", "step up manubri (Gambe)"),
            ObjSearchExercise("Gambe", "box jump"),
            ObjSearchExercise("Gambe", "pressa"),
            ObjSearchExercise("Gambe", "pressa inclinata 45"),
            ObjSearchExercise("Gambe", "pressa 90"),
            ObjSearchExercise("Gambe", "pressa verticale"),
            ObjSearchExercise("Gambe", "hack squat"),
            ObjSearchExercise("Gambe", "pistol squat kettlebell"),
            ObjSearchExercise("Gambe", "hypetrust"),
            ObjSearchExercise("Gambe", "sumo squat"),
            ObjSearchExercise("Gambe", "stacco (Gambe)"),
            ObjSearchExercise("Gambe", "adduttori machine"),
            ObjSearchExercise("Gambe", "affondo inverso"),
            ObjSearchExercise("Gambe", "adduttori elastico"),
            ObjSearchExercise("Gambe", "squat frontale"),
            ObjSearchExercise("Gambe", "jump squat (Gambe)"),
            ObjSearchExercise("Gambe", ""),
            ObjSearchExercise("Gambe", ""),
            ObjSearchExercise("Gambe", ""),

            ObjSearchExercise("Bicipiti", "curl manubri"),
            ObjSearchExercise("Bicipiti", "curl bilanciere"),
            ObjSearchExercise("Bicipiti", "hammer manubri"),
            ObjSearchExercise("Bicipiti", "curl concentrato"),
            ObjSearchExercise("Bicipiti", "curl cavi"),
            ObjSearchExercise("Bicipiti", "curl cavi corda"),
            ObjSearchExercise("Bicipiti", "curl elastici"),
            ObjSearchExercise("Bicipiti", "hammer elastici"),
            ObjSearchExercise("Bicipiti", "trazioni (Bicipiti)"),
            ObjSearchExercise("Bicipiti", "spider curl"),
            ObjSearchExercise("Bicipiti", "curl panca 45"),
            ObjSearchExercise("Bicipiti", "zottman Curl"),
            ObjSearchExercise("Bicipiti", "curl bilanciere ez"),
            ObjSearchExercise("Bicipiti", "curl TRX"),
            ObjSearchExercise("Bicipiti", ""),
            ObjSearchExercise("Bicipiti", ""),
            ObjSearchExercise("Bicipiti", ""),

            ObjSearchExercise("Dorso", "stacchi regular"),
            ObjSearchExercise("Dorso", "stacchi sumo"),
            ObjSearchExercise("Dorso", "stacchi rumeni"),
            ObjSearchExercise("Dorso", "lat machine"),
            ObjSearchExercise("Dorso", "lat machine T-Bar"),
            ObjSearchExercise("Dorso", "rematore manubri"),
            ObjSearchExercise("Dorso", "rematore cavi"),
            ObjSearchExercise("Dorso", "trazioni (Dorso)"),
            ObjSearchExercise("Dorso", "rematore bilanciere"),
            ObjSearchExercise("Dorso", "rematore su panca"),
            ObjSearchExercise("Dorso", "rematore bilanciere presa stretta"),
            ObjSearchExercise("Dorso", "push-down corda"),
            ObjSearchExercise("Dorso", "butterflies (Dorso)"),
            ObjSearchExercise("Dorso", "rematore manubri singolo"),
            ObjSearchExercise("Dorso", "rematore manubri invers"),
            ObjSearchExercise("Dorso", "rematore bilanciere presa stretta"),
            ObjSearchExercise("Dorso", "rematore renegade"),
            ObjSearchExercise("Dorso", "rematore TRX"),
            ObjSearchExercise("Dorso", "hyperextension"),
            ObjSearchExercise("Dorso", "rematore T-Bar"),

            ObjSearchExercise("Tricipiti", "panca piana presa stretta"),
            ObjSearchExercise("Tricipiti", "panca inclinata presa stretta"),
            ObjSearchExercise("Tricipiti", "french press bilanciere"),
            ObjSearchExercise("Tricipiti", "tricipiti cavi"),
            ObjSearchExercise("Tricipiti", "french press cavi"),
            ObjSearchExercise("Tricipiti", "tricipiti cavi con corda"),
            ObjSearchExercise("Tricipiti", "tricipiti TRX"),
            ObjSearchExercise("Tricipiti", "dips (Tricipiti)"),
            ObjSearchExercise("Tricipiti", "flessioni diamonds"),
            ObjSearchExercise("Tricipiti", "tricipiti alla panca"),
            ObjSearchExercise("Tricipiti", "press manubri"),
            ObjSearchExercise("Tricipiti", "flessioni tiger (Tricipiti)"),
            ObjSearchExercise("Tricipiti", "french press manubri"),
            ObjSearchExercise("Tricipiti", "flessioni diamond rialzo (Tricipiti)"),
            ObjSearchExercise("Tricipiti", ""),

            ObjSearchExercise("Spalle", "alzate laterali"),
            ObjSearchExercise("Spalle", "alzate frontali"),
            ObjSearchExercise("Spalle", "apinte manubri"),
            ObjSearchExercise("Spalle", "arnold con manubri"),
            ObjSearchExercise("Spalle", "tirate al mento manubri"),
            ObjSearchExercise("Spalle", "tirate al mento bilanciere"),
            ObjSearchExercise("Spalle", "alzate laterali cavi"),
            ObjSearchExercise("Spalle", "alzate frontali cavi"),
            ObjSearchExercise("Spalle", "military press bilanciere"),
            ObjSearchExercise("Spalle", "alzate frontali disco"),
            ObjSearchExercise("Spalle", "butterfly (Spalle)"),
            ObjSearchExercise("Spalle", "alzate frontali elastici"),
            ObjSearchExercise("Spalle", "alzate laterali elastici"),
            ObjSearchExercise("Spalle", "pressa spalle macchinario"),
            ObjSearchExercise("Spalle", ""),
            ObjSearchExercise("Spalle", ""),

            ObjSearchExercise("Polpacci", "polpacci su disco"),
            ObjSearchExercise("Polpacci", "calf machine"),
            ObjSearchExercise("Polpacci", "camminata punta di piedi"),
            ObjSearchExercise("Polpacci", "jump squat (Polpacci)"),
            ObjSearchExercise("Polpacci", "alzate in punta di piedi bilanciere"),
            ObjSearchExercise("Polpacci", "alzate in punta di piedi manubri"),
            ObjSearchExercise("Polpacci", ""),
            ObjSearchExercise("Polpacci", ""),

            ObjSearchExercise("Addominali", "crunch"),
            ObjSearchExercise("Addominali", "bicicletta (Addominali)"),
            ObjSearchExercise("Addominali", "addominali laterali"),
            ObjSearchExercise("Addominali", "plank"),
            ObjSearchExercise("Addominali", "plank laterale"),
            ObjSearchExercise("Addominali", "plank con salto"),
            ObjSearchExercise("Addominali", "sit-up"),
            ObjSearchExercise("Addominali", "super-man plank"),
            ObjSearchExercise("Addominali", "plank con rotazione bacino"),
            ObjSearchExercise("Addominali", "mountain-climber"),

            ObjSearchExercise("Cardio", "tapis-roulant"),
            ObjSearchExercise("Cardio", "cyclette"),
            ObjSearchExercise("Cardio", "corda"),
            ObjSearchExercise("Cardio", "ellittica"),
            ObjSearchExercise("Cardio", "step-up (Cardio)"),
            ObjSearchExercise("Cardio", "corsa"),
            ObjSearchExercise("Cardio", "jogging"),
            ObjSearchExercise("Cardio", "spinning"),
            ObjSearchExercise("Cardio", "vogatore"),
            ObjSearchExercise("Cardio", "marcia"),
            ObjSearchExercise("Cardio", "skip"),
            ObjSearchExercise("Cardio", "corsa calciata"),
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
            ObjSearchExercise("Chest", "high cables"),
            ObjSearchExercise("Chest", "bass cables"),
            ObjSearchExercise("Chest", "flat bench crosses"),
            ObjSearchExercise("Chest", "cables flat bench crosses"),
            ObjSearchExercise("Chest", "inclined bench crosses 45"),
            ObjSearchExercise("Chest", "flat bench press"),
            ObjSearchExercise("Chest", "inclined bench press 45"),
            ObjSearchExercise("Chest", "flat bench press barbell"),
            ObjSearchExercise("Chest", "chest press"),
            ObjSearchExercise("Chest", "inclined chest press"),
            ObjSearchExercise("Chest", "inclined bench press barbell"),
            ObjSearchExercise("Chest", "pectoral machine"),
            ObjSearchExercise("Chest", "trx crosses"),
            ObjSearchExercise("Chest", "push-ups"),
            ObjSearchExercise("Chest", "dips (Chest)"),
            ObjSearchExercise("Chest", "pullover"),
            ObjSearchExercise("Chest", "dips rings (Chest)"),
            ObjSearchExercise("Chest", "diamonds push-ups (Chest)"),
            ObjSearchExercise("Chest", "barbell decline bench"),
            ObjSearchExercise("Chest", "dumbbells decline bench"),
            ObjSearchExercise("Chest", ""),
            ObjSearchExercise("Chest", ""),

            ObjSearchExercise("Legs", "squat"),
            ObjSearchExercise("Legs", "leg extensions"),
            ObjSearchExercise("Legs", "bodyweight lunges"),
            ObjSearchExercise("Legs", "barbell lunges"),
            ObjSearchExercise("Legs", "dumbbells lunges"),
            ObjSearchExercise("Legs", "pistol squat"),
            ObjSearchExercise("Legs", "step up bodyweight (Legs)"),
            ObjSearchExercise("Legs", "step up dumbbells (Legs)"),
            ObjSearchExercise("Legs", "box jump"),
            ObjSearchExercise("Legs", "press"),
            ObjSearchExercise("Legs", "inclined press 45"),
            ObjSearchExercise("Legs", "90 press"),
            ObjSearchExercise("Legs", "vertical press"),
            ObjSearchExercise("Legs", "hack squat"),
            ObjSearchExercise("Legs", "pistol squat kettlebell"),
            ObjSearchExercise("Legs", "hypetrust"),
            ObjSearchExercise("Legs", "sumo squat"),
            ObjSearchExercise("Legs", "dead-lift (Legs)"),
            ObjSearchExercise("Legs", "abductors machine"),
            ObjSearchExercise("Legs", "reverse lunge"),
            ObjSearchExercise("Legs", "abductors elastic"),
            ObjSearchExercise("Legs", "frontal squat"),
            ObjSearchExercise("Legs", "jump squat (Legs)"),
            ObjSearchExercise("Legs", ""),
            ObjSearchExercise("Legs", ""),
            ObjSearchExercise("Legs", ""),

            ObjSearchExercise("Biceps", "curl barbell"),
            ObjSearchExercise("Biceps", "curl dumbbells"),
            ObjSearchExercise("Biceps", "hammer dumbbells"),
            ObjSearchExercise("Biceps", "concentrated curl"),
            ObjSearchExercise("Biceps", "cables curl"),
            ObjSearchExercise("Biceps", "cables rope curl"),
            ObjSearchExercise("Biceps", "curl elastic"),
            ObjSearchExercise("Biceps", "hammer elastic"),
            ObjSearchExercise("Biceps", "pull-ups (Biceps)"),
            ObjSearchExercise("Biceps", "spider curl"),
            ObjSearchExercise("Biceps", "curl bench 45"),
            ObjSearchExercise("Biceps", "zottman curl"),
            ObjSearchExercise("Biceps", "curl ez barbell"),
            ObjSearchExercise("Biceps", "curl trx"),
            ObjSearchExercise("Biceps", ""),
            ObjSearchExercise("Biceps", ""),
            ObjSearchExercise("Biceps", ""),

            ObjSearchExercise("Back", "regular dead-lift"),
            ObjSearchExercise("Back", "sumo dead-lift"),
            ObjSearchExercise("Back", "romanian dead-lift"),
            ObjSearchExercise("Back", "lat machine"),
            ObjSearchExercise("Back", "lat machine T-Bar"),
            ObjSearchExercise("Back", "dumbbells row"),
            ObjSearchExercise("Back", "cable row"),
            ObjSearchExercise("Back", "pull-ups (Back)"),
            ObjSearchExercise("Back", "barbell row"),
            ObjSearchExercise("Back", "bench row"),
            ObjSearchExercise("Back", "close grip barbell rowing machine"),
            ObjSearchExercise("Back", "push-down rope"),
            ObjSearchExercise("Back", "butterflies (Back)"),
            ObjSearchExercise("Back", "single dumbbell row"),
            ObjSearchExercise("Back", "inverted dumbbells rowing"),
            ObjSearchExercise("Back", "close grip barbell rowing machine"),
            ObjSearchExercise("Back", "renegade oarsman"),
            ObjSearchExercise("Back", "trx oarsman"),
            ObjSearchExercise("Back", "hyperextension"),
            ObjSearchExercise("Back", "t-bar oarsman"),

            ObjSearchExercise("Triceps", "close grip flat bench"),
            ObjSearchExercise("Triceps", "close grip incline bench"),
            ObjSearchExercise("Triceps", "french press barbell"),
            ObjSearchExercise("Triceps", "rope triceps"),
            ObjSearchExercise("Triceps", "french press cables"),
            ObjSearchExercise("Triceps", "cable triceps with rope"),
            ObjSearchExercise("Triceps", "trx triceps"),
            ObjSearchExercise("Triceps", "dips (Triceps)"),
            ObjSearchExercise("Triceps", "diamonds push-ups"),
            ObjSearchExercise("Triceps", "bench press triceps"),
            ObjSearchExercise("Triceps", "press dumbbells"),
            ObjSearchExercise("Triceps", "tiger push-ups (Triceps)"),
            ObjSearchExercise("Triceps", "french press dumbbells"),
            ObjSearchExercise("Triceps", "diamond raise push-ups (Triceps)"),
            ObjSearchExercise("Triceps", ""),

            ObjSearchExercise("Shoulders", "lateral raises"),
            ObjSearchExercise("Shoulders", "front raises"),
            ObjSearchExercise("Shoulders", "dumbbell thrusts"),
            ObjSearchExercise("Shoulders", "arnold"),
            ObjSearchExercise("Shoulders", "chin pull-up dumbbell"),
            ObjSearchExercise("Shoulders", "chin pull-up barbell"),
            ObjSearchExercise("Shoulders", "lateral cable raises"),
            ObjSearchExercise("Shoulders", "cables front raises"),
            ObjSearchExercise("Shoulders", "military press barbell"),
            ObjSearchExercise("Shoulders", "front disc raises"),
            ObjSearchExercise("Shoulders", "butterfly (Shoulders)"),
            ObjSearchExercise("Shoulders", "elastic front lifts"),
            ObjSearchExercise("Shoulders", "elastic side raises"),
            ObjSearchExercise("Shoulders", "machine shoulders press"),
            ObjSearchExercise("Shoulders", ""),
            ObjSearchExercise("Shoulders", ""),

            ObjSearchExercise("Calves", "calves on disc"),
            ObjSearchExercise("Calves", "calf machine"),
            ObjSearchExercise("Calves", "tiptoe walking"),
            ObjSearchExercise("Calves", "jump squat (Calves)"),
            ObjSearchExercise("Calves", "tiptoe up the barbell"),
            ObjSearchExercise("Calves", "raise toe of dumbbells"),
            ObjSearchExercise("Calves", ""),
            ObjSearchExercise("Calves", ""),

            ObjSearchExercise("Abdominals", "crunch"),
            ObjSearchExercise("Abdominals", "bicycle (Abs)"),
            ObjSearchExercise("Abdominals", "lateral abs"),
            ObjSearchExercise("Abdominals", "plank"),
            ObjSearchExercise("Abdominals", "side plank"),
            ObjSearchExercise("Abdominals", "jump plank"),
            ObjSearchExercise("Abdominals", "sit-up"),
            ObjSearchExercise("Abdominals", "super-man plank"),
            ObjSearchExercise("Abdominals", "plank with pelvis rotation"),
            ObjSearchExercise("Abdominals", "mountain-climber"),

            ObjSearchExercise("Cardio", "tapis-roulant"),
            ObjSearchExercise("Cardio", "cyclette"),
            ObjSearchExercise("Cardio", "rope"),
            ObjSearchExercise("Cardio", "elliptical"),
            ObjSearchExercise("Cardio", "step-up (Cardio)"),
            ObjSearchExercise("Cardio", "running"),
            ObjSearchExercise("Cardio", "jogging"),
            ObjSearchExercise("Cardio", "spinning"),
            ObjSearchExercise("Cardio", "rowing machine"),
            ObjSearchExercise("Cardio", "march"),
            ObjSearchExercise("Cardio", "skip"),
            ObjSearchExercise("Cardio", "run kicked"),
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
                val listExercise = ArrayList<ObjSearchExercise>()

                doc.forEach { exercise ->
                    exercise.forEach { (key, value /*value -> muscle,name*/) ->
                        if (key == "muscle") {
                            listExercise.add(ObjSearchExercise(value, null))
                        } else {
                            listExercise[listExercise.size - 1].nameExercise = value
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
                        if (key == "muscle") {
                            listExercise.add(ObjSearchExercise(value, null))
                        } else {
                            listExercise[listExercise.size - 1].nameExercise = value
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