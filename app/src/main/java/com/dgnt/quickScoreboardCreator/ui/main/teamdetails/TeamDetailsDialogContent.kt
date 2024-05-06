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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
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
    val valid = viewModel.valid
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Done -> onDone()
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
        viewModel.teamIconChanging,
        viewModel::onEvent,
        valid,
        { viewModel.onEvent(TeamDetailsEvent.OnDone) },
        onDone
    )

}

@Composable
private fun TeamDetailsInnerDialogContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    teamIcon: TeamIcon?,
    teamIconChanging: Boolean,
    onEvent: (TeamDetailsEvent) -> Unit,
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
                if (teamIconChanging) {
                    val group = TeamIcon.entries.groupBy { it.group }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(45.dp)
                    ) {
                        group.onEachIndexed { index, entry ->
                            val teamIconGroup = entry.key
                            val teamIcons = entry.value
                            header {
                                Text(
                                    stringResource(id = teamIconGroup.res),
                                    modifier = if (index > 0) Modifier.padding(
                                        start = 0.dp, end = 0.dp, top = 20.dp, bottom = 4.dp
                                    ) else Modifier
                                )
                            }
                            items(teamIcons.size) {
                                val icon = teamIcons[it]
                                Image(
                                    painterResource(icon.res),
                                    null,
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clickable {
                                            onEvent(TeamDetailsEvent.OnNewTeamIcon(icon))
                                        }
                                )
                            }
                        }

                    }
                } else

                    if (teamIcon != null) {

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier.clickable(onClick = {
                                onEvent(TeamDetailsEvent.OnTeamIconEdit)
                            })
                        ) {
                            val primaryColor = MaterialTheme.colorScheme.primary
                            val radiusModifier = 0.65f
                            Image(
                                painterResource(teamIcon.res),
                                null,
                                modifier = Modifier
                                    .drawBehind {
                                        drawCircle(
                                            style = Stroke(
                                                width = 20f
                                            ),
                                            color = primaryColor,
                                            radius = size.minDimension * radiusModifier
                                        )
                                    }
                                    .drawBehind {
                                        drawCircle(
                                            color = Color.White,
                                            radius = size.minDimension * radiusModifier
                                        )
                                    }
                            )
                            val tint = MaterialTheme.colorScheme.background
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit),
                                tint = tint,
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp)
                                    .align(Alignment.BottomEnd)
                                    .drawBehind {
                                        drawCircle(
                                            color = primaryColor.copy(alpha = 0.7f),
                                            radius = size.minDimension * 0.7f
                                        )
                                    }
                            )
                        }
                    } else
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(72.dp)
                                .height(72.dp)
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )

            }
        }
    )
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

@Preview(showBackground = true)
@Composable
private fun `New Icon Selection`() =
    TeamDetailsInnerDialogContent(
        "",
        {},
        "",
        {},
        TeamIcon.GORILLA,
        true,
        {},
        true,
        {},
        {},
    )

@Preview(showBackground = true)
@Composable
private fun `Gorilla`() =
    TeamDetailsInnerDialogContent(
        "",
        {},
        "",
        {},
        TeamIcon.GORILLA,
        false,
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
        false,
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
        false,
        {},
        true,
        {},
        {},
    )

@Preview(showBackground = true)
@Composable
private fun `Loading icon`() =
    TeamDetailsInnerDialogContent(
        "",
        {},
        "",
        {},
        null,
        false,
        {},
        true,
        {},
        {},
    )