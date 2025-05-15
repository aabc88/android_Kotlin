package com.example.ch4.section2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch4.R
import com.example.ch4.databinding.ActivityTest21Binding

class Test2_1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest21Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTest21Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    noti()
                } else {
                    Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            }

        binding.btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this, ""
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    noti()
                } else {
                    permissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                }
            } else {
                noti()
            }
        }

        //onclick
    }


    fun noti() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("oneChannel", "My Channel", NotificationManager.IMPORTANCE_HIGH)
            //등록 된 채널을 시스템에 등록
            manager.createNotificationChannel(channel)
            //등록 된 채널을 이용해 Builder 생성
            builder = NotificationCompat.Builder(this, "oneChannel")
        } else {
            builder = NotificationCompat.Builder(this)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("메시지 도착")
        builder.setContentText("안녕하세요")

        //확장 터치 이벤트
        val intent = Intent(this, DetailActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pIntent)
        //action추가
        val actionIntent = Intent(this, MyNotificationReceiver::class.java)
        val actionPendingIntent =
            PendingIntent.getBroadcast(this, 20, actionIntent, PendingIntent.FLAG_IMMUTABLE)
        builder.addAction(
            NotificationCompat.Action.Builder(
                android.R.drawable.stat_notify_chat,
                "Action",
                actionPendingIntent
            ).build()
        )
        //remote input
        val remoteInput = RemoteInput.Builder("key_reply").run {
            setLabel("답장")
            build()
        }
        //준비된 remoteInput을 Action으로 추가
        val remoteIntent = Intent(this, MyRemoteInputReceiver::class.java)
        val remotePendingIntent =
            PendingIntent.getBroadcast(this, 30, remoteIntent, PendingIntent.FLAG_MUTABLE)
        builder.addAction(
            NotificationCompat.Action.Builder(
                R.drawable.send, "답장", remotePendingIntent
            ).addRemoteInput(remoteInput).build()
        )
        //알림 발생

        /*//big picture style - 알림 눌렀을 때 사진 나오게
        val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.big)
        val bigStyle = NotificationCompat.BigPictureStyle()
        bigStyle.bigPicture(bigPicture)
        builder.setStyle(bigStyle)*/

        //big text style
        /*val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.bigText(resources.getString(R.string.big_txt))
        builder.setStyle(bigTextStyle)*/

        val person1: Person = Person.Builder()
            .setName("EJ")
            .setIcon(IconCompat.createWithResource(this,R.drawable.person1))
            .build()

        val person2: Person = Person.Builder()
            .setName("EZ")
            .setIcon(IconCompat.createWithResource(this,R.drawable.person2))
            .build()

        val message1 = NotificationCompat.MessagingStyle.Message(
            "hello",
            System.currentTimeMillis(),
            person1
        )

        val message2 = NotificationCompat.MessagingStyle.Message(
            "hello",
            System.currentTimeMillis(),
            person2
        )

        val messageStyle = NotificationCompat.MessagingStyle(person1)
            .addMessage(message1)
            .addMessage(message2)
        builder.setStyle(messageStyle)

        manager.notify(11, builder.build())

    }

}
