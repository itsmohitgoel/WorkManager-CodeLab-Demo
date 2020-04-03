package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import timber.log.Timber
import java.lang.IllegalArgumentException

class BlurWorker(ctx : Context, parames: WorkerParameters) : Worker(ctx, parames) {

    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)
        return try {
//            val picture = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.test)
            if (TextUtils.isEmpty(resourceUri)) {
                Timber.e("Invalid  input Uri")
                throw IllegalArgumentException("Invalid input Uri")
            }

            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)))

            val output = blurBitmap(picture, appContext)

            val outputUri = writeBitmapToFile(appContext, output)

            // write bitmap to a temp file
            makeStatusNotification("Output is $outputUri", appContext)

            Result.success()
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error applying in blur")
            Result.failure()
        }
    }


}