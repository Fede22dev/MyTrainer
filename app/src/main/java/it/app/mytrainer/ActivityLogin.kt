package it.app.mytrainer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        // Check if the user is already logged in
        currentUser?.let {
            /*val intent = Intent(this, ActivityUserChoice::class.java)
            Toast.makeText(
                        this, "Welcome back",
                        Toast.LENGTH_SHORT
                    ).show()
            startActivity(intent)
            finish()*/
        }
    }

    fun onClickLogin(v: View) {
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        login(email, password)
    }

    fun onClickRegistration(v: View) {
        val intent = Intent(this, ActivityUserChoice::class.java)
        startActivity(intent)
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    /*val intent = Intent(this, ANDIAMO ALLA HOME)
                    startActivity(intent)
                    finish()*/
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}