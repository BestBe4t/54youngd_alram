package com.example.jotso

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.jotso.data.livedata
import com.example.jotso.ui.people.MainPeopleFragment
import com.example.jotso.ui.yongd.MainYongdFragment

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val ab = supportActionBar

        ab!!.setIcon(R.drawable.ic_launcher)
        ab.setDisplayUseLogoEnabled(true)
        ab.setDisplayShowHomeEnabled(true)

        val now_state = Now_State()
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
    }

    private fun showFragment(clazz: Class<out Fragment>, create: () -> Fragment) {
        val manager = supportFragmentManager

        if (!clazz.isInstance(manager.findFragmentById(R.id.container))){
            manager.beginTransaction().replace(R.id.container, create()).commit()
        }
    }

    fun Now_State():LiveData<livedata>{
        val prefs:SharedPreferences = application.getSharedPreferences("jotso", Context.MODE_PRIVATE)
        val StateListeners = mutableListOf<(livedata) -> Unit>()

        return object : LiveData<livedata>(){
            private val listener = { livedata: livedata->
                postValue(livedata)
            }

            init {
                val username = prefs.getString("username", "yong54")
                Log.d(TAG, username+" : username")

                value = when{
                    username != "yong54d" -> livedata.Yongd(username!!)
                    else -> livedata.People(username)
                }
            }

            override fun onActive() {
                StateListeners.add(listener)
            }

            override fun onInactive() {
                StateListeners.remove(listener)
            }
        }
    }
}
