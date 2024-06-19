package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.ui.common.asIncrementDisplay
import kotlin.math.abs

@Composable
fun ScoringButton(
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    number: Int,
    index: Int,
    onIncrement: (Int, Boolean) -> Unit
) {
    Row(modifier = modifier) {
        OutlinedButton(
            modifier = buttonModifier,
            onClick = {
                onIncrement(index, false)
            },
            shape = RoundedCornerShape(10.dp, 0.dp, 0.dp, 10.dp)
        ) {
            Text(text = (abs(number) * -1).asIncrementDisplay())
        }
        Button(
            modifier = buttonModifier,
            onClick = {
                onIncrement(index, true)
            },
            shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 0.dp)
        ) {
            Text(text = abs(number).asIncrementDisplay())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoringButtonPreview() =
    ScoringButton(number = 5, index = 0) { _, _ -> }