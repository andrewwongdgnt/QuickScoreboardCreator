package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

sealed class ScoreboardDetailsEvent {

    data object OnConfirm : ScoreboardDetailsEvent()
    data object OnDismiss : ScoreboardDetailsEvent()
    data class OnTitleChange(val title: String) : ScoreboardDetailsEvent()
    data class OnDescriptionChange(val descriptionChange: String) : ScoreboardDetailsEvent()
}