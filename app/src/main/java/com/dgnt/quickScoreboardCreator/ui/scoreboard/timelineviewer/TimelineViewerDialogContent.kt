@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.DefaultAlertDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun TimelineViewerDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: TimelineViewerViewModel = hiltViewModel()
) {

    val labelInfo by viewModel.labelInfo.collectAsStateWithLifecycle()

    TimelineViewerInnerDialogContent(
        viewModel.uiEvent,
        onUiEvent,
        labelInfo,
        viewModel::onDismiss,
        viewModel::onSave
    )

}

@Composable
private fun TimelineViewerInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    labelInfo: Pair<String?, Int?>,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    DefaultAlertDialog(
        title = stringResource(id = R.string.timelineViewerTitle),
        confirmText = stringResource(id = R.string.save),
        onConfirm = onSave,
        dismissText = stringResource(id = android.R.string.cancel),
        onDismiss = onDismiss
    ) {


    }
}

@Composable
private fun Pair<String?, Int?>.format(): String {
    return first ?: second?.let {
        stringResource(id = it)
    } ?: ""
}

@Preview(showBackground = true)
@Composable
private fun `Regular Timeline`() =
    TimelineViewerInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        labelInfo = Pair(null, R.string.quarter),
        onDismiss = {},
        onSave = {},
    )



