package com.dgnt.quickScoreboardCreator.data.model.score

import com.dgnt.quickScoreboardCreator.data.model.BaseData
import com.dgnt.quickScoreboardCreator.data.state.State

sealed class Score<DT, T: ScoreData<DT>>(val data: T)   {
    data class NumberedScore(
        val numberedScoreData: NumberedScoreData
    ) : Score<Int, NumberedScoreData>(numberedScoreData)

    data class StateScore(
        val stateScoreData: StateScoreData
    ) : Score<State, StateScoreData>(stateScoreData)
}

