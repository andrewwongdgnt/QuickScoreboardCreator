package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.time

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData

@Composable
fun TimerDisplayContent(
    modifier: Modifier = Modifier,
    simpleMode: Boolean,
    timeData: TimeData,
    toIntervalEditor: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val editIconSize = 44.dp
        if (!simpleMode)
            Image(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .size(editIconSize)
                    .clickable(onClick = toIntervalEditor)
            )
        Text(
            text = timeData.formatTime(),
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable(
                    enabled = !simpleMode,
                    onClick = toIntervalEditor
                )

        )
        if (!simpleMode)
            Spacer(
                modifier = Modifier
                    .size(editIconSize)
            )
    }
}

@Preview(showBackground = true)
@Composable
private fun `12 seconds 500 milliseconds`() =
    TimerDisplayContent(
        timeData = TimeData(0, 12, 5),
        simpleMode = true,
        toIntervalEditor = { }
    )

@Preview(showBackground = true)
@Composable
private fun `2 seconds 500 milliseconds`() =
    TimerDisplayContent(
        timeData = TimeData(0, 2, 5),
        simpleMode = false,
        toIntervalEditor = { }
    )

@Preview(showBackground = true)
@Composable
private fun `12 minutes 8 seconds`() =
    TimerDisplayContent(
        timeData = TimeData(12, 8, 0),
        simpleMode = true,
        toIntervalEditor = { }
    )

@Preview(showBackground = true)
@Composable
private fun `13 minutes 35 seconds`() =
    TimerDisplayContent(
        timeData = TimeData(13, 35, 0),
        simpleMode = false,
        toIntervalEditor = { }
    )

@Preview(showBackground = true)
@Composable
private fun `13 minutes 35 seconds 800 milliseconds`() =
    TimerDisplayContent(
        timeData = TimeData(13, 35, 8),
        simpleMode = true,
        toIntervalEditor = { }
    )

@Preview(showBackground = true)
@Composable
private fun `213 minutes 35 seconds 800 milliseconds`() =
    TimerDisplayContent(
        timeData = TimeData(213, 35, 8),
        simpleMode = false,
        toIntervalEditor = { }
    )
