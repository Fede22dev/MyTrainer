package it.app.mytrainer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.ui.activities.registration.ActivityUserChoice
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = FireAuth.getCurrentUser()
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
        //Call to the method in FireAuth to check if the user exist in the FireAuthentication
        FireAuth.login(email, password) { result ->
            //if the auth is ok, the user is redirect on his home page
            if (result) {
                //check for the user type
                /*if(controllo di che tipo è l utente){
                       //atleta
                       val intent = Intent(this, ANDIAMO ALLA HOME atleta)
                                startActivity(intent)

                    }else{
                        //trainer
                          val intent = Intent(this, ANDIAMO ALLA HOME trainer)
                                startActivity(intent)
                    }
                    finish()*/
            } else {
                //Toast in case of failed auth
                Toast.makeText(
                    this, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
                //setting blank the field
                emailField.setText("")
                passwordField.setText("")
            }
        }

    }

    //Starting the choice activity
    fun onClickRegistration(v: View) {
        val intent = Intent(this, ActivityUserChoice::class.java)
        startActivity(intent)
    }


}