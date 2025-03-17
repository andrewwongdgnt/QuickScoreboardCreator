package com.dgnt.quickScoreboardCreator.feature.team.presentation.teampicker

sealed interface TeamPickerAction {
    data object Dismiss : TeamPickerAction
    data class TeamPicked(val id: Int) : TeamPickerAction

}