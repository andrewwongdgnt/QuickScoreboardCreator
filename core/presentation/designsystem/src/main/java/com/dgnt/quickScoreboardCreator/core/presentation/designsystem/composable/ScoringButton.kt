package com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.core.presentation.ui.asIncrementDisplay

@Composable
fun ScoringButton(
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    simpleMode: Boolean,
    number: Int,
    index: Int,
    onIncrement: (Int, Boolean) -> Unit
) {
    Row(modifier = modifier) {
        if (!simpleMode)
            OutlinedButton(
                modifier = buttonModifier,
                onClick = {
                    onIncrement(index, false)
                },
                shape = RoundedCornerShape(10.dp, 0.dp, 0.dp, 10.dp)
            ) {
                Text(text = (number * -1).asIncrementDisplay())
            }
        Button(
            modifier = buttonModifier,
            onClick = {
                onIncrement(index, true)
            },
            shape = if (simpleMode) RoundedCornerShape(20.dp) else RoundedCornerShape(0.dp, 10.dp, 10.dp, 0.dp)
        ) {
            Text(text = number.asIncrementDisplay())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoringButtonPreviewSimple() =
    ScoringButton(simpleMode = true, number = 5, index = 0) { _, _ -> }

@Preview(showBackground = true)
@Composable
private fun ScoringButtonPreviewAdvance() =
    ScoringButton(simpleMode = false, number = 5, index = 0) { _, _ -> }

@Preview(showBackground = true)
@Composable
private fun ScoringButtonPreviewSimpleAsNeg() =
    ScoringButton(simpleMode = true, number = -5, index = 0) { _, _ -> }

@Preview(showBackground = true)
@Composable
private fun ScoringButtonPreviewAdvanceAsNeg() =
    ScoringButton(simpleMode = false, number = -5, index = 0) { _, _ -> }