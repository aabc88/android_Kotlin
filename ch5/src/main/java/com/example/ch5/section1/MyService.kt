package com.example.ch5.section1

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        Log.d("EJ", "onBind: Myservice onBind")
        return MyBinder()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("EJ", "Myservice onCreate")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("EJ", "Myservice onDestroy")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("EJ", "Myservice onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("EJ", "Myservice onUnbind")
        return super.onUnbind(intent)
    }

    //bindService()로 자신을 구동시킨곳에 전달할 객체
    //Binder만 상속받아 작성하고, 클래스 내부의 작성 규칙은 없다.
    class MyBinder : Binder() {
        fun funA(arg: Int) = arg * arg
    }
}