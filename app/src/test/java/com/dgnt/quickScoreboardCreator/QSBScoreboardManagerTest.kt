package com.dgnt.quickScoreboardCreator

import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.data.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.manager.scoreBoard.QSBScoreboardManager
import com.dgnt.quickScoreboardCreator.manager.scoreTransformer.ScoreTransformer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSBScoreboardManagerTest {

    @MockK
    lateinit var scoreTransformer: ScoreTransformer

    @InjectMockKs
    lateinit var sut: QSBScoreboardManager

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testScoreUpdates() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.NoRule,
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
        every { scoreTransformer.transform(listOf(2, 0)) } returns listOf("2", "0")
        Assert.assertEquals("2", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(0, 1)
        every { scoreTransformer.transform(listOf(5, 0)) } returns listOf("5", "0")
        Assert.assertEquals("5", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)

        sut.resetScoreAtCurrentInterval(scoreIndex = 0)
        every { scoreTransformer.transform(listOf(0, 0)) } returns listOf("0", "0")
        Assert.assertEquals("0", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)

        sut.updateScore(1, 1)
        every { scoreTransformer.transform(listOf(0, 3)) } returns listOf("0", "3")
        Assert.assertEquals("3", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(1, 1)
        every { scoreTransformer.transform(listOf(0, 6)) } returns listOf("0", "6")
        Assert.assertEquals("6", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)

        sut.resetScoreAtCurrentInterval(scoreIndex = 1)
        every { scoreTransformer.transform(listOf(0, 0)) } returns listOf("0", "0")
        Assert.assertEquals("0", (sut.getScores().displayedScores[1] as? DisplayedScore.CustomDisplayedScore)?.display)
    }

    @Test
    fun testMaxScoreRule() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.MaxScoreRule(2),
                listOf(
                    ScoreData(0, 0, listOf(1)),
                    ScoreData(0, 0, listOf(1)),
                )
            ) to
                    IntervalData(
                        0, 0
                    )
        )

        every { scoreTransformer.transform(listOf(1, 0)) } returns listOf("1", "0")
        every { scoreTransformer.transform(listOf(2, 0)) } returns listOf("2", "0")

        sut.updateScore(0, 0)
        Assert.assertEquals("1", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(0, 0)
        Assert.assertEquals("2", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)
        sut.updateScore(0, 0)
        Assert.assertEquals("2", (sut.getScores().displayedScores[0] as? DisplayedScore.CustomDisplayedScore)?.display)

    }

    @Test
    fun testDeuceAdvantageScoreRule() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule(2),
                listOf(
                    ScoreData(0, 0, listOf(1)),
                    ScoreData(0, 0, listOf(1)),
                )
            ) to
                    IntervalData(
                        0, 0
                    )
        )

        every { scoreTransformer.transform(listOf(1, 0)) } returns listOf("1", "0")
        every { scoreTransformer.transform(listOf(2, 0)) } returns listOf("2", "0")
        every { scoreTransformer.transform(listOf(2, 1)) } returns listOf("2", "1")
        every { scoreTransformer.transform(listOf(2, 2)) } returns listOf("2", "2")

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