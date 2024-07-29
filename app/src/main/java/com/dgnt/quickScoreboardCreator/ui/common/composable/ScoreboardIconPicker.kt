package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.header

@Composable
fun ScoreboardIconPicker(
    onIconChange: (ScoreboardIcon) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(45.dp)
    ) {
        header {
            Text(
                stringResource(id = R.string.pickIconMsg),
            )
        }
        items(ScoreboardIcon.entries.toTypedArray()) { icon ->
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