package com.dgnt.quickScoreboardCreator.ui.scoreboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreboardActivityViewModel : ViewModel() {
    private val _updatedTeamData = MutableStateFlow<UpdatedTeamData?>(null)
    val updatedTeamData: StateFlow<UpdatedTeamData?> = _updatedTeamData.asStateFlow()

    private val _updatedIntervalData = MutableStateFlow<UpdatedIntervalData?>(null)
    val updatedIntervalData: StateFlow<UpdatedIntervalData?> = _updatedIntervalData.asStateFlow()

    fun onEvent(event: ScoreboardActivityEvent) {
        when (event) {
            is ScoreboardActivityEvent.OnUpdatedIntervalData -> _updatedIntervalData.value = event.updatedIntervalData
            is ScoreboardActivityEvent.OnUpdatedTeamData -> _updatedTeamData.value = event.updatedTeamData
        }
    }

}