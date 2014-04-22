package org.jetbrains.kotlin.gradle.androidsample.test
import android.test.ActivityInstrumentationTestCase2
import android.widget.TextView
import android.test.ViewAsserts.assertOnScreen

// Import the activity to test and the app's R
import org.jetbrains.kotlin.gradle.androidsample.MainActivity
import org.jetbrains.kotlin.gradle.androidsample.R
import kotlin.test.*
import android.widget.Button

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
}