package org.jetbrains.kotlin.gradle.androidsample

import android.os.Bundle
import android.content.Intent
import com.google.android.gms.gcm.GoogleCloudMessaging
import android.util.Log
import android.app.IntentService

class GcmIntentService : IntentService("GcmIntentService") {
    val TAG: String = "GcmIntentService"

    override fun onHandleIntent(intent: Intent?) {
        val extras: Bundle = intent?.getExtras()!!
        val gcm: GoogleCloudMessaging = GoogleCloudMessaging.getInstance(this)!!
        val messageType: String = gcm.getMessageType(intent)!!

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.d(TAG, "messageType: " + messageType + ",body:" + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.d(TAG, "messageType: " + messageType + ",body:" + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.d(TAG, "messageType: " + messageType + ",body:" + extras.toString());
            }
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        }
    }
}
