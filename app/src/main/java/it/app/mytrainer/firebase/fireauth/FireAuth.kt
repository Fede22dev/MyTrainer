package it.app.mytrainer.firebase.fireauth

import android.app.Activity
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
                            val currentUserId = auth.currentUser?.uid
                            Log.d(TAG, "signInWithEmail:success --- $currentUserId")


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

        fun createAccount(
            email: String,
            password: String,
            activity: Activity,
            callback: (Boolean, String) -> Unit
        ) {
            var bool = false
            var currentUserId = ""
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        currentUserId = auth.currentUser?.uid.toString()
                        Log.d(TAG, "createUserWithEmail:success --- $currentUserId")
                        bool = true
                    } else {
                        // If sign in fails
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    }
                    callback(bool, currentUserId)
                }
        }
    }
}