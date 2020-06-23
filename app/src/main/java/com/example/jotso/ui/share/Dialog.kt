package com.example.jotso.ui.share

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import com.example.jotso.R

class Dialog(context: Context){
    companion object{
        val PREFS = "54YongD"
        val TAG = "Dialog"
        lateinit var code:EditText
    }

    val context = context
    val pref = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun Dialog(){
        val builder = AlertDialog.Builder(context)
        val inflater:LayoutInflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.dialog, null)
        code = v.findViewById(R.id.code)
        builder.setView(v)
            .setPositiveButton("입력"){ dilog, i ->
                set_code(code.text.toString())
            }
            .setNegativeButton("취소", null)
            .show()
        builder.create()
    }

    fun set_code(code:String){
        val edit = pref.edit()
        edit.apply {
            putString("username", "yong54d")
            putString("code", code)
        }.apply()
        Log.d(TAG, "username: " + pref.getString("username", "noName") +" / code: " + pref.getString("code", "noCode"))
    }
}