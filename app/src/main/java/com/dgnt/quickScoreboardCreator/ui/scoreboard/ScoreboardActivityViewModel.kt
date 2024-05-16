package com.dgnt.quickScoreboardCreator.ui.scoreboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ScoreboardActivityViewModel : ViewModel() {
    var updatedTeamData by mutableStateOf<UpdatedTeamData?>(null)
    var updatedIntervalData by mutableStateOf<UpdatedIntervalData?>(null)

}