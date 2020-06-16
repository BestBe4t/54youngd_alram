package com.example.jotso.ui.people

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.jotso.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_people_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainPeopleFragment : Fragment() {

    companion object {
        fun newInstance() = MainPeopleFragment()
        val TAG = "People"
        var a_cnt = 0
        var b_cnt = 0
    }

    private lateinit var peopleViewModel: MainPeopleViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_people_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        peopleViewModel = ViewModelProvider(this).get(MainPeopleViewModel::class.java)
        // TODO: Use the ViewModel
        peopleViewModel.createNotificationChannel(this.context!!.applicationContext, NotificationManagerCompat.IMPORTANCE_DEFAULT, false, getString(R.string.app_name), "App notification channel")

        val channelId = "${android.R.attr.packageNames}-${getString(R.string.app_name)}"

        val intent = Intent(this.context, MainPeopleViewModel::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Notification 기능

        peopleViewModel.sendNotidication(this.context!!.applicationContext, channelId, pendingIntent)

        Log.d(TAG, "People")


        if (!peopleViewModel.isInternetConnected(this.context!!.applicationContext)) {
            Toast.makeText(this.context!!.applicationContext, "인터넷이 연결되어 있지 않습니다!", Toast.LENGTH_SHORT).show()
        }

        jot_imageA.setOnClickListener {
            a_cnt++
        }

        jot_imageB.setOnClickListener {
            b_cnt++
        }
    }
}
