package com.dgnt.quickScoreboardCreator.ui.scoreboardList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun ScoreboardItemContent(item: ScoreboardModel = ScoreboardModel("Basketball")) {
    Row {
        Column {
            Column(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)) {
                Text(text = item.title, style = typography.headlineLarge)
                Text(text = "VIEW DETAIL", style = typography.bodySmall)
            }
            Divider()
        }
    }
}
