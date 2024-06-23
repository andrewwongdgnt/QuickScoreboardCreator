@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.main.teamdetails


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.IconDisplay
import com.dgnt.quickScoreboardCreator.ui.common.header
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun TeamDetailsDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TeamDetailsViewModel = hiltViewModel()
) {
    val valid by viewModel.valid.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val icon by viewModel.icon.collectAsStateWithLifecycle()
    val iconChanging by viewModel.iconChanging.collectAsStateWithLifecycle()
    TeamDetailsInnerDialogContent(
        viewModel.uiEvent,
        onUiEvent,
        title,
        viewModel::onTitleChange,
        description,
        viewModel::onDescriptionChange,
        icon,
        viewModel::onIconChange,
        iconChanging,
        viewModel::onIconEdit,
        valid,
        viewModel::onDismiss,
        viewModel::onConfirm,
    )

}

@Composable
private fun TeamDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    icon: TeamIcon?,
    onIconChange: (TeamIcon) -> Unit,
    iconChanging: Boolean,
    onIconEdit: () -> Unit,
    valid: Boolean,
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
                text = stringResource(id = R.string.teamDetailsTitle)
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
                if (iconChanging) {
                    val group = TeamIcon.entries.groupBy { it.group }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(45.dp)
                    ) {
                        group.onEachIndexed { index, entry ->
                            val iconGroup = entry.key
                            val icons = entry.value
                            header {
                                Text(
                                    stringResource(id = iconGroup.res),
                                    modifier = if (index > 0) Modifier.padding(
                                        start = 0.dp, end = 0.dp, top = 20.dp, bottom = 4.dp
                                    ) else Modifier
                                )
                            }
                            items(icons.toTypedArray()) { icon ->
                                Image(
                                    painterResource(icon.res),
                                    null,
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clickable {
                                            onIconChange(icon)
                                        }
                                )
                            }
                        }

                    }
                } else
                    IconDisplay(
                        iconRes = icon?.res,
                        onClick = onIconEdit
                    )

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun `New Icon Selection`() =
    TeamDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        icon = TeamIcon.GORILLA,
        onIconChange = {},
        iconChanging = true,
        onIconEdit = {},
        valid = true,
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Gorilla`() =
    TeamDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        icon = TeamIcon.GORILLA,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        valid = true,
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Tiger`() =
    TeamDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        icon = TeamIcon.TIGER,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        valid = true,
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Alien`() =
    TeamDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        icon = TeamIcon.ALIEN,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        valid = true,
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Loading icon`() =
    TeamDetailsInnerDialogContent(
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
        onDismiss = {},
        onConfirm = {},
    )