package com.dgnt.quickScoreboardCreator.data.model.scoreBoard

import com.dgnt.quickScoreboardCreator.data.model.BaseData
import com.dgnt.quickScoreboardCreator.data.model.score.Score
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData

data class ScoreInfo<DT, T: ScoreData<DT>>(
    val carryOver: Boolean,
    val dataList: List<Score<DT, T>>
)