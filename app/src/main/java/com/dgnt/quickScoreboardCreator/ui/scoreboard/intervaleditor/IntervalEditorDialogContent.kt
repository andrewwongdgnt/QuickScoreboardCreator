@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun IntervalEditorDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: IntervalEditorViewModel = hiltViewModel()
) {


    IntervalEditorInnerDialogContent(
        viewModel.uiEvent,
        onUiEvent,
        viewModel.minuteString,
        viewModel.secondString,
        viewModel.intervalString,
        viewModel.labelInfo,
        viewModel.errors,
        viewModel::onEvent,
        { viewModel.onEvent(IntervalEditorEvent.OnDismiss) },
        { viewModel.onEvent(IntervalEditorEvent.OnConfirm) }
    )

}

@Composable
private fun IntervalEditorInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    minuteString: String,
    secondString: String,
    intervalString: String,
    labelInfo: Pair<String?, Int?>,
    errors: Set<IntervalEditorErrorType>,
    onEvent: (IntervalEditorEvent) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    AlertDialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = onDismiss,
        title = {
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(id = R.string.intervalEditorTitle)
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
                onClick = onConfirm,
                enabled = errors.isEmpty()
            ) {
                Text(stringResource(id = android.R.string.ok))
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val numberFieldWidth = 65.dp
                (errors.find { it is IntervalEditorErrorType.Time } as? IntervalEditorErrorType.Time)?.let { error ->
                    Text(
                        text = stringResource(id = R.string.invalidTimeMsg, TimeData(error.min, error.second, 0).formatTime()),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 6.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = minuteString,
                        onValueChange = {
                            if (it.length <= 3)
                                onEvent(IntervalEditorEvent.OnMinuteChange(it))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.width(numberFieldWidth)
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    TextField(
                        value = secondString,
                        onValueChange = {
                            if (it.length <= 2)
                                onEvent(IntervalEditorEvent.OnSecondChange(it))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.width(numberFieldWidth)
                    )

                }
                Spacer(modifier = Modifier.height(24.dp))
                (errors.find { it is IntervalEditorErrorType.Interval } as? IntervalEditorErrorType.Interval)?.let { error ->
                    Text(
                        text = stringResource(id = R.string.invalidIntervalMsg, error.value),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 6.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = labelInfo.format(),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    TextField(
                        value = intervalString,
                        onValueChange = {
                            if (it.length <= 2)
                                onEvent(IntervalEditorEvent.OnIntervalChange(it))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.width(numberFieldWidth)
                    )
                }
            }
        }
    )
}

@Composable
private fun Pair<String?, Int?>.format(): String {
    return first ?: second?.let {
        stringResource(id = it)
    } ?: ""
}

@Preview(showBackground = true)
@Composable
private fun `12 minutes 8 seconds`() =
    IntervalEditorInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        minuteString = "12",
        secondString = "8",
        intervalString = "1",
        labelInfo = Pair(null, R.string.quarter),
        errors = emptySet(),
        onEvent = {},
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `invalid time`() =
    IntervalEditorInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        minuteString = "12",
        secondString = "8",
        intervalString = "1",
        labelInfo = Pair(null, R.string.quarter),
        errors = setOf(IntervalEditorErrorType.Time(12, 0)),
        onEvent = {},
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `invalid interval`() =
    IntervalEditorInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        minuteString = "12",
        secondString = "8",
        intervalString = "1",
        labelInfo = Pair(null, R.string.quarter),
        errors = setOf(IntervalEditorErrorType.Interval(22)),
        onEvent = {},
        onDismiss = {},
        onConfirm = {},
    )

