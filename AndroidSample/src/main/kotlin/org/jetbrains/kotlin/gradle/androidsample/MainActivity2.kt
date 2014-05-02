package org.jetbrains.kotlin.gradle.androidsample

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.view.Menu
import android.view.View
import android.widget.Button
import android.content.Context

open class MainActivity2: Activity() {

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var next: Button = findViewById(R.id.Button02) as Button
        next.setOnClickListener(object: View.OnClickListener {
            public override fun onClick(view: View): Unit {
                val intent: Intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })

        var userinfo: Button = findViewById(R.id.button2) as Button
        userinfo.setOnClickListener(object: View.OnClickListener {
            public override fun onClick(view: View): Unit {
                val i = Intent(getApplicationContext() as Context, javaClass<MainActivity3>())
                startActivity(i)
            }
        })

        var fb: Button = findViewById(R.id.button) as Button
        fb.setOnClickListener(object: View.OnClickListener {
            public override fun onClick(view: View): Unit {
                val i = Intent(getApplicationContext() as Context, javaClass<FBAuthActivity>())
                startActivity(i)
            }
        })

        var pickphoto: Button = findViewById(R.id.button3) as Button
        pickphoto.setOnClickListener(object: View.OnClickListener {
            public override fun onClick(view: View): Unit {
                val i = Intent(getApplicationContext() as Context, javaClass<PickphotoActivity>())
                startActivity(i)
            }
        })
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true
    }
    
}
