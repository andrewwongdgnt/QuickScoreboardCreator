@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent


@Composable
fun TeamPickerDialogContent(
    onDone: () -> Unit,
    onTeamPicked: (Int, Int) -> Unit,
    viewModel: TeamPickerViewModel = hiltViewModel()
) {
    val teamList = viewModel.teamList.collectAsState(initial = emptyList())
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Done -> onDone()
                is UiEvent.TeamPicked -> onTeamPicked(event.scoreIndex, event.teamId)
                else -> Unit
            }
        }
    }

    TeamPickerInnerDialogContent(
        teamList,
        onDone,
        viewModel::onEvent
    )

}

@Composable
private fun TeamPickerInnerDialogContent(
    teamList: State<List<TeamItemData>>,
    onDismiss: () -> Unit,
    onEvent: (TeamPickerEvent) -> Unit,
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(id = R.string.teamPickerTitle)
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(id = android.R.string.cancel))
            }
        },
        text = {
            LazyColumn {
                val items = teamList.value
                itemsIndexed(items = items) { index, item ->
                    Row(
                        modifier = Modifier.clickable {
                            onEvent(TeamPickerEvent.OnTeamPicked(item.id))
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 3.dp, top = 10.dp, bottom = 10.dp)
                            ) {
                                Image(
                                    painterResource(item.teamIcon.res),
                                    null,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .width(48.dp)
                                )
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = item.title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                                    }

                                }
                            }
                            if (index < items.size - 1)
                                HorizontalDivider()
                        }
                    }
                }
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun `big team list`() =
    TeamPickerInnerDialogContent(
        derivedStateOf {
            listOf(
                TeamItemData(0, "DGNT1", TeamIcon.TIGER),
                TeamItemData(0, "DGNT2", TeamIcon.TIGER),
                TeamItemData(0, "DGNT3", TeamIcon.TIGER),
                TeamItemData(0, "DGNT", TeamIcon.TIGER),
                TeamItemData(0, "DGNT", TeamIcon.TIGER),
                TeamItemData(0, "DGNT", TeamIcon.TIGER),
                TeamItemData(0, "DGNT", TeamIcon.TIGER),
            )
        },
        {},
        {}
        )

