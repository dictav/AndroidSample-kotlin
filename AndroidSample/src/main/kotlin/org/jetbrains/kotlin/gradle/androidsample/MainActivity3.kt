package org.jetbrains.kotlin.gradle.androidsample

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import android.widget.ImageView
import com.facebook.Session
import com.facebook.SessionState
import com.facebook.Request
import com.facebook.model.GraphUser
import com.facebook.Response
import java.net.URL
import java.io.InputStream
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.app.LoaderManager.LoaderCallbacks
import android.support.v4.content.Loader
import android.support.v4.app.FragmentActivity
import java.net.HttpURLConnection
import java.io.BufferedInputStream
import android.content.SharedPreferences
import android.widget.Toast

open class MainActivity3: FragmentActivity(){

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super<FragmentActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        (findViewById(R.id.userpicture) as ImageView).setImageResource(R.drawable.com_facebook_profile_default_icon)
        (findViewById(R.id.username) as TextView).setText("")
        (findViewById(R.id.usernickname) as TextView).setText("")
        (findViewById(R.id.useremail) as TextView).setText("")
        (findViewById(R.id.userbirthday) as TextView).setText("")
        (findViewById(R.id.usergender) as TextView).setText("")
    }

    public override fun onResume() {
        super<FragmentActivity>.onResume()
        var session :Session? = Session.getActiveSession()
        if(session==null){
            session = Session.openActiveSessionFromCache(this);
        }
        if(session!=null && session?.isOpened()!!){
            onSessionOpen(session!!, session?.getState()!!, null)
        }else{
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show()
        }

    }

    private fun onSessionOpen(session: Session, state: SessionState , exception: Exception?) {
        if (state.isOpened()) {
            Request.newMeRequest(session, object: Request.GraphUserCallback {
                // callback after Graph API response with user object
                override fun onCompleted(user : GraphUser?, response: Response?) {
                    if (user != null) {

                        val args : Bundle = Bundle();
                        args.putString("url", "http://graph.facebook.com/" + user.getId() + "/picture?type=large");
                        getSupportLoaderManager()?.initLoader(0, args, object: LoaderCallbacks<Bitmap> {
                            override fun onLoaderReset(p0: Loader<Bitmap>?) {
                            }

                            override fun onLoadFinished(p0: Loader<Bitmap>?, p1: Bitmap?) {
                                (findViewById(R.id.userpicture) as ImageView).setImageBitmap(p1)
                            }
                            override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Bitmap>? {
                                if(p1 != null) {
                                    val url : String? = p1?.getString("url")
                                    return AsyncWorker(getApplicationContext(), url)
                                }
                                return null
                            }
                        })?.forceLoad()

                        (findViewById(R.id.username) as TextView).setText(user.getName())
                        (findViewById(R.id.usernickname) as TextView).setText(user.getUsername())
                        (findViewById(R.id.useremail) as TextView).setText(user.getProperty("email").toString())
                        (findViewById(R.id.userbirthday) as TextView).setText(user.getProperty("birthday").toString())
                        (findViewById(R.id.usergender) as TextView).setText(user.getProperty("gender").toString())
                    }
                }
            })?.executeAsync()
        }
    }

    private class AsyncWorker(context : Context?, url : String?) : AsyncTaskLoader<Bitmap>(context) {

        var url : String? = url

        override fun loadInBackground(): Bitmap?  {
            val profilePicUrl: URL = URL(url)
            val connection : HttpURLConnection = profilePicUrl.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input : InputStream? = connection.getInputStream()
            val bafinput : BufferedInputStream = BufferedInputStream(input!!)
            val bmp : Bitmap? = BitmapFactory.decodeStream(bafinput)
            //input?.close()
            //connection.disconnect()
            return bmp
        }
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu)
        return true
    }

    
}
