package com.applozic.sampleKT

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.applozic.mobicomkit.Applozic
import com.applozic.mobicomkit.listners.AlLogoutHandler
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity
import com.applozic.sampleKT.richMessage.SampleRichMessagesActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launchChatScreenButton = findViewById<Button>(R.id.launchChatScreenButton)
        val launchChatWithUserButton = findViewById<Button>(R.id.launchIndividualChat)
        val listOfRichMessages = findViewById<Button>(R.id.listOfRichMessages)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        launchChatScreenButton.setOnClickListener {
            this.launchChatScreen()
        }

        launchChatWithUserButton.setOnClickListener {
            this.initiateChatClick()
        }

        listOfRichMessages.setOnClickListener {
            this.launchRichMessagesList()
        }

        logoutButton.setOnClickListener {
            this.logout()
        }
    }

    // Launch chat screen
    private fun launchChatScreen() {
        val intent = Intent(
                this, ConversationActivity::class.java
        )
        this.startActivity(intent)
    }

    // Launch the dialog prompt for enter userId in case of one to one chat launch
    private fun initiateChatClick() {
        val fragment: DialogFragment = InitiateDialogFragment()
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(InitiateDialogFragmentTAG)
        if (prev != null) {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
        fragment.show(fragmentTransaction, InitiateDialogFragmentTAG)
    }

    // Launch demo rich messages list screen
    private fun launchRichMessagesList() {
        val intent = Intent(
                this, SampleRichMessagesActivity::class.java
        )
        this.startActivity(intent)
    }

    // Logout user from applozic
    private fun logout() {
        val dialog = AlertDialogHelper().setProgressDialog(this, getString(R.string.please_wait_info))
        dialog.show()
        Applozic.logoutUser(this, object : AlLogoutHandler {
            override fun onSuccess(context: Context?) {
                dialog.dismiss()

                val intent = Intent(
                        this@MainActivity, LoginActivity::class.java
                )
                this@MainActivity.startActivity(intent)
                this@MainActivity.finish()
            }

            override fun onFailure(exception: Exception?) {
                dialog.dismiss()
            }
        })
    }

    companion object {
        private const val InitiateDialogFragmentTAG = "InitiateDialogFragment"
    }
}