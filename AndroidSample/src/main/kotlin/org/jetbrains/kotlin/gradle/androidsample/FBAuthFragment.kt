package org.jetbrains.kotlin.gradle.androidsample

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.facebook.Session
import com.facebook.SessionState
import android.util.Log
import com.facebook.UiLifecycleHelper
import android.content.Intent
import com.facebook.widget.LoginButton

/**
 * Created by sambaiz on 2014/04/27.
 */
open class FBAuthFragment : Fragment() {

    private val TAG : String = "FBAuthFragment";
    private var uiHelper : UiLifecycleHelper? = null

    public override fun onCreateView(inflater :LayoutInflater?, container: ViewGroup?,
            savedInstanceState :Bundle?) : View{
        val view = inflater?.inflate(R.layout.activity_fb_auth_activity, container, false)
        val authButton : LoginButton  = view?.findViewById(R.id.Button02) as LoginButton
        authButton.setFragment(this)

        return view!!
    }

    private fun onSessionStateChange(session: Session, state: SessionState , exception: Exception?) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private val callback : Session.StatusCallback= object: Session.StatusCallback{
        public override fun call(session : Session?, state: SessionState?, exception: Exception?) {
            onSessionStateChange(session!!, state!!, exception)
        }
    }

    public fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState);
        uiHelper = UiLifecycleHelper(getActivity(), callback);
        uiHelper?.onCreate(savedInstanceState);
    }

    public override fun onResume() {
        super.onResume();

        val session :Session? = Session.getActiveSession();
        if (session != null &&
        (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState()!!, null);
        }

        uiHelper?.onResume();
    }

    public override fun onActivityResult(requestCode: kotlin.Int, resultCode: kotlin.Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper?.onActivityResult(requestCode, resultCode, data);
    }

    public override fun onPause() {
        super.onPause();
        uiHelper?.onPause();
    }

    public override fun onDestroy() {
        super.onDestroy();
        uiHelper?.onDestroy();
    }

    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState);
        uiHelper?.onSaveInstanceState(outState);
    }
}