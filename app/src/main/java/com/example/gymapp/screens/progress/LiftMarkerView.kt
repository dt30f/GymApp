package com.example.gymapp.screens.progress

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.gymapp.data.LiftEntry
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class LiftMarkerView(
    context: Context,
    private val lifts: List<LiftEntry>
) : MarkerView(context, android.R.layout.simple_list_item_1) {

    private val weightText = TextView(context)
    private val dateText = TextView(context)
    private val container = LinearLayout(context)

    private val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    init {
        // Container setup
        container.orientation = LinearLayout.VERTICAL
        container.setPadding(18, 14, 18, 14)
        container.gravity = Gravity.CENTER

        // Background: rounded + subtle border
        val bg = GradientDrawable().apply {
            cornerRadius = 18f
            setColor(Color.parseColor("#111A2E")) // dark surface
            setStroke(2, Color.parseColor("#1F2A40")) // subtle border
        }
        container.background = bg

        // Weight text
        weightText.apply {
            setTextColor(Color.WHITE)
            textSize = 16f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }

        // Date text
        dateText.apply {
            setTextColor(Color.parseColor("#AAB3C5"))
            textSize = 12f
        }

        container.addView(weightText)
        container.addView(dateText)

        // replace default content view
        val root = findViewById<TextView>(android.R.id.text1)
        root.text = ""
        root.setBackgroundColor(Color.TRANSPARENT)
        root.setPadding(0, 0, 0, 0)
        root.visibility = TextView.GONE

        addView(container)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e == null) return

        val index = e.x.toInt()
        if (index in lifts.indices) {
            val lift = lifts[index]
            weightText.text = "${lift.weight} kg"
            dateText.text = formatter.format(Date(lift.date))
        }

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        // centered above point
        return MPPointF(-(width / 2f), -height.toFloat() - 10f)
    }
}