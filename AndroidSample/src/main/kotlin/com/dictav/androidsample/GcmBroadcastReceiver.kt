package com.dictav.androidsample

import android.content.Context
import android.content.Intent
import android.content.ComponentName
import android.support.v4.content.WakefulBroadcastReceiver
import android.app.Activity
import android.util.Log

class GcmBroadcastReceiver : WakefulBroadcastReceiver() {

    class object{
        fun completeWakefulIntent(intent : Intent?){
            WakefulBroadcastReceiver.completeWakefulIntent(intent)
        }
    }
    override fun onReceive(context : Context, intent : Intent) {
        val comp : ComponentName =
            ComponentName(context?.getPackageName(), javaClass<GcmIntentService>().getName())
        WakefulBroadcastReceiver.startWakefulService(context, (intent.setComponent(comp)))
        setResultCode(Activity.RESULT_OK)
    }
}