package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultSnackbar (
    snackbarData: SnackbarData,
    onSnackbarDismissed: () -> Unit
){
    val actionLabel = snackbarData.visuals.actionLabel
    val actionComposable: (@Composable () -> Unit)? = if (actionLabel != null) {
        @Composable {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = SnackbarDefaults.actionColor),
                onClick = { snackbarData.performAction() },
                content = { Text(actionLabel) }
            )
        }
    } else {
        null
    }
    val dismissActionComposable: (@Composable () -> Unit)? =
        if (snackbarData.visuals.withDismissAction) {
            @Composable {
                IconButton(
                    onClick = {
                        onSnackbarDismissed()
                        snackbarData.dismiss()
                    },
                    content = {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "",
                        )
                    }
                )
            }
        } else {
            null
        }
    Snackbar(
        modifier = Modifier.padding(12.dp),
        action = actionComposable,
        dismissAction = dismissActionComposable,
        content = { Text(snackbarData.visuals.message) }
    )
}