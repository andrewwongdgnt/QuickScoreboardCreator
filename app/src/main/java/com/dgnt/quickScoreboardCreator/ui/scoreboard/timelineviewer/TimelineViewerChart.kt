package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.ui.theme.TimelineViewerTeamColors
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.EntryXComparator

@Composable
fun TimelineViewerChart(
    modifier: Modifier = Modifier,
    historicalInterval: HistoricalInterval
) {
    val intervalLabelString = stringResource(
        id = R.string.intervalLabel, when (val label = historicalInterval.intervalLabel) {
            is IntervalLabel.CustomIntervalLabel -> label.value
            is IntervalLabel.ResourceIntervalLabel -> stringResource(id = label.res)
        }, historicalInterval.intervalLabel.index + 1
    )
    val commonTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val commonTextSize = 20f
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            factory = { context ->
                val chart = LineChart(context)
                val dataSetList = historicalInterval.historicalScoreGroupList.mapValues { entry ->
                    val historicalScore = entry.value
                    val scoreIndex = entry.key
                    historicalScore.primaryScoreList.map {
                        Entry(it.time.toFloat(), it.score.toFloat())
                    }.let { entries ->
                        val label = when (val tl = historicalScore.teamLabel) {
                            is TeamLabel.CustomTeamLabel -> tl.name
                            TeamLabel.NoTeamLabel -> context.getString(R.string.genericTeamTitle, scoreIndex + 1)
                        }

                        LineDataSet(entries.sortedWith(EntryXComparator()), label).apply {
                            val lineColor = TimelineViewerTeamColors[scoreIndex % TimelineViewerTeamColors.size].toArgb()
                            setDrawCircles(true)
                            setDrawValues(false)
                            circleRadius = 5f
                            color = lineColor
                            circleHoleColor = lineColor
                            setCircleColor(lineColor)
                            set

                        }
                    }

                }.values.toList()
                chart.data = LineData(dataSetList)

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
        Text(
            text = intervalLabelString,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

