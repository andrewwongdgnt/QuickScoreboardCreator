@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker


import android.annotation.SuppressLint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.team.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamItemData
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.composable.team.CategorizedTeamListContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun TeamPickerDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TeamPickerViewModel = hiltViewModel()
) {
    val categorizedTeamList = viewModel.categorizedTeamList.collectAsState(initial = emptyList())

    TeamPickerInnerDialogContent(
        viewModel.uiEvent,
        onUiEvent,
        categorizedTeamList.value,
        viewModel::onEvent
    ) { viewModel.onEvent(TeamPickerEvent.OnDismiss) }

}

@Composable
private fun TeamPickerInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    categorizedTeamList: List<CategorizedTeamItemData>,
    onEvent: (TeamPickerEvent) -> Unit,
    onDismiss: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    AlertDialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),
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
            CategorizedTeamListContent(
                onItemClick = { onEvent(TeamPickerEvent.OnTeamPicked(it.id)) },
                categorizedTeamList = categorizedTeamList
            )
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun `big team list`() =
    TeamPickerInnerDialogContent(
        emptyFlow(),
        {},
        listOf(
            CategorizedTeamItemData(
                "D",
                listOf(
                    TeamItemData(0, "DGNT", "My Description 1", TeamIcon.TIGER),
                    TeamItemData(1, "Dragons", "My Description 2", TeamIcon.TIGER),
                    TeamItemData(2, "Darkness", "My Description 3", TeamIcon.TIGER)
                )
            ),
            CategorizedTeamItemData(
                "T",
                listOf(
                    TeamItemData(3, "tricksters", "tricky people", TeamIcon.TIGER),
                    TeamItemData(5, "Terminators", "My Description 5", TeamIcon.TIGER)
                )
            ),
            CategorizedTeamItemData(
                "J",
                listOf(
                    TeamItemData(4, "Jedi Council", "My Description 4", TeamIcon.TIGER)
                )
            )

        ),
        {},
        {}
    )
