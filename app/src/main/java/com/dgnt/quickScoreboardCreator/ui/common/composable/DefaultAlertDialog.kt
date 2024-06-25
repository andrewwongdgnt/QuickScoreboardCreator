package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DefaultAlertDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    actionIcon: ImageVector? = null,
    actionContentDescription: String = "",
    actionOnClick: () -> Unit = {},
    actionOnLongClick: () -> Unit = {},
    confirmText: String = "",
    confirmEnabled: Boolean = true,
    onConfirm: () -> Unit = {},
    dismissText: String = "",
    onDismiss: () -> Unit = {},
    body: @Composable () -> Unit
) {

    AlertDialog(
        modifier = modifier,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.titleSmall,
                    text = title
                )
                actionIcon?.let {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionContentDescription,
                        modifier = Modifier.combinedClickable(
                            onClick = actionOnClick,
                            onLongClick = actionOnLongClick
                        )
                    )
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(dismissText)
            }
        },
        confirmButton = {
            Button(
                enabled = confirmEnabled,
                onClick = onConfirm
            ) {
                Text(confirmText)
            }
        },
        text = body
    )
}