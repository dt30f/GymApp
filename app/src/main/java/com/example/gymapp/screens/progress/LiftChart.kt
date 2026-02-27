package com.example.gymapp.screens.progress

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gymapp.data.LiftEntry
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LiftChart(lifts: List<LiftEntry>) {

    val dateFormatter = remember {
        SimpleDateFormat("dd/MM", Locale.getDefault())
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),

        factory = { context ->
            LineChart(context).apply {

                setBackgroundColor(Color.parseColor("#121212"))

                axisRight.isEnabled = false
                description.isEnabled = false
                legend.textColor = Color.WHITE

                xAxis.apply {
                    textColor = Color.WHITE
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    isGranularityEnabled = true
                    labelRotationAngle = -45f
                }

                axisLeft.apply {
                    textColor = Color.WHITE
                    setDrawGridLines(true)
                    gridColor = Color.DKGRAY
                }

                setScaleEnabled(true)
                setPinchZoom(true)
                isDragEnabled = true
                setVisibleXRangeMaximum(5f)
            }
        },

        update = { chart ->

            if (lifts.isEmpty()) {
                chart.clear()
                return@AndroidView
            }

            val entries = lifts.mapIndexed { index, lift ->
                Entry(index.toFloat(), lift.weight)
            }

            val dataSet = LineDataSet(entries, "Weight Progress").apply {
                color = Color.parseColor("#692020")
                circleRadius = 6f
                setCircleColor(Color.parseColor("#7d7575"))
                valueTextColor = Color.WHITE
                lineWidth = 3f
                setDrawFilled(true)
                fillColor = Color.parseColor("#692020")
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawValues(false)
            }

            chart.data = LineData(dataSet)

            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index in lifts.indices) {
                        dateFormatter.format(Date(lifts[index].date))
                    } else ""
                }
            }

            // ✅ Marker se postavlja OVDE (ima pristup novim lifts)
            chart.marker = LiftMarkerView(chart.context, lifts)

            chart.notifyDataSetChanged()
            chart.invalidate()
            chart.animateX(500)
        }
    )
}

