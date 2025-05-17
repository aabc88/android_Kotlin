package com.example.ch5.section4

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch5.R
import com.example.ch5.databinding.ActivityTest41Binding
import com.example.ch5_outer.MyAIDLInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Test4_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest41Binding
    var connectionMode = "none"
    var aidlService: MyAIDLInterface? = null
    var aidlJob: Job? = null

    fun changeViewEnable() = when (connectionMode) {
        "aidl" -> {
            binding.ivPlay.isEnabled = false
            binding.ivStop.isEnabled = true
        }

        else -> {
            binding.ivPlay.isEnabled = true
            binding.ivStop.isEnabled = false
            binding.pb1.progress = 0
        }
    }

    val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            //서비스로부터 넘어온객체를 받음
            //프로세스간의 통신을 대행하는 Stub이다. aidl을 구현했음으로 실제 객체로 인지해서 함수호출
            aidlService = MyAIDLInterface.Stub.asInterface(service)
            aidlService?.start()
            binding.pb1.max = aidlService?.duration ?:0

            val scope = CoroutineScope(Dispatchers.Main + Job())
            aidlJob = scope.launch {
                while (binding.pb1.progress < binding.pb1.max) {
                    delay(1000)
                    binding.pb1.incrementProgressBy(1000)
                }
            }
            connectionMode = "aidl"
            changeViewEnable()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            aidlService = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTest41Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.ivPlay.setOnClickListener {
            //1
            //intent로 실행
            val intent = Intent("com.example.ch5.ACTION_SERVICE_AIDL")
            //bindService로 외부 앱 실행 시키려면 꼭 package 명시
            intent.setPackage("com.example.ch5_outer")
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        binding.ivStop.setOnClickListener {
            aidlService?.stop()
            unbindService(connection)
            aidlJob?.cancel()
            connectionMode = "none"
            changeViewEnable()
        }

    }//onCreate
    //본문
}