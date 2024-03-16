package com.dgnt.quickScoreboardCreator.manager

import com.dgnt.quickScoreboardCreator.data.model.BaseData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.data.model.scoreBoard.IntervalInfo
import com.dgnt.quickScoreboardCreator.data.model.scoreBoard.ScoreInfo
import com.dgnt.quickScoreboardCreator.data.model.scoreBoard.Scoreboard
import org.joda.time.DateTime


class QSBScoreboardManager<SDT, S : ScoreData<SDT>, IDT, I: BaseData<IDT>>(
    name: String,
    description: String?,
    scoreInfo: ScoreInfo<SDT, S>,
    intervalInfo: IntervalInfo<IDT, I>,
    val createdDate: DateTime = DateTime.now(),
    var lastModifiedDate: DateTime? = null
) {
    private val scoreboard: Scoreboard<SDT, S, IDT, I> =
        Scoreboard(
            name,
            description,
            scoreInfo,
            intervalInfo
        )

    var name: String
        get() = scoreboard.name
        set(value) {
            scoreboard.name = value
            lastModifiedDate = DateTime.now()
        }

    var description: String?
        get() = scoreboard.description
        set(value) {
            scoreboard.description = value
            lastModifiedDate = DateTime.now()
        }


    fun updateScore(scoreIndex: Int, incrementIndex: Int) {
        scoreboard.scoreInfo.dataList[scoreIndex].data.update(incrementIndex)
    }

    fun getScore(scoreIndex: Int) =
        scoreboard.scoreInfo.dataList[scoreIndex].data

    fun reset(scoreIndex: Int? = null, intervalIndex: Int? = null) {
        scoreIndex?.let {
            scoreboard.scoreInfo.dataList[it].data.reset()
        }
        intervalIndex?.let {
            scoreboard.intervalInfo.dataList[it].data.reset()
        }
    }

    fun resetIntervalAndAllScore(intervalIndex: Int) {
        reset(intervalIndex = intervalIndex)
        (0 until scoreboard.teamSize).forEach {
            reset(scoreIndex = it)
        }
    }

}