package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultSnackbar
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.SnackBar
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.team.presentation.categorizedteamlist.CategorizedTeamListContent
import com.dgnt.quickScoreboardCreator.feature.team.presentation.uievent.TeamDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun TeamListContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TeamListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TeamListInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
private fun TeamListInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: TeamListState,
    onAction: (TeamListAction) -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mustClearDeletedTeamList by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {

        fun dismissSnackBar(clear: Boolean) {
            mustClearDeletedTeamList = clear
            snackBarHostState.currentSnackbarData?.dismiss()
        }
        uiEvent.collect { event ->
            when (event) {
                is SnackBar.QuantitySnackBar -> {
                    dismissSnackBar(false)
                    scope.launch {
                        val result = snackBarHostState.showSnackbar(
                            message = context.resources.getQuantityString(event.message, event.quantity, event.quantity),
                            actionLabel = context.getString(event.action),
                            withDismissAction = true
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> onAction(TeamListAction.UndoDelete)
                            SnackbarResult.Dismissed -> {
                                if (mustClearDeletedTeamList)
                                    onAction(TeamListAction.ClearDeletedTeamList)
                            }
                        }
                    }
                }

                is TeamDetails -> {
                    dismissSnackBar(true)
                }

                else -> Unit
            }
            onUiEvent(event)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackBarData ->
                DefaultSnackbar(snackbarData = snackBarData, onSnackbarDismissed = { mustClearDeletedTeamList = true })
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(TeamListAction.Add) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { padding ->

        if (state.categorizedTeamList.isEmpty())
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                text = stringResource(R.string.noTeams),
                style = MaterialTheme.typography.titleLarge,
            )
        else
            CategorizedTeamListContent(
                modifier = Modifier
                    .padding(padding)
                    .padding(bottom = 100.dp),
                onItemClick = { onAction(TeamListAction.Edit(it)) },
                onItemDelete = { onAction(TeamListAction.Delete(it)) },
                categorizedTeamList = state.categorizedTeamList
            )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamListContentPreview(
    @PreviewParameter(TeamListPreviewStateProvider::class) state: TeamListState
) = QuickScoreboardCreatorTheme {
    Surface {
        TeamListInnerContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}
