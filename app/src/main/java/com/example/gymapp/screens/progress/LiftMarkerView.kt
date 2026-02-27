package com.example.gymapp.screens.progress

import android.content.Context
import android.graphics.Color
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

    private val tvContent: TextView = findViewById(android.R.id.text1)
    private val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    init {
        tvContent.setTextColor(Color.WHITE)
        tvContent.setBackgroundColor(Color.parseColor("#1E1E1E"))
        tvContent.setPadding(16, 8, 16, 8)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val index = e.x.toInt()
            if (index in lifts.indices) {
                val lift = lifts[index]
                val dateStr = formatter.format(Date(lift.date))
                tvContent.text = "${lift.weight} kg\n$dateStr"
            }
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}
