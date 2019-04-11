package com.designdemo.uaha.workers

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.designdemo.uaha.util.KEY_NOTIF_LASTDATE
import com.designdemo.uaha.util.NotifUtil
import java.text.SimpleDateFormat
import java.util.*

class NotifWorker(cxt: Context, params: WorkerParameters) : Worker(cxt, params) {

    private val dateFormatter = SimpleDateFormat(
            "MM/dd/yyyy 'at' HH:mm",
            Locale.getDefault()
    )

    /**
     * Send a notification with the current date & time
     */
    override fun doWork(): Result {
        val formattedDate = dateFormatter.format(Date())

        val outputData = Data.Builder()
                .putString(KEY_NOTIF_LASTDATE, formattedDate)
                .build()

        NotifUtil.makeStatusNotification(
                "Sent: ${dateFormatter.format(Date())}",
                applicationContext
        )
        return Result.success(outputData)
    }
}