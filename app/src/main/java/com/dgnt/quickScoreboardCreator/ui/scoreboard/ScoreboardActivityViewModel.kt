package com.dgnt.quickScoreboardCreator.ui.scoreboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ScoreboardActivityViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var teamSelectedData by mutableStateOf(TeamSelectedData(-1, -1))

}