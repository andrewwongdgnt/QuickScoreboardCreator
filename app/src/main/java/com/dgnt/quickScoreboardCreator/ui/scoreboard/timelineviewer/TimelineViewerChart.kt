package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TimelineViewerChart(
    modifier: Modifier = Modifier,
    historicalInterval: HistoricalInterval
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            modelProducer.runTransaction {

                lineSeries {
                    historicalInterval.historicalScoreGroupList.values.map { it.primaryScoreList }.forEach {

                        series(it.map { it.time }, it.map { it.score })
                    }
                }
            }
        }
    }
    ComposeChart1(modelProducer = modelProducer)
}

@Composable
private fun ComposeChart1(modifier: Modifier = Modifier, modelProducer: CartesianChartModelProducer) {
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
        ),
        modelProducer,
    )
}