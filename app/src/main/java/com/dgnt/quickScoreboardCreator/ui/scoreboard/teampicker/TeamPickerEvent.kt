package com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker

sealed class TeamPickerEvent {
    data object OnDone : TeamPickerEvent()
    data class OnTeamPicked(val teamId: Int) : TeamPickerEvent()
}