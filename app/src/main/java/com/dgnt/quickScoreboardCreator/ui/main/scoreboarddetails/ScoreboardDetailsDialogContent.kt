@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.ui.common.composable.IconDisplay
import com.dgnt.quickScoreboardCreator.ui.common.composable.ScoreboardIconPicker
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
    val icon by viewModel.icon.collectAsStateWithLifecycle()
    val iconChanging by viewModel.iconChanging.collectAsStateWithLifecycle()
    val isNewEntity by viewModel.isNewEntity.collectAsStateWithLifecycle()

    ScoreboardDetailsInnerDialogContent(
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
        isNewEntity = isNewEntity,
        onDelete = viewModel::onDelete,
        onDismiss = viewModel::onDismiss,
        onConfirm = viewModel::onConfirm,
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
    icon: ScoreboardIcon?,
    onIconChange: (ScoreboardIcon) -> Unit,
    iconChanging: Boolean,
    onIconEdit: () -> Unit,
    valid: Boolean,
    isNewEntity: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    val context = LocalContext.current
    DefaultAlertDialog(
        title = stringResource(id = R.string.scoreboardDetailsTitle),
        actionIcon = Icons.Default.Delete.takeUnless { isNewEntity },
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

@Preview(showBackground = true)
@Composable
private fun `New Icon Selection`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        icon = null,
        onIconChange = {},
        iconChanging = true,
        onIconEdit = {},
        valid = true,
        isNewEntity = true,
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Basketball`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        icon = ScoreboardIcon.BASKETBALL,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        valid = true,
        isNewEntity = false,
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Hockey`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        icon = ScoreboardIcon.HOCKEY,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        valid = true,
        isNewEntity = true,
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Loading Icon`() =
    ScoreboardDetailsInnerDialogContent(
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
        isNewEntity = true,
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )