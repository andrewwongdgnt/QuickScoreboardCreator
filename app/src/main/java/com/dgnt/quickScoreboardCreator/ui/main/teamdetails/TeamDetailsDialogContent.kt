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
import com.dgnt.quickScoreboardCreator.ui.common.header
import com.dgnt.quickScoreboardCreator.ui.composable.IconDisplay
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
        description,
        icon,
        iconChanging,
        viewModel::onEvent,
        valid
    )

}

@Composable
private fun TeamDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    title: String,
    description: String,
    icon: TeamIcon?,
    iconChanging: Boolean,
    onEvent: (TeamDetailsEvent) -> Unit,
    valid: Boolean
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }
    AlertDialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = { onEvent(TeamDetailsEvent.OnDismiss) },
        title = {
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(id = R.string.teamDetailsTitle)
            )
        },
        dismissButton = {
            Button(
                onClick = { onEvent(TeamDetailsEvent.OnDismiss) }
            ) {
                Text(stringResource(id = android.R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                enabled = valid,
                onClick = { onEvent(TeamDetailsEvent.OnConfirm) }
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
                    onValueChange = { onEvent(TeamDetailsEvent.OnTitleChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.titlePlaceholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { onEvent(TeamDetailsEvent.OnDescriptionChange(it)) },
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
                                            onEvent(TeamDetailsEvent.OnNewIcon(icon))
                                        }
                                )
                            }
                        }

                    }
                } else
                    IconDisplay(
                        iconRes = icon?.res,
                        onClick = {
                            onEvent(TeamDetailsEvent.OnIconEdit)
                        }
                    )

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun `New Icon Selection`() =
    TeamDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        TeamIcon.GORILLA,
        true,
        {},
        true
    )

@Preview(showBackground = true)
@Composable
private fun `Gorilla`() =
    TeamDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        TeamIcon.GORILLA,
        false,
        {},
        true
    )

@Preview(showBackground = true)
@Composable
private fun `Tiger`() =
    TeamDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        TeamIcon.TIGER,
        false,
        {},
        true,
    )

@Preview(showBackground = true)
@Composable
private fun `Alien`() =
    TeamDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        TeamIcon.ALIEN,
        false,
        {},
        true,
    )

@Preview(showBackground = true)
@Composable
private fun `Loading icon`() =
    TeamDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        null,
        false,
        {},
        true,
    )