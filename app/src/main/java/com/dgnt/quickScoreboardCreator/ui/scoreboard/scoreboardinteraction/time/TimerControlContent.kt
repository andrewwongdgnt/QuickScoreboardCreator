package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.rememberPauseCircle
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.rememberPlayCircle
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.rememberStopCircle
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.ScoreboardInteractionEvent

@Composable
fun TimerControlContent(
    modifier: Modifier = Modifier,
    timerInProgress: Boolean,
    onEvent: (ScoreboardInteractionEvent) -> Unit,
) {

    val iconSize = 56.dp
    Row(modifier = modifier) {
        Icon(
            imageVector = if (timerInProgress) rememberPauseCircle() else rememberPlayCircle(),
            contentDescription = stringResource(id = if (timerInProgress) R.string.pauseTimer else R.string.startTimer),
            modifier = Modifier
                .size(iconSize)
                .clickable(
                    onClick = {
                        if (timerInProgress)
                            onEvent(ScoreboardInteractionEvent.PauseTimer(false))
                        else
                            onEvent(ScoreboardInteractionEvent.StartTimer)
                    }
                )
        )

        Spacer(modifier = Modifier.width(5.dp))
        Icon(
            imageVector = rememberStopCircle(),
            contentDescription = stringResource(id = R.string.stopTimer),
            modifier = Modifier
                .size(iconSize)
                .clickable(onClick = { onEvent(ScoreboardInteractionEvent.PauseTimer(true)) })
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun `in progress timer`() =
    TimerControlContent(timerInProgress = true, onEvent = {})

@Preview(showBackground = true)
@Composable
private fun `paused timer`() =
    TimerControlContent(timerInProgress = false, onEvent = {})