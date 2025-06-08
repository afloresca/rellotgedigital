package com.home.rellotgedigital

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.home.rellotgedigital.R
import java.util.*
import kotlin.math.*

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : View(context, attrs, defStyleAttr) {

    private var time: Calendar? = null
    private val timePaint = Paint()
    private val datePaint = Paint()

    init {
        setupPaints()
    }

    private fun setupPaints() {
        with(timePaint) {
            color = ContextCompat.getColor(context, R.color.text)
            textAlign = Paint.Align.CENTER
            typeface = Typeface.createFromAsset(context.assets, "digi.ttf")
            isAntiAlias = true
        }

        with(datePaint) {
            color = ContextCompat.getColor(context, R.color.text)
            textAlign = Paint.Align.CENTER
            typeface = timePaint.typeface
            isAntiAlias = true
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {

        super.onConfigurationChanged(newConfig)
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateTextSizes(w, h)
    }

    fun updateTextSizes(width: Int, height: Int) {
        val screenMin = min(width, height) // Use the smaller dimension as reference

        timePaint.textSize = screenMin * if (width > height) 0.70f else 0.40f

        // Date text = 40% of time text size
        datePaint.textSize = timePaint.textSize * 0.4f

        // Ensure minimum readable sizes
        timePaint.textSize = max(timePaint.textSize, 32f) // At least 32px
        datePaint.textSize = max(datePaint.textSize, 12f) // At least 12px
    }


    fun updateTime(newTime: Calendar) {
        time = newTime
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        time?.let { drawTime(canvas, it) }
    }

    private fun drawTime(canvas: Canvas, time: Calendar) {
        val centerX = width / 2f
        val centerY = height / 1.75f
        setBackgroundColor(getResources().getColor(R.color.background))
        // Draw time
        val timeText = String.format("%02d:%02d",
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE))
        canvas.drawText(timeText, centerX, centerY, timePaint)

        // Draw date
        val dateText = String.format("%s %02d-%02d-%d",
            getWeekdayAbbreviation(time),
            time.get(Calendar.DAY_OF_MONTH),
            time.get(Calendar.MONTH) + 1,
            time.get(Calendar.YEAR))
        canvas.drawText(dateText, centerX, centerY + datePaint.textSize, datePaint)
    }

    private fun getWeekdayAbbreviation(time: Calendar): String {
        return when (time.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "DL."
            Calendar.TUESDAY -> "DM."
            Calendar.WEDNESDAY -> "DC."
            Calendar.THURSDAY -> "DJ."
            Calendar.FRIDAY -> "DV."
            Calendar.SATURDAY -> "DS."
            Calendar.SUNDAY -> "DG."
            else -> ""
        }
    }
}