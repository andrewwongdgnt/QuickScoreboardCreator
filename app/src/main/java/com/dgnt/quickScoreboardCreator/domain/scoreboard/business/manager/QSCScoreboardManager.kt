package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.Scoreboard
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import kotlin.math.abs


class QSCScoreboardManager : ScoreboardManager {

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

    override val incrementList: List<List<Int>>
        get() = currentScoreInfo.dataList.map { it.increments }

    private val currentTeamSize: Int
        get() = incrementList.size

    private val currentScoreInfo get() = scoreboard.intervalList[currentIntervalIndex].first

    override fun updateScore(scoreIndex: Int, incrementIndex: Int, positive: Boolean) {
        val scoreInfo = currentScoreInfo

        scoreInfo.dataList[scoreIndex].apply {
            val incrementer = abs(increments[incrementIndex]).let {
                if (positive)
                    it
                else
                    it * -1
            }

            val newScore = current + incrementer
            if (scoreInfo.scoreRule is ScoreRule.ScoreRuleTrigger.MaxScoreRule) {
                val trigger = scoreInfo.scoreRule.trigger.toInt()
                if (newScore > trigger) {
                    current = trigger
                    return
                } else if (newScore < trigger * -1) {
                    current = trigger * -1
                    return
                }
            }
            current = newScore
        }
    }

    override fun getScores(): DisplayedScoreInfo {
        val scoreInfo = currentScoreInfo

        if (scoreInfo.scoreRule is ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule && currentTeamSize == 2 && scoreInfo.dataList.all { it.current >= scoreInfo.scoreRule.trigger.toInt() }) {

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
        (0 until currentTeamSize).forEach {
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