package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.teamdisplay

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.presentation.resourcemapping.iconRes


@Composable
fun TeamDisplayContent(
    modifier: Modifier = Modifier,
    simpleMode: Boolean,
    teamDisplay: TeamDisplay,
    teamNumber: Int,
    onEditClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(enabled = !simpleMode, onClick = onEditClick)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        ImageContent(
            modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterVertically),
            simpleMode = simpleMode,
            teamDisplay = teamDisplay
        )
        val textValue = when (teamDisplay) {
            is TeamDisplay.Selected -> {
                teamDisplay.name
            }

            is TeamDisplay.UnSelected -> {
                if (simpleMode)
                    stringResource(R.string.genericTeamTitle, teamNumber)
                else
                    stringResource(R.string.unselectedTeamTitle)
            }
        }
        Text(
            text = textValue,
            fontSize = 30.sp,
            maxLines = 1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .basicMarquee(initialDelayMillis = 1000)
                .align(Alignment.CenterVertically)
        )

    }
}

@Composable
private fun ImageContent(
    modifier: Modifier = Modifier,
    simpleMode: Boolean,
    teamDisplay: TeamDisplay
) {
    val imageModifier = modifier
        .requiredSize(56.dp)
        .alpha(if (simpleMode && teamDisplay is TeamDisplay.UnSelected) 0f else 100f)

    when (teamDisplay) {
        is TeamDisplay.Selected -> {
            Image(
                painter = painterResource(teamDisplay.icon.iconRes()),
                null,
                modifier = imageModifier
            )
        }

        is TeamDisplay.UnSelected -> {

            Image(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = imageModifier
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun `Advance Team display`() =
    TeamDisplayContent(
        simpleMode = false,
        teamDisplay = TeamDisplay.Selected(
            "GORILLA",
            TeamIcon.GORILLA
        ),
        teamNumber = 1,
        onEditClick = {}
    )

@Preview(showBackground = true)
@Composable
private fun `Simple Team display`() =
    TeamDisplayContent(
        simpleMode = true,
        teamDisplay = TeamDisplay.Selected(
            "GORILLA",
            TeamIcon.GORILLA
        ),
        teamNumber = 1,
        onEditClick = {}
    )

@Preview(showBackground = true)
@Composable
private fun `Advance No team display`() =
    TeamDisplayContent(
        simpleMode = false,
        teamDisplay = TeamDisplay.UnSelected,
        teamNumber = 1,
        onEditClick = {}
    )

@Preview(showBackground = true)
@Composable
private fun `Simple No team display`() =
    TeamDisplayContent(
        simpleMode = true,
        teamDisplay = TeamDisplay.UnSelected,
        teamNumber = 1,
        onEditClick = {}
    )
