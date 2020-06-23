package com.example.jotso

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.jotso.data.livedata
import com.example.jotso.ui.people.MainPeopleFragment
import com.example.jotso.ui.share.Dialog
import com.example.jotso.ui.yongd.MainYongdFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {
    companion object{
        val TAG = "MainActivity"
        var a_cnt = 0
        var b_cnt = 0
        val PREFS = "54YongD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val ab = supportActionBar

        ab!!.setIcon(R.drawable.ic_launcher)
        ab.setDisplayUseLogoEnabled(true)
        ab.setDisplayShowHomeEnabled(true)

        val now_state = Now_State()
//        registerToken()

        now_state.observe(this, Observer { livedata ->
            when(livedata){
                is livedata.Yongd -> {
                    Log.d(TAG, "Yongd")
                    showFragment(MainYongdFragment::class.java){
                        MainYongdFragment()
                    }
                }

                is livedata.People -> {
                    Log.d(TAG, "People")
                    showFragment(MainPeopleFragment::class.java){
                        MainPeopleFragment()
                    }
                }
            }
        })

        if (!isInternetConnected(this)) {
            Toast.makeText(this, "인터넷이 연결되어 있지 않습니다!", Toast.LENGTH_SHORT).show()
        }

        jot_imageA.setOnClickListener {
            a_cnt++
            chkYongd(a_cnt, b_cnt)
        }

        jot_imageB.setOnClickListener {
            b_cnt++
            chkYongd(a_cnt, b_cnt)
        }
    }

    private fun showFragment(clazz: Class<out Fragment>, create: () -> Fragment) {
        val manager = supportFragmentManager

        if (!clazz.isInstance(manager.findFragmentById(R.id.container))){
            manager.beginTransaction().replace(R.id.container, create()).commit()
        }
    }

    fun Now_State():LiveData<livedata>{
        val prefs:SharedPreferences = application.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val StateListeners = mutableListOf<(livedata) -> Unit>()

        return object : LiveData<livedata>(){
            private val listener = { livedata: livedata->
                postValue(livedata)
            }

            init {
                val username = prefs.getString("username", "no Value")

                value = when(username){
                    "yong54d" -> livedata.Yongd(username)
                    else -> livedata.People(username!!)
                }
                Log.d(TAG, value.toString()+" : username_value")
            }

            override fun onActive() {
                StateListeners.add(listener)
            }

            override fun onInactive() {
                StateListeners.remove(listener)
            }
        }
    }

    fun isInternetConnected(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCap = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCap) ?: return false

        return actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(
            NetworkCapabilities.TRANSPORT_ETHERNET) ||actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    fun chkYongd(a_cnt:Int, b_cnt:Int){
        Log.d(TAG, a_cnt.toString() + " " + b_cnt.toString())
        if(a_cnt == 5 && b_cnt == 4) {
            Dialog(this).Dialog()
        }
    }

    fun CloudMessaging(){

    }

//    fun registerToken(){
//        var pushToken:String? = null
//        var uid = FirebaseAuth.getInstance().currentUser!!.uid
//        var map = mutableMapOf<String, Any>()
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            pushToken = it.token
//            map["pushtoken"] = pushToken!!
//            FirebaseFirestore.getInstance().collection("pushtoken").document(uid!!).set(map)
//        }
//    }

//    fun reset_SDK(){
//        val options =
//            FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.getApplicationDefault())
//                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
//                .build()
//
//        FirebaseApp.initializeApp(options)
//    }
}
