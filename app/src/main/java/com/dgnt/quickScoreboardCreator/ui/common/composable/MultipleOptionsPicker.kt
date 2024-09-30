package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> MultipleOptionsPicker(
    modifier: Modifier = Modifier,
    header: String,
    options: List<OptionData<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = header, style = MaterialTheme.typography.titleMedium)
        options.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == item.data,
                    onClick = {
                        onOptionSelected(item.data)
                    }
                )
                Text(text = item.label)
            }
        }
    }
}

data class OptionData<T>(
    val label: String,
    val data: T
)