package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TimeLimitPicker(
    modifier: Modifier = Modifier,
    minuteString: String,
    onMinuteChange: (String) -> Unit,
    secondString: String,
    onSecondChange: (String) -> Unit,
    numberFieldWidth: Dp = 65.dp
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TextField(
            value = minuteString,
            onValueChange = {
                if (it.length <= 3)
                    onMinuteChange(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.width(numberFieldWidth)
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        TextField(
            value = secondString,
            onValueChange = {
                if (it.length <= 2)
                    onSecondChange(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.width(numberFieldWidth)
        )

    }
}