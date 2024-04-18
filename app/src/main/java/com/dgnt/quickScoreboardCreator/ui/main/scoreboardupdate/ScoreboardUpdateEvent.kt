package com.dgnt.quickScoreboardCreator.ui.main.scoreboardupdate

sealed class ScoreboardUpdateEvent {

    data object OnUpdate : ScoreboardUpdateEvent()
}