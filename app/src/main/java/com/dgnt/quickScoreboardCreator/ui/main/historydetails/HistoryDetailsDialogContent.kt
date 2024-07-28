package com.dgnt.quickScoreboardCreator.ui.main.historydetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.DefaultAlertDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun TimelineSaverDialogContent(
    updatedHistoryId: Long?,
    onUiEvent: (UiEvent) -> Unit,
    viewModel: HistoryDetailsViewModel = hiltViewModel()
) {

    viewModel.onHistoryIdUpdate(updatedHistoryId)

    val title by viewModel.title.collectAsStateWithLifecycle()
    val icon by viewModel.icon.collectAsStateWithLifecycle()
    TimelineSaverInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        placeHolderTitle = title,
        icon = icon,
        onTitleChange = viewModel::onTitleChange,
        onDismiss = viewModel::onDismiss,
        onSave = viewModel::onConfirm
    )
}

@Composable
private fun TimelineSaverInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    placeHolderTitle: String,
    icon: ScoreboardIcon?,
    onTitleChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit

) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    DefaultAlertDialog(
        title = stringResource(id = R.string.timelineSaverTitle),
        confirmText = stringResource(id = R.string.save),
        dismissText = stringResource(id = android.R.string.cancel),
        onConfirm = onSave,
        onDismiss = onDismiss

    ) {

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = placeHolderTitle,
            onValueChange = onTitleChange,
            leadingIcon = {
                icon?.let {
                    Image(
                        painterResource(icon.res),
                        null,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .width(48.dp)

                    )
                }
            }
        )

    }
}

@Composable
@Preview
private fun `normal save`() {
    TimelineSaverInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        placeHolderTitle = "my title",
        icon = ScoreboardIcon.HOCKEY,
        onTitleChange = {},
        onDismiss = {},
        onSave = {},
    )
}