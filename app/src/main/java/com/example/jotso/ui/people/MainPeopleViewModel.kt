package com.example.jotso.ui.people

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.jotso.R
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MainPeopleViewModel : ViewModel() {
    companion object{
        val titile = "5454"
        val content = "Notification test"
        val NOTIFICATION_ID = 1001
    }

    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
        }
    }

    fun sendNotidication(context: Context, channelID: String, pendingIntent: PendingIntent){
        val builder = NotificationCompat.Builder(context, channelID)
        builder.setSmallIcon(R.drawable.ic_launcher)
        builder.setContentTitle(titile)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}
