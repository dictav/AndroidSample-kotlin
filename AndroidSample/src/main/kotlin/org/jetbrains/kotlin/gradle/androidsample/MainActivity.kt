package org.jetbrains.kotlin.gradle.androidsample

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.view.Menu
import android.view.View
import android.widget.Button
import org.msgpack.annotation.Message;
import com.google.android.gms.gcm.GoogleCloudMessaging
import android.content.Context
import android.os.AsyncTask
import android.util.Log

/**
 * Created by dictav on 4/21/14.
 */

open class MainActivity: Activity() {
    Message open class Hoge(){
        var name : String = ""
        var age : Int = 0
    }

    val GCM_SENDER_ID : String = "691669003379"
    var gcm : GoogleCloudMessaging? = null
    var context : Context? = null

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = getApplicationContext()
        gcm = GoogleCloudMessaging.getInstance(this);
        registerInBackground();

        var next: Button = findViewById(R.id.Button01) as Button
        next setOnClickListener (object: View.OnClickListener {
            public override fun onClick(view: View): Unit {

                /* ??? why don't work?
                 * val intent = Intent(view.getContext(), javaClass<MainActivity2>())
                 */
                val intent = Intent()
                intent.setClassName("org.jetbrains.kotlin.gradle.androidsample", "org.jetbrains.kotlin.gradle.androidsample.MainActivity2");
                startActivityForResult(intent, 0)


            }
        })
    }

    fun registerInBackground() {
        object : AsyncTask<Unit, Unit, String>() {
            override protected fun doInBackground(vararg args: Unit?): String? {
                var msg : String = ""
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                val regid : String? = gcm?.register(GCM_SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                return msg;
            }

            override protected fun onPostExecute(result: String) {
                Log.i("device-registered", result)
            }
        }.execute()
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true
    }

}

