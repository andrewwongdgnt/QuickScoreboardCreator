package com.dgnt.quickScoreboardCreator.manager.scoreBoard

import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalInfo
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.data.model.scoreBoard.Scoreboard
import com.dgnt.quickScoreboardCreator.data.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.data.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.manager.scoreTransformer.ScoreTransformer
import org.joda.time.DateTime


class QSBScoreboardManager(
    private val scoreTransformer: ScoreTransformer
) : ScoreboardManager {
    private val scoreboard: Scoreboard =
        Scoreboard(
            "",
            null,
            DateTime.now(),
            null,
            ScoreInfo(false, ScoreRule.NoRule, listOf()),
            IntervalInfo(false, listOf()),
        )


    override var name: String
        get() = scoreboard.name
        set(value) {
            scoreboard.name = value
            updateLastModifiedDate()
        }

    override var description: String?
        get() = scoreboard.description
        set(value) {
            scoreboard.description = value
            updateLastModifiedDate()
        }

    override var createdDate: DateTime
        get() = scoreboard.createdDate
        set(value) {
            scoreboard.createdDate = value
        }

    override var lastModifiedDate: DateTime?
        get() = scoreboard.lastModifiedDate
        set(value) {
            scoreboard.lastModifiedDate = value
        }

    override var scoreInfo: ScoreInfo<ScoreData>
        get() = scoreboard.scoreInfo
        set(value) {
            scoreboard.scoreInfo = value
        }

    override var intervalInfo: IntervalInfo<IntervalData>
        get() = scoreboard.intervalInfo
        set(value) {
            scoreboard.intervalInfo = value
        }


    override fun updateScore(scoreIndex: Int, incrementIndex: Int) {
        val scoreInfo = scoreboard.scoreInfo

        scoreInfo.dataList[scoreIndex].apply {
            val newScore = current + increments[incrementIndex]
            if ((scoreInfo.scoreRule is ScoreRule.ScoreRuleTrigger.MaxScoreRule && newScore <= scoreInfo.scoreRule.trigger) || scoreInfo.scoreRule !is ScoreRule.ScoreRuleTrigger.MaxScoreRule)
                current = newScore
        }
        updateLastModifiedDate()
    }

    override fun getScores(): DisplayedScoreInfo {
        val scoreInfo = scoreboard.scoreInfo

        if (scoreInfo.scoreRule is ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule && scoreboard.teamSize == 2 && scoreInfo.dataList.all { it.current >= scoreInfo.scoreRule.trigger }) {

            val firstScore = scoreInfo.dataList[0].current
            val secondScore = scoreInfo.dataList[1].current
            return if (firstScore == secondScore)
                DisplayedScoreInfo(listOf(DisplayedScore.Blank, DisplayedScore.Blank), DisplayedScore.Deuce)
            else if (firstScore > secondScore)
                DisplayedScoreInfo(listOf(DisplayedScore.Advantage, DisplayedScore.Blank), DisplayedScore.Blank)
            else
                DisplayedScoreInfo(listOf(DisplayedScore.Blank, DisplayedScore.Advantage), DisplayedScore.Blank)
        }
        val mappedScores = scoreTransformer.transform(scoreInfo.dataList.map { it.current }).map { DisplayedScore.CustomDisplayedScore(it) }
        return DisplayedScoreInfo(mappedScores, DisplayedScore.Blank)
    }

    override fun reset(scoreIndex: Int?, intervalIndex: Int?) {
        scoreIndex?.let {
            scoreboard.scoreInfo.dataList[it].reset()
        }
        intervalIndex?.let {
            scoreboard.intervalInfo.dataList[it].reset()
        }
        updateLastModifiedDate()
    }

    override fun resetIntervalAndAllScore(intervalIndex: Int) {
        reset(intervalIndex = intervalIndex)
        (0 until scoreboard.teamSize).forEach {
            reset(scoreIndex = it)
        }
        updateLastModifiedDate()
    }

    override fun provideTransformMap(map: Map<Int, String>) = scoreTransformer.provideTransformMap(map)

    private fun updateLastModifiedDate() {
        scoreboard.lastModifiedDate = DateTime.now()
    }

}