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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.DefaultSnackbar
import com.dgnt.quickScoreboardCreator.ui.common.composable.carditem.CardItemContent
import com.dgnt.quickScoreboardCreator.ui.common.composable.carditem.SwipeBox
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch


@Composable
fun ScoreboardListContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardListViewModel = hiltViewModel()
) {

    val categorizedScoreboards = viewModel.categorizedScoreboards.collectAsStateWithLifecycle(
        initialValue = CategorizedScoreboardType(listOf()) to CategorizedScoreboardItemData(listOf())
    )

    ScoreboardListInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        categorizedScoreboards = categorizedScoreboards.value,
        onLaunch = viewModel::onLaunch,
        onAdd = viewModel::onAdd,
        onEdit = viewModel::onEdit,
        onDelete = { viewModel.onDelete(it) },
        onUndoDelete = viewModel::onUndoDelete,
        onClearDeletedScoreboardList = viewModel::onClearDeletedScoreboardList
    )

}

@Composable
private fun ScoreboardListInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    categorizedScoreboards: Pair<CategorizedScoreboardType, CategorizedScoreboardItemData>,
    onLaunch: (id: Int, type: ScoreboardType) -> Unit,
    onAdd: () -> Unit,
    onEdit: (id: Int, type: ScoreboardType) -> Unit,
    onDelete: (id: Int) -> Unit,
    onUndoDelete: () -> Unit,
    onClearDeletedScoreboardList: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mustClearDeletedScoreboardList by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {

        fun dismissSnackbar(clear: Boolean) {
            mustClearDeletedScoreboardList = clear
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
                        when (result) {
                            SnackbarResult.ActionPerformed -> onUndoDelete()
                            SnackbarResult.Dismissed -> {
                                if (mustClearDeletedScoreboardList)
                                    onClearDeletedScoreboardList()
                            }
                        }

                    }
                }

                is UiEvent.ScoreboardDetails,
                is UiEvent.LaunchScoreboard -> {
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
                DefaultSnackbar(snackbarData = snackbarData, onSnackbarDismissed = { mustClearDeletedScoreboardList = true })
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { padding ->


        val categoryList = listOf(
            categorizedScoreboards.first.let {
                stringResource(R.string.defaultScoreboardConfig) to it.scoreboardTypeList.map { scoreboardType ->
                    ScoreboardItemData(
                        -1,
                        scoreboardType,
                        stringResource(scoreboardType.titleRes),
                        stringResource(scoreboardType.descriptionRes),
                        scoreboardType.icon
                    )
                }
            },
            categorizedScoreboards.second.let {
                stringResource(R.string.customScoreboardConfig) to it.scoreboardItemDataList
            }
        )
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(bottom = 100.dp)
                .fillMaxSize()
        ) {

            categoryList
                .forEach { category ->
                    val title = category.first
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
                    val itemList = category.second
                    items(
                        items = itemList,
                        key = { item ->
                            item.id.takeIf { it >= 0 } ?: item.type
                        }
                    ) { scoreboard ->
                        val cardItemContent = @Composable {
                            CardItemContent(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                id = scoreboard.id,
                                title = scoreboard.title,
                                description = scoreboard.description,
                                iconRes = scoreboard.icon.res,
                                onClick = {
                                    onLaunch(scoreboard.id, scoreboard.type)
                                }
                            ) {
                                IconButton(onClick = {
                                    onEdit(scoreboard.id, scoreboard.type)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(R.string.launch)
                                    )
                                }
                            }
                        }
                        scoreboard.id.takeIf { it >= 0 }?.let {
                            SwipeBox(
                                modifier = Modifier.animateItem(),
                                onDelete = {
                                    onDelete(scoreboard.id)
                                },
                                content = cardItemContent
                            )
                        } ?: run {
                            cardItemContent()
                        }
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
        uiEvent = emptyFlow(),
        onUiEvent = {},
        categorizedScoreboards = CategorizedScoreboardType(listOf(ScoreboardType.BASKETBALL, ScoreboardType.HOCKEY, ScoreboardType.SPIKEBALL))
                to
                CategorizedScoreboardItemData(emptyList()),
        onLaunch = { _, _ -> },
        onAdd = {},
        onEdit = { _, _ -> },
        onDelete = {},
        onUndoDelete = {},
        onClearDeletedScoreboardList = {},
    )

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun `Defaults and customs`() =
    ScoreboardListInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        categorizedScoreboards = CategorizedScoreboardType(listOf(ScoreboardType.BASKETBALL, ScoreboardType.HOCKEY, ScoreboardType.SPIKEBALL))
                to
                CategorizedScoreboardItemData(
                    listOf(
                        ScoreboardItemData(0, ScoreboardType.NONE, "My Scoreboard 1", "My Description 1", ScoreboardIcon.BASKETBALL),
                        ScoreboardItemData(0, ScoreboardType.NONE, "My Scoreboard 2", "My Description 2", ScoreboardIcon.TENNIS),
                        ScoreboardItemData(0, ScoreboardType.NONE, "My Scoreboard 3", "My Description 3 ", ScoreboardIcon.BOXING),
                    )
                ),
        onLaunch = { _, _ -> },
        onAdd = {},
        onEdit = { _, _ -> },
        onDelete = {},
        onUndoDelete = {},
        onClearDeletedScoreboardList = {},
    )