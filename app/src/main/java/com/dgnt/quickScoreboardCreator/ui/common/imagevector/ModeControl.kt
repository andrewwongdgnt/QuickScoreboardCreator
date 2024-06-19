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
fun rememberToggleOff(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "toggle_off",
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
                moveTo(11.75f, 29.792f)
                quadToRelative(-4.083f, 0f, -6.938f, -2.854f)
                quadTo(1.958f, 24.083f, 1.958f, 20f)
                reflectiveQuadToRelative(2.854f, -6.937f)
                quadToRelative(2.855f, -2.855f, 6.938f, -2.855f)
                horizontalLineToRelative(16.5f)
                quadToRelative(4.083f, 0f, 6.938f, 2.855f)
                quadToRelative(2.854f, 2.854f, 2.854f, 6.937f)
                reflectiveQuadToRelative(-2.854f, 6.938f)
                quadToRelative(-2.855f, 2.854f, -6.938f, 2.854f)
                close()
                moveToRelative(0f, -2.625f)
                horizontalLineToRelative(16.5f)
                quadToRelative(3f, 0f, 5.083f, -2.084f)
                quadTo(35.417f, 23f, 35.417f, 20f)
                reflectiveQuadToRelative(-2.084f, -5.083f)
                quadToRelative(-2.083f, -2.084f, -5.083f, -2.084f)
                horizontalLineToRelative(-16.5f)
                quadToRelative(-3f, 0f, -5.083f, 2.084f)
                quadTo(4.583f, 17f, 4.583f, 20f)
                reflectiveQuadToRelative(2.084f, 5.083f)
                quadToRelative(2.083f, 2.084f, 5.083f, 2.084f)
                close()
                moveToRelative(-0.042f, -2.875f)
                quadToRelative(1.792f, 0f, 3.063f, -1.25f)
                quadToRelative(1.271f, -1.25f, 1.271f, -3.042f)
                quadToRelative(0f, -1.792f, -1.271f, -3.042f)
                quadToRelative(-1.271f, -1.25f, -3.063f, -1.25f)
                quadToRelative(-1.791f, 0f, -3.041f, 1.25f)
                reflectiveQuadTo(7.417f, 20f)
                quadToRelative(0f, 1.792f, 1.25f, 3.042f)
                quadToRelative(1.25f, 1.25f, 3.041f, 1.25f)
                close()
                moveTo(20f, 20f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberToggleOn(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "toggle_on",
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
                moveTo(11.75f, 29.792f)
                quadToRelative(-4.083f, 0f, -6.938f, -2.854f)
                quadTo(1.958f, 24.083f, 1.958f, 20f)
                reflectiveQuadToRelative(2.854f, -6.937f)
                quadToRelative(2.855f, -2.855f, 6.938f, -2.855f)
                horizontalLineToRelative(16.5f)
                quadToRelative(4.083f, 0f, 6.938f, 2.855f)
                quadToRelative(2.854f, 2.854f, 2.854f, 6.937f)
                reflectiveQuadToRelative(-2.854f, 6.938f)
                quadToRelative(-2.855f, 2.854f, -6.938f, 2.854f)
                close()
                moveToRelative(16.5f, -2.625f)
                quadToRelative(3f, 0f, 5.083f, -2.084f)
                quadTo(35.417f, 23f, 35.417f, 20f)
                reflectiveQuadToRelative(-2.084f, -5.083f)
                quadToRelative(-2.083f, -2.084f, -5.083f, -2.084f)
                horizontalLineToRelative(-16.5f)
                quadToRelative(-3f, 0f, -5.083f, 2.084f)
                quadTo(4.583f, 17f, 4.583f, 20f)
                reflectiveQuadToRelative(2.084f, 5.083f)
                quadToRelative(2.083f, 2.084f, 5.083f, 2.084f)
                close()
                moveToRelative(0.042f, -2.875f)
                quadToRelative(1.791f, 0f, 3.041f, -1.25f)
                reflectiveQuadTo(32.583f, 20f)
                quadToRelative(0f, -1.792f, -1.25f, -3.042f)
                quadToRelative(-1.25f, -1.25f, -3.041f, -1.25f)
                quadToRelative(-1.792f, 0f, -3.063f, 1.25f)
                quadToRelative(-1.271f, 1.25f, -1.271f, 3.042f)
                quadToRelative(0f, 1.792f, 1.271f, 3.042f)
                quadToRelative(1.271f, 1.25f, 3.063f, 1.25f)
                close()
                moveTo(20f, 20f)
                close()
            }
        }.build()
    }
}