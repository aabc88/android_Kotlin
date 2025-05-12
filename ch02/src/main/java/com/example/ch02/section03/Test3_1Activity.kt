package com.example.ch02.section03

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch02.R
import com.example.ch02.databinding.ActivityTest31Binding
import java.io.File

class Test3_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest31Binding
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

        binding.btnInternalStorage.setOnClickListener {
            val file = File(filesDir, "test.txt") //내장 앱 별 메모리 루트의 test.txt
            val writeStream = file.writer()
            writeStream.write("hello world - internal")
            writeStream.flush()

            val readStream = file.reader().buffered()
            readStream.forEachLine {
                binding.tvResult.text = it
            }
        }

        binding.btnExternalStorage.setOnClickListener {
            val file = File(getExternalFilesDir(null), "test.txt")
            val writeStream = file.writer()
            writeStream.write("hello world - external")
            writeStream.flush()

            val readStream = file.reader().buffered()
            readStream.forEachLine {
                binding.tvResult.text = it
            }
        }
    }
}