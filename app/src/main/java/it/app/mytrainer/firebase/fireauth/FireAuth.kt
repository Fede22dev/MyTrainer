package it.app.mytrainer.firebase.fireauth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FireAuth {

    companion object {

        private lateinit var auth: FirebaseAuth

        private const val TAG = "FIREAUTH [DEBUG]"

        //Initialize of the Fb authentication
        private fun initializeFbAuth() {
            auth = Firebase.auth
        }

        fun getCurrentUser(): FirebaseUser? {
            initializeFbAuth()
            val currentUser = auth.currentUser
            Log.d(TAG, "User: ${currentUser?.uid}")
            return currentUser
        }

        private fun getCurrentUserId(): String? {
            return getCurrentUser()?.uid
        }

        //Fun to check the email and pass
        fun login(email: String, password: String, callback: (Boolean) -> Unit) {
            if (email.isBlank() || password.isBlank()) {
                callback(false)
            } else {
                initializeFbAuth()
                var bool = false
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            Log.d(TAG, "signInWithEmail:success")
                            val currentUserId = getCurrentUserId()


                            //ESEMPIO FireStore.cerca documento con stesso nome in firestore e returna il tipo(athlete / trainer)


                            bool = true
                        } else {
                            // If sign in fails
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                        }
                        callback(bool)
                    }
            }
        }
    }
}