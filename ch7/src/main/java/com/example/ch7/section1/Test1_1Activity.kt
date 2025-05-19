package com.example.ch7.section1

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch7.R
import com.example.ch7.databinding.ActivityTest11Binding

class Test1_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest11Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTest11Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                Toast.makeText(this, "권한허용", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한거부", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, "android.permission.ACCESS_FINE_LOCATION"
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val manager = getSystemService(LOCATION_SERVICE) as LocationManager
                var result = "All providers : "
                //이 디바이스에 있는 모든 location provider 목록
                val providers = manager.allProviders

                for (provider in providers)
                    result += "$provider,"
                Log.d("EJ", "provider : ${providers}")

                result = "Enabled Provider : "
                val enabledProviders = manager.getProviders(true)
                for (provider in enabledProviders)
                    result += "$provider, "
                Log.d("EJ", "enabledProvider : ${result}")

                val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val accuracy = location.accuracy
                    val time = location.time

                    binding.tvResult.text = "위도 : $latitude, 경도 : $longitude, 정확도 : $accuracy, 시간 : $time"
                } ?: {
                    Log.d("EJ", "location null")
                }

            } else {
                launcher.launch("android.permission.ACCESS_FINE_LOCATION")
            }

        }
    }//onCreate
}//MainActivity