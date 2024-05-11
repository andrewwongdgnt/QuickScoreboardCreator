package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCScoreboardManagerTest {

    @InjectMockKs
    lateinit var sut: QSCScoreboardManager

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testScoreUpdates() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.NoRule,
                mapOf(),
                listOf(
                    ScoreData(0, 0, listOf(2, 3)),
                    ScoreData(0, 0, listOf(2, 3)),
                )
            ) to
                    IntervalData(
                        0, 0
                    )
        )

        sut.updateScore(0, 0)
        Assert.assertEquals("2", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(0, 1)
        Assert.assertEquals("5", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)

        sut.resetCurrentScore(scoreIndex = 0)
        Assert.assertEquals("0", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)

        sut.updateScore(1, 1)
        Assert.assertEquals("3", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(1, 1)
        Assert.assertEquals("6", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)

        sut.resetCurrentScore(scoreIndex = 1)
        Assert.assertEquals("0", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)
    }

    @Test
    fun testMaxScoreRule() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.MaxScoreRule(2u),
                mapOf(),
                listOf(
                    ScoreData(0, 0, listOf(1)),
                    ScoreData(0, 0, listOf(20)),
                )
            ) to
                    IntervalData(
                        0, 0
                    )
        )

        sut.updateScore(0, 0)
        Assert.assertEquals("1", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(0, 0)
        Assert.assertEquals("2", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(0, 0)
        Assert.assertEquals("2", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(1, 0)
        Assert.assertEquals("2", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(1, 0, false)
        Assert.assertEquals("-2", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)

    }

    @Test
    fun testDeuceAdvantageScoreRule() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule(2u),
                mapOf(),
                listOf(
                    ScoreData(0, 0, listOf(1)),
                    ScoreData(0, 0, listOf(1)),
                )
            ) to
                    IntervalData(
                        0, 0
                    )
        )

        sut.updateScore(0, 0)
        Assert.assertEquals("1", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(0, 0)
        Assert.assertEquals("2", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(1, 0)
        Assert.assertEquals("1", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(1, 0)
        sut.getScores().apply {
            Assert.assertEquals(DisplayedScore.Blank, displayedScores[0])
            Assert.assertEquals(DisplayedScore.Blank, displayedScores[1])
            Assert.assertEquals(DisplayedScore.Deuce, overallDisplayedScore)
        }
        sut.updateScore(0, 0)
        sut.getScores().apply {
            Assert.assertEquals(DisplayedScore.Advantage, displayedScores[0])
            Assert.assertEquals(DisplayedScore.Blank, displayedScores[1])
            Assert.assertEquals(DisplayedScore.Blank, overallDisplayedScore)
        }
        sut.updateScore(1, 0)
        sut.getScores().apply {
            Assert.assertEquals(DisplayedScore.Blank, displayedScores[0])
            Assert.assertEquals(DisplayedScore.Blank, displayedScores[1])
            Assert.assertEquals(DisplayedScore.Deuce, overallDisplayedScore)
        }
        sut.updateScore(1, 0)
        sut.getScores().apply {
            Assert.assertEquals(DisplayedScore.Blank, displayedScores[0])
            Assert.assertEquals(DisplayedScore.Advantage, displayedScores[1])
            Assert.assertEquals(DisplayedScore.Blank, overallDisplayedScore)
        }

    }

}