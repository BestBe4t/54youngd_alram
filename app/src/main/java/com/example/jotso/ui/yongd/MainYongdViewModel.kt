package com.example.jotso.ui.yongd

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.jotso.R
import com.example.jotso.data.list_item
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MainYongdViewModel : ViewModel() {
    companion object{
        val titile = "5454"
        val content = "Notification test"
        val NOTIFICATION_ID = 1001
        val URL = "https://www.youtube.com/channel/UCPE3HrEDpXeAB0_d1uLwAdQ"
        val PREFS = "54YongD"
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

    fun isInternetConnected(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val networkCap = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCap) ?: return false

            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }else{
            connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
        }
    }

    fun getSource():Document{
        val url = URL
        val doc = Jsoup.connect(url).method(Connection.Method.GET).execute()
        val title = doc.parse()
        return title
    }

    fun getList(context: Context): ArrayList<list_item>{
        val Video_list=ArrayList<list_item>()
        val prfs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        for(i in 0..prfs.getInt("cnt", 0)){
            var s_title = "title"+i.toString()
            var s_content = "content"+i.toString()
            var title = prfs.getString(s_title, "Test_Title")
            var content = prfs.getString(s_content, "Test_Content")

            Video_list.add(list_item(i, title!!, content!!))
        }

        return Video_list
    }
}
