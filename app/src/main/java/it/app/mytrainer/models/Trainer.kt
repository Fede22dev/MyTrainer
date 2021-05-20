package it.app.mytrainer.models

class Trainer {
    companion object{
        private var hashMapTrainer= HashMap<String, Any>()

        fun putEmail(email: String){
            hashMapTrainer["Email"]= email
        }

        fun putName(name: String){
            hashMapTrainer["Name"]= name
        }

        fun putSurname(surname: String){
            hashMapTrainer["Surname"]= surname
        }

        fun putDate(dateOfBirth: String){
            hashMapTrainer["BirthDate"]= dateOfBirth
        }

        fun putGym(gym: String){
            hashMapTrainer["Gym"]= gym
        }

        fun putSpec(spec: String){
            hashMapTrainer["Specialization"]= spec
        }
    }
}