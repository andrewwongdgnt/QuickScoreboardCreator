package com.dgnt.quickScoreboardCreator.ui.scoreboardlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity

@Preview(showBackground = true)
@Composable
fun ScoreboardListContent() {
    var title by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf(
        ScoreboardEntity(title = "Basketball", description = "Basketball Desc"),
        ScoreboardEntity(title = "Spike Ball", description = "Spike Ball Desc"),
        ScoreboardEntity(title = "Hockey", description = "Hockey Desc"),
    )) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                if (title.isNotBlank())
                    items = items + ScoreboardEntity(title = title, description = "")
                title = ""
            }) {
                Text(text = "Add")
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = items,
                itemContent = {
                    ScoreboardItemContent(it)
                })
        }
    }
}
