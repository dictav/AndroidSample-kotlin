package org.jetbrains.kotlin.gradle.androidsample

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.view.Menu
import android.content.Context
import android.app.NotificationManager

/**
 * Created by dictav on 4/21/14.
 */

open class NotificateActivity: Activity() {

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
    }

    override fun onResume(){
        super.onResume()
        val ex : Intent? = getIntent()
        val mManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mManager.cancelAll()
        GcmIntentService.notification_num = 0
        val intent : Intent = Intent(this, javaClass<MainActivity>())
        startActivity(intent)
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu)
        return true
    }

}

