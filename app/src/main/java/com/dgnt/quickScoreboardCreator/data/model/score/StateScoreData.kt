package com.dgnt.quickScoreboardCreator.data.model.score

import com.dgnt.quickScoreboardCreator.data.model.BaseData
import com.dgnt.quickScoreboardCreator.data.state.State

data class StateScoreData(
    override var current: State,
    override val initial: State
) : ScoreData<State> {
    override val increments = listOf<State>()

    override fun update(incrementIndex: Int) {
        current.next?.let {
            current = it
        }
    }
}
