package com.dgnt.quickScoreboardCreator.feature.history.presentation.historydetails

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.IconDisplay
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconDrawableResHolder
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconGroupStringResHolder
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconPicker
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.iconRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun HistoryDetailsDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: HistoryDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryDetailsInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun HistoryDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: HistoryDetailsState,
    onAction: (HistoryDetailsAction) -> Unit

) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    val context = LocalContext.current
    DefaultAlertDialog(
        title = stringResource(id = R.string.historyDetailsTitle),
        actionIcon = Icons.Default.Delete,
        actionContentDescription = stringResource(id = R.string.delete),
        actionOnClick = {
            Toast.makeText(context, R.string.longClickDeleteMsg, Toast.LENGTH_LONG).show()
        },
        actionOnLongClick = { onAction(HistoryDetailsAction.Delete) },
        confirmText = stringResource(id = android.R.string.ok),
        confirmEnabled = state.valid,
        onConfirm = { onAction(HistoryDetailsAction.Confirm) },
        dismissText = stringResource(id = android.R.string.cancel),
        onDismiss = { onAction(HistoryDetailsAction.Dismiss) }

    ) {
        if (state.iconState is HistoryIconState.Picked.Changing)
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                IconPicker(
                    iconGroups = mapOf(IconGroupStringResHolder(R.string.pickIconMsg) to SportIcon.entries.map { IconDrawableResHolder(it, it.iconRes()) }),
                    onCancel = { onAction(HistoryDetailsAction.IconEdit(false)) },
                    onIconChange = { onAction(HistoryDetailsAction.IconChange(it.originalIcon)) }
                )
            }
        else
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = state.title,
                    onValueChange = { onAction(HistoryDetailsAction.TitleChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.titlePlaceholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = state.description,
                    onValueChange = { onAction(HistoryDetailsAction.DescriptionChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.descriptionPlaceholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))


                IconDisplay(
                    iconRes = (state.iconState as? HistoryIconState.Picked)?.sportIcon?.iconRes(),
                    onClick = { onAction(HistoryDetailsAction.IconEdit(true)) }
                )
            }

    }
}

@Composable
@Preview(showBackground = true)
private fun HistoryDetailsDialogContentPreview(
    @PreviewParameter(HistoryDetailsPreviewStateProvider::class) state: HistoryDetailsState
) = QuickScoreboardCreatorTheme {
    Surface {
        HistoryDetailsInnerDialogContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}

