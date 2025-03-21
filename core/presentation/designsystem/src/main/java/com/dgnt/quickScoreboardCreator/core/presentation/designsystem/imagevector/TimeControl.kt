package com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector

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
fun rememberPause(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "pause",
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
                moveTo(24.5f, 31.458f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.791f)
                quadToRelative(-0.771f, -0.792f, -0.771f, -1.875f)
                verticalLineTo(11.208f)
                quadToRelative(0f, -1.083f, 0.771f, -1.875f)
                quadToRelative(0.771f, -0.791f, 1.854f, -0.791f)
                horizontalLineToRelative(4.292f)
                quadToRelative(1.083f, 0f, 1.875f, 0.791f)
                quadToRelative(0.791f, 0.792f, 0.791f, 1.875f)
                verticalLineToRelative(17.584f)
                quadToRelative(0f, 1.083f, -0.791f, 1.875f)
                quadToRelative(-0.792f, 0.791f, -1.875f, 0.791f)
                close()
                moveToRelative(-13.292f, 0f)
                quadToRelative(-1.083f, 0f, -1.875f, -0.791f)
                quadToRelative(-0.791f, -0.792f, -0.791f, -1.875f)
                verticalLineTo(11.208f)
                quadToRelative(0f, -1.083f, 0.791f, -1.875f)
                quadToRelative(0.792f, -0.791f, 1.875f, -0.791f)
                horizontalLineTo(15.5f)
                quadToRelative(1.083f, 0f, 1.854f, 0.791f)
                quadToRelative(0.771f, 0.792f, 0.771f, 1.875f)
                verticalLineToRelative(17.584f)
                quadToRelative(0f, 1.083f, -0.771f, 1.875f)
                quadToRelative(-0.771f, 0.791f, -1.854f, 0.791f)
                close()
                moveTo(24.5f, 28.792f)
                horizontalLineToRelative(4.292f)
                verticalLineTo(11.208f)
                horizontalLineTo(24.5f)
                close()
                moveToRelative(-13.292f, 0f)
                horizontalLineTo(15.5f)
                verticalLineTo(11.208f)
                horizontalLineToRelative(-4.292f)
                close()
                moveToRelative(0f, -17.584f)
                verticalLineToRelative(17.584f)
                close()
                moveToRelative(13.292f, 0f)
                verticalLineToRelative(17.584f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberPauseCircle(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "pause_circle",
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
                moveTo(16.708f, 26.542f)
                quadToRelative(0.584f, 0f, 0.959f, -0.375f)
                reflectiveQuadToRelative(0.375f, -0.917f)
                verticalLineTo(14.708f)
                quadToRelative(0f, -0.5f, -0.375f, -0.875f)
                reflectiveQuadToRelative(-0.959f, -0.375f)
                quadToRelative(-0.541f, 0f, -0.916f, 0.375f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                verticalLineToRelative(10.542f)
                quadToRelative(0f, 0.5f, 0.375f, 0.875f)
                reflectiveQuadToRelative(0.916f, 0.375f)
                close()
                moveToRelative(6.584f, 0f)
                quadToRelative(0.541f, 0f, 0.916f, -0.375f)
                reflectiveQuadToRelative(0.375f, -0.917f)
                verticalLineTo(14.708f)
                quadToRelative(0f, -0.5f, -0.375f, -0.875f)
                reflectiveQuadToRelative(-0.916f, -0.375f)
                quadToRelative(-0.584f, 0f, -0.959f, 0.375f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                verticalLineToRelative(10.542f)
                quadToRelative(0f, 0.5f, 0.375f, 0.875f)
                reflectiveQuadToRelative(0.959f, 0.375f)
                close()
                moveTo(20f, 36.375f)
                quadToRelative(-3.375f, 0f, -6.375f, -1.292f)
                quadToRelative(-3f, -1.291f, -5.208f, -3.521f)
                quadToRelative(-2.209f, -2.229f, -3.5f, -5.208f)
                quadTo(3.625f, 23.375f, 3.625f, 20f)
                reflectiveQuadToRelative(1.292f, -6.375f)
                quadToRelative(1.291f, -3f, 3.521f, -5.208f)
                quadToRelative(2.229f, -2.209f, 5.208f, -3.5f)
                quadTo(16.625f, 3.625f, 20f, 3.625f)
                reflectiveQuadToRelative(6.375f, 1.292f)
                quadToRelative(3f, 1.291f, 5.208f, 3.521f)
                quadToRelative(2.209f, 2.229f, 3.5f, 5.208f)
                quadToRelative(1.292f, 2.979f, 1.292f, 6.354f)
                reflectiveQuadToRelative(-1.292f, 6.375f)
                quadToRelative(-1.291f, 3f, -3.521f, 5.208f)
                quadToRelative(-2.229f, 2.209f, -5.208f, 3.5f)
                quadToRelative(-2.979f, 1.292f, -6.354f, 1.292f)
                close()
                moveTo(20f, 20f)
                close()
                moveToRelative(0f, 13.75f)
                quadToRelative(5.667f, 0f, 9.708f, -4.042f)
                quadTo(33.75f, 25.667f, 33.75f, 20f)
                reflectiveQuadToRelative(-4.042f, -9.708f)
                quadTo(25.667f, 6.25f, 20f, 6.25f)
                reflectiveQuadToRelative(-9.708f, 4.042f)
                quadTo(6.25f, 14.333f, 6.25f, 20f)
                reflectiveQuadToRelative(4.042f, 9.708f)
                quadTo(14.333f, 33.75f, 20f, 33.75f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberPlay(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "play_arrow",
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
                moveTo(15.542f, 30f)
                quadToRelative(-0.667f, 0.458f, -1.334f, 0.062f)
                quadToRelative(-0.666f, -0.395f, -0.666f, -1.187f)
                verticalLineTo(10.917f)
                quadToRelative(0f, -0.75f, 0.666f, -1.146f)
                quadToRelative(0.667f, -0.396f, 1.334f, 0.062f)
                lineToRelative(14.083f, 9f)
                quadToRelative(0.583f, 0.375f, 0.583f, 1.084f)
                quadToRelative(0f, 0.708f, -0.583f, 1.083f)
                close()
                moveToRelative(0.625f, -10.083f)
                close()
                moveToRelative(0f, 6.541f)
                lineToRelative(10.291f, -6.541f)
                lineToRelative(-10.291f, -6.542f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberPlayCircle(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "play_circle",
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
                moveTo(16f, 15.333f)
                verticalLineToRelative(9.334f)
                quadToRelative(0f, 0.791f, 0.667f, 1.166f)
                quadToRelative(0.666f, 0.375f, 1.333f, -0.041f)
                lineToRelative(7.333f, -4.709f)
                quadToRelative(0.584f, -0.375f, 0.584f, -1.083f)
                reflectiveQuadToRelative(-0.584f, -1.083f)
                lineTo(18f, 14.208f)
                quadToRelative(-0.667f, -0.416f, -1.333f, -0.041f)
                quadToRelative(-0.667f, 0.375f, -0.667f, 1.166f)
                close()
                moveToRelative(4f, 21.042f)
                quadToRelative(-3.375f, 0f, -6.375f, -1.292f)
                quadToRelative(-3f, -1.291f, -5.208f, -3.521f)
                quadToRelative(-2.209f, -2.229f, -3.5f, -5.208f)
                quadTo(3.625f, 23.375f, 3.625f, 20f)
                reflectiveQuadToRelative(1.292f, -6.375f)
                quadToRelative(1.291f, -3f, 3.521f, -5.208f)
                quadToRelative(2.229f, -2.209f, 5.208f, -3.5f)
                quadTo(16.625f, 3.625f, 20f, 3.625f)
                reflectiveQuadToRelative(6.375f, 1.292f)
                quadToRelative(3f, 1.291f, 5.208f, 3.521f)
                quadToRelative(2.209f, 2.229f, 3.5f, 5.208f)
                quadToRelative(1.292f, 2.979f, 1.292f, 6.354f)
                reflectiveQuadToRelative(-1.292f, 6.375f)
                quadToRelative(-1.291f, 3f, -3.521f, 5.208f)
                quadToRelative(-2.229f, 2.209f, -5.208f, 3.5f)
                quadToRelative(-2.979f, 1.292f, -6.354f, 1.292f)
                close()
                moveTo(20f, 20f)
                close()
                moveToRelative(0f, 13.75f)
                quadToRelative(5.667f, 0f, 9.708f, -4.042f)
                quadTo(33.75f, 25.667f, 33.75f, 20f)
                reflectiveQuadToRelative(-4.042f, -9.708f)
                quadTo(25.667f, 6.25f, 20f, 6.25f)
                reflectiveQuadToRelative(-9.708f, 4.042f)
                quadTo(6.25f, 14.333f, 6.25f, 20f)
                reflectiveQuadToRelative(4.042f, 9.708f)
                quadTo(14.333f, 33.75f, 20f, 33.75f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberStop(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "stop",
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
                moveTo(12.833f, 12.833f)
                verticalLineToRelative(14.334f)
                close()
                moveToRelative(0f, 16.959f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.771f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                verticalLineTo(12.833f)
                quadToRelative(0f, -1.083f, 0.771f, -1.854f)
                quadToRelative(0.771f, -0.771f, 1.854f, -0.771f)
                horizontalLineToRelative(14.334f)
                quadToRelative(1.083f, 0f, 1.854f, 0.771f)
                quadToRelative(0.771f, 0.771f, 0.771f, 1.854f)
                verticalLineToRelative(14.334f)
                quadToRelative(0f, 1.083f, -0.771f, 1.854f)
                quadToRelative(-0.771f, 0.771f, -1.854f, 0.771f)
                close()
                moveToRelative(0f, -2.625f)
                horizontalLineToRelative(14.334f)
                verticalLineTo(12.833f)
                horizontalLineTo(12.833f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberStopCircle(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "stop_circle",
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
                moveTo(20f, 36.375f)
                quadToRelative(-3.458f, 0f, -6.458f, -1.25f)
                reflectiveQuadToRelative(-5.209f, -3.458f)
                quadToRelative(-2.208f, -2.209f, -3.458f, -5.209f)
                quadToRelative(-1.25f, -3f, -1.25f, -6.458f)
                reflectiveQuadToRelative(1.25f, -6.437f)
                quadToRelative(1.25f, -2.98f, 3.458f, -5.188f)
                quadToRelative(2.209f, -2.208f, 5.209f, -3.479f)
                quadToRelative(3f, -1.271f, 6.458f, -1.271f)
                reflectiveQuadToRelative(6.438f, 1.271f)
                quadToRelative(2.979f, 1.271f, 5.187f, 3.479f)
                reflectiveQuadToRelative(3.479f, 5.188f)
                quadToRelative(1.271f, 2.979f, 1.271f, 6.437f)
                reflectiveQuadToRelative(-1.271f, 6.458f)
                quadToRelative(-1.271f, 3f, -3.479f, 5.209f)
                quadToRelative(-2.208f, 2.208f, -5.187f, 3.458f)
                quadToRelative(-2.98f, 1.25f, -6.438f, 1.25f)
                close()
                moveTo(20f, 20f)
                close()
                moveToRelative(0f, 13.75f)
                quadToRelative(5.833f, 0f, 9.792f, -3.958f)
                quadTo(33.75f, 25.833f, 33.75f, 20f)
                reflectiveQuadToRelative(-3.958f, -9.792f)
                quadTo(25.833f, 6.25f, 20f, 6.25f)
                reflectiveQuadToRelative(-9.792f, 3.958f)
                quadTo(6.25f, 14.167f, 6.25f, 20f)
                reflectiveQuadToRelative(3.958f, 9.792f)
                quadTo(14.167f, 33.75f, 20f, 33.75f)
                close()
                moveToRelative(-5f, -7.458f)
                horizontalLineToRelative(10.042f)
                quadToRelative(0.541f, 0f, 0.916f, -0.354f)
                quadToRelative(0.375f, -0.355f, 0.375f, -0.938f)
                verticalLineTo(14.958f)
                quadToRelative(0f, -0.541f, -0.375f, -0.916f)
                reflectiveQuadToRelative(-0.916f, -0.375f)
                horizontalLineTo(15f)
                quadToRelative(-0.583f, 0f, -0.938f, 0.375f)
                quadToRelative(-0.354f, 0.375f, -0.354f, 0.916f)
                verticalLineTo(25f)
                quadToRelative(0f, 0.583f, 0.354f, 0.938f)
                quadToRelative(0.355f, 0.354f, 0.938f, 0.354f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberSkipNext(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "skip_next",
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
                moveTo(29.25f, 29.75f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.958f)
                verticalLineTo(11.542f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.959f, -0.375f)
                quadToRelative(0.541f, 0f, 0.916f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                verticalLineToRelative(16.875f)
                quadToRelative(0f, 0.583f, -0.395f, 0.958f)
                quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                close()
                moveToRelative(-17.792f, -1.417f)
                quadToRelative(-0.666f, 0.459f, -1.354f, 0.084f)
                quadToRelative(-0.687f, -0.375f, -0.687f, -1.125f)
                verticalLineTo(12.708f)
                quadToRelative(0f, -0.75f, 0.687f, -1.125f)
                quadToRelative(0.688f, -0.375f, 1.354f, 0.084f)
                lineToRelative(10.584f, 7.25f)
                quadToRelative(0.583f, 0.416f, 0.583f, 1.083f)
                reflectiveQuadToRelative(-0.583f, 1.083f)
                close()
                moveTo(12.042f, 20f)
                close()
                moveToRelative(0f, 4.75f)
                lineTo(18.958f, 20f)
                lineToRelative(-6.916f, -4.75f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberSkipPrevious(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "skip_previous",
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
                moveTo(10.75f, 29.75f)
                quadToRelative(-0.583f, 0f, -0.958f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.958f)
                verticalLineTo(11.542f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.958f, -0.375f)
                quadToRelative(0.542f, 0f, 0.917f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                verticalLineToRelative(16.875f)
                quadToRelative(0f, 0.583f, -0.375f, 0.958f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                close()
                moveToRelative(17.792f, -1.417f)
                lineToRelative(-10.584f, -7.25f)
                quadToRelative(-0.583f, -0.416f, -0.583f, -1.083f)
                reflectiveQuadToRelative(0.583f, -1.083f)
                lineToRelative(10.584f, -7.25f)
                quadToRelative(0.666f, -0.459f, 1.354f, -0.104f)
                quadToRelative(0.687f, 0.354f, 0.687f, 1.145f)
                verticalLineToRelative(14.584f)
                quadToRelative(0f, 0.791f, -0.687f, 1.146f)
                quadToRelative(-0.688f, 0.354f, -1.354f, -0.105f)
                close()
                moveTo(27.958f, 20f)
                close()
                moveToRelative(0f, 4.75f)
                verticalLineToRelative(-9.5f)
                lineTo(21.042f, 20f)
                close()
            }
        }.build()
    }
}