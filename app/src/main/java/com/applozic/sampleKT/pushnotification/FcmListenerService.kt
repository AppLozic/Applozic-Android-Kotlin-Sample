package com.applozic.sampleKT.pushnotification

import android.util.Log
import com.applozic.mobicomkit.Applozic
import com.applozic.mobicomkit.api.account.register.RegisterUserClientService
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmListenerService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, "Message data:" + remoteMessage.data)
        if (remoteMessage.data.isNotEmpty()) {
            if (Applozic.isApplozicNotification(this, remoteMessage.data)) {
                Log.i(TAG, "Applozic notification processed")
                return
            }
        }
        // Your own app notifications handling
    }

    override fun onNewToken(registrationId: String) {
        super.onNewToken(registrationId)
        Log.i(
            TAG,
            "Found Registration Id:$registrationId"
        )
        if (MobiComUserPreference.getInstance(this).isRegistered) {
            try {
                RegisterUserClientService(this).updatePushNotificationId(registrationId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val TAG = "FcmListenerService"
    }
}