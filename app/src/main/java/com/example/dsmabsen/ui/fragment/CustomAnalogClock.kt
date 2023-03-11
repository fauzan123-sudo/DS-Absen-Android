package com.example.dsmabsen.ui.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.dsmabsen.R
import java.util.*

class CustomAnalogClock(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paintDetik = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintMenit = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintJam = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintCircleFill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var second = 0

    init {
        paintDetik.color = ContextCompat.getColor(context, R.color._danger)
        paintDetik.style = Paint.Style.STROKE
        paintDetik.strokeWidth = 15f

        paintMenit.color = ContextCompat.getColor(context, R.color._dark)
        paintMenit.style = Paint.Style.STROKE
        paintMenit.strokeWidth = 15f

        paintJam.color = ContextCompat.getColor(context, R.color._primary)
        paintJam.style = Paint.Style.STROKE
        paintJam.strokeWidth = 15f

//        paintCircle.color = ContextCompat.getColor(context, R.color.teal_700)
//        paintCircle.style = Paint.Style.STROKE
//        paintCircle.strokeWidth = 35f
//
//
//        paintCircleFill.color = ContextCompat.getColor(context, R.color.teal_200)
//        paintCircleFill.style = Paint.Style.FILL
//        paintCircleFill.strokeWidth = 25f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Menggambar AnalogClock dengan jarum jam dan menit
//        canvas.drawCircle(centerX, centerY, radius, paintCircle)
//        canvas.drawCircle(centerX, centerY, radius, paintCircleFill)
        canvas.drawLine(
            centerX, centerY,
            centerX + (radius * 0.5f) * kotlin.math.cos(Math.toRadians((getHour() * 30).toDouble() - 90))
                .toFloat(),
            centerY + (radius * 0.5f) * kotlin.math.sin(Math.toRadians((getHour() * 30).toDouble() - 90))
                .toFloat(),
            paintDetik
        )
        canvas.drawLine(
            centerX, centerY,
            centerX + (radius * 0.7f) * kotlin.math.cos(Math.toRadians((getMinute() * 6).toDouble() - 90))
                .toFloat(),
            centerY + (radius * 0.7f) * kotlin.math.sin(Math.toRadians((getMinute() * 6).toDouble() - 90))
                .toFloat(),
            paintMenit
        )

        // Menggambar jarum detik
        canvas.drawLine(
            centerX, centerY,
            centerX + (radius * 0.8f) * kotlin.math.cos(Math.toRadians((second * 6).toDouble() - 90))
                .toFloat(),
            centerY + (radius * 0.8f) * kotlin.math.sin(Math.toRadians((second * 6).toDouble() - 90))
                .toFloat(),
            paintJam
        )

        val clock = findViewById<CustomAnalogClock>(R.id.custom_analog_clock)

        val layoutParams = clock.layoutParams
        layoutParams.width = 200 // Set width to 200 pixels
        layoutParams.height = 200 // Set height to 200 pixels
        clock.layoutParams = layoutParams
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = (w / 2f) - 10f
        rect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }

    fun setTime(hour: Int, minute: Int, second: Int) {
        this.second = second
        invalidate()
    }

    private fun getHour(): Int {
        val calendar = Calendar.getInstance()
        return (calendar.get(Calendar.HOUR_OF_DAY) % 12)
    }

    private fun getMinute(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MINUTE)
    }
}