package org.jetbrains.kotlin.gradle.androidsample

import android.os.Bundle
import android.app.Activity
import android.view.Menu
import android.widget.TextView
import android.widget.ImageView
import com.facebook.Session
import com.facebook.SessionState
import android.util.Log
import com.facebook.Request
import com.facebook.model.GraphUser
import com.facebook.Response
import android.widget.Toast

open class MainActivity3: Activity() {

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        (findViewById(R.id.userpicture) as ImageView).setImageResource(R.drawable.com_facebook_profile_default_icon);
        (findViewById(R.id.username) as TextView).setText("");
        (findViewById(R.id.usernickname) as TextView).setText("");
        (findViewById(R.id.useremail) as TextView).setText("");
        (findViewById(R.id.userbirthday) as TextView).setText("");
        (findViewById(R.id.usergender) as TextView).setText("");
    }

    public override fun onResume() {
        super.onResume();

        val session :Session? = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            onSessionOpen(session, session.getState()!!, null);
        }else{
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
        }
    }

    private fun onSessionOpen(session: Session, state: SessionState , exception: Exception?) {
        if (state.isOpened()) {
            Request.newMeRequest(session, object: Request.GraphUserCallback {
                // callback after Graph API response with user object
                override fun onCompleted(user : GraphUser?, response: Response?) {
                    if (user != null) {
                        Log.i("aaaa", user.getInnerJSONObject().toString());
                        //(findViewById(R.id.userpicture) as ImageView).setImageURI
                        (findViewById(R.id.username) as TextView).setText(user.getName());
                        (findViewById(R.id.usernickname) as TextView).setText(user.getUsername());
                        (findViewById(R.id.useremail) as TextView).setText(user.getProperty("email").toString());
                        (findViewById(R.id.userbirthday) as TextView).setText(user.getProperty("birthday").toString());
                        (findViewById(R.id.usergender) as TextView).setText(user.getProperty("gender").toString());
                    }
                }
            })?.executeAsync();
        }
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true
    }
    
}
