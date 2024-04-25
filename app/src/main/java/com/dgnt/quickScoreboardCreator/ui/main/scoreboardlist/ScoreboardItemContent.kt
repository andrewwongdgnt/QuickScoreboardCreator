package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.R


@Composable
fun ScoreboardItemContent(
    item: ScoreboardItemData,
    onEvent: (ScoreboardListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable {
                onEvent(ScoreboardListEvent.OnLaunch(item))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Column(modifier = Modifier.padding(start = 6.dp, top = 12.dp, bottom = 12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item.title, style = typography.titleLarge, modifier = Modifier.weight(1f))
                    if (item.type == null)
                        IconButton(onClick = {
                            onEvent(ScoreboardListEvent.OnDelete(listOf(item)))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    IconButton(onClick = {
                        onEvent(ScoreboardListEvent.OnEdit(item))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                }
                Text(text = item.description, style = typography.labelSmall, fontStyle = FontStyle.Italic, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreboardItemContentPreview() =
    ScoreboardItemContent(ScoreboardItemData(0, null, title = "Basketball", description = "Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool"), {})

