package it.app.mytrainer.ui.activities.starter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.ui.activities.home.ActivityHomeAthlete
import it.app.mytrainer.ui.activities.home.ActivityHomeTrainer
import it.app.mytrainer.ui.activities.registration.ActivityUserChoice
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    private val TAG = "LOGIN"
    private val auth = Firebase.auth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Initialize facebook SDK
        FacebookSdk.sdkInitialize(this)

        //Initialize facebook login button
        callbackManager = CallbackManager.Factory.create()
        btnLoginFacebook.setReadPermissions("email", "public_profile")
        btnLoginFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook: onSuccess: $loginResult")

                progressBarLogin.visibility = View.VISIBLE
                btnLoginFacebook.visibility = View.INVISIBLE
                imageViewBackgroundLogin.visibility = View.INVISIBLE
                layoutLoginEditTextEmail.visibility = View.INVISIBLE
                layoutLoginEditTextPassword.visibility = View.INVISIBLE
                loginBtn.visibility = View.INVISIBLE
                textViewCreateAccount.visibility = View.INVISIBLE

                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook: onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.w(TAG, "facebook: onError ", error)
            }
        })

        emailFieldLogin.doOnTextChanged { _, _, _, _ ->
            layoutLoginEditTextEmail.error = null
        }

        passwordFieldLogin.doOnTextChanged { _, _, _, _ ->
            layoutLoginEditTextPassword.error = null
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken: $token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential: success ($credential)")

                    FireAuth.getCurrentUser { type ->
                        // Check if the user is already logged in
                        when (type) {
                            0 -> {
                                val intent = Intent(this, ActivityHomeTrainer::class.java)
                                Toast.makeText(
                                    this, getString(R.string.welcome_back_trainer_login),
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(intent)
                                finish()
                            }

                            1 -> {
                                val intent = Intent(this, ActivityHomeAthlete::class.java)
                                Toast.makeText(
                                    this, getString(R.string.welcome_back_athlete_login),
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(intent)
                                finish()
                            }

                            -1 -> {
                                val intent = Intent(this, ActivityUserChoice::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential: failure", task.exception)

                    progressBarLogin.visibility = View.INVISIBLE
                    btnLoginFacebook.visibility = View.VISIBLE
                    imageViewBackgroundLogin.visibility = View.VISIBLE
                    layoutLoginEditTextEmail.visibility = View.VISIBLE
                    layoutLoginEditTextPassword.visibility = View.VISIBLE
                    loginBtn.visibility = View.VISIBLE
                    textViewCreateAccount.visibility = View.VISIBLE

                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun onClickLogin(v: View) {

        val email = emailFieldLogin.text.toString().trim()
        val password = passwordFieldLogin.text.toString().trim()

        progressBarLogin.visibility = View.VISIBLE
        btnLoginFacebook.visibility = View.INVISIBLE
        imageViewBackgroundLogin.visibility = View.INVISIBLE
        layoutLoginEditTextEmail.visibility = View.INVISIBLE
        layoutLoginEditTextPassword.visibility = View.INVISIBLE
        loginBtn.visibility = View.INVISIBLE
        textViewCreateAccount.visibility = View.INVISIBLE
        layoutLoginEditTextEmail.error = null
        layoutLoginEditTextPassword.error = null

        //Call to the method in FireAuth to check if the user exist in the FireAuthentication
        FireAuth.login(email, password) { result, type ->
            //if the auth is ok, the user is redirect on his home page
            if (result) {
                //check for the user type
                if (type == 1) {
                    //It's an athlete, go to the athlete home
                    val intent = Intent(this, ActivityHomeAthlete::class.java)
                    startActivity(intent)
                } else {
                    //It's a trainer, go to the trainer home
                    val intent = Intent(this, ActivityHomeTrainer::class.java)
                    startActivity(intent)
                }

                finish()

            } else {
                //Toast in case of failed auth
                Toast.makeText(
                    this, getString(R.string.authentication_failed),
                    Toast.LENGTH_SHORT
                ).show()

                //setting blank the field and restore visibility
                progressBarLogin.visibility = View.INVISIBLE
                btnLoginFacebook.visibility = View.VISIBLE
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