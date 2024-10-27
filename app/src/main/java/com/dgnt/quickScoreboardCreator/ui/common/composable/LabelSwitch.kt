package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun LabelSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Switch,
                onClick = {
                    onCheckedChange(!checked)
                }
            ),
        verticalAlignment = Alignment.CenterVertically

    ) {

        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun `Checked`() =
    LabelSwitch(
        "label",
        true,
        {}
    )

@Preview(showBackground = true)
@Composable
private fun `Unchecked`() =
    LabelSwitch(
        "label",
        false,
        {}
    )

@Preview(showBackground = true)
@Composable
private fun `Long label`() =
    LabelSwitch(
        "this is my label this is my label this is my label this is my label ",
        false,
        {}
    )
