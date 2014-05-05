package org.jetbrains.kotlin.gradle.androidsample

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.view.Menu
import android.view.View
import android.widget.Button
import org.msgpack.annotation.Message
import com.google.android.gms.gcm.GoogleCloudMessaging
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.ConnectionResult
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.app.NotificationManager

/**
 * Created by dictav on 4/21/14.
 */

open class MainActivity: Activity() {
    Message open class Hoge(){
        var name : String = ""
        var age : Int = 0
    }

    private val EXTRA_MESSAGE : String = "message"
    private val PROPERTY_REG_ID : String = "registration_id"
    private val PROPERTY_APP_VERSION : String = "appVersion"

    private val PLAY_SERVICES_RESOLUTION_REQUEST : Int = 9000
    private val GCM_TAG : String = "GCM on MainActivity"
    private val GCM_SENDER_ID : String = "691669003379"
    private var gcm : GoogleCloudMessaging? = null
    private var context : Context? = null
    private var regid : String? = null

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkPlayServices()) {
            context = getApplicationContext()
            gcm = GoogleCloudMessaging.getInstance(this)
            regid = getRegistrationId(context!!)

            if (regid?.isEmpty()!!) {
                registerInBackground()
            }else{
                Log.i(GCM_TAG, "Device already registered , registration ID=" + regid)
            }
        } else {
            Log.i(GCM_TAG, "No valid Google Play Services APK found.")
        }

        var next: Button = findViewById(R.id.Button01) as Button
        next setOnClickListener (object: View.OnClickListener {
            public override fun onClick(view: View): Unit {

                /* ??? why don't work?
                 * val intent = Intent(view.getContext(), javaClass<MainActivity2>())
                 */
                val intent = Intent()
                intent.setClassName("org.jetbrains.kotlin.gradle.androidsample", "org.jetbrains.kotlin.gradle.androidsample.MainActivity2")
                startActivityForResult(intent, 0)


            }
        })
    }

    ////////for GCM Method start

    private fun checkPlayServices() : Boolean{
        val resultCode : Int = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST)?.show()
            } else {
                Log.i(GCM_TAG, "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }

    private fun registerInBackground() {
        object : AsyncTask<Unit, Unit, String>() {
            override protected fun doInBackground(vararg args: Unit?): String? {
                var msg : String = ""
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context)
                }
                regid = gcm?.register(GCM_SENDER_ID)
                msg = "Device registered, registration ID=" + regid
                storeRegistrationId(context, regid)
                return msg
            }

            override protected fun onPostExecute(result: String) {
                Log.i(GCM_TAG, result)
            }
        }.execute()
    }

    private fun storeRegistrationId(context : Context?, regId : String?) {
        val prefs : SharedPreferences  = getGCMPreferences(context!!)
        val appVersion : Int = getAppVersion(context!!)
        Log.i(GCM_TAG, "Saving regId on app version " + appVersion)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString(PROPERTY_REG_ID, regId)
        editor.putInt(PROPERTY_APP_VERSION, appVersion)
        editor.commit()
    }

    private fun getRegistrationId(context : Context) : String {
        val prefs : SharedPreferences = getGCMPreferences(context)
        val registrationId : String = prefs.getString(PROPERTY_REG_ID, "")!!
        if (registrationId.isEmpty()) {
            Log.i(GCM_TAG, "Registration not found.")
            return ""
        }

        val registeredVersion : Int = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE)
        val currentVersion : Int = getAppVersion(context)
        if (registeredVersion != currentVersion) {
            Log.i(GCM_TAG, "App version changed.")
            return ""
        }
        return registrationId
    }

    private fun getGCMPreferences(context : Context) : SharedPreferences {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(javaClass<MainActivity>().getSimpleName(),
                Context.MODE_PRIVATE)
    }

    private fun getAppVersion (context : Context) : Int{
        val packageInfo : PackageInfo = context.getPackageManager()?.
        getPackageInfo(context.getPackageName(), 0)!!
        return packageInfo.versionCode
    }

    ///////////////for GCM Method end

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu)
        return true
    }

}

