package com.dgnt.quickScoreboardCreator.ui.scoreboardlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.ui.theme.QuickScoreboardCreatorTheme


@Composable
fun ScoreboardItemContent(
    item: ScoreboardEntity,
    onEvent: (ScoreboardListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column {
            Column(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = item.title, style = typography.headlineLarge, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        onEvent(ScoreboardListEvent.OnDelete(listOf(item)))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
                Text(text = item.description, style = typography.labelSmall, fontStyle = FontStyle.Italic, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Divider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreboardItemContentPreview() {
    ScoreboardItemContent(ScoreboardEntity(title = "Basketball", description = "Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool Basketball is pretty cool"), {})
}
