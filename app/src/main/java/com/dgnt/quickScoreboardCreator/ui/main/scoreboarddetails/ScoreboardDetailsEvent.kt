package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon

sealed class ScoreboardDetailsEvent {

    data object OnConfirm : ScoreboardDetailsEvent()
    data object OnDismiss : ScoreboardDetailsEvent()
    data object OnIconEdit : ScoreboardDetailsEvent()
    data class OnNewIcon(val icon: ScoreboardIcon) : ScoreboardDetailsEvent()
    data class OnTitleChange(val title: String) : ScoreboardDetailsEvent()
    data class OnDescriptionChange(val descriptionChange: String) : ScoreboardDetailsEvent()
}