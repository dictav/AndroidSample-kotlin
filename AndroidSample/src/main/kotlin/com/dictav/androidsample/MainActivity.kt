package com.dictav.androidsample

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
import com.dictav.androidsample.R
import org.msgpack.MessagePack
import org.msgpack.annotation.Message
import java.awt.Component
import com.android.vending.billing.IInAppBillingService
import android.os.IBinder
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.Context
import android.R.bool
import com.dictav.androidsample.util.IabHelper
import com.dictav.androidsample.util.IabResult
import com.dictav.androidsample.util.Inventory
import com.dictav.androidsample.util.Purchase
import android.os.Build
import android.widget.TextView
import com.dictav.androidsample.util.IabHelper.OnIabPurchaseFinishedListener
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.ConnectionResult
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.app.NotificationManager

/**
 * Created by dictav on 4/21/14.
 */

open class MainActivity: Activity() {
    // MessagePack sample class
    Message open class Hoge(){
        var name : String = ""
        var age : Int = 0
    }

    // prepare in app billing
    val TAG = "billing"
    val RC_REQUEST = 10001
    var base64Lisence = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoylAYewHK0GC8/lRxYZq423UZscLIpE2GxHd/lUnmxfaEGruTu0TgjD1PeGaLLkt6SkGzPZwqOjUP2cRceRszSeFpz+WEgh9iToJltbYrfd9J6wExUzSsjbJhvYI701emr0iEfZ+S2Re3vvxA4o/F+Exlt+QUhi0wr0Mskn1I6ocqek9IRP6mx0irwBUfsoasSHwzUmZ9vONxTp/vNbjw+QrTZpGePnSm4f6BIDVwY8hIH0QpCaTM9D/IGW/kKkXFmlLI8lUrqTr51ZlZgBAAkxjn6dHD59TX32KiZ9JgerhzSExiKeyPIC6c3J272lXZKZYXAvizmcIRJ6tEzzgqQIDAQAB"
    var mService : IInAppBillingService? = null
    var mHelper : IabHelper? = null

    private var purchase : Button? = null

    var mServiceConn : ServiceConnection = object : ServiceConnection {
        public override fun onServiceDisconnected(name: ComponentName?) : Unit {
            mService = null;
        }

        public override fun onServiceConnected(name: ComponentName?, service: IBinder?) : Unit {
            mService = IInAppBillingService.Stub.asInterface(service);
            val response = mService?.isBillingSupported(3, getPackageName(), "inapp");
            if(response == 0) {
                // has billing!
                Log.d("response","OK")
            }
            else {
                // no billing V3...
                Log.d("response","NO")
            }
        }
    }

    fun startBilling() {
        val model = Build.MODEL.toLowerCase()
        if (model.contains("sdk")) {
            Log.d(TAG, "Could not enable on Emulator")
           return
        }

        mHelper = IabHelper(this, base64Lisence)
        if (mHelper == null) {
            return
        }
        val helper = mHelper!!

        helper.startSetup(object : IabHelper.OnIabSetupFinishedListener {
            public override fun onIabSetupFinished(result: IabResult? ) {
                Log.d("tag", "Setup finished.");
                if (result == null) {
                    return
                }

                if (!result!!.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d("complain","Problem setting up in-app billing: " + result);
                    return
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d("tag", "Setup successful. Querying inventory.");
                helper.queryInventoryAsync(mGotInventoryListener);
            }
        });
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
        startBilling()

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
                intent.setClassName("com.dictav.androidsample", "com.dictav.androidsample.MainActivity2")
                startActivityForResult(intent, 0)
            }
        })

        purchase = findViewById(R.id.purchase) as Button
        purchase?.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                Log.d(TAG, "Buy doya purchase button clicked.")

                // launch the gas purchase UI flow.
                // We will be notified of completion via mPurchaseFinishedListener
                setWaitScreen(true)
                Log.d(TAG, "Launching purchase flow for doya maximum.")

                /* TODO: for security, generate your payload here for verification. See the comments on
                 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                 *        an empty string, but on a production app you should carefully generate this. */
                var payload : String? = null

                val purchaseKey = getResources()?.getString(R.string.doya_maximum)
                mHelper?.launchPurchaseFlow(view.getContext() as Activity
                        , purchaseKey
                        , RC_REQUEST
                        , mPurchaseFinishedListener
                        , "")
            }
        })
        purchase?.setEnabled(false)
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    val mGotInventoryListener = object : IabHelper.QueryInventoryFinishedListener {
        override fun onQueryInventoryFinished( result:IabResult?,  inventory:Inventory?) {
            Log.d(TAG, "Query inventory finished.")

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result == null || result!!.isFailure()) {
                Log.d("complain","Failed to query inventory: " + result)
                return;
            }

            Log.d(TAG, "Query inventory was successful.")

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             * */

            // Do we have the doya maximum?
            val purchaseKey = getResources()?.getString(R.string.doya_maximum)
            val doyaMaximumPurchase = inventory?.getPurchase(purchaseKey)
            var message = ""
            if (doyaMaximumPurchase != null && verifyDeveloperPayload(doyaMaximumPurchase)) {
                message = "User HAS doya maximum"
                purchase?.setEnabled(false)
            } else {
                message = "User DOES NOT HAVE doya maximum"
                purchase?.setEnabled(true)
            }

            val textView = findViewById(R.id.purchaseText) as TextView
            textView.setText(message)
            Log.d(TAG, message)
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    }

    val mPurchaseFinishedListener = object : IabHelper.OnIabPurchaseFinishedListener {
        override fun onIabPurchaseFinished(result:IabResult? , purchase:Purchase? ) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase)

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return

            if (result?.isFailure()!!) {
                Log.d(TAG, "Error purchasing: " + result)
                setWaitScreen(false)
                return
            }
            if (!verifyDeveloperPayload(purchase!!)) {
                Log.d(TAG,"Error purchasing. Authenticity verification failed.")
                setWaitScreen(false)
                return
            }

            Log.d(TAG, "Purchase successful.")

            val purchaseKey = getResources()?.getString(R.string.doya_maximum)
            if (purchase.getSku().equals(purchaseKey)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper?.consumeAsync(purchase, mConsumeFinishedListener);
            } else {
                Log.d(TAG, "Unexpected purchase")
            }
        }
    }

    // Called when consumption is complete
    val mConsumeFinishedListener = object: IabHelper.OnConsumeFinishedListener {
        override fun onConsumeFinished(purchase: Purchase? , result: IabResult? ) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result)

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return

            if (result != null && result.isSuccess()) {
                Log.d(TAG, "Consumption successful. Provisioning.")
            }
            else {
                Log.d(TAG, "Error while consuming: " + result)
            }
            setWaitScreen(false)
            Log.d(TAG, "End consumption flow.")
        }
    }



    /** Verifies the developer payload of a purchase. */
    fun verifyDeveloperPayload(p: Purchase): Boolean {
        val payload = p.getDeveloperPayload()

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true
    }



    public override fun onDestroy() : Unit {
        super.onDestroy()
        if (mService != null) {
            unbindService(mServiceConn)
        }
    }

    public fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d(TAG, requestCode.toString())
        Log.d(TAG, resultCode.toString())
        Log.d(TAG, data.toString())
        mHelper?.flagEndAsync()
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

    // Enables or disables the "please wait" screen.
    fun setWaitScreen(set: Boolean) {
        /*
        findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
        */
    }

}

