package com.osueat.hungry.notification

import android.app.Activity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.osueat.hungry.R
import java.util.*

class NotificationSender(activity: Activity) {

    private val activity = activity

    val ORDER_IN_PROGRESS_TITLE = "Your order is in progress now"
    val ORDER_IN_PROGRESS_CONTENT = "Delicious food is coming soon!"
    val ORDER_READY_TITLE = "Your order is ready now"
    val ORDER_READY_CONTENT = "Enjoy your meal and have a good day!"

    fun sendNotification(title: String, content: String) {
        val notificationHandler = NotificationHandler(activity)
        notificationHandler.createNotificationChannel()
        var builder = NotificationCompat.Builder(activity, "1")
            .setSmallIcon(R.drawable.cat_background)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(activity)) {
            // notificationId is a unique int for each notification that you must define
            notify((Date().time % 86400).toInt() , builder.build())
        }
    }

}