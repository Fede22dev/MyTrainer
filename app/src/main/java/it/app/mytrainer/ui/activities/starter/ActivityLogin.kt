package it.app.mytrainer.ui.activities.starter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
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

    private val TAG = "ACTIVITY_LOGIN"
    private val auth = Firebase.auth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Setting the onClick and the login with FB
        loginFB()

        emailFieldLogin.doOnTextChanged { _, _, _, _ ->
            layoutLoginEditTextEmail.error = null
        }

        passwordFieldLogin.doOnTextChanged { _, _, _, _ ->
            layoutLoginEditTextPassword.error = null
        }
    }

    //Login with FB
    private fun loginFB() {
        //Initialize facebook SDK
        FacebookSdk.sdkInitialize(this)

        //Initialize facebook login button
        callbackManager = CallbackManager.Factory.create()
        btnLoginFacebook.setReadPermissions("email", "public_profile")
        btnLoginFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook: onSuccess: $loginResult")

                //Setting the visibility for FB Login
                setVisibilityForLoginFB()

                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook: onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.w(TAG, "facebook: onError ", error)
            }
        })
    }

    //Visibility FB
    private fun setVisibilityForLoginFB() {
        progressBarLogin.visibility = View.VISIBLE
        titleAppLogin.visibility = View.INVISIBLE
        btnLoginFacebook.visibility = View.INVISIBLE
        imageViewBackgroundLogin.visibility = View.INVISIBLE
        layoutLoginEditTextEmail.visibility = View.INVISIBLE
        layoutLoginEditTextPassword.visibility = View.INVISIBLE
        btnLogin.visibility = View.INVISIBLE
        btnCreateAccount.visibility = View.INVISIBLE
    }

    //Access in the app with FBToken
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken: $token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential: success ($credential)")

                    //Get the right activity
                    startRightActivity()

                } else {

                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential: failure", task.exception)

                    //Resetting the visibility
                    resetVisibilityFB()

                    Snackbar.make(constraintActivityLogin,
                        getString(R.string.authentication_failed),
                        Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()
                }
            }
    }

    //Understanding what activity gonna start
    private fun startRightActivity() {
        FireAuth.getCurrentUserType { type ->
            // Check if the user is already logged in
            when (type) {

                0 -> {
                    //If 0 is a trainer, calling the right method
                    startHomeTrainer()
                }

                1 -> {
                    //If 1 is an athlete, calling the right method
                    startHomeAthlete()
                }

                -1 -> {
                    //If -1 is not registered yet
                    startUserChoice()
                }
            }
        }
    }

    //Starting the trainer activity
    private fun startHomeTrainer() {
        val intent = Intent(this, ActivityHomeTrainer::class.java)
        Toast.makeText(
            this, getString(R.string.welcome_back_trainer_login),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(intent)
        finish()
    }

    //Starting the athlete activity
    private fun startHomeAthlete() {
        val intent = Intent(this, ActivityHomeAthlete::class.java)
        Toast.makeText(
            this, getString(R.string.welcome_back_athlete_login),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(intent)
        finish()
    }

    //Starting the choice activity
    private fun startUserChoice() {
        val intent = Intent(this, ActivityUserChoice::class.java)
        startActivity(intent)
        finish()
    }

    private fun resetVisibilityFB() {
        progressBarLogin.visibility = View.INVISIBLE
        titleAppLogin.visibility = View.VISIBLE
        btnLoginFacebook.visibility = View.VISIBLE
        imageViewBackgroundLogin.visibility = View.VISIBLE
        layoutLoginEditTextEmail.visibility = View.VISIBLE
        layoutLoginEditTextPassword.visibility = View.VISIBLE
        btnLogin.visibility = View.VISIBLE
        btnCreateAccount.visibility = View.VISIBLE
    }

    //Result of login FB
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    //On click for the login
    fun onClickLogin(v: View) {

        val email = emailFieldLogin.text.toString().trim()
        val password = passwordFieldLogin.text.toString().trim()

        //Setting the visibility for login
        setVisibilityForLoginNoFB()

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
                //Snackbar in case of failed auth
                Snackbar.make(constraintActivityLogin,
                    getString(R.string.authentication_failed),
                    Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(this, R.color.app_foreground))
                    .setTextColor(ContextCompat.getColor(this, R.color.white))
                    .show()

                //setting blank the field and restore visibility
                resetVisibilityForLoginNoFB()
            }
        }
    }

    //Visibility No FB
    private fun setVisibilityForLoginNoFB() {
        progressBarLogin.visibility = View.VISIBLE
        titleAppLogin.visibility = View.INVISIBLE
        btnLoginFacebook.visibility = View.INVISIBLE
        imageViewBackgroundLogin.visibility = View.INVISIBLE
        layoutLoginEditTextEmail.visibility = View.INVISIBLE
        layoutLoginEditTextPassword.visibility = View.INVISIBLE
        btnLogin.visibility = View.INVISIBLE
        btnCreateAccount.visibility = View.INVISIBLE

        //Resetting the field
        layoutLoginEditTextEmail.error = null
        layoutLoginEditTextPassword.error = null
    }

    //Resetting the field and the visibility
    private fun resetVisibilityForLoginNoFB() {
        progressBarLogin.visibility = View.INVISIBLE
        titleAppLogin.visibility = View.VISIBLE
        btnLoginFacebook.visibility = View.VISIBLE
        imageViewBackgroundLogin.visibility = View.VISIBLE
        layoutLoginEditTextEmail.visibility = View.VISIBLE
        layoutLoginEditTextPassword.visibility = View.VISIBLE
        btnLogin.visibility = View.VISIBLE
        btnCreateAccount.visibility = View.VISIBLE

        emailFieldLogin.setText("")
        passwordFieldLogin.setText("")

        layoutLoginEditTextEmail.error = getString(R.string.fields_not_correct)
        layoutLoginEditTextEmail.errorIconDrawable = null
        layoutLoginEditTextPassword.error = getString(R.string.fields_not_correct)
        layoutLoginEditTextPassword.errorIconDrawable = null

        YoYo.with(Techniques.Shake)
            .playOn(layoutLoginEditTextEmail)

        YoYo.with(Techniques.Shake)
            .playOn(layoutLoginEditTextPassword)
    }

    //Starting the choice activity
    fun onClickRegistration(v: View) {
        FireAuth.signOut()
        val intent = Intent(this, ActivityUserChoice::class.java)
        startActivity(intent)
        finish()
    }
}