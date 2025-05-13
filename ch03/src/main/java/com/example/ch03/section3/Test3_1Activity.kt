package com.example.ch03.section3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch03.R
import com.example.ch03.databinding.ActivityTest31Binding
import kotlinx.coroutines.Runnable

class Test3_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest31Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTest31Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnToast.setOnClickListener {
            Toast.makeText(this, "show toast", Toast.LENGTH_SHORT).show()
        }

        binding.btnSum.setOnClickListener {
            /*var sum = 0L
            val obj = object: Runnable{
                override fun run() {
                    for (i in 1..20_000_000_00) sum += i
                    //에러 > toast
                    Toast.makeText(this@Test3_1Activity, "sum = $sum", Toast.LENGTH_SHORT).show()
                }
            }
            Thread(obj).start()
            binding.tvResult.text = "sum = $sum"*/

            //thread handler구조
            val handler = object : Handler(Looper.getMainLooper()) {
                //아래의 함수는 main thread에 의해 호출된다.
                //thread가 sendMessage() 하는 순간
                //매개변수는 thread에서 전달한 데이터

                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    if (msg.what == 1) {
                        Toast.makeText(this@Test3_1Activity, "${msg.obj as Long}", Toast.LENGTH_SHORT).show()
                        binding.tvResult.text = "sum = ${msg.obj as Long}"
                    }
                }
            }
            val obj = object : Runnable {
                override fun run() {
                    var sum = 0L
                    for (i in 1..5_000_000_000) sum += i
                    //데이터 발생 개발자 스레드에서 화면 변경 못하니 main Thread에게 의뢰

                    val message = Message()
                    message.what = 1
                    message.obj = sum
                    handler.sendMessage(message)//요청 순간 main thread 동작
                }
            }
            val thread = Thread(obj)
            thread.start()
        }
    }
}