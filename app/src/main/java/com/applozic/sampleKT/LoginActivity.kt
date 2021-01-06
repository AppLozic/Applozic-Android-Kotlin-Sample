package com.applozic.sampleKT

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.applozic.mobicomkit.Applozic
import com.applozic.mobicomkit.api.account.register.RegistrationResponse
import com.applozic.mobicomkit.api.account.user.User
import com.applozic.mobicomkit.listners.AlLoginHandler
import com.applozic.mobicomkit.listners.AlPushNotificationHandler
import com.applozic.mobicommons.json.GsonUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userIdEditText = findViewById<EditText>(R.id.userIdET)
        val passwordEditText = findViewById<EditText>(R.id.passwordET)
        val displayNameEditText = findViewById<EditText>(R.id.displayNameET)
        val loginButton = findViewById<Button>(R.id.login)

        loginButton.setOnClickListener {
            val userId = userIdEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, R.string.error_message_for_userId_empty, Toast.LENGTH_SHORT)
                        .show()
            } else {
                val dialog = AlertDialogHelper().setProgressDialog(
                        this,
                        getString(R.string.please_wait_info)
                )
                dialog.show()
                val user = User()
                user.userId =
                        userId //userId it can be any unique user identifier NOTE : +,*,? are not allowed chars in userId.
                user.displayName = displayNameEditText.text.toString()
                user.authenticationTypeId = User.AuthenticationType.APPLOZIC.getValue()
                user.password = password
                this.loginUser(user, dialog)
            }
        }
    }

    // Login the user to applozic
    private fun loginUser(user: User, alertDialog: AlertDialog) {
        Applozic.connectUser(this, user, object : AlLoginHandler {
            override fun onSuccess(registrationResponse: RegistrationResponse?, context: Context?) {
                // After successful registration with Applozic server the callback will come here
                alertDialog.dismiss()
                registerPushAndLaunchMainActivity()
            }

            override fun onFailure(
                    registrationResponse: RegistrationResponse?,
                    exception: Exception?
            ) {
                // If any failure in registration the callback  will come here
                alertDialog.dismiss()
                var errorText = "Some Internal error occurred"
                if (registrationResponse != null) {
                    errorText = registrationResponse.message
                } else if (exception != null) {
                    errorText = GsonUtils.getJsonFromObject(exception, exception.javaClass)
                }
                Toast.makeText(this@LoginActivity, errorText, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Get the token from firebase and update in applozic server
    fun registerPushAndLaunchMainActivity() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FCM", "getInstanceId failed:", task.exception)
                        launchMainActivity()
                        return@OnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result?.token
                    Log.i("FCM", "Found token :$token")
                    if (token == null) {
                        Log.i("FCM", "FCM token is null returning")
                        return@OnCompleteListener
                    }
                    Applozic.registerForPushNotification(
                            this@LoginActivity,
                            token,
                            object : AlPushNotificationHandler {
                                override fun onSuccess(registrationResponse: RegistrationResponse?) {
                                    launchMainActivity()
                                }

                                override fun onFailure(
                                        registrationResponse: RegistrationResponse?,
                                        exception: java.lang.Exception?
                                ) {
                                }
                            })
                })
    }

    fun launchMainActivity() {
        val intent = Intent(
                this, MainActivity::class.java
        )
        this.startActivity(intent)
        this.finish()
    }
}

