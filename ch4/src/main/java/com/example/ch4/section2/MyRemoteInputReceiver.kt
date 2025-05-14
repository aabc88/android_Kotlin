package com.example.ch4.section2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class MyRemoteInputReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val txt = RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence("key_reply")

        Log.d("EJ", "onReceive: ${txt}")

        //받은 다음 발생한 notification을 동일 id로 갱신하거나 취소시키지 않으면
        //nofi는 안사라지고 계속 돈다

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("oneChannel", "My Channel", NotificationManager.IMPORTANCE_HIGH)
            //등록 된 채널을 시스템에 등록
            manager.createNotificationChannel(channel)
            //등록 된 채널을 이용해 Builder 생성
            builder = NotificationCompat.Builder(context, "oneChannel")
        } else {
            builder = NotificationCompat.Builder(context)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("답장완료")
        builder.setContentText("메시지가 성공적으로 전송되었습니다.")

        manager.notify(11,builder.build())
    }
}