package com.example.ch02.section01

import android.os.Binder
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch02.R
import com.example.ch02.databinding.ActivitySomeBinding

class SomeActivity : AppCompatActivity() {
    lateinit var binding: ActivitySomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGet.setOnClickListener {
            val sharedPref = getSharedPreferences("data", MODE_PRIVATE)
            val data1 = sharedPref.getString("data1", "")
            val data2 = sharedPref.getInt("data2", 0)
            binding.tvResult.text = "data1 : $data1, data2 : $data2"

            val sharedPref2 = getPreferences(MODE_PRIVATE)
            val data3 = sharedPref2.getString("data1", "")
            val data4 = sharedPref2.getInt("data2", 0)
            binding.tvActivityResult.text = "data1 : $data3, data2 : $data4"
        }
    }
}