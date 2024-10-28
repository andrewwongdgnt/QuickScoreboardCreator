package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.domain.history.business.logic.HistoryCreator
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalEndSoundType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import javax.inject.Inject
import kotlin.math.abs


class QSCScoreboardManager @Inject constructor(
    private val winCalculator: WinCalculator,
    private val historyCreator: HistoryCreator
) : ScoreboardManager {


    override var winRule: WinRule = WinRule.Count

    override var intervalList: List<Pair<ScoreInfo, IntervalData>> = listOf()
        set(value) {
            historyCreator.init(value)
            field = value
        }

    private var currentIntervalIndex: Int = 0

    private val primaryIncrementList: List<List<Int>>
        get() = currentScoreInfo.dataList.map { it.primary.increments }
    private val secondaryIncrementList: List<List<Int>>
        get() = currentScoreInfo.dataList.mapNotNull { it.secondary?.increments }

    override var primaryScoresUpdateListener: ((DisplayedScoreInfo) -> Unit)? = null

    override var secondaryScoresUpdateListener: ((DisplayedScoreInfo) -> Unit)? = null

    override var timeUpdateListener: ((Long) -> Unit)? = null

    override var intervalIndexUpdateListener: ((Int) -> Unit)? = null

    override var primaryIncrementListUpdateListener: ((List<List<Int>>) -> Unit)? = null

    override var secondaryIncrementListUpdateListener: ((List<List<Int>>) -> Unit)? = null

    override var teamSizeUpdateListener: ((Int) -> Unit)? = null

    override var winnersUpdateListener: ((Set<Int>) -> Unit)? = null

    override var intervalOnEndListener: ((IntervalEndSound) -> Unit)? = null

    override val currentTeamSize: Int
        get() = currentScoreInfo.dataList.size

    private val currentScoreInfo get() = intervalList[currentIntervalIndex].first

    private val currentIntervalData get() = intervalList[currentIntervalIndex].second


    override fun triggerUpdateListeners() {
        timeUpdateListener?.invoke(currentIntervalData.current)
        primaryScoresUpdateListener?.invoke(getPrimaryScores())
        secondaryScoresUpdateListener?.invoke(getSecondaryScores())
        intervalIndexUpdateListener?.invoke(currentIntervalIndex)
        primaryIncrementListUpdateListener?.invoke(primaryIncrementList)
        secondaryIncrementListUpdateListener?.invoke(secondaryIncrementList)
        teamSizeUpdateListener?.invoke(currentTeamSize)
    }

    override fun updateScore(isPrimary: Boolean, scoreIndex: Int, incrementIndex: Int, positive: Boolean) {
        val scoreInfo = currentScoreInfo
        val scoreGroup = scoreInfo.dataList[scoreIndex]

        val scoreData = if (isPrimary)
            scoreGroup.primary
        else
            scoreGroup.secondary ?: return

        val newScore = scoreData.run {
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
            current
        }

        historyCreator.addEntry(
            intervalIndex = currentIntervalIndex,
            currentTime = currentIntervalData.current,
            isPrimary = isPrimary,
            scoreIndex = scoreIndex,
            currentScore = newScore,
            currentDisplayedScore = if (isPrimary) transformPrimaryScores(scoreInfo)[scoreIndex] else transformSecondaryScores(scoreInfo)[scoreIndex]
        )

        if (isPrimary) {
            if (scoreInfo.scoreRule is ScoreRule.Trigger.Max && newScore > scoreInfo.scoreRule.trigger) {
                proceedToNextInterval()
                return
            }

            get2ScoresForDeuceAdv(scoreInfo)?.takeIf { abs(it.first - it.second) >= 2 }?.run {
                proceedToNextInterval()
                return
            }

            // this means one of the team is below the trigger and one is above
            if (scoreInfo.scoreRule is ScoreRule.Trigger.DeuceAdvantage && currentTeamSize == 2 && scoreInfo.dataList.any { it.primary.current > scoreInfo.scoreRule.trigger } && scoreInfo.dataList.any { it.primary.current < scoreInfo.scoreRule.trigger }) {
                proceedToNextInterval()
                return
            }
        }

        primaryScoresUpdateListener?.invoke(getPrimaryScores())
        secondaryScoresUpdateListener?.invoke(getSecondaryScores())
    }

    private fun getPrimaryScores(): DisplayedScoreInfo {
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
            val mappedScores = transformPrimaryScores(scoreInfo).map { DisplayedScore.Custom(it) }
            return DisplayedScoreInfo(mappedScores, DisplayedScore.Blank)
        }
    }

    private fun getSecondaryScores(): DisplayedScoreInfo {
        val scoreInfo = currentScoreInfo

        val mappedScores = transformSecondaryScores(scoreInfo).map { DisplayedScore.Custom(it) }
        return DisplayedScoreInfo(mappedScores, DisplayedScore.Blank)

    }

    override fun updateTime(value: Long) {
        currentIntervalData.current = value.coerceAtLeast(0)
        if (value == 0L && !currentIntervalData.increasing)
            proceedToNextInterval()
        else
            timeUpdateListener?.invoke(currentIntervalData.current)
    }

    override fun updateTimeBy(value: Long) {
        val newValue = if (currentIntervalData.increasing)
            abs(value)
        else
            abs(value) * -1
        updateTime(currentIntervalData.current + newValue)
    }

    override fun resetTime() {
        updateTime(currentIntervalData.initial)
    }

    override fun canTimeAdvance() =
        currentIntervalData.increasing || (!currentIntervalData.increasing && currentIntervalData.current > 0)

    override fun updateInterval(index: Int) {

        // Boundaries
        if (index !in intervalList.indices)
            return


        winCalculator.store(currentScoreInfo, currentIntervalIndex)

        val currentScoreGroups = currentScoreInfo.dataList

        currentIntervalIndex = index

        val newScoreGroups = currentScoreInfo.dataList
        newScoreGroups.forEachIndexed { i, newScoreGroup ->
            currentScoreGroups.getOrNull(i)?.let { currentScoreGroup ->
                newScoreGroup.primary.current = currentScoreGroup.primary.current
                newScoreGroup.secondary?.let { secondary ->
                    secondary.current = currentScoreGroup.secondary?.current ?: secondary.initial
                }
            }
        }

        triggerUpdateListeners()
    }

    override fun createTimeline(intervalLabel: IntervalLabel, teamList: List<TeamLabel>) =
        historyCreator.create(intervalLabel, teamList)

    private fun transformPrimaryScores(scoreInfo: ScoreInfo) =
        scoreInfo.dataList.map { it.primary.current }.map {
            scoreInfo.scoreToDisplayScoreMap[it] ?: it.toString()
        }

    private fun get2ScoresForDeuceAdv(scoreInfo: ScoreInfo): Pair<Int, Int>? {
        return if (scoreInfo.scoreRule is ScoreRule.Trigger.DeuceAdvantage && currentTeamSize == 2 && scoreInfo.dataList.all { it.primary.current >= scoreInfo.scoreRule.trigger })
            scoreInfo.dataList[0].primary.current to scoreInfo.dataList[1].primary.current
        else
            return null
    }

    private fun transformSecondaryScores(scoreInfo: ScoreInfo) =
        scoreInfo.dataList.mapNotNull { it.secondary?.current?.toString() }

    private fun proceedToNextInterval() {

        winCalculator.store(currentScoreInfo, currentIntervalIndex)
        intervalOnEndListener?.invoke(currentIntervalData.soundEffect)

        if (currentIntervalIndex >= intervalList.size - 1) {
            currentIntervalIndex = intervalList.size - 1

            val winners = winCalculator.calculate(winRule)
            winnersUpdateListener?.invoke(winners)
            return
        }

        currentIntervalIndex++
        currentIntervalData.reset()
        if (winRule.scoreCarriesOver) {
            val previousScoreGroups = intervalList[currentIntervalIndex - 1].first.dataList
            val currentScoreGroups = currentScoreInfo.dataList
            currentScoreGroups.forEachIndexed { index, currentScoreGroup ->
                previousScoreGroups.getOrNull(index)?.let { previousScoreGroup ->
                    currentScoreGroup.primary.current = previousScoreGroup.primary.current
                    currentScoreGroup.secondary?.let { secondary ->
                        secondary.current = previousScoreGroup.secondary?.current ?: secondary.initial
                    }
                }
            }
        } else
            currentScoreInfo.dataList.forEach {
                it.primary.reset()
                it.secondary?.reset()
            }

        triggerUpdateListeners()
    }


}