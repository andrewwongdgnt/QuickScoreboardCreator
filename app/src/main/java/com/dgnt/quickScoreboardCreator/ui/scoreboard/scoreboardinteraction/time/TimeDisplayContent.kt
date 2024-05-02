package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.joda.time.Duration

@Composable
fun TimeDisplayContent(
    timerValue: Long,
    modifier: Modifier = Modifier
) {
    Text(
        text = timerValue.formatTime(),
        fontSize = 50.sp,
        modifier = modifier
    )
}


//TODO Move this to some sort of business class
private fun Long.formatTime(): String {
    val duration = Duration.millis(this)
    val hours = duration.standardHours
    val minutes = duration.standardMinutes % 60
    val seconds = duration.standardSeconds % 60
    val centiSeconds = (this % 1000) / 100
    return String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, centiSeconds)
}

@Preview(showBackground = true)
@Composable
fun `12 minutes`() =
    TimeDisplayContent(720000)

@Preview(showBackground = true)
@Composable
fun `1 hour 12 minutes`() =
    TimeDisplayContent(720000 * 6)

@Preview(showBackground = true)
@Composable
fun `1 hour 12 minutes 8 seconds`() =
    TimeDisplayContent(720000 * 6 + 8000)

@Preview(showBackground = true)
@Composable
fun `1 hour 13 minutes 35 seconds`() =
    TimeDisplayContent(720000 * 6 + 95000)

@Preview(showBackground = true)
@Composable
fun `1 hour 13 minutes 35 seconds 880 milliseconds`() =
    TimeDisplayContent(720000 * 6 + 95000 + 880)