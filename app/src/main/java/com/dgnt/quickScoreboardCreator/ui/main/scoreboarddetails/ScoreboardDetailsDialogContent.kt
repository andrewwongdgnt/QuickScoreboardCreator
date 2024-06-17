@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails


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
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.header
import com.dgnt.quickScoreboardCreator.ui.composable.IconDisplay
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
    val scoreboardIcon by viewModel.scoreboardIcon.collectAsStateWithLifecycle()
    val scoreboardIconChanging by viewModel.scoreboardIconChanging.collectAsStateWithLifecycle()

    ScoreboardDetailsInnerDialogContent(
        viewModel.uiEvent,
        onUiEvent,
        title,
        description,
        scoreboardIcon,
        scoreboardIconChanging,
        viewModel::onEvent,
        valid,
    )
}

@Composable
private fun ScoreboardDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    title: String,
    description: String,
    scoreboardIcon: ScoreboardIcon?,
    scoreboardIconChanging: Boolean,
    onEvent: (ScoreboardDetailsEvent) -> Unit,
    valid: Boolean,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }
    AlertDialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = { onEvent(ScoreboardDetailsEvent.OnDismiss) },
        title = {
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(id = R.string.scoreboardDetailsTitle)
            )
        },
        dismissButton = {
            Button(
                onClick = { onEvent(ScoreboardDetailsEvent.OnDismiss) }
            ) {
                Text(stringResource(id = android.R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                enabled = valid,
                onClick = { onEvent(ScoreboardDetailsEvent.OnConfirm) }
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
                    onValueChange = { onEvent(ScoreboardDetailsEvent.OnTitleChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.titlePlaceholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { onEvent(ScoreboardDetailsEvent.OnDescriptionChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.descriptionPlaceholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (scoreboardIconChanging) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(45.dp)
                    ) {
                        header {
                            Text(
                                stringResource(id = R.string.pickIconMsg),
                            )
                        }
                        items(ScoreboardIcon.entries.toTypedArray()){ icon ->
                            Image(
                                painterResource(icon.res),
                                null,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clickable {
                                        onEvent(ScoreboardDetailsEvent.OnNewScoreboardIcon(icon))
                                    }
                            )
                        }

                    }
                } else
                    IconDisplay(
                        iconRes = scoreboardIcon?.res,
                        onClick = {
                            onEvent(ScoreboardDetailsEvent.OnScoreboardIconEdit)
                        }
                    )

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun `New Icon Selection`() =
    ScoreboardDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        null,
        true,
        {},
        true,
    )

@Preview(showBackground = true)
@Composable
private fun `Basketball`() =
    ScoreboardDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        ScoreboardIcon.BASKETBALL,
        false,
        {},
        true,
    )

@Preview(showBackground = true)
@Composable
private fun `Hockey`() =
    ScoreboardDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        ScoreboardIcon.HOCKEY,
        false,
        {},
        true,
    )

@Preview(showBackground = true)
@Composable
private fun `Loading Icon`() =
    ScoreboardDetailsInnerDialogContent(
        emptyFlow(),
        {},
        "",
        "",
        null,
        false,
        {},
        true,
    )