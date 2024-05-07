package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.ui.composable.rememberPause
import com.dgnt.quickScoreboardCreator.ui.composable.rememberPlay
import com.dgnt.quickScoreboardCreator.ui.composable.rememberSkipNext
import com.dgnt.quickScoreboardCreator.ui.composable.rememberSkipPrevious
import com.dgnt.quickScoreboardCreator.ui.composable.rememberStop

@Composable
fun TimeControlContent(
    timerInProgress: Boolean,
    onStartClick: () -> Unit,
    onPauseClick: (Boolean) -> Unit,
    onSkip: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconSize = 56.dp
        Row(
            modifier = Modifier
        ) {
            Icon(
                imageVector = rememberSkipPrevious(),
                contentDescription = stringResource(id = R.string.stopTimer),
                modifier = Modifier.size(iconSize).clickable(onClick = { onSkip(false) })
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = rememberStop(),
                contentDescription = stringResource(id = R.string.stopTimer),
                modifier = Modifier.size(iconSize).clickable(onClick = { onPauseClick(true) })
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (timerInProgress) rememberPause() else rememberPlay(),
                contentDescription = stringResource(id = if (timerInProgress) R.string.pauseTimer else R.string.startTimer),
                modifier = Modifier.size(iconSize).clickable(
                    onClick = if (timerInProgress) {
                        { onPauseClick(false) }
                    } else onStartClick
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = rememberSkipNext(),
                contentDescription = stringResource(id = R.string.stopTimer),
                modifier = Modifier.size(iconSize).clickable(onClick = { onSkip(true) })
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun `in progress timer`() =
    TimeControlContent(true, {}, {}, {})

@PreviewScreenSizes
@Composable
private fun `paused timer`() =
    TimeControlContent(false, {}, {}, {})
