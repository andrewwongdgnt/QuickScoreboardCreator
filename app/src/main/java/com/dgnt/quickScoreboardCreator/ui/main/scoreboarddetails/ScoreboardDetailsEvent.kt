package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

sealed class ScoreboardDetailsEvent {

    data object OnConfirm : ScoreboardDetailsEvent()
    data object OnDismiss : ScoreboardDetailsEvent()
}