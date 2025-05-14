package com.example.ch4.section1_broad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch4.R
import com.example.ch4.databinding.ActivityTest11Binding
import com.example.ch4.databinding.ActivityTest12Binding

class Test1_2Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest12Binding
    lateinit var screenReceiver: BroadcastReceiver
    lateinit var batteryReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTest12Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        screenReceiver = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?
            ) {
                //두번째 매개변수로 리시버를 실행시킨 intent 객체 전달.
                when (intent?.action) {
                    Intent.ACTION_SCREEN_ON -> Log.d("EJ", "onReceive: screen on")
                    Intent.ACTION_SCREEN_OFF -> Log.d("EJ", "onReceive: screen off")

                }
            }

        }
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)

        registerReceiver(screenReceiver, filter)
        //코드에서 원하는 시점에 디바이스의 배터리 상태 파악
        //각종 배터리 상태를 Intent에 extra데이터로 담아준다.
        val batteryIntent =
            registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        when (batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
            BatteryManager.BATTERY_PLUGGED_USB -> {
                binding.tvCharging.text = "USB Plugged"
                binding.ivCharging.setImageBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.usb
                    )
                )
            }

            BatteryManager.BATTERY_PLUGGED_AC -> {
                binding.tvCharging.text = "AC Plugged"
                binding.ivCharging.setImageBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ac
                    )
                )
            }

            else -> binding.tvCharging.text = "Not Plugged".also {
                binding.ivCharging.setImageBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ic_launcher_background
                    )
                )
            }
        }

        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 0
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 1

        val pct = level / scale.toFloat() * 100
        binding.tvPercent.text = "$pct"

        //이벤트 상황으로 배터리 상태 파악
        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_POWER_CONNECTED -> Log.d("EJ", "power connected...")
                    Intent.ACTION_POWER_DISCONNECTED -> Log.d("EJ", "power disconnected...")
                }
            }
        }

        val batterFilter = IntentFilter()
        batterFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        batterFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)

        registerReceiver(batteryReceiver, batterFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
        unregisterReceiver(batteryReceiver)
    }
}