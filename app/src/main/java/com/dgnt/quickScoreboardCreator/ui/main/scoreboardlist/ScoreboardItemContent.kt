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
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType


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
                    Text(text = item.title, style = typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                    if (item.type == ScoreboardType.NONE && item.id >= 0)
                        IconButton(onClick = {
                            onEvent(ScoreboardListEvent.OnDelete(item.id))
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
private fun `Normal`() =
    ScoreboardItemContent(ScoreboardItemData(0, ScoreboardType.NONE, title = "Basketball", description = "NBA Sport"), {})

@Preview(showBackground = true)
@Composable
private fun `Long description`() =
    ScoreboardItemContent(ScoreboardItemData(0, ScoreboardType.NONE, title = "Basketball", description = "Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool"), {})

@Preview(showBackground = true)
@Composable
private fun `Long title and long description`() =
    ScoreboardItemContent(ScoreboardItemData(0, ScoreboardType.NONE, title = "Basketball Basketball Basketball Basketball Basketball Basketball Basketball Basketball Basketball Basketball", description = "Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool"), {})

