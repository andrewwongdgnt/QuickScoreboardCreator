package com.dgnt.quickScoreboardCreator.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.R

@Composable
fun IconDisplay(
    @DrawableRes iconRes: Int? = null,
    onClick: () -> Unit = {}
) {
    if (iconRes != null) {

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier.clickable(onClick = onClick)
        ) {
            val primaryColor = MaterialTheme.colorScheme.primary
            val radiusModifier = 0.65f
            Image(
                painterResource(iconRes),
                null,
                modifier = Modifier
                    .drawBehind {
                        drawCircle(
                            style = Stroke(
                                width = 20f
                            ),
                            color = primaryColor,
                            radius = size.minDimension * radiusModifier
                        )
                    }
                    .drawBehind {
                        drawCircle(
                            color = Color.White,
                            radius = size.minDimension * radiusModifier
                        )
                    }
            )
            val tint = MaterialTheme.colorScheme.background
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit),
                tint = tint,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp)
                    .align(Alignment.BottomEnd)
                    .drawBehind {
                        drawCircle(
                            color = primaryColor.copy(alpha = 0.7f),
                            radius = size.minDimension * 0.7f
                        )
                    }
            )
        }
    } else
        CircularProgressIndicator(
            modifier = Modifier
                .size(72.dp)
                .padding(5.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
}