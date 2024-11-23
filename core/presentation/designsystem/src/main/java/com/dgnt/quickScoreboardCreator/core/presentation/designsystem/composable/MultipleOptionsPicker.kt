package com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector.Speaker
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector.TriangleDown

@Composable
fun <T> MultipleOptionsPicker(
    modifier: Modifier = Modifier,
    header: String,
    headerIconPair: Pair<ImageVector, String>? = null,
    onHeaderIconClick: () -> Unit = { },
    options: List<OptionData<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = header,
                style = MaterialTheme.typography.titleMedium
            )
            headerIconPair?.let {
                IconButton(onClick = onHeaderIconClick) {
                    Icon(
                        imageVector = it.first,
                        contentDescription = it.second
                    )
                }

            }
        }
        if (options.size <= 4)
            RadioGroup(
                options,
                selectedOption,
                onOptionSelected
            )
        else
            DropDown(
                options,
                selectedOption,
                onOptionSelected
            )

    }
}

@Composable
private fun <T> RadioGroup(
    options: List<OptionData<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {

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
            Text(
                modifier = Modifier.weight(1f),
                text = item.label
            )
            item.iconPair?.let {
                IconButton(onClick = item.onIconClick) {
                    Icon(
                        imageVector = it.first,
                        contentDescription = it.second
                    )
                }

            }
        }
    }

}

@Composable
private fun <T> DropDown(
    options: List<OptionData<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.label
                    )
                    item.iconPair?.let {
                        IconButton(onClick = item.onIconClick) {
                            Icon(
                                imageVector = it.first,
                                contentDescription = it.second
                            )
                        }

                    }
                }
            },
                onClick = {
                    isDropDownExpanded.value = false
                    onOptionSelected.invoke(item.data)
                })
        }
    }


}

data class OptionData<T>(
    val label: String,
    val data: T,
    val iconPair: Pair<ImageVector, String>? = null,
    val onIconClick: () -> Unit = { },
)

@Preview(showBackground = true)
@Composable
private fun `4 options`() {
    MultipleOptionsPicker(
        header = "Header",
        options = listOf(
            OptionData(
                label = "Final",
                data = "WinRule.Final"
            ),
            OptionData(
                label = "Count",
                data = "WinRule.Count"
            ),
            OptionData(
                label = "Sum",
                data = "WinRule.Sum"
            ),
        ),
        selectedOption = "WinRule.Sum",
        onOptionSelected = { }
    )
}


@Preview(showBackground = true)
@Composable
private fun `4 options with header icon`() {
    MultipleOptionsPicker(
        header = "Header",
        headerIconPair = Speaker to "",
        options = listOf(
            OptionData(
                label = "Final",
                data = "WinRule.Final",
                iconPair = Speaker to ""
            ),
            OptionData(
                label = "Count",
                data = "WinRule.Count",
                iconPair = Speaker to ""
            ),
            OptionData(
                label = "Sum",
                data = "WinRule.Sum",
                iconPair = Speaker to ""
            ),
        ),
        selectedOption = "WinRule.Sum",
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
                data = 0
            ),
            OptionData(
                label = "Bell",
                data = 1
            ),
            OptionData(
                label = "Buzzer",
                data = 2
            ),
            OptionData(
                label = "Low Buzzer",
                data = 3
            ),
            OptionData(
                label = "Horn",
                data = 4
            ),
            OptionData(
                label = "Whistle",
                data = 5
            ),
        ),
        selectedOption = 3,
        onOptionSelected = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun `6 options with header icon`() {
    MultipleOptionsPicker(
        header = "Header",
        headerIconPair = Speaker to "",
        options = listOf(
            OptionData(
                label = "None",
                data = 0
            ),
            OptionData(
                label = "Bell",
                data = 1,
                iconPair = Speaker to ""
            ),
            OptionData(
                label = "Buzzer",
                data = 2,
                iconPair = Speaker to ""
            ),
            OptionData(
                label = "Low Buzzer",
                data = 3,
                iconPair = Speaker to ""
            ),
            OptionData(
                label = "Horn",
                data = 4,
                iconPair = Speaker to ""
            ),
            OptionData(
                label = "Whistle",
                data = 5,
                iconPair = Speaker to ""
            ),
        ),
        selectedOption = 3,
        onOptionSelected = { }
    )
}