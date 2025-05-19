package com.example.ch8.section1

import android.Manifest
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyCallback
import android.telephony.TelephonyCallback.CallStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch8.R
import com.example.ch8.databinding.ActivityTest11Binding

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
        val launcher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it.all { it.value })
                    Toast.makeText(this, "권한허용", Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, "권한거부", Toast.LENGTH_SHORT).show()
            }

        binding.btnGet.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_NUMBERS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    telephonyManager.registerTelephonyCallback(
                        mainExecutor,
                        object : TelephonyCallback(), CallStateListener {
                            override fun onCallStateChanged(state: Int) {
                                when (state) {
                                    TelephonyManager.CALL_STATE_IDLE -> {
                                        Log.d("EJ", "idle")
                                    }

                                    TelephonyManager.CALL_STATE_RINGING -> {
                                        Log.d("EJ", "ringing")
                                    }

                                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                                        Log.d("EJ", "offhook")
                                    }
                                }

                            }

                        })
                }
                var phoneNumber = ""
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val subManager = getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                    for (info: SubscriptionInfo in subManager.activeSubscriptionInfoList!!) {
                        val id = info.subscriptionId
                        phoneNumber = subManager.getPhoneNumber(id)
                    }
                } else {
                   phoneNumber = telephonyManager.line1Number
                }
                getRequestNetwork()
            } else {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_PHONE_NUMBERS
                    )
                )
            }
        }
    }

    private fun getRequestNetwork() {
        val networkReq = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        val conManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        conManager.requestNetwork(networkReq, object: ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                Log.d("EJ", "onAvailable")
            }
            override fun onLost(network: android.net.Network) {
                super.onLost(network)
                Log.d("EJ", "onLost")
            }
            override fun onUnavailable() {
                super.onUnavailable()
                Log.d("EJ", "onUnavailable")
            }
        })
    }
}