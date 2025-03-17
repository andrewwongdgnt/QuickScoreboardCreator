@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.feature.team.presentation.teampicker


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.team.presentation.categorizedteamlist.CategorizedTeamListContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun TeamPickerDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TeamPickerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TeamPickerInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
private fun TeamPickerInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: TeamPickerState,
    onAction: (TeamPickerAction) -> Unit
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    DefaultAlertDialog(
        title = stringResource(id = R.string.teamPickerTitle),
        confirmText = stringResource(id = android.R.string.cancel),
        onConfirm = { onAction(TeamPickerAction.Dismiss) },
        onDismissRequest = { onAction(TeamPickerAction.Dismiss) }

    ) {
        CategorizedTeamListContent(
            onItemClick = { onAction(TeamPickerAction.TeamPicked(it)) },
            categorizedTeamList = state.categorizedTeamList
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun TeamPickerDialogContentPreview(
    @PreviewParameter(TeamPickerPreviewStateProvider::class) state: TeamPickerState
) = QuickScoreboardCreatorTheme {
    Surface {
        TeamPickerInnerDialogContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}
