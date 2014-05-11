package com.dictav.androidsample

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
import android.graphics.Matrix
import android.graphics.Bitmap
import android.content.ContentValues
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

open class PickphotoActivity: Activity() {

    private val REQUEST_GALLERY = 0
    private val REQUEST_CAMERA = 1
    private var mImageUri: Uri? = null

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
        var button2 : Button = findViewById(R.id.button2) as Button
        button2.setOnClickListener(object: View.OnClickListener {
            public override fun onClick(view: View): Unit {
                onExtraClick(view);
            }
        })
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            val exifData = data?.getData()!!
            val ins: InputStream? = getContentResolver()?.openInputStream(exifData);
            val img: Bitmap? = BitmapFactory.decodeStream(ins);

            if (img != null) {
               // 選択した画像を表示
                (findViewById(R.id.imageView) as ImageView).setImageBitmap(pictureTurn(img, exifData));
            }else{
                Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            if (mImageUri == null) {
                Log.d("", "URI is null")
                return
            }
            val uri : Uri = mImageUri!!

            val ins : InputStream? = getContentResolver()?.openInputStream(uri);
            val img : Bitmap? = BitmapFactory.decodeStream(ins);
            ins?.close();
            if (img != null) {
                (findViewById(R.id.imageView) as ImageView).setImageBitmap(pictureTurn(img, uri));
            }else{
                Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true
    }

    public fun onExtraClick(view: View) {
        val filename = "" +System.currentTimeMillis() + ".jpg"

        val values = ContentValues()
        values.put(MediaStore.MediaColumns.TITLE, filename)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        mImageUri = getContentResolver()?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent()
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun pictureTurn(img : Bitmap, uri : Uri) : Bitmap {
        val columns = array<String>(MediaStore.MediaColumns.DATA)
        val c = getContentResolver()?.query(uri, columns, null, null, null)
        if (c == null) {
            Log.d("", "Could not get cursor");
            return img;
        }

        c.moveToFirst()
        val str = c.getString(0)
        if (str == null) {
            Log.d("", "Could not get exif");
            return img;
        }

        val exifInterface = ExifInterface(c.getString(0)!!)
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
        return Bitmap.createBitmap(img as Bitmap, 0, 0, img?.getWidth() as Int,
                img?.getHeight() as Int, mat, true)
    }

}
