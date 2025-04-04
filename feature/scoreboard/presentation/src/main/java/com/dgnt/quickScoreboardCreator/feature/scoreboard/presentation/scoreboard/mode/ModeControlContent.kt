package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.mode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector.rememberToggleOff
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector.rememberToggleOn

@Composable
fun ModeControlContent(
    modifier: Modifier = Modifier,
    simpleMode: Boolean,
    onToggleModeChange: (Boolean) -> Unit,
) {

    val iconWidth = 56.dp
    val iconHeight = 40.dp
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = if (simpleMode) rememberToggleOff() else rememberToggleOn(),
            contentDescription = stringResource(id = if (simpleMode) R.string.simpleMode else R.string.advanceMode),
            modifier = Modifier
                .width(iconWidth)
                .height(iconHeight)
                .clickable {
                    onToggleModeChange(simpleMode)
                }
        )
        Text(
            modifier = if (!simpleMode) Modifier.height(0.dp) else Modifier,
            text = stringResource(id = R.string.simpleMode)
        )

        Text(
            modifier = if (simpleMode) Modifier.height(0.dp) else Modifier,
            text = stringResource(id = R.string.advanceMode)
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun `simple mode`() =
    ModeControlContent(
        simpleMode = true,
        onToggleModeChange = {}
    )

@Preview(showBackground = true)
@Composable
private fun `advance mode`() =
    ModeControlContent(
        simpleMode = false,
        onToggleModeChange = {}
    )