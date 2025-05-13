package com.example.ch3_outer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch3_outer.databinding.ActivityOneBinding

class OneActivity : AppCompatActivity() {
    lateinit var binding: ActivityOneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGoSelf.setOnClickListener {
            //standard로 실행, 매번 객체가 생성되며 task에 계속 추가됨

            /*val intent = Intent(this, OneActivity::class.java)
            startActivity(intent)*/

            //singleTop, manifest에 선언하는 것이 좀 더 일반적이지만 테스트 편의성을 위해
            //코드에서 선언
            // task의 top에 있는 경우에 한해서 다시 생성하지 않는다.
            val intent = Intent("com.example.ch3_outer.ACTION_ONE")
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("one", "onNewIntent")
    }
}