package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.TriangleDown

@Composable
fun <T> MultipleOptionsPicker(
    modifier: Modifier = Modifier,
    header: String,
    options: List<OptionData<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    if (options.size <= 4)
        RadioGroup(
            modifier,
            header,
            options,
            selectedOption,
            onOptionSelected
        )
    else
        DropDown(
            modifier,
            header,
            options,
            selectedOption,
            onOptionSelected
        )

}

@Composable
private fun <T> RadioGroup(
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

@Composable
private fun <T> DropDown(
    modifier: Modifier = Modifier,
    header: String,
    options: List<OptionData<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = header, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    isDropDownExpanded.value = true
                }
                .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                .padding(8.dp)
        ) {
            Text(text = options.find { it.data == selectedOption }?.label ?: run { "" }, modifier = Modifier.weight(1f))
            Icon(
                imageVector = TriangleDown,
                contentDescription = stringResource(R.string.dropDownIndicator)
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            }) {
            options.forEach { item ->
                DropdownMenuItem(text = {
                    Text(text = item.label)
                },
                    onClick = {
                        isDropDownExpanded.value = false
                        onOptionSelected.invoke(item.data)
                    })
            }
        }


    }
}

data class OptionData<T>(
    val label: String,
    val data: T
)

@Preview(showBackground = true)
@Composable
private fun `4 options`() {
    MultipleOptionsPicker(
        header = "Header",
        options = listOf(
            OptionData(
                label = "Final",
                data = WinRule.Final
            ),
            OptionData(
                label = "Count",
                data = WinRule.Count
            ),
            OptionData(
                label = "Sum",
                data = WinRule.Sum
            ),
        ),
        selectedOption = WinRule.Sum,
        onOptionSelected = { }
    )
}


@Preview(showBackground = true)
@Composable
private fun `6 options`() {
    MultipleOptionsPicker(
        header = "Header",
        options = listOf(
            OptionData(
                label = "None",
                data = IntervalEndSound.None
            ),
            OptionData(
                label = "Bell",
                data = IntervalEndSound.Bell
            ),
            OptionData(
                label = "Buzzer",
                data = IntervalEndSound.Buzzer
            ),
            OptionData(
                label = "Low Buzzer",
                data = IntervalEndSound.LowBuzzer
            ),
            OptionData(
                label = "Horn",
                data = IntervalEndSound.Horn
            ),
            OptionData(
                label = "Whistle",
                data = IntervalEndSound.Whistle
            ),
        ),
        selectedOption = IntervalEndSound.LowBuzzer,
        onOptionSelected = { }
    )
}