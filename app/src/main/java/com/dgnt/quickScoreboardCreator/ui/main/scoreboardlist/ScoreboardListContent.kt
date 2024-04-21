@file:OptIn(ExperimentalFoundationApi::class)

package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.common.util.UiEvent


@Composable
fun ScoreboardListContent(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ScoreboardListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val defaultScoreboardList = viewModel.defaultScoreboardList.collectAsState(initial = emptyList())
    val scoreboardList = viewModel.scoreboardList.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar.ShowQuantitySnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = context.resources.getQuantityString(event.message, event.quantity, event.quantity),
                        actionLabel = context.getString(event.action),
                        withDismissAction = true
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(ScoreboardListEvent.OnUndoDelete)
                    }
                }

                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(ScoreboardListEvent.OnAdd)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { padding ->
        val categoryList = listOf(
            Pair(stringResource(R.string.defaultScoreboardConfig), defaultScoreboardList.value.map {
                ScoreboardItemData(null, it, stringResource(it.titleRes), stringResource(it.descriptionRes))
            }),
            Pair(stringResource(R.string.customScoreboardConfig), scoreboardList.value)
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(16.dp)
                        )
                    }
                    val itemList = it.second
                    items(itemList) { scoreboard ->
                        ScoreboardItemContent(
                            item = scoreboard,
                            onEvent = viewModel::onEvent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEvent(ScoreboardListEvent.OnLaunch(scoreboard))
                                }
                        )
                    }
                }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ScoreboardListContentPreview() =
    ScoreboardListContent({})