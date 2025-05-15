package com.example.ch5_outer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger

class MyMessengerService : Service() {
    lateinit var messenger: Messenger // 이 앱에서 만든 메신저 외부 앱이 이 메신저를 이용해 데이터 전달
    lateinit var replyMessenger: Messenger //외부 앱이 만든 메신저, 이 메신저로 외부 앱에 데이터 전달
    lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        messenger = Messenger(IncomingHandler(this))
        return messenger.binder
    }

    inner class IncomingHandler(context: Context) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                10 -> {
                    //음원 play 요청이라 가정
                    //외부에서 전달한 데이터 중 결과 데이터를 받을 messenger까지도 넘어왔다.
                    replyMessenger = msg.replyTo
                    if(!player.isPlaying) {
                        player = MediaPlayer.create(this@MyMessengerService, R.raw.music)
                        try {
                            val replyMsg = Message()
                            replyMsg.what = 10
                            val  replyBundle = Bundle()
                            replyBundle.putInt("durantion", player.duration)
                            replyMsg.obj = replyBundle
                            replyMessenger.send(replyMsg)

                            player.start()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                20 -> {
                    if (player.isPlaying) player.stop()
                }

            }
        }
    }
}