package it.app.mytrainer.firebase.fireauth

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.app.mytrainer.firebase.firestore.FireStore

class FireAuth {

    companion object {

        private val auth = Firebase.auth
        private const val TAG = "FIREAUTH"

        fun getCurrentUserType(callback: (Int) -> Unit) {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                Log.d(TAG, "Going to find out the type of: ${currentUser.uid}")
                val fireStore = FireStore()
                fireStore.findType(currentUser.uid) { type ->
                    callback(type)
                }
            } else {
                callback(-1)
            }
        }

        //Fun to check the email and pass
        fun login(email: String, password: String, callback: (Boolean, Int) -> Unit) {
            if (email.isBlank() || password.isBlank()) {
                callback(false, -1)
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            val currentUserId = auth.currentUser?.uid!!
                            Log.d(TAG, "SignInWithEmail: success")

                            //Looking for user type
                            val fireStore = FireStore()
                            fireStore.findType(currentUserId) { type ->
                                if (type == -1) {
                                    callback(false, type)
                                } else {
                                    callback(true, type)
                                }
                            }
                        } else {
                            // If sign in fails
                            Log.w(TAG, "SignInWithEmail: failure", task.exception)
                            callback(false, -1)
                        }
                    }
            }
        }

        fun createAccount(
            email: String,
            password: String,
            activity: Activity,
            callback: (Boolean, String) -> Unit,
        ) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        val currentUserId = auth.currentUser?.uid.toString()
                        Log.d(TAG, "CreateUserWithEmail: success")
                        callback(true, currentUserId)
                    } else {
                        // If sign in fails
                        Log.w(TAG, "CreateUserWithEmail: failure", task.exception)
                        callback(false, "")
                    }
                }
        }

        fun getCurrentUserAuth(): FirebaseUser? {
            return auth.currentUser
        }

        fun deleteCurrentUser() {
            auth.currentUser?.delete()?.addOnSuccessListener {
                Log.d(TAG, "Delete account: success")
            }?.addOnFailureListener { e ->
                Log.w(TAG, "Delete account: failure", e)
            }
        }

        fun signOut() {
            auth.signOut()
        }

        fun userReauthenticate(password: String, callback: (Boolean) -> Unit) {
            if (password != "") {
                val user = auth.currentUser!!
                val credential = EmailAuthProvider.getCredential(user.email!!, password)

                user.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User re-authenticated: success")
                            callback(true)
                        } else {
                            Log.w(TAG, "User re-authenticated: failure", task.exception)
                            callback(false)
                        }
                    }
            } else {
                callback(false)
            }
        }
    }
}