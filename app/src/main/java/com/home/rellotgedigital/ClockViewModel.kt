package com.home.rellotgedigital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ClockViewModel : ViewModel() {
    private val _currentTime = MutableLiveData<Calendar>()
    val currentTime: LiveData<Calendar> = _currentTime

    fun startClock(timeZone: TimeZone) {
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                _currentTime.value = Calendar.getInstance(timeZone)
                handler.postDelayed(this, 1000)
            }
        })
    }
}