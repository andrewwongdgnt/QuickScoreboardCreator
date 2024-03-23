package com.dgnt.quickScoreboardCreator.business.scoreboard.manager


import com.dgnt.quickScoreboardCreator.business.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.scoreboard.Scoreboard
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.state.DisplayedScoreInfo


class QSBScoreboardManager : ScoreboardManager {

    private var scoreToDisplayScoreMap: Map<Int, String> = mapOf()

    private val scoreboard: Scoreboard =
        Scoreboard(
            false,
            listOf(),
            0
        )

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
        val mappedScores = transform(scoreInfo.dataList.map { it.current }).map { DisplayedScore.CustomDisplayedScore(it) }
        return DisplayedScoreInfo(mappedScores, DisplayedScore.Blank)
    }

    override fun resetCurrentScore(scoreIndex: Int) {
        currentScoreInfo.dataList[scoreIndex].reset()
    }

    override fun resetCurrentScores() {
        (0 until scoreboard.currentTeamSize).forEach {
            resetCurrentScore(it)
        }
    }

    override fun provideTransformMap(map: Map<Int, String>) {
        scoreToDisplayScoreMap = map
    }

    override fun proceedToNextInterval() {
        currentIntervalIndex++
        if (scoreCarriesOver) {
            val previousScores = scoreboard.intervalList[currentIntervalIndex - 1].first.dataList
            val currentScores = currentScoreInfo.dataList
            currentScores.forEachIndexed { index, scoreData ->
                scoreData.current = previousScores[index].current
            }
        }
    }

    override fun restartTime() {
        //TODO
    }

    override fun resumeTime() {
        //TODO
    }

    override fun pauseTime() {
        //TODO
    }

    private fun transform(list: List<Int>) =
        list.map {
            scoreToDisplayScoreMap[it] ?: it.toString()
        }


}