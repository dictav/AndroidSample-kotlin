package org.jetbrains.kotlin.gradle.androidsample

import android.os.Bundle
import android.content.Intent
import com.google.android.gms.gcm.GoogleCloudMessaging
import android.util.Log
import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.app.Notification
import android.net.Uri
import android.app.PendingIntent

class GcmIntentService : IntentService("GcmIntentService") {
    private val TAG: String = "GcmIntentService"
    private var mManager : NotificationManager? = null
    private var number : Int = 0;

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
            sendNotification()
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private fun sendNotification() {
        mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val n : Notification = Notification();
        val intent : Intent = Intent(getApplicationContext() as Context, javaClass<NotificateActivity>())
        val pi  : PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)!!

        n.icon = R.drawable.ic_launcher
        n.tickerText = "NotificationSample";
        n.number = number;

        n.setLatestEventInfo(getApplicationContext(), "NotificationSample", "It calls it.", pi)

        n.defaults = n.defaults.toInt() or Notification.DEFAULT_SOUND.toInt()

        mManager?.notify(number, n);
        number++;
    }
}
