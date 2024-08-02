package com.dgnt.quickScoreboardCreator.ui.theme

import androidx.compose.ui.graphics.Color


private val lightTimelineViewerTeamColors = listOf(
    Color(173, 35, 35),
    Color(42, 75, 215),
    Color(29, 105, 20),
    Color(129, 74, 25),
    Color(129, 38, 192),
    Color(80, 80, 80),
    Color(18, 99, 108),
    Color(54, 54, 54),
    Color(128, 118, 26),
    Color(122, 30, 99)
)

private val darkTimelineViewerTeamColors = listOf(
    Color(255, 89, 89),
    Color(97, 127, 255),
    Color(99, 245, 83),
    Color(238, 154, 79),
    Color(187, 106, 243),
    Color(160, 160, 160),
    Color(129, 197, 122),
    Color(86, 236, 255),
    Color(210, 210, 210),
    Color(255, 233, 39),
    Color(255, 56, 208)
)



fun getTimelineViewerTeamColor(index: Int, isDarkTheme: Boolean): Color {
    return if (isDarkTheme)
        darkTimelineViewerTeamColors[index % darkTimelineViewerTeamColors.size]
    else
        lightTimelineViewerTeamColors[index % lightTimelineViewerTeamColors.size]
}