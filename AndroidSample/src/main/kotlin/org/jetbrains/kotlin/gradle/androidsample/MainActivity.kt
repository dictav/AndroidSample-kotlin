package org.jetbrains.kotlin.gradle.androidsample

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.view.Menu
import android.view.View
import android.widget.Button
import android.util.Log
import org.jetbrains.kotlin.gradle.androidsample.R
import org.msgpack.MessagePack
import org.msgpack.annotation.Message
import java.awt.Component
import com.android.vending.billing.IInAppBillingService
import android.os.IBinder
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.Context
import android.R.bool
import org.jetbrains.kotlin.gradle.androidsample.util.IabHelper
import org.jetbrains.kotlin.gradle.androidsample.util.IabResult
import org.jetbrains.kotlin.gradle.androidsample.util.Inventory
import org.jetbrains.kotlin.gradle.androidsample.util.Purchase
import android.provider.Settings.Secure
import android.os.Build

/**
 * Created by dictav on 4/21/14.
 */

open class MainActivity: Activity() {
    Message open class Hoge(){
        var name : String = ""
        var age : Int = 0
    }

    /*
    IInAppBillingService mService;

ServiceConnection mServiceConn = new ServiceConnection() {
   @Override
   public void onServiceDisconnected(ComponentName name) {
       mService = null;
   }

   @Override
   public void onServiceConnected(ComponentName name,
      IBinder service) {
       mService = IInAppBillingService.Stub.asInterface(service);
   }
};
     */

    // create
    val TAG = "tag"
    var base64Lisence = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoylAYewHK0GC8/lRxYZq423UZscLIpE2GxHd/lUnmxfaEGruTu0TgjD1PeGaLLkt6SkGzPZwqOjUP2cRceRszSeFpz+WEgh9iToJltbYrfd9J6wExUzSsjbJhvYI701emr0iEfZ+S2Re3vvxA4o/F+Exlt+QUhi0wr0Mskn1I6ocqek9IRP6mx0irwBUfsoasSHwzUmZ9vONxTp/vNbjw+QrTZpGePnSm4f6BIDVwY8hIH0QpCaTM9D/IGW/kKkXFmlLI8lUrqTr51ZlZgBAAkxjn6dHD59TX32KiZ9JgerhzSExiKeyPIC6c3J272lXZKZYXAvizmcIRJ6tEzzgqQIDAQAB"
    var mService : IInAppBillingService? = null
    var mHelper : IabHelper? = null

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

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startBilling()
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    val mGotInventoryListener = object : IabHelper.QueryInventoryFinishedListener {
        override fun onQueryInventoryFinished( result:IabResult?,  inventory:Inventory?) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result == null || result!!.isFailure()) {
                Log.d("complain","Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            // Do we have the infinite gas plan?
            Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
            mSubscribedToInfiniteGas = (infiniteGasPurchase != null &&
            verifyDeveloperPayload(infiniteGasPurchase));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
            + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
                return;
            }

            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
             */
        }
    }


    public override fun onDestroy() : Unit {
        super.onDestroy()
        if (mService != null) {
            unbindService(mServiceConn)
        }
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true
    }
}

