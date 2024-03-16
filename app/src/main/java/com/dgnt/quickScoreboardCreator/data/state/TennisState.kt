package com.dgnt.quickScoreboardCreator.data.state

enum class TennisState(
    override val next: State? = null
) : State {
    S_40,
    S_30(S_40),
    S_15(S_30),
    LOVE(S_15),


}