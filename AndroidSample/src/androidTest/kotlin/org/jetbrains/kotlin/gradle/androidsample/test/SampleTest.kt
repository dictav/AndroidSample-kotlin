package org.jetbrains.kotlin.gradle.androidsample.test
import android.test.ActivityInstrumentationTestCase2
import android.widget.TextView
import android.test.ViewAsserts.assertOnScreen

// Import the activity to test and the app's R
import org.jetbrains.kotlin.gradle.androidsample.MainActivity
import org.jetbrains.kotlin.gradle.androidsample.R
import kotlin.test.*
import android.widget.Button

import org.msgpack.MessagePack
import redis.clients.jedis.Jedis
import com.android.vending.billing.IInAppBillingService

/**
 * Created by dictav on 4/22/14.
 */
class SampleTest() : ActivityInstrumentationTestCase2<MainActivity?>("org.jetbrains.kotlin.gradle.androidsample", javaClass<MainActivity?>()) {

    var myActivity : MainActivity? = null

    protected override fun setUp() {
        super<ActivityInstrumentationTestCase2>.setUp()
        myActivity = getActivity()
    }

    public fun testHelloWorld() {
        val textHello = myActivity?.findViewById(R.id.Button01) as Button
        assertEquals(textHello.getText(), "Next")
    }

    /* MessagePack の使い方 */
    public fun testMessagePack() {
        val src = MainActivity.Hoge()
        src.name = "shintaro"
        src.age = 33
        val msgpack = MessagePack()

        // byte列に書き出し
        val bytes = msgpack.write(src)
        // byte列からクラスを生成
        val dst = msgpack.read(bytes, javaClass<MainActivity.Hoge>())

        assertEquals(src.name,dst?.name)
        assertEquals(src.age,dst?.age)
    }

    /* Redis の使い方 */
    public fun testRedis() {
        val jedis = Jedis("10.0.2.2")
        jedis.set("hoge", "10")
        assertEquals(jedis.get("hoge"), "10")
    }

    /* Billing に接続 */
    public fun testBilling() {
        val mService = IInAppBillingService.Stub.asInterface(service);
        val response = mService.isBillingSupported(3, getPackageName(), "inapp");
        if(response == BILLING_RESPONSE_RESULT_OK) {
            // has billing!
        }
        else {
            // no billing V3...
        }
    }
}