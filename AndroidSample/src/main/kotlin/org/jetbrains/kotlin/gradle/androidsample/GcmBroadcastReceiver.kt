package org.jetbrains.kotlin.gradle.androidsample

import android.content.Context
import android.content.Intent
import android.content.ComponentName
import android.support.v4.content.WakefulBroadcastReceiver
import android.app.Activity

class GcmBroadcastReceiver : WakefulBroadcastReceiver() {
    override fun onReceive(context : Context, intent : Intent) {
        val comp : ComponentName =
            ComponentName(context?.getPackageName(), javaClass<GcmIntentService>().getName());
        WakefulBroadcastReceiver.startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}