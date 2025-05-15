package com.example.ch5.section2

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService2 : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        Log.d("EJ", "onCreate: MyService2")
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("oneChannel", "My Channel", NotificationManager.IMPORTANCE_HIGH)
            //등록 된 채널을 시스템에 등록
            manager.createNotificationChannel(channel)
            //등록 된 채널을 이용해 Builder 생성
            builder = NotificationCompat.Builder(this, "oneChannel")
        } else {
            builder = NotificationCompat.Builder(this)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("작업중")
        builder.setContentText("잠시만 대기")

        startForeground(11, builder.build())
    }
}