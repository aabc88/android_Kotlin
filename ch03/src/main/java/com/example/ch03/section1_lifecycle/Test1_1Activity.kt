package com.example.ch03.section1_lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch03.R
import com.example.ch03.databinding.ActivityTest11Binding

class Test1_1Activity : AppCompatActivity() {
    var count = 0
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

        binding.btnGoSome.setOnClickListener {
            startActivity(Intent(this, SomeActivity::class.java))

        }
        Log.d("test11", "onCreate...")

        binding.btnIncrement.setOnClickListener {
            count++
            binding.tvResult.text = count.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", count)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        Log.d("test11", "onStart...")
    }

    override fun onResume() {
        super.onResume()
        Log.d("test11", "onResume...")
    }

    override fun onPause() {
        super.onPause()
        Log.d("test11", "onPause...")
    }

    override fun onStop() {
        super.onStop()
        Log.d("test11", "onStop...")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("test11", "onDestroy...")
    }

    //최초실행
    //onCreate > onStart > onResume
    //화면전환 or 이동
    //onPause
    //onStop
    //종료
    //onDestroy

}