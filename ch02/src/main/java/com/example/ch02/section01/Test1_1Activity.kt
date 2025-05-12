package com.example.ch02.section01

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch02.R
import com.example.ch02.databinding.ActivityTest11Binding

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

        binding.btnGo.setOnClickListener {
            val intent = Intent(this, SomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnSave.setOnClickListener {
            //preference 객체 획득
            val pref = getSharedPreferences("data", MODE_PRIVATE)
            pref.edit().run {
                putString("data1", "hello")
                putInt("data2", 10)
                apply()
            }

            //단일 액티비티의 데이터 영속화
            val sharedPref2 = getPreferences(MODE_PRIVATE)
            sharedPref2.edit().run {
                putString("data1", "hello")
                putInt("data2", 10)
                apply()
            }
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