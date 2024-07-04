package com.dgnt.quickScoreboardCreator.ui.common.imagevector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberTimeline(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "timeline",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4.875f, 29.833f)
                quadToRelative(-1.208f, 0f, -2.083f, -0.875f)
                quadToRelative(-0.875f, -0.875f, -0.875f, -2.083f)
                quadToRelative(0f, -1.25f, 0.875f, -2.125f)
                reflectiveQuadToRelative(2.083f, -0.875f)
                quadToRelative(0.208f, 0f, 0.417f, 0.042f)
                quadToRelative(0.208f, 0.041f, 0.5f, 0.125f)
                lineToRelative(8.083f, -8.084f)
                quadToRelative(-0.125f, -0.291f, -0.125f, -0.5f)
                verticalLineToRelative(-0.416f)
                quadToRelative(0f, -1.25f, 0.875f, -2.125f)
                reflectiveQuadToRelative(2.083f, -0.875f)
                quadToRelative(1.209f, 0f, 2.084f, 0.875f)
                reflectiveQuadToRelative(0.875f, 2.125f)
                quadToRelative(0f, 0.166f, -0.125f, 0.916f)
                lineToRelative(4.5f, 4.5f)
                quadToRelative(0.291f, -0.083f, 0.5f, -0.125f)
                quadToRelative(0.208f, -0.041f, 0.416f, -0.041f)
                quadToRelative(0.25f, 0f, 0.438f, 0.041f)
                quadToRelative(0.187f, 0.042f, 0.479f, 0.125f)
                lineToRelative(6.417f, -6.416f)
                quadToRelative(-0.084f, -0.292f, -0.104f, -0.5f)
                quadToRelative(-0.021f, -0.209f, -0.021f, -0.417f)
                quadToRelative(0f, -1.25f, 0.875f, -2.125f)
                reflectiveQuadToRelative(2.083f, -0.875f)
                quadToRelative(1.208f, 0f, 2.104f, 0.875f)
                quadToRelative(0.896f, 0.875f, 0.896f, 2.125f)
                quadToRelative(0f, 1.208f, -0.875f, 2.083f)
                quadToRelative(-0.875f, 0.875f, -2.125f, 0.875f)
                quadToRelative(-0.208f, 0f, -0.417f, -0.021f)
                quadToRelative(-0.208f, -0.02f, -0.5f, -0.104f)
                lineToRelative(-6.416f, 6.417f)
                quadToRelative(0.083f, 0.292f, 0.104f, 0.479f)
                quadToRelative(0.021f, 0.188f, 0.021f, 0.438f)
                quadToRelative(0f, 1.208f, -0.875f, 2.083f)
                quadToRelative(-0.875f, 0.875f, -2.084f, 0.875f)
                quadToRelative(-1.208f, 0f, -2.083f, -0.875f)
                quadTo(22f, 24.5f, 22f, 23.292f)
                verticalLineToRelative(-0.438f)
                quadToRelative(0f, -0.187f, 0.125f, -0.479f)
                lineToRelative(-4.5f, -4.5f)
                quadToRelative(-0.292f, 0.083f, -0.5f, 0.104f)
                quadToRelative(-0.208f, 0.021f, -0.417f, 0.021f)
                quadToRelative(-0.083f, 0f, -0.916f, -0.125f)
                lineTo(7.75f, 25.958f)
                quadToRelative(0.083f, 0.292f, 0.083f, 0.48f)
                verticalLineToRelative(0.437f)
                quadToRelative(0f, 1.208f, -0.875f, 2.083f)
                quadToRelative(-0.875f, 0.875f, -2.083f, 0.875f)
                close()
            }
        }.build()
    }
}