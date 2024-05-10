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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.composable.LabelSwitch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ScoreboardDetailsDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardDetailsViewModel = hiltViewModel()
) {
    val valid = viewModel.valid

    ScoreboardDetailsInnerDialogContent(
        viewModel.uiEvent,
        onUiEvent,
        viewModel.title,
        { viewModel.title = it },
        viewModel.description,
        { viewModel.description = it },
        viewModel.scoreCarriesOver,
        { viewModel.scoreCarriesOver = it },
        valid,
        { viewModel.onEvent(ScoreboardDetailsEvent.OnConfirm) },
        { viewModel.onEvent(ScoreboardDetailsEvent.OnDismiss) }
    )
}

@Composable
private fun ScoreboardDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    scoreCarriesOver: Boolean,
    onScoreCarriesOverChange: (Boolean) -> Unit,
    valid: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(id = R.string.scoreboardDetailsTitle)
            )
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(id = android.R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                enabled = valid,
                onClick = onConfirm
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
                    onValueChange = onTitleChange,
                    placeholder = { Text(text = stringResource(R.string.titlePlaceholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                spacer()
                TextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    placeholder = { Text(text = stringResource(R.string.descriptionPlaceholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
                spacer()
                LabelSwitch(
                    label = stringResource(id = R.string.scoreCarriesOver),
                    checked = scoreCarriesOver,
                    onCheckedChange = onScoreCarriesOverChange,
                    modifier = Modifier.fillMaxWidth(),
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
        {},
        "",
        {},
        true,
        {},
        true,
        {},
        {},
    )