package org.jetbrains.kotlin.gradle.androidsample

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.facebook.Session
import com.facebook.SessionState
import android.util.Log

/**
 * Created by sambaiz on 2014/04/27.
 */
open class FBAuthFragment : Fragment() {

    val TAG : String = "FBAuthFragment";

    public override fun onCreateView(inflater :LayoutInflater?, container: ViewGroup?,
            savedInstanceState :Bundle?) : View{
        return inflater?.inflate(R.layout.fragment_blank, container, false)!!
    }

    private fun onSessionStateChange(session: Session, state: SessionState , exception: Exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private val callback : Session.StatusCallback= object: Session.StatusCallback{
        public override fun call(session : Session?, state: SessionState?, exception: Exception?) {
            onSessionStateChange(session!!, state!!, exception!!);
        }
    };
}