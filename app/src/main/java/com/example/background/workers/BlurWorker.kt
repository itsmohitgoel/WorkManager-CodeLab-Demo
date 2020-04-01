package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R
import timber.log.Timber

class BlurWorker(ctx : Context, parames: WorkerParameters) : Worker(ctx, parames) {

    override fun doWork(): Result {
        val appContext = applicationContext

        makeStatusNotification("Blurring image", appContext)
        try {
            val picture = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.test)

            val output = blurBitmap(picture, appContext)

            val outputUri = writeBitmapToFile(appContext, output)

            // write bitmap to a temp file
            makeStatusNotification("Output is $outputUri", appContext)

            return Result.success()
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error applying in blur")
            return Result.failure()
        }
    }
}