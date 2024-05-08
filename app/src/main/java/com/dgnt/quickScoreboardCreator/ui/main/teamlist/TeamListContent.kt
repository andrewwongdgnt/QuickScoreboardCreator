@file:OptIn(ExperimentalFoundationApi::class)

package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import TeamItemContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.team.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamItemData
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import kotlinx.coroutines.launch

@Composable
fun TeamListContent(
    toTeamDetails: (UiEvent.TeamDetails) -> Unit,
    viewModel: TeamListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val categorizedTeamList = viewModel.categorizedTeamList.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar.ShowQuantitySnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.resources.getQuantityString(event.message, event.quantity, event.quantity),
                            actionLabel = context.getString(event.action),
                            withDismissAction = true
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(TeamListEvent.OnUndoDelete)
                        }
                    }
                }

                is UiEvent.TeamDetails -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    toTeamDetails(event)
                }

                else -> Unit
            }
        }
    }

    TeamListInnerContent(
        snackbarHostState,
        {
            viewModel.onEvent(TeamListEvent.OnAdd)
        },
        categorizedTeamList.value,
        viewModel::onEvent
    )

}

@Composable
private fun TeamListInnerContent(
    snackbarHostState: SnackbarHostState,
    onFABClick: () -> Unit,
    categorizedTeamList: List<CategorizedTeamItemData>,
    onItemClick: (TeamListEvent) -> Unit,
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onFABClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(bottom = 100.dp)
                .fillMaxSize()
        ) {

            categorizedTeamList
                .forEach {
                    val title = it.title
                    stickyHeader {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(10.dp)
                        )
                    }
                    val itemList = it.teamItemDataList
                    items(itemList) { team ->
                        TeamItemContent(
                            item = team,
                            onEvent = onItemClick,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun `Empty teams`() =
    TeamListInnerContent(
        SnackbarHostState(),
        {},
        emptyList(),
        {}
    )

@Preview(showBackground = true)
@Composable
private fun `Some teams`() =
    TeamListInnerContent(
        SnackbarHostState(),
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