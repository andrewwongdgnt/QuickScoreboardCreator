package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScoreboardInteractionContent(
    viewModel: ScoreboardInteractionViewModel = hiltViewModel()
) {
        Text(text = "Andrew Wong")

}

@Preview(showBackground = true)
@Composable
private fun PreviewScoreboardInteractionContent() {
    ScoreboardInteractionContent()
}