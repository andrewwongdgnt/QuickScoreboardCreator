package com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.BackButton
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.util.header

@Composable
fun <T> IconPicker(
    iconGroups: Map<IconGroupStringResHolder, List<IconDrawableResHolder<T>>>,
    onCancel: () -> Unit,
    onIconChange: (IconDrawableResHolder<T>) -> Unit
) {
    BackButton (onCancel)
    Spacer(modifier = Modifier.height(8.dp))
    LazyVerticalGrid(
        columns = GridCells.Adaptive(45.dp)
    ) {
        iconGroups.onEachIndexed { index, entry ->

            val iconGroup = entry.key
            val icons = entry.value
            header {
                Text(
                    stringResource(id = iconGroup.res),
                    modifier = if (index > 0) Modifier.padding(
                        start = 0.dp, end = 0.dp, top = 20.dp, bottom = 4.dp
                    ) else Modifier
                )
            }
            items(icons.toTypedArray()) { icon ->
                Image(
                    painterResource(icon.res),
                    null,
                    modifier = Modifier
                        .padding(2.dp)
                        .clickable {
                            onIconChange(icon)
                        }
                )
            }
        }


    }
}