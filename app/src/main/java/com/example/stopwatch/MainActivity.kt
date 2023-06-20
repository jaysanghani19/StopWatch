package com.example.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.stopwatch.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var isStarted = false

    private lateinit var service: Intent

    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnStart.setOnClickListener {
            startOrStop()
        }

        binding.btnReset.setOnClickListener {
            resetWatch()
        }
        // initializing the WatchService
        service = Intent(this, WatchService::class.java)

//        In Argument we're passing
//        updateTime as BroadcastReceiver and IntentFilter
        registerReceiver(updateTime, IntentFilter(WatchService.UPDATEDTIME))
    }

    //    startOrStop will check is stopwatch is running or not
    fun startOrStop() {
        if (isStarted)
            stopWatch()
        else
            startWatch()

    }

    //    stopWatch will stop the time by using the stopService
    fun stopWatch() {
        stopService(service)
        binding.btnStart.text = "START"
        isStarted = false

    }

    //    startWatch will start timer from left last time using startService
    fun startWatch() {
        service.putExtra(WatchService.CURRENTTIME, time)
        startService(service)
        binding.btnStart.text = "STOP"
        isStarted = true

    }

    //    ResetWatch will settime to 0
    fun resetWatch() {
        stopWatch()
        time = 0.0
        val timeString = setTime(time)
        binding.etTimer.text = timeString
    }

    //    Setting Time to Certain Format
//    Like hours:minutes:seconds
    fun setTime(t: Double): String {
        val intTime = t.roundToInt()
        val hours: Int = intTime % 86400 / 3600
        val minutes: Int = intTime % 86400 % 3600 / 60
        val seconds: Int = intTime % 86400 % 3600 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    //    To Receive Data from the WatchServiceClass
//    Using BroadcastReceiver
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(WatchService.CURRENTTIME, 0.0)
            binding.etTimer.text = setTime(time)
        }

    }
}


