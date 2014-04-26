package org.jetbrains.kotlin.gradle.androidsample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.facebook.android.Facebook
import com.facebook.android.FacebookError
import com.facebook.android.DialogError
import com.facebook.android.Facebook.DialogListener
import android.util.Log
import android.support.v4.app.FragmentActivity

open class FBAuthActivity: FragmentActivity() {

    val FB_APP_ID = "283294208447253"
    var facebook : Facebook? = null
    var fb_fragment : FBAuthFragment? = null

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fb_auth_activity)

        facebook = Facebook(FB_APP_ID)
        val button: Button = findViewById(R.id.Button02) as Button
        button setOnClickListener (object: View.OnClickListener {
            public override fun onClick(view: View): Unit {

                if (savedInstanceState == null) {
                    fb_fragment = FBAuthFragment()
                    getSupportFragmentManager()?.beginTransaction()
                    ?.add(android.R.id.content, fb_fragment)?.commit();
                } else {
                    fb_fragment = getSupportFragmentManager()?.findFragmentById(android.R.id.content) as FBAuthFragment
                }
            }
        })
    }
}
