package com.example.ch03.section3

import android.os.AsyncTask
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

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
            /*val handler = object : Handler(Looper.getMainLooper()) {
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
            thread.start()*/

            //방법2 - AsyncTask(deprecate)
            //Thread, Handler에의해 처리되어야 할 내용을 AsyncTask상속 클래스에 담아서 처리
            //제네릭타입 : 백그라운드 작업에 전달 될 테이터타입, 스레드에 의해 반복적으로 발생되는 데이터 타입, 스레드의 최종 결과 데이터타입
            /*class MyAsyncTask: AsyncTask<Void, Long, Long>() {
                override fun doInBackground(vararg params: Void?): Long? {
                    var sum = 0L
                    for(i in 1..1000000000) sum += i

                    return sum
                }

                override fun onPostExecute(result: Long?) {
                    //main Thread에 의해 실행
                    //UI
                    Toast.makeText(this@Test3_1Activity, "$result", Toast.LENGTH_SHORT).show()
                }
            }
            MyAsyncTask().execute()*/

            //방법3 코루틴
            //Main Thread에 의해 실행될 코루틴과 백그라운드 업무를 담당하기 위한 코루틴이 같이 동작
            //두 코루틴간 데이터 전달을 위해서
            val channel = Channel<Int>()

            //백그라운드 업무를 담당하는 코루틴이 실행될 스코프
            //코루틴은 항상 스코프 내에서 실행
            //Dispatchers.Default - 극한의 cpu작업이 필요한 코루틴을 돌리는 스레드
            val scope = CoroutineScope(Dispatchers.Default + Job())
            scope.launch {
                //코루틴 구동
                var sum = 0L
                for (i in 1..1000000000) sum += i

                //다른 코루틴에게 데이터 전달 channel을 이용해서
                channel.send(sum.toInt())
            }

            GlobalScope.launch(Dispatchers.Main) {
                //Main Thread에의해 실행되는 코루틴 구동
                channel.consumeEach {
                    //channel에 데이터가 발행된다면
                    Toast.makeText(this@Test3_1Activity, "$it", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }
}