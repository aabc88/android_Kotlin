package com.example.ch5.section3

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch5.R
import com.example.ch5.databinding.ActivityTest31Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Test3_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest31Binding
    var connectionMode = "none"
    lateinit var messenger: Messenger
    lateinit var replyMessenger: Messenger

    var messengerJob: Job? = null//코루틴

    fun changeViewEnable() = when (connectionMode) {
        "messenger" -> {
            binding.ivPlay.isEnabled = false
            binding.ivStop.isEnabled = true
        }

        else -> {
            binding.ivPlay.isEnabled = true
            binding.ivStop.isEnabled = false
            binding.pb1.progress = 0
        }
    }

    //외부에서 전달한 데이터를 받기 위한 handler
    inner class HandleReplyMsg : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                //7
                10 -> {
                    val bundle = msg.obj as Bundle
                    bundle.getInt("duration")?.let {
                        when {
                            it > 0 -> {
                                binding.pb1.max = it
                                //코루틴 구동 시켜서 코루틴에 의해 pb 값 변경
                                val scope = CoroutineScope(Dispatchers.Main + Job())
                                messengerJob = scope.launch {
                                    while (binding.pb1.progress < binding.pb1.max) {
                                        delay(1000)
                                        binding.pb1.incrementProgressBy(1000)
                                    }
                                }
                                changeViewEnable()
                            }

                            else -> {
                                connectionMode = "none"
                                unbindService(connection)
                                changeViewEnable()
                            }
                        }
                    }
                }
            }
        }
    }

    val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            //3
            messenger = Messenger(service)
            val msg = Message()
            msg.what = 10
            msg.replyTo = replyMessenger
            //4
            messenger.send(msg)
            connectionMode = "messenger"
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTest31Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replyMessenger = Messenger(HandleReplyMsg())

        binding.ivPlay.setOnClickListener {
            //1
            //intent로 실행
            val intent = Intent("com.example.ch5_outer.ACTION_SERVICE_Messenger")
            //bindService로 외부 앱 실행 시키려면 꼭 package 명시
            intent.setPackage("com.example.ch5_outer")
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        binding.ivStop.setOnClickListener {
            val msg = Message()
            msg.what = 20
            //a
            messenger.send(msg)
            unbindService(connection)
            messengerJob?.cancel()
            connectionMode = "none"
            changeViewEnable()
        }

    }

    override fun onStop() {
        super.onStop()
        if(connectionMode=="messenger") {
            val msg = Message()
            msg.what = 20
            messenger.send(msg)
            unbindService(connection)
        }

        connectionMode = "none"
        changeViewEnable()
    }
}