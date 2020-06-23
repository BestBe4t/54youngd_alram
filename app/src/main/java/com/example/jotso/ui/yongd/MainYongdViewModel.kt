package com.example.jotso.ui.yongd

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.jotso.R
import com.example.jotso.data.list_item
import com.example.jotso.data.msgdata
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import okhttp3.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class MainYongdViewModel : ViewModel() {
    companion object{
        val content = "Notification test"
        val NOTIFICATION_ID = 1001
        val URL = "https://www.youtube.com/channel/UCPE3HrEDpXeAB0_d1uLwAdQ"
        val PREFS = "54YongD"
        val TAG = "Yongd_Viewmodel"
        val PRFS_TITLE = "Title"
        val PRFS_CNT = "Cnt"
    }

    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
        }
    }
// 미완성(여러개의 리스트를 출력하는거 생각하기)
    fun getList(context: Context): ArrayList<list_item>{
        val Video_list=ArrayList<list_item>()
        val prfs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        for(i in 0..prfs.getInt(PRFS_CNT, 0)){
            var s_title = "title"+i.toString()
            var title = prfs.getString(PRFS_TITLE, "Test_Title")

            Video_list.add(list_item(i, title!!))
        }
        return Video_list
    }

    fun getHtml():Document{
        val url = URL
        val doc = Jsoup.connect(url).method(Connection.Method.GET).execute()
        val parse = doc.parse()
        return parse
    }

    fun isNewVideo(html: Document, context:Context):String{
        val prfs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val latest_title = extractionTitle(html.toString())
        val saved_title = prfs.getString(PRFS_TITLE, "No Title")
        if (latest_title == saved_title) {
            Log.v(TAG, saved_title)
            return "No Change"
        }else{
            val cnt = prfs.getInt(PRFS_CNT, 0)
            prfs.edit().apply{
                putString(PRFS_TITLE, latest_title)
                putInt(PRFS_CNT, cnt+1)
            }

            return latest_title
        }
    }

    fun extractionTitle(html: String):String{
        val split_text1 = "accessibilityData"; val split_text2 = "label"; val split_text3=":\""; val split_text4 = "게시자"
        val result = html.split(split_text1)[1].split(split_text2)[1].split(split_text3)[1].split(split_text4)[0]
        return result
    }

    fun sendingMsg(Title: String, Token:String?) {
        val JSON = MediaType.parse("application/json; charset=utf-8")//Post전송 JSON Type
        val FCM_url = "https://fcm.googleapis.com/fcm/send" //FCM HTTP를 호출하는 URL
        val serverKey = "AAAAyMXc4AY:APA91bGYIB9pfFRLGNK1ooPSINFaClCnQvVTxfViHQTsH90IlN7fBd_8kUTnF9N8O3RRV7A1IXeijp-IEwmorjc4OV4nmRIR8098-KX2X_UJiTmo5LryCTRqedNqRi2Y7FsPqXkCv66N"
        val To = Token
        val Message = "새로운 영상이 올라왔습니다!"
        //Firebase에서 복사한 서버키
        var okHttpClient = OkHttpClient()
        var gson = Gson()

        var pushGson = msgdata()
        pushGson.to = To                  //푸시토큰 세팅
        pushGson.notification?.title = Title  //푸시 타이틀 세팅
        pushGson.notification?.body = Message //푸시 메시지 세팅

        var body = RequestBody.create(JSON, gson?.toJson(pushGson)!!)
        var request = Request
            .Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=" + serverKey)
            .url(FCM_url)       //푸시 URL 세팅
            .post(body)     //pushDTO가 담긴 body 세팅
            .build()
        okHttpClient?.newCall(request)?.enqueue(object : Callback {
            //푸시 전송
            override fun onFailure(call: Call?, e: IOException?) {
                Log.d(TAG, "push fail")
            }

            override fun onResponse(call: Call?, response: Response?) {
                println(response?.body()?.string())  //요청이 성공했을 경우 결과값 출력
                println(request.toString())
            }
        })
    }

    fun getToken(context: Context):String?{
        var token:String? = null
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result?.token

                // Log and toast
                Log.d(TAG, token)

            })
        return token
    }
}
