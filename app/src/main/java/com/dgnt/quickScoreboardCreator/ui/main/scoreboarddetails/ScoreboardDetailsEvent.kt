package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

sealed class ScoreboardDetailsEvent {

    data object OnDone : ScoreboardDetailsEvent()
}