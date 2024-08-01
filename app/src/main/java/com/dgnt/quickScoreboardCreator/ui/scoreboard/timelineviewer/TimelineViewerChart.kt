package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import android.view.View
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.ui.theme.TimelineViewerTeamColors
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.EntryXComparator
import com.github.mikephil.charting.utils.MPPointF
import org.joda.time.Duration

@Composable
fun TimelineViewerChart(
    modifier: Modifier = Modifier,
    historicalInterval: HistoricalInterval
) {
    val commonTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val commonTextSize = 20f
    val localContext = LocalContext.current
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        update = { chart ->
            val range = historicalInterval.range
            val dataSetList = historicalInterval.historicalScoreGroupList.mapValues { entry ->
                val historicalScore = entry.value
                val scoreIndex = entry.key
                historicalScore.primaryScoreList.map {
                    val time = when (range) {
                        is HistoricalIntervalRange.CountDown -> (range.start - it.time).toFloat()
                        HistoricalIntervalRange.Infinite -> it.time.toFloat()
                    }
                    val lineColor = TimelineViewerTeamColors[scoreIndex % TimelineViewerTeamColors.size].toArgb()
                    Entry(time, it.score.toFloat(), lineColor)

                }.let { entries ->
                    val label = when (val tl = historicalScore.teamLabel) {
                        is TeamLabel.Custom -> tl.name
                        TeamLabel.None -> localContext.getString(R.string.genericTeamTitle, scoreIndex + 1)
                    }

                    LineDataSet(entries.sortedWith(EntryXComparator()), label).apply {
                        setDrawCircles(true)
                        setDrawValues(false)
                        circleRadius = 5f
                        (values.firstOrNull()?.data as? Int)?.let { lineColor ->
                            color = lineColor
                            circleHoleColor = lineColor
                            setCircleColor(lineColor)
                        }

                    }
                }

            }.values.toList()
            chart.data = LineData(dataSetList)
            when (range) {
                is HistoricalIntervalRange.CountDown -> chart.xAxis.axisMaximum = range.start.toFloat()
                HistoricalIntervalRange.Infinite -> Unit
            }
            chart.xAxis.apply {
                axisMinimum = 0f

                valueFormatter = object : ValueFormatter() {

                    override fun getFormattedValue(value: Float): String {

                        //TODO exactly the same code in QSCTimeTransformer.toTimeData() so should extract it out
                        val milli = value.toLong()
                        val duration = Duration.millis(milli)
                        val minutes = duration.standardMinutes
                        val seconds = duration.standardSeconds % 60
                        val centiSeconds = (milli % 1000) / 100

                        return TimeData(minutes.toInt(), seconds.toInt(), centiSeconds.toInt()).formatTime(true)
                    }
                }
            }

            chart.marker = object : MarkerView(localContext, R.layout.timeline_marker_view) {
                private var mOffset: MPPointF? = null

                override fun refreshContent(e: Entry, highlight: Highlight) {
                    (findViewById<View>(R.id.tvContent) as TextView).apply {
                        text = "${e.y.toInt()}"
                        (e.data as? Int)?.let {
                            setTextColor(it)
                        }
                    }
                }

                override fun getOffset(): MPPointF {
                    return super.getOffset()
                }

            }
            chart.invalidate()
        },
        factory = { context ->
            val chart = LineChart(context)

            // Enable touch gestures
            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = false

            // Set styling
            chart.description.apply {
                isEnabled = false
            }
            chart.legend.apply {
                isEnabled = true
                textSize = commonTextSize
                textColor = commonTextColor
            }
            chart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textSize = commonTextSize
                textColor = commonTextColor
                setLabelCount(5, true)
            }
            chart.axisLeft.apply {
                textSize = commonTextSize
                textColor = commonTextColor
            }
            chart.axisRight.isEnabled = false

            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
}

