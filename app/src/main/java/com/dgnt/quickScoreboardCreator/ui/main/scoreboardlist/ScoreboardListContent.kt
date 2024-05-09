@file:OptIn(ExperimentalFoundationApi::class)

package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.composable.DefaultSnackbar
import kotlinx.coroutines.launch


@Composable
fun ScoreboardListContent(
    toScoreboardDetails: (UiEvent.ScoreboardDetails) -> Unit,
    launchScoreboard: (UiEvent.LaunchScoreboard) -> Unit,
    viewModel: ScoreboardListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val categorizedScoreboards = viewModel.categorizedScoreboards.collectAsState(initial = CategorizedScoreboardType(R.string.defaultScoreboardConfig, listOf()) to CategorizedScoreboardItemData(R.string.customScoreboardConfig, listOf()))
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarDismissedByUser by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar.ShowQuantitySnackbar -> {
                    snackbarDismissedByUser = false
                    snackbarHostState.currentSnackbarData?.dismiss()
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.resources.getQuantityString(event.message, event.quantity, event.quantity),
                            actionLabel = context.getString(event.action),
                            withDismissAction = true
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> viewModel.onEvent(ScoreboardListEvent.OnUndoDelete)
                            SnackbarResult.Dismissed -> {
                                if (snackbarDismissedByUser)
                                    viewModel.onEvent(ScoreboardListEvent.OnClearDeletedScoreboardList)

                            }
                        }

                    }
                }

                is UiEvent.ScoreboardDetails -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    viewModel.onEvent(ScoreboardListEvent.OnClearDeletedScoreboardList)
                    toScoreboardDetails(event)
                }

                is UiEvent.LaunchScoreboard -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    viewModel.onEvent(ScoreboardListEvent.OnClearDeletedScoreboardList)
                    launchScoreboard(event)
                }

                else -> Unit
            }
        }
    }

    ScoreboardListInnerContent(
        snackbarHostState,
        { viewModel.onEvent(ScoreboardListEvent.OnAdd) },
        categorizedScoreboards.value,
        viewModel::onEvent,
        { snackbarDismissedByUser = true }
    )

}

@Composable
private fun ScoreboardListInnerContent(
    snackbarHostState: SnackbarHostState,
    onFABClick: () -> Unit,
    categorizedScoreboards: Pair<CategorizedScoreboardType, CategorizedScoreboardItemData>,
    onEvent: (ScoreboardListEvent) -> Unit,
    onSnackbarDismissed: () -> Unit,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                DefaultSnackbar(snackbarData = snackbarData, onSnackbarDismissed = onSnackbarDismissed)
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


        val categoryList = listOf(
            categorizedScoreboards.first.let {
                stringResource(it.titleRes) to it.scoreboardTypeList.map { scoreboardType ->
                    ScoreboardItemData(null, scoreboardType, stringResource(scoreboardType.titleRes), stringResource(scoreboardType.descriptionRes))
                }
            },
            categorizedScoreboards.second.let {
                stringResource(it.titleRes) to it.scoreboardItemDataList
            }
        )
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(bottom = 100.dp)
                .fillMaxSize()
        ) {

            categoryList
                .forEach {
                    val title = it.first
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
                    val itemList = it.second
                    items(itemList) { scoreboard ->
                        ScoreboardItemContent(
                            item = scoreboard,
                            onEvent = onEvent,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun `Only defaults`() =
    ScoreboardListInnerContent(
        SnackbarHostState(),
        {},
        CategorizedScoreboardType(R.string.defaultScoreboardConfig, listOf(ScoreboardType.BASKETBALL, ScoreboardType.HOCKEY, ScoreboardType.SPIKEBALL))
                to
                CategorizedScoreboardItemData(R.string.customScoreboardConfig, emptyList()),
        {},
        {}
    )

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun `Defaults and customs`() =
    ScoreboardListInnerContent(
        SnackbarHostState(),
        {},
        CategorizedScoreboardType(R.string.defaultScoreboardConfig, listOf(ScoreboardType.BASKETBALL, ScoreboardType.HOCKEY, ScoreboardType.SPIKEBALL))
                to
                CategorizedScoreboardItemData(
                    R.string.customScoreboardConfig, listOf(
                        ScoreboardItemData(0, null, "My Scoreboard 1", "My Description 1"),
                        ScoreboardItemData(0, null, "My Scoreboard 2", "My Description 2"),
                        ScoreboardItemData(0, null, "My Scoreboard 3", "My Description 3 "),
                    )
                ),
        {},
        {}
    )