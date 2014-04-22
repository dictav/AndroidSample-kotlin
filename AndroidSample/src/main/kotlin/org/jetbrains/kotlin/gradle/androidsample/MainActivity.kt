package org.jetbrains.kotlin.gradle.androidsample

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.view.Menu
import android.view.View
import android.widget.Button
import android.util.Log
import org.jetbrains.kotlin.gradle.androidsample.R
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;

/**
 * Created by dictav on 4/21/14.
 */

open class MainActivity: Activity() {
    Message open class Hoge(){
        var name : String = ""
        var age : Int = 0
    }

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true
    }

}

