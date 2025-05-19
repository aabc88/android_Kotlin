package com.example.ch7.section2

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch7.R
import com.example.ch7.databinding.ActivityTest11Binding
import com.example.ch7.databinding.ActivityTest21Binding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

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

        binding.btnLocation.setOnClickListener {
            //위치정보 api
            val providerClient = LocationServices.getFusedLocationProviderClient(this)
            val launcher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                if (it) {
                    Toast.makeText(this, "권한허용", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "권한거부", Toast.LENGTH_SHORT).show()
                }
            }
            //GoogleApiClient에 등록할 콜백 준비
            //위치정보 제공자가 결정되거나 이용하던 제공자가 이용 불가능 상태의 콜백
            val connectionCallback = object : GoogleApiClient.ConnectionCallbacks {
                //이용 가능 시 callback
                override fun onConnected(p0: Bundle?) {
                    if (ContextCompat.checkSelfPermission(
                            this@Test2_1Activity,
                            "android.permission.ACCESS_FINE_LOCATION"
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        providerClient.lastLocation.addOnSuccessListener(
                            this@Test2_1Activity,
                            object : OnSuccessListener<Location> {
                                override fun onSuccess(p0: Location?) {
                                    val latitude = p0?.latitude
                                    val longitude = p0?.longitude
                                    val accuracy = p0?.accuracy
                                    val time = p0?.time

                                    binding.tvResult.text = "위도 : $latitude, 경도 : $longitude, 정확도 : $accuracy, 시간 : $time"
                                }

                            })
                    } else {
                        launcher.launch("android.permission.ACCESS_FINE_LOCATION")
                    }
                }

                //이용하던 제공자가 이용 불가능 상태로 변하는 순간
                override fun onConnectionSuspended(p0: Int) {
                    Toast.makeText(this@Test2_1Activity, "일시 제공자 불능", Toast.LENGTH_SHORT).show()
                }

            }
            //현 시점 디바이스에 이용 가능 제공자가 없을 때 콜
            val failedCallback = object : GoogleApiClient.OnConnectionFailedListener {
                override fun onConnectionFailed(p0: ConnectionResult) {

                }
            }
            //GoogleApiClient 초기화하면서 준비한 콜백 등록
            val apiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)//play-service의 다양한 api중 어느 api를 사용 ?
                .addConnectionCallbacks(connectionCallback)
                .addOnConnectionFailedListener(failedCallback)
                .build()

            //위치정보 제공자 결정, 결과는 각종 등록된
            apiClient.connect()
        }
    }
}