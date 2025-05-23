package com.dgnt.quickScoreboardCreator.feature.history.presentation.historylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultSnackbar
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.carditem.CardItemContent
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.carditem.SwipeBox
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.DateDisplayUtil.getDayOfMonthSuffix
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.HistoryDetails
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.SnackBar
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.iconRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.joda.time.format.DateTimeFormat

@Composable
fun HistoryListContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: HistoryListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryListInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
private fun HistoryListInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: HistoryListState,
    onAction: (HistoryListAction) -> Unit,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mustClearDeletedHistoryList by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {

        fun dismissSnackBar(clear: Boolean) {
            mustClearDeletedHistoryList = clear
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
                            SnackbarResult.ActionPerformed -> onAction(HistoryListAction.UndoDelete)
                            SnackbarResult.Dismissed -> {
                                if (mustClearDeletedHistoryList)
                                    onAction(HistoryListAction.ClearDeleteHistoryList)
                            }
                        }
                    }
                }

                is HistoryDetails -> {
                    dismissSnackBar(true)
                }

                else -> Unit
            }
            onUiEvent(event)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData ->
                DefaultSnackbar(snackbarData = snackbarData, onSnackbarDismissed = { mustClearDeletedHistoryList = true })
            }
        }
    ) { padding ->

        if (state.categorizedHistoryList.isEmpty())
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                text = stringResource(R.string.noHistory),
                style = MaterialTheme.typography.titleLarge,
            )
        else
            CategorizedHistoryListContent(
                modifier = Modifier
                    .padding(padding)
                    .padding(bottom = 100.dp),
                onLaunch = { onAction(HistoryListAction.Launch(it)) },
                onEdit = { onAction(HistoryListAction.Edit(it)) },
                onDelete = { onAction(HistoryListAction.Delete(it)) },
                categorizedHistoryList = state.categorizedHistoryList
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
                ) { history ->

                    SwipeBox(
                        modifier = Modifier.animateItem(),
                        onDelete = {
                            onDelete.invoke(history.id)
                        },
                        content = {
                            CardItemContent(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                title = history.title,
                                description = history.description,
                                iconRes = history.icon.iconRes(),
                                supportingText = "${getDayOfMonthSuffix(history.lastModified.dayOfMonth)}, ${history.lastModified.toString(DateTimeFormat.forPattern("h:mm a"))}",
                                onClick = {
                                    onLaunch(history.id)
                                }
                            ) {
                                IconButton(onClick = {
                                    onEdit(history.id)
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
private fun HistoryListContentPreview(
    @PreviewParameter(HistoryListPreviewStateProvider::class) state: HistoryListState
) = QuickScoreboardCreatorTheme {
    Surface {
        HistoryListInnerContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}