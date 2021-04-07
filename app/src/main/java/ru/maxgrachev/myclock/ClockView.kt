package ru.maxgrachev.myclock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class ClockView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var CLOCK_RADIUS = 0.0f
    private val pointPosition: PointF = PointF(0.0f, 0.0f)
    private val coeffH = 0.5f
    private val coeffM = 0.8f
    private val coeffS = 0.8f
    private val SECOND_NUMBER = 60
    private val MINUTE_NUMBER = 60
    private val HOUR_NUMBER = 12

    init {
        isClickable = true
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textSize = 50.0f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("Serif", Typeface.NORMAL)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        CLOCK_RADIUS = (min(w, h) / 2 * 0.9).toFloat()
    }

    private fun PointF.caculateArrowPosition(timeValue: Int, coeff: Float, unitsNumber: Int) {
        val angle = -Math.PI - 360 / unitsNumber * timeValue * Math.PI / 180
        x = (CLOCK_RADIUS * coeff * sin(angle)).toFloat() + width / 2
        y = (CLOCK_RADIUS * coeff * cos(angle)).toFloat() + height / 2
    }

    override fun onDraw(canvas: Canvas) {

        var currentTimeHours: Int = Calendar.getInstance().get(Calendar.HOUR)
        var currentTimeMinutes: Int = Calendar.getInstance().get(Calendar.MINUTE)
        var currentTimeSeconds: Int = Calendar.getInstance().get(Calendar.SECOND)

//        Clock
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), CLOCK_RADIUS, paint)

//         Draw the text labels
        paint.style = Paint.Style.FILL
        val labelRadius = CLOCK_RADIUS
        for (i in 1..12) {
            val angle = -Math.PI - 360 / 12 * i * Math.PI / 180
            val numTextX = (CLOCK_RADIUS * 0.85 * sin(angle)).toFloat() + width / 2
            val numTextY = (CLOCK_RADIUS * 0.85 * cos(angle)).toFloat() + height / 2+15f //TODO 15f????
            canvas.drawText(i.toString(), numTextX, numTextY, paint)
        }

//        Hours
        paint.color = Color.GRAY
        pointPosition.caculateArrowPosition(currentTimeHours, coeffH, HOUR_NUMBER)
        canvas.drawLine(
                (width / 2).toFloat(), (height / 2).toFloat(),
                pointPosition.x, pointPosition.y, paint)

//        Minutes
        pointPosition.caculateArrowPosition(currentTimeMinutes, coeffM, MINUTE_NUMBER)
        canvas.drawLine(
                (width / 2).toFloat(), (height / 2).toFloat(),
                pointPosition.x, pointPosition.y, paint)

//        Seconds
        paint.color = Color.RED
        pointPosition.caculateArrowPosition(currentTimeSeconds, coeffS, SECOND_NUMBER)
        canvas.drawLine(
                (width / 2).toFloat(), (height / 2).toFloat(),
                pointPosition.x, pointPosition.y, paint)

        postInvalidateDelayed(500);
    }
}