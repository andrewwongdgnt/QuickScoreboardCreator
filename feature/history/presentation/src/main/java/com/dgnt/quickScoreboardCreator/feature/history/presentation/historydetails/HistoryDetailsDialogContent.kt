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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.IconDisplay
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconDrawableResHolder
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconGroupStringResHolder
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconPicker
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
    val valid by viewModel.valid.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val icon by viewModel.icon.collectAsStateWithLifecycle()
    val iconChanging by viewModel.iconChanging.collectAsStateWithLifecycle()
    HistoryDetailsInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        title = title,
        onTitleChange = viewModel::onTitleChange,
        description = description,
        onDescriptionChange = viewModel::onDescriptionChange,
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
    description: String,
    onDescriptionChange: (String) -> Unit,
    icon: SportIcon?,
    onIconChange: (SportIcon) -> Unit,
    iconChanging: Boolean,
    onIconEdit: (Boolean) -> Unit,
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
        actionIcon = Icons.Default.Delete,
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
        if (iconChanging)
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                IconPicker(
                    iconGroups = mapOf(IconGroupStringResHolder(R.string.pickIconMsg) to SportIcon.entries.map { IconDrawableResHolder(it, it.iconRes()) }),
                    onCancel = { onIconEdit(false) },
                    onIconChange = { onIconChange(it.originalIcon) }
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
                    value = title,
                    onValueChange = onTitleChange,
                    placeholder = { Text(text = stringResource(R.string.titlePlaceholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    placeholder = { Text(text = stringResource(R.string.descriptionPlaceholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))


                IconDisplay(
                    iconRes = icon?.iconRes(),
                    onClick = { onIconEdit(true) }
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
        description = "description 222",
        onDescriptionChange = {},
        icon = SportIcon.HOCKEY,
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
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        icon = null,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        valid = true,
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )