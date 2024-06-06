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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ScoreboardDetailsDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardDetailsViewModel = hiltViewModel()
) {
    val valid by viewModel.valid.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()

    ScoreboardDetailsInnerDialogContent(
        viewModel.uiEvent,
        onUiEvent,
        title,
        description,
        viewModel::onEvent,
        valid,
    )
}

@Composable
private fun ScoreboardDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    title: String,
    description: String,
    onEvent: (ScoreboardDetailsEvent) -> Unit,
    valid: Boolean,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }
    AlertDialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = { onEvent(ScoreboardDetailsEvent.OnDismiss) },
        title = {
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(id = R.string.scoreboardDetailsTitle)
            )
        },
        dismissButton = {
            Button(
                onClick = { onEvent(ScoreboardDetailsEvent.OnDismiss) }
            ) {
                Text(stringResource(id = android.R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                enabled = valid,
                onClick = { onEvent(ScoreboardDetailsEvent.OnConfirm) }
            ) {
                Text(stringResource(id = android.R.string.ok))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val spacer: @Composable () -> Unit = { Spacer(modifier = Modifier.height(8.dp)) }
                TextField(
                    value = title,
                    onValueChange = { onEvent(ScoreboardDetailsEvent.OnTitleChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.titlePlaceholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                spacer()
                TextField(
                    value = description,
                    onValueChange = { onEvent(ScoreboardDetailsEvent.OnDescriptionChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.descriptionPlaceholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun `Empty data`() =
    ScoreboardDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        {},
        true,
    )