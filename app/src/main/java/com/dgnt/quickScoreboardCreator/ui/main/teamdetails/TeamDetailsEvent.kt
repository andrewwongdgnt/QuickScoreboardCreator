package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

sealed class TeamDetailsEvent {

    data object OnDone : TeamDetailsEvent()
}