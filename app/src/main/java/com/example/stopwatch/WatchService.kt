package com.example.stopwatch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.Timer
import java.util.TimerTask

class WatchService : Service() {
    private val timer = Timer()

    //    onStartCommand it'll start the timer
//    using timer.scheduledAtFixedRate function
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getDoubleExtra(CURRENTTIME, 0.0)
        timer.scheduleAtFixedRate(stopWatchTimer(time), 0, 1000)
        return START_NOT_STICKY

    }

    //    onDestroy it'll stop the timer
    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    //    Key's to send and receive data for service
    companion object {
        const val CURRENTTIME = "current time"
        const val UPDATEDTIME = "updated time"
    }

    //    This innerclass will increase the time and send through broadcast
//    it'll make intent and through that we will send data
    inner class stopWatchTimer(private var time: Double) : TimerTask() {
        override fun run() {
            val intent = Intent(UPDATEDTIME)
            time++
            intent.putExtra(CURRENTTIME, time)
            sendBroadcast(intent)
        }

    }
}