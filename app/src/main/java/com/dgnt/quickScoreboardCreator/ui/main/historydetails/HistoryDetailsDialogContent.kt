package com.dgnt.quickScoreboardCreator.ui.main.historydetails

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.ui.common.composable.IconDisplay
import com.dgnt.quickScoreboardCreator.ui.common.composable.ScoreboardIconPicker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun HistoryDetailsDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: HistoryDetailsViewModel = hiltViewModel()
) {
    val valid by viewModel.valid.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val icon by viewModel.icon.collectAsStateWithLifecycle()
    val iconChanging by viewModel.iconChanging.collectAsStateWithLifecycle()
    HistoryDetailsInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        title = title,
        onTitleChange = viewModel::onTitleChange,
        icon = icon,
        onIconChange = viewModel::onIconChange,
        iconChanging = iconChanging,
        onIconEdit = viewModel::onIconEdit,
        valid = valid,
        onDelete = viewModel::onDelete,
        onDismiss = viewModel::onDismiss,
        onConfirm = viewModel::onConfirm
    )
}

@Composable
private fun HistoryDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    icon: ScoreboardIcon?,
    onIconChange: (ScoreboardIcon) -> Unit,
    iconChanging: Boolean,
    onIconEdit: () -> Unit,
    valid: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit

) {
    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    val context = LocalContext.current
    DefaultAlertDialog(
        title = stringResource(id = R.string.historyDetailsTitle),
        actionContentDescription = stringResource(id = R.string.delete),
        actionOnClick = {
            Toast.makeText(context, R.string.longClickDeleteMsg, Toast.LENGTH_LONG).show()
        },
        actionOnLongClick = onDelete,
        confirmText = stringResource(id = android.R.string.ok),
        confirmEnabled = valid,
        onConfirm = onConfirm,
        dismissText = stringResource(id = android.R.string.cancel),
        onDismiss = onDismiss

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = onTitleChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (iconChanging)
                ScoreboardIconPicker(
                    onIconChange = onIconChange
                )
            else
                IconDisplay(
                    iconRes = icon?.res,
                    onClick = onIconEdit
                )
        }

    }
}

@Composable
@Preview(showBackground = true)
private fun `New Icon Selection`() {
    HistoryDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "my title",
        onTitleChange = {},
        icon = ScoreboardIcon.HOCKEY,
        onIconChange = {},
        iconChanging = true,
        onIconEdit = {},
        valid = true,
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun `Loading Icon`() =
    HistoryDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "333",
        onTitleChange = {},
        icon = null,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        valid = true,
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )