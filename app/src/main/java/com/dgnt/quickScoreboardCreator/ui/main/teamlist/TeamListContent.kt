package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.team.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamItemData
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.composable.DefaultSnackbar
import com.dgnt.quickScoreboardCreator.ui.composable.team.CategorizedTeamListContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun TeamListContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TeamListViewModel = hiltViewModel()
) {
    val categorizedTeamList = viewModel.categorizedTeamList.collectAsStateWithLifecycle(initialValue = emptyList())

    TeamListInnerContent(
        viewModel.uiEvent,
        onUiEvent,
        { viewModel.onEvent(TeamListEvent.OnAdd) },
        categorizedTeamList.value,
        viewModel::onEvent
    )

}

@Composable
private fun TeamListInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    onFABClick: () -> Unit,
    categorizedTeamList: List<CategorizedTeamItemData>,
    onEvent: (TeamListEvent) -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mustClearDeletedTeamList by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {

        fun dismissSnackbar(clear: Boolean) {
            mustClearDeletedTeamList = clear
            snackbarHostState.currentSnackbarData?.dismiss()
        }
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar.ShowQuantitySnackbar -> {
                    dismissSnackbar(false)
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.resources.getQuantityString(event.message, event.quantity, event.quantity),
                            actionLabel = context.getString(event.action),
                            withDismissAction = true
                        )
                        when(result) {
                            SnackbarResult.ActionPerformed -> onEvent(TeamListEvent.OnUndoDelete)
                            SnackbarResult.Dismissed -> {
                                if (mustClearDeletedTeamList)
                                    onEvent(TeamListEvent.OnClearDeletedTeamList)
                            }
                        }
                    }
                }

                is UiEvent.TeamDetails -> {
                    dismissSnackbar(true)
                }

                else -> Unit
            }
            onUiEvent(event)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                DefaultSnackbar(snackbarData = snackbarData, onSnackbarDismissed = { mustClearDeletedTeamList = true })
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFABClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { padding ->

        CategorizedTeamListContent(
            modifier = Modifier
                .padding(padding)
                .padding(bottom = 100.dp),
            onItemClick = { onEvent(TeamListEvent.OnEdit(it)) },
            onItemDelete = { onEvent(TeamListEvent.OnDelete(it)) },
            categorizedTeamList = categorizedTeamList
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun `Empty teams`() =
    TeamListInnerContent(
        emptyFlow(),
        {},
        {},
        emptyList(),
        {}
    )

@Preview(showBackground = true)
@Composable
private fun `Some teams`() =
    TeamListInnerContent(
        emptyFlow(),
        {},
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
        {}
    )