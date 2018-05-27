package com.example.aven.projekt2

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import java.io.InputStream
import android.graphics.BitmapFactory
import android.util.Log


/**
 * Created by Aven on 2018-05-27.
 */
class ImageAsyncDownloader: AsyncTask<String, Void, Bitmap> {
    override fun doInBackground(vararg p0: String?): Bitmap? {
        val urldisplay = p0[0]
        var mIcon11: Bitmap? = null
        try {
            val inS = java.net.URL(urldisplay).openStream()
            mIcon11 = BitmapFactory.decodeStream(inS)
        } catch (e: Exception) {
            Log.e("Error", e.message)
            e.printStackTrace()
        }

        return mIcon11
    }

    var bmImage: ImageView
    var block: Block

     constructor(bmImage: ImageView, block: Block) {
        this.bmImage = bmImage
         this.block = block
    }

    override fun onPostExecute(result: Bitmap?) {
        bmImage.setImageBitmap(result)
        block.bitmap = result
    }
}