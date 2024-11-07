package com.dgnt.quickScoreboardCreator.ui.common.composable

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.TriangleDown
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.TriangleUp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoveableElement(
    context: Context,
    header: String,
    size: Int,
    currentIndex: Int,
    lastIndex: Int,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = header,
            style = MaterialTheme.typography.titleMedium
        )
        if (size > 1) {
            if (currentIndex > 0)
                IconButton(onClick = onMoveUp) {
                    Icon(
                        imageVector = TriangleUp,
                        contentDescription = stringResource(R.string.up)
                    )
                }
            if (currentIndex < lastIndex)
                IconButton(onClick = onMoveDown) {
                    Icon(
                        imageVector = TriangleDown,
                        contentDescription = stringResource(R.string.down)
                    )
                }
            Icon(modifier = Modifier.combinedClickable(
                onClick = { Toast.makeText(context, R.string.longClickDeleteMsg, Toast.LENGTH_LONG).show() },
                onLongClick = onDelete
            ),
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete)
            )

        }
    }
}