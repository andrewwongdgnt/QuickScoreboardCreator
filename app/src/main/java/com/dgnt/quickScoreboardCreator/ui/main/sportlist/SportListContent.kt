@file:OptIn(ExperimentalFoundationApi::class)

package com.dgnt.quickScoreboardCreator.ui.main.sportlist

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
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.CategorizedSportListItem
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.CategorizedSportType
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIcon
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportListItem
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportType
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultSnackbar
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.carditem.CardItemContent
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.carditem.SwipeBox
import com.dgnt.quickScoreboardCreator.ui.common.resourcemapping.descriptionRes
import com.dgnt.quickScoreboardCreator.ui.common.resourcemapping.iconRes
import com.dgnt.quickScoreboardCreator.ui.common.resourcemapping.titleRes
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch


@Composable
fun SportListContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: SportListViewModel = hiltViewModel()
) {

    val categorizedSports = viewModel.categorizedSports.collectAsStateWithLifecycle(
        initialValue = CategorizedSportType(listOf()) to CategorizedSportListItem(listOf())
    )

    SportListInnerContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        categorizedSports = categorizedSports.value,
        onLaunch = viewModel::onLaunch,
        onAdd = viewModel::onAdd,
        onEdit = viewModel::onEdit,
        onDelete = { viewModel.onDelete(it) },
        onUndoDelete = viewModel::onUndoDelete,
        onClearDeletedSportList = viewModel::onClearDeletedSportList
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SportListInnerContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    categorizedSports: Pair<CategorizedSportType, CategorizedSportListItem>,
    onLaunch: (SportIdentifier) -> Unit,
    onAdd: () -> Unit,
    onEdit: (SportIdentifier) -> Unit,
    onDelete: (id: Int) -> Unit,
    onUndoDelete: () -> Unit,
    onClearDeletedSportList: () -> Unit,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mustClearDeletedSportList by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {

        fun dismissSnackbar(clear: Boolean) {
            mustClearDeletedSportList = clear
            snackBarHostState.currentSnackbarData?.dismiss()
        }
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.SnackBar.QuantitySnackBar -> {
                    dismissSnackbar(false)
                    scope.launch {
                        val result = snackBarHostState.showSnackbar(
                            message = context.resources.getQuantityString(event.message, event.quantity, event.quantity),
                            actionLabel = context.getString(event.action),
                            withDismissAction = true
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> onUndoDelete()
                            SnackbarResult.Dismissed -> {
                                if (mustClearDeletedSportList)
                                    onClearDeletedSportList()
                            }
                        }

                    }
                }

                is UiEvent.SportDetails,
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
            SnackbarHost(snackBarHostState) { snackbarData ->
                DefaultSnackbar(snackbarData = snackbarData, onSnackbarDismissed = { mustClearDeletedSportList = true })
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
            categorizedSports.first.let {
                stringResource(R.string.defaultSportConfig) to it.sportTypeList.map { sportType ->
                    SportListItem(
                        SportIdentifier.Default(sportType),
                        stringResource(sportType.titleRes()),
                        stringResource(sportType.descriptionRes()),
                        sportType.icon
                    )
                }
            },
            categorizedSports.second.let {
                stringResource(R.string.customSportConfig) to it.sportListItemList
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
                                    onLaunch(sport.sportIdentifier)
                                }
                            ) {
                                IconButton(onClick = {
                                    onEdit(sport.sportIdentifier)
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
                                        onDelete(sId.id)
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

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun `Only defaults`() =
    SportListInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        categorizedSports = CategorizedSportType(listOf(SportType.BASKETBALL, SportType.HOCKEY, SportType.SPIKEBALL))
                to
                CategorizedSportListItem(emptyList()),
        onLaunch = { _ -> },
        onAdd = {},
        onEdit = { _ -> },
        onDelete = {},
        onUndoDelete = {},
        onClearDeletedSportList = {},
    )

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun `Defaults and customs`() =
    SportListInnerContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        categorizedSports = CategorizedSportType(listOf(SportType.BASKETBALL, SportType.HOCKEY, SportType.SPIKEBALL))
                to
                CategorizedSportListItem(
                    listOf(
                        SportListItem(SportIdentifier.Default(SportType.BASKETBALL), "My Sport 1", "My Description 1", SportIcon.BASKETBALL),
                        SportListItem(SportIdentifier.Default(SportType.TENNIS), "My Sport 2", "My Description 2", SportIcon.TENNIS),
                        SportListItem(SportIdentifier.Default(SportType.BASKETBALL), "My Sport 3", "My Description 3 ", SportIcon.BOXING),
                    )
                ),
        onLaunch = { _ -> },
        onAdd = {},
        onEdit = { _ -> },
        onDelete = {},
        onUndoDelete = {},
        onClearDeletedSportList = {},
    )