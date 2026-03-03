package com.example.gymapp.screens.progress

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
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
        // modernije, krace: 12 Feb
        SimpleDateFormat("dd MMM", Locale.getDefault())
    }

    // da animacija ne radi svaki put na recomposition
    var lastSize by remember { mutableStateOf(0) }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),

        factory = { context ->
            LineChart(context).apply {

                // Transparent jer ti Chart vec stoji u Card containeru
                setBackgroundColor(Color.TRANSPARENT)

                description.isEnabled = false
                legend.isEnabled = false

                axisRight.isEnabled = false

                // X axis
                xAxis.apply {
                    textColor = Color.parseColor("#AAB3C5")
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    granularity = 1f
                    isGranularityEnabled = true
                    labelRotationAngle = 0f
                   // avoidFirstLastClipping = true
                    // manje labela, cistije
                    setLabelCount(4, true)
                }

                // Y axis
                axisLeft.apply {
                    textColor = Color.parseColor("#AAB3C5")
                    setDrawAxisLine(false)
                    setDrawGridLines(true)
                    gridColor = Color.parseColor("#1F2A40") // suptilan grid
                    setLabelCount(5, true)
                }

                // Interaction
                setScaleEnabled(true)
                setPinchZoom(true)
                isDragEnabled = true

                // da ne bude prevelik “zoom out”
                setVisibleXRangeMaximum(7f)
                setVisibleXRangeMinimum(4f)

                // highlight izgleda lepse
                isHighlightPerTapEnabled = true
                isHighlightPerDragEnabled = true

                // Extra padding da marker ne bude odsečen
                setExtraOffsets(8f, 8f, 8f, 12f)
            }
        },

        update = { chart ->

            if (lifts.isEmpty()) {
                chart.clear()
                chart.invalidate()
                return@AndroidView
            }

            val entries = lifts.mapIndexed { index, lift ->
                Entry(index.toFloat(), lift.weight)
            }

            val accent = Color.parseColor("#E53935")          // tvoj accent (crvena)
            val accentSoft = Color.parseColor("#33E53935")    // 20% alpha

            val dataSet = LineDataSet(entries, null).apply {
                color = accent
                lineWidth = 2.2f

                // modernije tačke
                setDrawCircles(true)
                circleRadius = 3.2f
                setCircleColor(Color.parseColor("#E6E6E6"))
                setCircleHoleColor(accent)
                circleHoleRadius = 1.6f

                // bez value texta na liniji
                setDrawValues(false)

                // fill - veoma suptilan (možeš i skroz false)
                setDrawFilled(true)
                fillColor = accentSoft

                // bez previše “gumene” krive
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.16f

                // highlight linija
                highLightColor = Color.parseColor("#66FFFFFF")
                highlightLineWidth = 1.2f
            }

            chart.data = LineData(dataSet).apply {
                setDrawValues(false)
            }

            // X axis formatter (datum)
            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index in lifts.indices) {
                        dateFormatter.format(Date(lifts[index].date))
                    } else ""
                }
            }

            // Marker (lep, custom layout)
            chart.marker = LiftMarkerView(chart.context, lifts)

            chart.notifyDataSetChanged()
            chart.invalidate()

            // animiraj samo kad se doda nova tacka
            if (lifts.size > lastSize) {
                chart.animateX(350)
            }
            lastSize = lifts.size

            // auto scroll na kraj (najnovije)
            chart.moveViewToX((lifts.size - 1).toFloat())
        }
    )
}