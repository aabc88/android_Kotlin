package com.example.ch5.section2

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.IBinder
import android.util.Log

//JobScheduler에 의해 실행 될 background작업
class MyJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("EJ", "onStartJob: ")
        return false//업무가 종료되면 onDestroy가 호출됨
        //true : 함수는 종료되었지만 업무는 아직 끝나지 않았어 기다려
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("EJ", "onStopJob: ")
        return true
    }

}