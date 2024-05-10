package com.dgnt.quickScoreboardCreator.ui.scoreboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ScoreboardActivityViewModel : ViewModel() {
    var teamSelectedData by mutableStateOf(TeamSelectedData(-1, -1))

}