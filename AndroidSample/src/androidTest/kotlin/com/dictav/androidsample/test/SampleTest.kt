package com.dictav.androidsample.test
import android.test.ActivityInstrumentationTestCase2
import android.widget.TextView
import android.test.ViewAsserts.assertOnScreen

// Import the activity to test and the app's R
import com.dictav.androidsample.MainActivity
import com.dictav.androidsample.R
import kotlin.test.*
import android.widget.Button

import org.msgpack.MessagePack
import redis.clients.jedis.Jedis
import com.android.vending.billing.IInAppBillingService
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.regions.Region
import org.apache.commons.logging.Log
import android.util.Log
import com.amazonaws.services.s3.model.Bucket
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ResponseHeaderOverrides
import com.amazonaws.services.s3.model.PutObjectRequest
import java.io.File

/**
 * Created by dictav on 4/22/14.
 */
class SampleTest() : ActivityInstrumentationTestCase2<MainActivity?>("com.dictav.androidsample", javaClass<MainActivity?>()) {

    var myActivity : MainActivity? = null

    protected override fun setUp() {
        super<ActivityInstrumentationTestCase2>.setUp()
        myActivity = getActivity()
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
        assertNotNull(jedis)
        jedis.set("hoge", "10")
        assertEquals(jedis.get("hoge"), "10")
    }

    /* AWS S3 の使い方 */
    public fun testAWS() {
        val accessKey = myActivity?.getString(R.string.aws_access_key)
        val secretKey = myActivity?.getString(R.string.aws_secret_key)
        val bucketName = "doya"
        val s3Client = AmazonS3Client( BasicAWSCredentials(accessKey, secretKey) )
        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1))
        val buckets = s3Client.listBuckets()
        assertNotNull(buckets)
        assertTrue( buckets!!.size > 0 )

        // upload
        val fileName = "piyo.txt"
        val path = myActivity?.getFilesDir()
        val stream = myActivity?.openFileOutput(fileName, 0)
        stream?.write("hello, aws".toByteArray())
        stream?.close()

        val file = File(path,fileName)
        val por = PutObjectRequest("doya",fileName,file)
        s3Client.putObject(por)
    }
}
