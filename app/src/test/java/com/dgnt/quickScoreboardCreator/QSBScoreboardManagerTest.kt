package com.dgnt.quickScoreboardCreator

import com.dgnt.quickScoreboardCreator.data.model.interval.Interval
import com.dgnt.quickScoreboardCreator.data.model.interval.TimedIntervalData
import com.dgnt.quickScoreboardCreator.data.model.score.NumberedScoreData
import com.dgnt.quickScoreboardCreator.data.model.score.Score
import com.dgnt.quickScoreboardCreator.data.model.score.StateScoreData
import com.dgnt.quickScoreboardCreator.data.model.scoreBoard.IntervalInfo
import com.dgnt.quickScoreboardCreator.data.model.scoreBoard.ScoreInfo
import com.dgnt.quickScoreboardCreator.data.state.TennisState
import com.dgnt.quickScoreboardCreator.manager.QSBScoreboardManager
import org.joda.time.Duration
import org.junit.Assert
import org.junit.Test


class QSBScoreboardManagerTest {


    @Test
    fun testNumberedScore() {
        val sbm = QSBScoreboardManager(
            "", "", ScoreInfo(
                true,
                listOf(
                    Score.NumberedScore(NumberedScoreData(0, 0, listOf(2, 3))),
                    Score.NumberedScore(NumberedScoreData(0, 0, listOf(2, 3))),
                )
            ),
            IntervalInfo(
                false,
                listOf(
                    Interval.TimedInterval(TimedIntervalData(Duration.standardMinutes(5).millis, Duration.standardMinutes(5).millis))
                )
            )
        )

        sbm.updateScore(0, 0)
        Assert.assertEquals(2, sbm.getScore(0).current)
        sbm.updateScore(0, 1)
        Assert.assertEquals(5, sbm.getScore(0).current)

        sbm.reset(scoreIndex = 0)
        Assert.assertEquals(0, sbm.getScore(0).current)

        sbm.updateScore(1, 1)
        Assert.assertEquals(3, sbm.getScore(1).current)
        sbm.updateScore(1, 1)
        Assert.assertEquals(6, sbm.getScore(1).current)

        sbm.reset(scoreIndex = 1)
        Assert.assertEquals(0, sbm.getScore(1).current)
    }

    @Test
    fun testStateScore() {
        val sbm = QSBScoreboardManager(
            "", "", ScoreInfo(
                false,
                listOf(
                    Score.StateScore(StateScoreData(TennisState.LOVE, TennisState.LOVE)),
                    Score.StateScore(StateScoreData(TennisState.LOVE, TennisState.LOVE)),
                )
            ),
            IntervalInfo(
                false,
                listOf(
                    Interval.TimedInterval(TimedIntervalData(Duration.standardMinutes(5).millis, Duration.standardMinutes(5).millis))
                )
            )
        )

        sbm.updateScore(0, 0)
        Assert.assertEquals(TennisState.S_15, sbm.getScore(0).current)
        sbm.updateScore(0, 1)
        Assert.assertEquals(TennisState.S_30, sbm.getScore(0).current)

        sbm.reset(scoreIndex = 0)
        Assert.assertEquals(TennisState.LOVE, sbm.getScore(0).current)

        sbm.updateScore(1, 1)
        Assert.assertEquals(TennisState.S_15, sbm.getScore(1).current)
        sbm.updateScore(1, 1)
        Assert.assertEquals(TennisState.S_30, sbm.getScore(1).current)

        sbm.reset(scoreIndex = 1)
        Assert.assertEquals(TennisState.LOVE, sbm.getScore(1).current)
    }
}