package com.example.ch7.section3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch7.R
import com.example.ch7.databinding.ActivityTest31Binding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions

class Test3_1Activity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityTest31Binding
    var googleMap: GoogleMap? = null
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
        val mapFragment =
            supportFragmentManager.findFragmentById(binding.main.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun moveMap(latitude: Double, longitude: Double) {
        val latLng = com.google.android.gms.maps.model.LatLng(latitude, longitude)
        val position = com.google.android.gms.maps.model.CameraPosition.Builder()
            .target(latLng).zoom(16.0f).build()

        //center이동
        googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        //마커올리기
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        markerOptions.position(latLng)
        markerOptions.title("MyLocation")
        googleMap?.addMarker(markerOptions)

    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        moveMap(37.5666102, 126.9783881)
    }
}

/*

MD5: 1F:3D:6F:CC:35:EF:B4:A1:12:1C:CE:C5:38:A4:AE:73
SHA1: CA:12:BF:03:CD:E8:02:3D:85:D0:CB:45:3C:E8:A6:B7:F6:DD:00:5B
SHA-256: 39:C4:B6:68:41:E0:31:BA:D6:73:A8:7B:D1:E7:9D:FE:83:E3:67:40:42:FE:44:06:CF:0F:75:8D:58:54:F8:30
Valid until: 2055년 4월 9일 금요일*/
