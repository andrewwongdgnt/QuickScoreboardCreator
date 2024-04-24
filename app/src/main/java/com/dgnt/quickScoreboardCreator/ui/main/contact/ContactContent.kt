package com.dgnt.quickScoreboardCreator.ui.main.contact

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ContactContent(
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Privacy policy")
        Text(text = "Email")
        Text(text = "Rate this app")
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactContentPreview() {
    ContactContent()
}