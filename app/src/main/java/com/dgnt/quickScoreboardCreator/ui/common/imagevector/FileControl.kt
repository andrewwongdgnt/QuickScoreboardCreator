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
fun rememberExportNotes(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "export_notes",
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
                moveTo(32.167f, 29.083f)
                verticalLineToRelative(3.5f)
                quadToRelative(0f, 0.25f, 0.187f, 0.438f)
                quadToRelative(0.188f, 0.187f, 0.438f, 0.187f)
                reflectiveQuadToRelative(0.437f, -0.187f)
                quadToRelative(0.188f, -0.188f, 0.188f, -0.438f)
                verticalLineToRelative(-4.958f)
                quadToRelative(0f, -0.292f, -0.188f, -0.479f)
                quadToRelative(-0.187f, -0.188f, -0.479f, -0.188f)
                horizontalLineToRelative(-4.958f)
                quadToRelative(-0.25f, 0f, -0.438f, 0.188f)
                quadToRelative(-0.187f, 0.187f, -0.187f, 0.437f)
                reflectiveQuadToRelative(0.187f, 0.438f)
                quadToRelative(0.188f, 0.187f, 0.438f, 0.187f)
                horizontalLineToRelative(3.5f)
                lineToRelative(-4.417f, 4.417f)
                quadToRelative(-0.208f, 0.208f, -0.208f, 0.437f)
                quadToRelative(0f, 0.23f, 0.208f, 0.438f)
                reflectiveQuadToRelative(0.437f, 0.208f)
                quadToRelative(0.23f, 0f, 0.438f, -0.208f)
                close()
                moveTo(7.875f, 34.708f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.77f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.855f)
                verticalLineTo(7.875f)
                quadToRelative(0f, -1.083f, 0.771f, -1.854f)
                quadToRelative(0.771f, -0.771f, 1.854f, -0.771f)
                horizontalLineToRelative(24.25f)
                quadToRelative(1.083f, 0f, 1.854f, 0.771f)
                quadToRelative(0.771f, 0.771f, 0.771f, 1.854f)
                verticalLineToRelative(12.833f)
                quadToRelative(-0.625f, -0.333f, -1.292f, -0.541f)
                quadToRelative(-0.666f, -0.209f, -1.333f, -0.334f)
                verticalLineTo(7.875f)
                horizontalLineTo(7.875f)
                verticalLineToRelative(24.208f)
                horizontalLineTo(20f)
                quadToRelative(0.167f, 0.709f, 0.396f, 1.375f)
                quadToRelative(0.229f, 0.667f, 0.521f, 1.25f)
                close()
                moveToRelative(0f, -4.583f)
                verticalLineToRelative(1.958f)
                verticalLineTo(7.875f)
                verticalLineToRelative(11.958f)
                verticalLineToRelative(-0.166f)
                verticalLineToRelative(10.458f)
                close()
                moveToRelative(3.917f, -3.292f)
                quadToRelative(0f, 0.584f, 0.396f, 0.959f)
                quadToRelative(0.395f, 0.375f, 0.937f, 0.375f)
                horizontalLineTo(20f)
                quadToRelative(0.167f, -0.667f, 0.396f, -1.334f)
                quadToRelative(0.229f, -0.666f, 0.521f, -1.291f)
                horizontalLineToRelative(-7.792f)
                quadToRelative(-0.542f, 0f, -0.937f, 0.375f)
                quadToRelative(-0.396f, 0.375f, -0.396f, 0.916f)
                close()
                moveToRelative(0f, -6.833f)
                quadToRelative(0f, 0.542f, 0.396f, 0.917f)
                quadToRelative(0.395f, 0.375f, 0.937f, 0.375f)
                horizontalLineToRelative(11.75f)
                quadToRelative(0.75f, -0.5f, 1.563f, -0.854f)
                quadToRelative(0.812f, -0.355f, 1.77f, -0.563f)
                verticalLineToRelative(-0.25f)
                quadToRelative(-0.125f, -0.417f, -0.5f, -0.687f)
                quadToRelative(-0.375f, -0.271f, -0.833f, -0.271f)
                horizontalLineToRelative(-13.75f)
                quadToRelative(-0.542f, 0f, -0.937f, 0.395f)
                quadToRelative(-0.396f, 0.396f, -0.396f, 0.938f)
                close()
                moveToRelative(0f, -6.875f)
                quadToRelative(0f, 0.542f, 0.396f, 0.938f)
                quadToRelative(0.395f, 0.395f, 0.937f, 0.395f)
                horizontalLineToRelative(13.75f)
                quadToRelative(0.542f, 0f, 0.937f, -0.395f)
                quadToRelative(0.396f, -0.396f, 0.396f, -0.938f)
                quadToRelative(0f, -0.542f, -0.396f, -0.937f)
                quadToRelative(-0.395f, -0.396f, -0.937f, -0.396f)
                horizontalLineToRelative(-13.75f)
                quadToRelative(-0.542f, 0f, -0.937f, 0.396f)
                quadToRelative(-0.396f, 0.395f, -0.396f, 0.937f)
                close()
                moveToRelative(18.458f, 24.75f)
                quadToRelative(-3.208f, 0f, -5.5f, -2.292f)
                quadToRelative(-2.292f, -2.291f, -2.292f, -5.458f)
                quadToRelative(0f, -3.25f, 2.292f, -5.542f)
                quadToRelative(2.292f, -2.291f, 5.542f, -2.291f)
                quadToRelative(3.208f, 0f, 5.5f, 2.291f)
                quadToRelative(2.291f, 2.292f, 2.291f, 5.542f)
                quadToRelative(0f, 3.167f, -2.291f, 5.458f)
                quadToRelative(-2.292f, 2.292f, -5.542f, 2.292f)
                close()
            }
        }.build()
    }
}