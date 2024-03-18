package com.dgnt.quickScoreboardCreator.manager.scoreBoard


import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalData
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
            false,
            listOf(),
            0
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

    override var scoreCarriesOver: Boolean
        get() = scoreboard.scoreCarriesOver
        set(value) {
            scoreboard.scoreCarriesOver = value
        }

    override var intervalList: List<Pair<ScoreInfo<ScoreData>, IntervalData>>
        get() = scoreboard.intervalList
        set(value) {
            scoreboard.intervalList = value
        }

    override var currentIntervalIndex: Int
        get() = scoreboard.currentIntervalIndex
        set(value) {
            scoreboard.currentIntervalIndex = value
        }

    private val currentScoreInfo get() = scoreboard.intervalList[currentIntervalIndex].first

    override fun updateScore(scoreIndex: Int, incrementIndex: Int) {
        val scoreInfo = currentScoreInfo

        scoreInfo.dataList[scoreIndex].apply {
            val newScore = current + increments[incrementIndex]
            if ((scoreInfo.scoreRule is ScoreRule.ScoreRuleTrigger.MaxScoreRule && newScore <= scoreInfo.scoreRule.trigger) || scoreInfo.scoreRule !is ScoreRule.ScoreRuleTrigger.MaxScoreRule)
                current = newScore
        }
        updateLastModifiedDate()
    }

    override fun getScores(): DisplayedScoreInfo {
        val scoreInfo = currentScoreInfo

        if (scoreInfo.scoreRule is ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule && scoreboard.currentTeamSize == 2 && scoreInfo.dataList.all { it.current >= scoreInfo.scoreRule.trigger }) {

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

    override fun resetScoreAtCurrentInterval(scoreIndex: Int) {
        currentScoreInfo.dataList[scoreIndex].reset()
        updateLastModifiedDate()
    }

    override fun resetCurrentScores() {
        (0 until scoreboard.currentTeamSize).forEach {
            resetScoreAtCurrentInterval(it)
        }
        updateLastModifiedDate()
    }

    override fun provideTransformMap(map: Map<Int, String>) = scoreTransformer.provideTransformMap(map)

    private fun updateLastModifiedDate() {
        scoreboard.lastModifiedDate = DateTime.now()
    }

}