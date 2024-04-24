package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.common.util.UiEvent

@Composable
fun ScoreboardDetailsContent(
    onDone: () -> Unit,
    viewModel: ScoreboardDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Done -> onDone()
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.message),
                        actionLabel = event.action?.let { context.getString(it) },
                        withDismissAction = true
                    )
                }

                else -> Unit
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(ScoreboardDetailsEvent.OnDone)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.done)
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            TextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                placeholder = { Text(text = stringResource(R.string.scoreboardNamePlaceholder)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                placeholder = { Text(text = stringResource(R.string.scoreboardDescriptionPlaceholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )
        }
    }
}