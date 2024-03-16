package com.dgnt.quickScoreboardCreator.data.model.score

data class NumberedScoreData(
    override var current: Int,
    override val initial: Int,
    override val increments: List<Int>
) : ScoreData<Int> {
    override fun update(incrementIndex: Int) {
        current += increments[incrementIndex]
    }
}
