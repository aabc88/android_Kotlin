package com.example.ch02.section4_camera

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch02.R
import com.example.ch02.databinding.ActivityTest41Binding
import java.io.File
import java.text.SimpleDateFormat

class Test4_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest41Binding
    lateinit var filePath: String
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

        val thumbnailLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            //데이터 획득 이미지 데이터가 직접 전달된다.
            val bitmap = it.data?.extras?.get("data") as android.graphics.Bitmap
            binding.ivCameraResult.setImageBitmap(bitmap)
        }



        binding.btnThumb.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            thumbnailLauncher.launch(intent)
        }

        val fileLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                val option = BitmapFactory.Options()
                option.inSampleSize = 10
                val bitmap = BitmapFactory.decodeFile(filePath, option)
                bitmap?.let {
                    binding.ivCameraResult.setImageBitmap(bitmap)
                }
            }
        binding.btnFile.setOnClickListener {
            val timeStamp = SimpleDateFormat("yyyyMMdd=HHmmss").format(System.currentTimeMillis())
            val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", dir)
            filePath = file.absolutePath
            //camera app 에게 공개 할 파일정보
            val uri = FileProvider.getUriForFile(
                this,
                "com.example.ch02.fileprovider",
                file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            fileLauncher.launch(intent)

        }


    }
}