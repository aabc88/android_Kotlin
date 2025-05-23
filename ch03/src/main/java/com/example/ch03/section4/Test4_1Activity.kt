package com.example.ch03.section4

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch03.R
import com.example.ch03.databinding.ActivityTest31Binding
import com.example.ch03.databinding.ActivityTest41Binding

class Test4_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest41Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTest41Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.btnShow.setOnClickListener {
            //글 입력이 되는 ET에 포커스 지정하고 코드에서 키보드를 올림
            binding.etKey.requestFocus()
            manager.showSoftInput(binding.etKey, InputMethodManager.SHOW_IMPLICIT)

        }
        binding.btnHide.setOnClickListener {
            manager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }
    }
}