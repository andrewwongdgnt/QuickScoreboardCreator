@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamdetails


import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.presentation.resourcemapping.iconRes
import com.dgnt.quickScoreboardCreator.feature.team.presentation.resourcemapping.titleRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun TeamDetailsDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TeamDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TeamDetailsInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TeamDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: TeamDetailsState,
    onAction: (TeamDetailsAction) -> Unit
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    val context = LocalContext.current
    DefaultAlertDialog(
        title = stringResource(id = R.string.teamDetailsTitle),
        actionIcon = Icons.Default.Delete.takeUnless { state.isNewEntity },
        actionContentDescription = stringResource(id = R.string.delete),
        actionOnClick = {
            Toast.makeText(context, R.string.longClickDeleteMsg, Toast.LENGTH_LONG).show()
        },
        actionOnLongClick = { onAction(TeamDetailsAction.Delete) },
        confirmText = stringResource(id = android.R.string.ok),
        confirmEnabled = state.valid,
        onConfirm = { onAction(TeamDetailsAction.Confirm) },
        dismissText = stringResource(id = android.R.string.cancel),
        onDismiss = { onAction(TeamDetailsAction.Dismiss) }
    ) {
        if (state.iconState is TeamIconState.Picked.Changing) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                IconPicker(
                    iconGroups = TeamIcon.entries.groupBy { it.group }
                        .mapKeys { IconGroupStringResHolder(it.key.titleRes()) }
                        .mapValues { teamIcon ->
                            teamIcon.value.map {
                                IconDrawableResHolder(it, it.iconRes())
                            }
                        },
                    onCancel = { onAction(TeamDetailsAction.IconEdit(false)) },
                    onIconChange = { onAction(TeamDetailsAction.IconChange(it.originalIcon)) }
                )
            }
        } else
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = state.title,
                    onValueChange = { onAction(TeamDetailsAction.TitleChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.namePlaceholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = state.description,
                    onValueChange = { onAction(TeamDetailsAction.DescriptionChange(it)) },
                    placeholder = { Text(text = stringResource(R.string.descriptionPlaceholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))

                IconDisplay(
                    iconRes = (state.iconState as? TeamIconState.Picked)?.teamIcon?.iconRes(),
                    onClick = { onAction(TeamDetailsAction.IconEdit(true)) }
                )

            }

    }


}

@Preview(showBackground = true)
@Composable
private fun TeamDetailsDialogContentPreview(
    @PreviewParameter(TeamDetailsPreviewStateProvider::class) state: TeamDetailsState
) = QuickScoreboardCreatorTheme {
    Surface {
        TeamDetailsInnerDialogContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}