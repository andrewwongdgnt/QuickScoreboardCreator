package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.ui.composable.rememberPause
import com.dgnt.quickScoreboardCreator.ui.composable.rememberPlay
import com.dgnt.quickScoreboardCreator.ui.composable.rememberStop
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.ScoreboardInteractionEvent

@Composable
fun TimerContent(
    timerInProgress: Boolean,
    timeData: TimeData,
    onEvent: (ScoreboardInteractionEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val iconSize = 56.dp
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = rememberStop(),
            contentDescription = stringResource(id = R.string.stopTimer),
            modifier = Modifier
                .size(iconSize)
                .clickable(onClick = { onEvent(ScoreboardInteractionEvent.PauseTimer(true)) })
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = timeData.formatTime(),
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = if (timerInProgress) rememberPause() else rememberPlay(),
            contentDescription = stringResource(id = if (timerInProgress) R.string.pauseTimer else R.string.startTimer),
            modifier = with(Modifier) {
                size(iconSize)
                    .clickable(
                        onClick = {
                            if (timerInProgress)
                                onEvent(ScoreboardInteractionEvent.PauseTimer(false))
                            else
                                onEvent(ScoreboardInteractionEvent.StartTimer)
                        }
                    )
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
    }

}

private fun TimeData.formatTime() :String{
    return if (minute == 0L)
        String.format("%d.%d", second, centiSecond)
    else
        String.format("%02d:%02d", minute, second)
}

@Preview(showBackground = true)
@Composable
private fun `12 seconds 500 milliseconds`() =
    TimerContent(true, TimeData(0,12,5), {})

@Preview(showBackground = true)
@Composable
private fun `2 seconds 500 milliseconds`() =
    TimerContent(false,TimeData(0,2,5), {})

@Preview(showBackground = true)
@Composable
private fun `12 minutes 8 seconds`() =
    TimerContent(false,TimeData(12,8,0), {})

@Preview(showBackground = true)
@Composable
private fun `13 minutes 35 seconds`() =
    TimerContent(false,TimeData(13,35,0), {})

@Preview(showBackground = true)
@Composable
private fun `13 minutes 35 seconds 800 milliseconds`() =
    TimerContent(false,TimeData(13,35,8), {})

@Preview(showBackground = true)
@Composable
private fun `213 minutes 35 seconds 800 milliseconds`() =
    TimerContent(false,TimeData(213,35,8), {})
