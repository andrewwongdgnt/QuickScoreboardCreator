package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TeamListContent(
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Team List")
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamListContentPreview() {
    TeamListContent()
}