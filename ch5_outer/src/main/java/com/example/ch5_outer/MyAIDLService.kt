package com.example.ch5_outer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MyAIDLService : Service() {
    lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        player= MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onBind(intent: Intent): IBinder {
        return object : MyAIDLInterface.Stub() {
            override fun start() {
                if(!player.isPlaying){
                    player = MediaPlayer.create(this@MyAIDLService, R.raw.music)
                    try {
                        player.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun stop() {
                if(player.isPlaying) player.stop()
            }

            override fun getDuration(): Int {
                return if (player.isPlaying)
                    player.duration
                else 0
            }
        }
    }


}