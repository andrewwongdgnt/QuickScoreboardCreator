@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.main.teamdetails


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent


@Composable
fun TeamDetailsDialogContent(
    onDone: () -> Unit,
    viewModel: TeamDetailsViewModel = hiltViewModel()
) {
    var valid by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Done -> onDone()
                is UiEvent.Validation -> {
                    valid = event.valid
                }

                else -> Unit
            }
        }
    }

    TeamDetailsInnerDialogContent(
        viewModel.title,
        { viewModel.title = it },
        viewModel.description,
        { viewModel.description = it },
        viewModel.teamIcon,
        {
            // TODO
        },
        valid,
        { viewModel.onEvent(TeamDetailsEvent.OnDone) },
        { onDone() }
    )

}

@Composable
private fun TeamDetailsInnerDialogContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    teamIcon: TeamIcon,
    onTeamIconChange: () -> Unit,
    valid: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    AlertDialog(
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
                Box(
                    modifier = Modifier.clickable(onClick = onTeamIconChange)
                ) {
                    Image(
                        painterResource(teamIcon.res),
                        null,
                        modifier = Modifier.padding(10.dp)
                    )
                    val color = MaterialTheme.colorScheme.primary
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit),
                        modifier = Modifier.align(Alignment.BottomEnd)
                            .drawBehind {
                                drawCircle(
                                    style = Stroke(
                                        width = 5f
                                    ),
                                    color = color,
                                    radius = size.minDimension*0.7f
                                )
                            }
                    )
                }

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun `Gorilla`() =
    TeamDetailsInnerDialogContent(
        "",
        {},
        "",
        {},
        TeamIcon.GORILLA,
        {},
        true,
        {},
        {},
    )

@Preview(showBackground = true)
@Composable
private fun `Tiger`() =
    TeamDetailsInnerDialogContent(
        "",
        {},
        "",
        {},
        TeamIcon.TIGER,
        {},
        true,
        {},
        {},
    )
@Preview(showBackground = true)
@Composable
private fun `Alien`() =
    TeamDetailsInnerDialogContent(
        "",
        {},
        "",
        {},
        TeamIcon.ALIEN,
        {},
        true,
        {},
        {},
    )