package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.Scoreboard
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import kotlin.math.abs


class QSCScoreboardManager : ScoreboardManager {


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

    override var intervalList: List<Pair<ScoreInfo, IntervalData>>
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

    private val currentIntervalData get() = scoreboard.intervalList[currentIntervalIndex].second

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
            current = if (newScore < 0)
                0
            else
                newScore
        }

        val newScore = scoreInfo.dataList[scoreIndex].current

        if (scoreInfo.scoreRule is ScoreRule.ScoreRuleTrigger.MaxScoreRule) {
            val trigger = scoreInfo.scoreRule.trigger
            if (newScore > trigger) {
                proceedToNextInterval()
                return
            }
        }

        get2ScoresForDeuceAdv(scoreInfo)?.takeIf { abs(it.first - it.second) >= 2 }?.run {
            proceedToNextInterval()
        }
    }

    override fun getScores(): DisplayedScoreInfo {
        val scoreInfo = currentScoreInfo

        return get2ScoresForDeuceAdv(scoreInfo)?.let {
            val firstScore = it.first
            val secondScore = it.second
            if (firstScore == secondScore)
                DisplayedScoreInfo(listOf(DisplayedScore.Blank, DisplayedScore.Blank), DisplayedScore.Deuce)
            else if (firstScore > secondScore)
                DisplayedScoreInfo(listOf(DisplayedScore.Advantage, DisplayedScore.Blank), DisplayedScore.Blank)
            else
                DisplayedScoreInfo(listOf(DisplayedScore.Blank, DisplayedScore.Advantage), DisplayedScore.Blank)
        } ?: run {
            val mappedScores = transform(scoreInfo).map { DisplayedScore.CustomDisplayedScore(it) }
            return DisplayedScoreInfo(mappedScores, DisplayedScore.Blank)
        }


    }

    override fun proceedToNextInterval() {
        //at the end so don't increase anymore
        if (currentIntervalIndex >= scoreboard.intervalList.size - 1)
            return

        currentIntervalIndex++
        currentIntervalData.reset()
        if (scoreCarriesOver) {
            val previousScores = scoreboard.intervalList[currentIntervalIndex - 1].first.dataList
            val currentScores = currentScoreInfo.dataList
            currentScores.forEachIndexed { index, scoreData ->
                scoreData.current = previousScores[index].current
            }
        } else
            currentScoreInfo.dataList.forEach { it.reset() }
    }

    override fun setTime(value: Long) {
        if (value <= 0)
            proceedToNextInterval()
        else
            currentIntervalData.current = value
    }

    override fun getInitialTime() =
        currentIntervalData.initial

    override fun isTimeIncreasing() =
        currentIntervalData.increasing

    private fun transform(scoreInfo: ScoreInfo) =
        scoreInfo.dataList.map { it.current }.map {
            scoreInfo.scoreToDisplayScoreMap[it] ?: it.toString()
        }

    private fun get2ScoresForDeuceAdv(scoreInfo: ScoreInfo): Pair<Int, Int>? {
        return if (scoreInfo.scoreRule is ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule && currentTeamSize == 2 && scoreInfo.dataList.all { it.current >= scoreInfo.scoreRule.trigger })
            scoreInfo.dataList[0].current to scoreInfo.dataList[1].current
        else
            return null
    }


}