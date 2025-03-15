@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.intervaleditor


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.TimeLimitPicker
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.value
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun IntervalEditorDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: IntervalEditorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    IntervalEditorInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun IntervalEditorInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: IntervalEditorState,
    onAction: (IntervalEditorAction) -> Unit
) = state.run {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    DefaultAlertDialog(
        title = stringResource(id = R.string.intervalEditorTitle),
        confirmText = stringResource(id = android.R.string.ok),
        confirmEnabled = errors.isEmpty(),
        onConfirm = { onAction(IntervalEditorAction.Confirm) },
        dismissText = stringResource(id = android.R.string.cancel),
        onDismiss = { onAction(IntervalEditorAction.Dismiss) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val numberFieldWidth = 65.dp
            (errors.find { it is IntervalEditorErrorType.Time })?.let { error ->
                val errorMsg = when (error) {
                    is IntervalEditorErrorType.Time.Invalid -> stringResource(id = R.string.invalidTimeMsg, TimeData(error.min, error.second, 0).formatTime())
                    is IntervalEditorErrorType.Time.Empty -> stringResource(R.string.emptyTimeMsg)
                    is IntervalEditorErrorType.Time.Zero -> stringResource(R.string.zeroTimeMsg)
                    else -> null
                }
                errorMsg?.let {
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 6.dp)
                    )
                }
            }

            TimeLimitPicker(
                minuteString = minuteString,
                onMinuteChange = { onAction(IntervalEditorAction.MinuteChange(it)) },
                secondString = secondString,
                onSecondChange = { onAction(IntervalEditorAction.SecondChange(it)) },
                numberFieldWidth = numberFieldWidth
            )
            Spacer(modifier = Modifier.height(24.dp))
            (errors.find { it is IntervalEditorErrorType.Interval })?.let { error ->
                val errorMsg = when (error) {
                    is IntervalEditorErrorType.Interval.Invalid -> stringResource(id = R.string.invalidIntervalMsg, error.value)
                    is IntervalEditorErrorType.Interval.Empty -> stringResource(R.string.emptyIntervalMsg)
                    else -> null
                }
                errorMsg?.let {
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 6.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label.value(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                TextField(
                    value = intervalString,
                    onValueChange = {
                        if (it.length <= 2)
                            onAction(IntervalEditorAction.IntervalChange(it))
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
}

@Preview(showBackground = true)
@Composable
private fun IntervalEditorContentPreview(
    @PreviewParameter(IntervalEditorPreviewStateProvider::class) state: IntervalEditorState
) = QuickScoreboardCreatorTheme {
    Surface {
        IntervalEditorInnerDialogContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}