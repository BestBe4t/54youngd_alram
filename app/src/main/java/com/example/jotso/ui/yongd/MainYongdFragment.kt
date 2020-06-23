package com.example.jotso.ui.yongd

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.jotso.MYFirebaseMessagingService
import com.example.jotso.R
import com.example.jotso.data.list_item
import com.example.jotso.data.m_adapter
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.android.synthetic.main.main_yongd_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainYongdFragment : Fragment() {

    companion object {
        fun newInstance() = MainYongdFragment()
        val TAG = "Yongd"
    }

    private lateinit var yongdViewModel: MainYongdViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_yongd_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        yongdViewModel = ViewModelProvider(this).get(MainYongdViewModel::class.java)
        // TODO: Use the ViewModel
        yongdViewModel.createNotificationChannel(this.context!!.applicationContext, NotificationManagerCompat.IMPORTANCE_DEFAULT, false, getString(R.string.app_name), "App notification channel")

        val channelId = "${android.R.attr.packageNames}-${getString(R.string.app_name)}"

        val intent = Intent(this.context, MainYongdViewModel::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val video_list:ArrayList<list_item> = yongdViewModel.getList(this.context!!)
        val adap = m_adapter(this.context!!, video_list)

        list_view.adapter = adap


        //Notification 기능

        GlobalScope.launch {
            val html = yongdViewModel.getHtml()
            val latest_title = yongdViewModel.isNewVideo(html, context!!)

            if (latest_title != "No Change"){

                Log.v(TAG, latest_title)
            }else{
                val token = yongdViewModel.getToken(context!!)
                yongdViewModel.sendingMsg(latest_title, token)
                Log.v(TAG, "No Change")
            }
        }

    }
}
