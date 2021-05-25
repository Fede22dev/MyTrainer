package it.app.mytrainer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.ui.activities.registration.ActivityUserChoice
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailFieldLogin.doOnTextChanged { _, _, _, _ ->
            layoutLoginEditTextEmail.error = null
        }

        passwordFieldLogin.doOnTextChanged { _, _, _, _ ->
            layoutLoginEditTextPassword.error = null
        }
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

        val email = emailFieldLogin.text.toString().trim()
        val password = passwordFieldLogin.text.toString().trim()

        progressBarLogin.visibility = View.VISIBLE
        imageViewBackgroundLogin.visibility = View.INVISIBLE
        layoutLoginEditTextEmail.visibility = View.INVISIBLE
        layoutLoginEditTextPassword.visibility = View.INVISIBLE
        loginBtn.visibility = View.INVISIBLE
        textViewCreateAccount.visibility = View.INVISIBLE
        layoutLoginEditTextEmail.error = null
        layoutLoginEditTextPassword.error = null

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
                    this, getString(R.string.authentication_failed),
                    Toast.LENGTH_SHORT
                ).show()

                //setting blank the field and restore visibility
                progressBarLogin.visibility = View.INVISIBLE
                imageViewBackgroundLogin.visibility = View.VISIBLE
                layoutLoginEditTextEmail.visibility = View.VISIBLE
                layoutLoginEditTextPassword.visibility = View.VISIBLE
                loginBtn.visibility = View.VISIBLE
                textViewCreateAccount.visibility = View.VISIBLE

                emailFieldLogin.setText("")
                passwordFieldLogin.setText("")

                layoutLoginEditTextEmail.error = getString(R.string.fields_not_correct)
                layoutLoginEditTextEmail.errorIconDrawable = null
                layoutLoginEditTextPassword.error = getString(R.string.fields_not_correct)
                layoutLoginEditTextPassword.errorIconDrawable = null
            }
        }
    }

    //Starting the choice activity
    fun onClickRegistration(v: View) {
        val intent = Intent(this, ActivityUserChoice::class.java)
        startActivity(intent)
    }
}