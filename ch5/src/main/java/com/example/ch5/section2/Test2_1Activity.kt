package com.example.ch5.section2

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch5.R
import com.example.ch5.databinding.ActivityTest11Binding
import com.example.ch5.databinding.ActivityTest21Binding

class Test2_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest21Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTest21Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    Toast.makeText(this, "권한 허용됨", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            }

        binding.btnBackground.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this, ""
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    //서비스를 foreground service로 구동
                    val serviceIntent = Intent(this, MyService2::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent)
                    } else {
                        startService(serviceIntent)
                    }
                } else {
                    permissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                }
            } else {
                //서비스를 foreground service로 구동
                val serviceIntent = Intent(this, MyService2::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                } else {
                    startService(serviceIntent)
                }
            }
        }

        binding.btnJob.setOnClickListener {
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            //Builder에 어떤 JobService를 실행시키는 정보인지가 명시되었다.
            //jobId : 1 개발자가 주는 식별자
            //나중에 외부에서 동작하는 JobService를 종료시킬 때 사용
            val builder = JobInfo.Builder(1, ComponentName(this, MyJobService::class.java))
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)//wifi
            jobScheduler?.schedule(builder.build())
        }


    }
}