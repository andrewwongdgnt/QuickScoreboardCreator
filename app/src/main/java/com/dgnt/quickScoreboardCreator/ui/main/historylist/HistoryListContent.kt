package com.dgnt.quickScoreboardCreator.ui.main.historylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import com.dgnt.quickScoreboardCreator.domain.history.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoryItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.DateDisplayUtil.getDayOfMonthSuffix
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.DefaultSnackbar
import com.dgnt.quickScoreboardCreator.ui.common.composable.carditem.CardItemContent
import com.dgnt.quickScoreboardCreator.ui.common.composable.carditem.SwipeBox
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

@Composable
fun HistoryListContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: HistoryListViewModel = hiltViewModel()
) {
    val categorizedHistoryList = viewModel.categorizedHistoryList.collectAsStateWithLifecycle(initialValue = emptyList())

    HistoryListInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        categorizedHistoryList = categorizedHistoryList.value,
        onLaunch = viewModel::onLaunch,
        onEdit = viewModel::onEdit,
        onDelete = { viewModel.onDelete(it) },
        onUndoDelete = viewModel::onUndoDelete,
        onClearDeletedHistoryList = viewModel::onClearDeletedHistoryList
    )

}

@Composable
private fun HistoryListInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    categorizedHistoryList: List<CategorizedHistoryItemData>,
    onLaunch: (id: Int) -> Unit,
    onEdit: (id: Int) -> Unit,
    onDelete: (id: Int) -> Unit,
    onUndoDelete: () -> Unit,
    onClearDeletedHistoryList: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mustClearDeletedHistoryList by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {

        fun dismissSnackbar(clear: Boolean) {
            mustClearDeletedHistoryList = clear
            snackbarHostState.currentSnackbarData?.dismiss()
        }
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.SnackBar.QuantitySnackBar -> {
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
                                if (mustClearDeletedHistoryList)
                                    onClearDeletedHistoryList()
                            }
                        }
                    }
                }

                is UiEvent.HistoryDetails -> {
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
                DefaultSnackbar(snackbarData = snackbarData, onSnackbarDismissed = { mustClearDeletedHistoryList = true })
            }
        }
    ) { padding ->

        CategorizedHistoryListContent(
            modifier = Modifier
                .padding(padding)
                .padding(bottom = 100.dp),
            onLaunch = onLaunch,
            onEdit = onEdit,
            onDelete = onDelete,
            categorizedHistoryList = categorizedHistoryList
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategorizedHistoryListContent(
    modifier: Modifier = Modifier,
    onLaunch: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    categorizedHistoryList: List<CategorizedHistoryItemData>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {

        categorizedHistoryList
            .forEach { category ->
                val title = category.dateTime.toString(DateTimeFormat.forPattern("MMMM YYYY"))
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
                val itemList = category.historyItemDataList
                items(
                    items = itemList,
                    key = { it.id }
                ) { team ->

                    SwipeBox(
                        modifier = Modifier.animateItem(),
                        onDelete = {
                            onDelete.invoke(team.id)
                        },
                        content = {
                            CardItemContent(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                title = team.title,
                                description = "${getDayOfMonthSuffix(team.lastModified.dayOfMonth)}, ${team.lastModified.toString(DateTimeFormat.forPattern("h:mm a"))}",
                                iconRes = team.icon.res,
                                onClick = {
                                    onLaunch(team.id)
                                }
                            ) {
                                IconButton(onClick = {
                                    onEdit(team.id)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(R.string.edit)
                                    )
                                }
                            }
                        }
                    )

                }
            }
    }
}

@Preview(showBackground = true)
@Composable
private fun `Empty history`() =
    HistoryListInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        categorizedHistoryList = emptyList(),
        onLaunch = {},
        onEdit = {},
        onDelete = {},
        onUndoDelete = {},
        onClearDeletedHistoryList = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Some histories`() =
    HistoryListInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        categorizedHistoryList = listOf(
            CategorizedHistoryItemData(
                DateTime(2024, 6, 1, 0, 0),
                listOf(
                    HistoryItemData(1, "Tennis", ScoreboardIcon.TENNIS, DateTime(2024, 6, 2, 10, 0), DateTime(2024, 6, 2, 10, 0)),
                    HistoryItemData(2, "Tennis", ScoreboardIcon.TENNIS, DateTime(2024, 6, 1, 9, 0), DateTime(2024, 6, 1, 9, 0)),
                )
            ),
            CategorizedHistoryItemData(
                DateTime(2024, 5, 1, 0, 0),
                listOf(
                    HistoryItemData(4, "Basketball NBA", ScoreboardIcon.BASKETBALL, DateTime(2024, 5, 2, 14, 0), DateTime(2024, 5, 2, 14, 0)),
                    HistoryItemData(7, "Hockey NHL", ScoreboardIcon.HOCKEY, DateTime(2024, 5, 2, 10, 0), DateTime(2024, 5, 2, 10, 0)),
                )
            ),
            CategorizedHistoryItemData(
                DateTime(2024, 1, 1, 0, 0),
                listOf(
                    HistoryItemData(8, "Pick up bball", ScoreboardIcon.BASKETBALL, DateTime(2024, 1, 31, 16, 14), DateTime(2024, 1, 31, 16, 14)),
                    HistoryItemData(55, "Pick up bball", ScoreboardIcon.BASKETBALL, DateTime(2024, 1, 22, 16, 14), DateTime(2024, 1, 22, 16, 14)),
                    HistoryItemData(83, "Pick up bball", ScoreboardIcon.BASKETBALL, DateTime(2024, 1, 21, 16, 14), DateTime(2024, 1, 21, 16, 14)),
                )
            )

        ),
        onLaunch = {},
        onEdit = {},
        onDelete = {},
        onUndoDelete = {},
        onClearDeletedHistoryList = {},
    )