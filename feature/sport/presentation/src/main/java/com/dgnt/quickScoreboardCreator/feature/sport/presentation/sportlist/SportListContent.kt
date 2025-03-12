@file:OptIn(ExperimentalFoundationApi::class)

package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist

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
import androidx.compose.material3.Surface
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
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.SnackBar
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportListItem
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.descriptionRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.iconRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.titleRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent.LaunchScoreboard
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent.SportDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch


@Composable
fun SportListContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: SportListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    SportListInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction,
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SportListInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: SportListState,
    onAction: (SportListAction) -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mustClearDeletedSportList by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {

        fun dismissSnackBar(clear: Boolean) {
            mustClearDeletedSportList = clear
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
                            SnackbarResult.ActionPerformed -> onAction(SportListAction.UndoDelete)
                            SnackbarResult.Dismissed -> {
                                if (mustClearDeletedSportList)
                                    onAction(SportListAction.ClearDeletedSportList)
                            }
                        }

                    }
                }

                is SportDetails,
                is LaunchScoreboard -> {
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
                DefaultSnackbar(snackbarData = snackBarData, onSnackbarDismissed = { mustClearDeletedSportList = true })
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(SportListAction.Add) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { padding ->
        val categoryList = listOf(
            stringResource(R.string.defaultSportConfig) to state.defaultSportList.map { sportType ->
                SportListItem(
                    SportIdentifier.Default(sportType),
                    stringResource(sportType.titleRes()),
                    stringResource(sportType.descriptionRes()),
                    sportType.icon
                )
            },
            stringResource(R.string.customSportConfig) to state.customSportList
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
                            when (val sId = item.sportIdentifier) {
                                is SportIdentifier.Custom -> sId.id
                                is SportIdentifier.Default -> sId.sportType
                            }
                        }
                    ) { sport ->
                        val cardItemContent = @Composable {
                            CardItemContent(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                title = sport.title,
                                description = sport.description,
                                iconRes = sport.icon.iconRes(),
                                onClick = {
                                    onAction(SportListAction.Launch(sport.sportIdentifier))
                                }
                            ) {
                                IconButton(onClick = {
                                    onAction(SportListAction.Edit(sport.sportIdentifier))
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(R.string.edit)
                                    )
                                }
                            }
                        }
                        when (val sId = sport.sportIdentifier) {
                            is SportIdentifier.Custom ->
                                SwipeBox(
                                    modifier = Modifier.animateItem(),
                                    onDelete = {
                                        onAction(SportListAction.Delete(sId.id))
                                    },
                                    content = cardItemContent
                                )

                            is SportIdentifier.Default -> cardItemContent()
                        }
                    }
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SportListContentPreview(
    @PreviewParameter(SportListPreviewStateProvider::class) state: SportListState
) = QuickScoreboardCreatorTheme {
    Surface {
        SportListInnerContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}