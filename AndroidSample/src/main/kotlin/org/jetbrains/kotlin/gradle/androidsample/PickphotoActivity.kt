package org.jetbrains.kotlin.gradle.androidsample

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.view.Menu
import android.view.View
import android.widget.Button
import android.graphics.BitmapFactory
import java.io.InputStream
import android.widget.ImageView
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.graphics.Matrix
import android.graphics.Bitmap

open class PickphotoActivity: Activity() {

    private val REQUEST_GALLERY = 0;

    protected override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickphoto)

        var button : Button = findViewById(R.id.button) as Button
        button.setOnClickListener(object: View.OnClickListener {
            public override fun onClick(view: View): Unit {
                val intent : Intent = Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        })
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            val options : BitmapFactory.Options = BitmapFactory.Options();
            options.inSampleSize = 8; //load scales bitmaps to memory
            val ins : InputStream? = getContentResolver()?.openInputStream(data?.getData()!!);
            val img = BitmapFactory.decodeStream(ins);
            ins?.close();

            val columns = array<String>("_data") //kotin can't find "MediaStore.Images.Media.DATA"
            val c = getContentResolver()?.query(data?.getData() as Uri, columns, null, null, null)
            c?.moveToFirst()
            val exifInterface = ExifInterface(c?.getString(0)!!)
            val exifR : Int = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            val orientation : Float =
                    when (exifR) {
                        ExifInterface.ORIENTATION_ROTATE_90 ->  90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }

            val mat : Matrix? = Matrix()
            mat?.postRotate(orientation)
            val turnedBitmap = Bitmap.createBitmap(img as Bitmap, 0, 0, img?.getWidth() as Int,
                    img?.getHeight() as Int, mat, true)

            // 選択した画像を表示
            (findViewById(R.id.imageView) as ImageView).setImageBitmap(turnedBitmap);
        }
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true
    }

}
