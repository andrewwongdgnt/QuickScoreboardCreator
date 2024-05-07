package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamDisplayContent(
    modifier: Modifier = Modifier,
    teamDisplay: TeamDisplay,
    onEditClick: () -> Unit
) {
    Row(
        modifier = modifier
        .clickable(onClick = onEditClick)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        ImageContent(
            modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterVertically),
            teamDisplay
        )
        val textValue = when (teamDisplay) {
            is TeamDisplay.SelectedTeamDisplay -> {
                teamDisplay.name
            }

            is TeamDisplay.UnSelectedTeamDisplay -> {
                stringResource(R.string.unselectedTeamTitle)
            }
        }
        Text(
            text = textValue,
            fontSize = 30.sp,
            maxLines = 1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .basicMarquee(delayMillis = 1000)
                .align(Alignment.CenterVertically)
        )

    }
}

@Composable
private fun ImageContent(
    modifier: Modifier = Modifier,
    teamDisplay: TeamDisplay
) {
    val imageModifier = modifier
        .requiredSize(56.dp)
    when (teamDisplay) {
        is TeamDisplay.SelectedTeamDisplay -> {
            Image(
                painter = painterResource(teamDisplay.teamIcon.res),
                null,
                modifier = imageModifier
            )
        }

        is TeamDisplay.UnSelectedTeamDisplay -> {
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
private fun `Team display`() =
    TeamDisplayContent(
        teamDisplay = TeamDisplay.SelectedTeamDisplay(
            "GORILLA",
            TeamIcon.GORILLA
        ),
        onEditClick = {}
    )

@Preview(showBackground = true)
@Composable
private fun `No team display`() =
    TeamDisplayContent(
        teamDisplay = TeamDisplay.UnSelectedTeamDisplay,
        onEditClick = {}
    )
