@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent

@Composable
fun ScoreboardDetailsDialogContent(
    onDone: () -> Unit,
    viewModel: ScoreboardDetailsViewModel = hiltViewModel()
) {
    var valid by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Done -> onDone()
                is UiEvent.Validation -> {
                    valid = event.valid
                }

                else -> Unit
            }
        }
    }
    AlertDialog(
        onDismissRequest = { onDone() },
        title = {
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(id = R.string.scoreboardDetailsTitle)
            )
        },
        dismissButton = {
            Button(
                onClick = { onDone() }
            ) {
                Text(stringResource(id = android.R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                enabled = valid,
                onClick = {
                    viewModel.onEvent(ScoreboardDetailsEvent.OnDone)
                }
            ) {
                Text(stringResource(id = R.string.add))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
    )

}