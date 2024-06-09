package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test


class QSCScoreboardManagerTest {

    @MockK
    private lateinit var winCalculator: WinCalculator

    @InjectMockKs
    private lateinit var sut: QSCScoreboardManager

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        resetMocks()
    }

    private fun resetMocks() {
        sut.primaryScoresUpdateListener = mockk()
        every { sut.primaryScoresUpdateListener?.invoke(any()) } answers { }
        sut.secondaryScoresUpdateListener = mockk()
        every { sut.secondaryScoresUpdateListener?.invoke(any()) } answers { }
        sut.timeUpdateListener = mockk()
        every { sut.timeUpdateListener?.invoke(any()) } answers { }
        sut.intervalIndexUpdateListener = mockk()
        every { sut.intervalIndexUpdateListener?.invoke(any()) } answers { }
        sut.primaryIncrementListUpdateListener = mockk()
        every { sut.primaryIncrementListUpdateListener?.invoke(any()) } answers { }
        sut.secondaryIncrementListUpdateListener = mockk()
        every { sut.secondaryIncrementListUpdateListener?.invoke(any()) } answers { }
        sut.teamSizeUpdateListener = mockk()
        every { sut.teamSizeUpdateListener?.invoke(any()) } answers { }
        sut.winnersUpdateListener = mockk()
        every { sut.winnersUpdateListener?.invoke(any()) } answers { }

        every { winCalculator.store(any(), any()) } answers { }
        every { winCalculator.calculate(any()) } answers { setOf(0) }
    }

    @Test
    fun testPrimaryScoreUpdates() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.NoRule,
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(0, 0, listOf(2, 3)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                    ScoreGroup(
                        ScoreData(0, 0, listOf(2, 3)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        0, 0
                    )
        )

        run `increase player 1 by 2`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("2"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase player 1 by 3`@{
            sut.updateScore(true, 0, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("5"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase player 2 by 3`@{
            sut.updateScore(true, 1, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("5"),
                            DisplayedScore.CustomDisplayedScore("3"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase player 2 by 3`@{
            sut.updateScore(true, 1, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("5"),
                            DisplayedScore.CustomDisplayedScore("6"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

    }

    @Test
    fun testSecondaryScoreUpdates() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.NoRule,
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(0, 0, listOf(2, 3)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                    ScoreGroup(
                        ScoreData(0, 0, listOf(2, 3)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        0, 0
                    )
        )

        run `increase player 1 secondary by 1`@{
            sut.updateScore(false, 0, 0)
            verify(exactly = 1) {
                sut.secondaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("1"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase player 2 secondary by 1`@{
            sut.updateScore(false, 1, 0)
            verify(exactly = 1) {
                sut.secondaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("1"),
                            DisplayedScore.CustomDisplayedScore("1"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
    }

    @Test
    fun testAdvancingToNextIntervalFromTime() {
        sut.winRule = WinRule.Final
        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.NoRule,
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(2, 2, listOf(2, 3)),
                        ScoreData(0, 0, listOf(1))
                    ),
                    ScoreGroup(
                        ScoreData(3, 3, listOf(2, 3)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        4, 4
                    ),
            ScoreInfo(
                ScoreRule.NoRule,
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(2, 2, listOf(2, 3)),
                        ScoreData(0, 0, listOf(1))
                    ),
                    ScoreGroup(
                        ScoreData(3, 3, listOf(2, 3)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        4, 4
                    )
        )

        run `update time to 3`@{
            sut.updateTime(3)
            verify(exactly = 1) { sut.timeUpdateListener?.invoke(3L) }
            resetMocks()
        }
        run `update time to 0`@{
            sut.updateTime(0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("2"),
                            DisplayedScore.CustomDisplayedScore("3"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            verify(exactly = 1) { sut.timeUpdateListener?.invoke(4L) }
            verify(exactly = 1) { sut.intervalIndexUpdateListener?.invoke(1) }
            verify(exactly = 1) {
                sut.primaryIncrementListUpdateListener?.invoke(
                    listOf(
                        listOf(2, 3),
                        listOf(2, 3),
                    )
                )
            }
            verify(exactly = 1) { sut.teamSizeUpdateListener?.invoke(2) }
            resetMocks()
        }

    }

    @Test
    fun testMaxScoreRule() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.MaxScoreRule(2),
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(0, 0, listOf(1)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                    ScoreGroup(
                        ScoreData(0, 0, listOf(20)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        0, 0
                    ),

            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.MaxScoreRule(2),
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(0, 0, listOf(1)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                    ScoreGroup(
                        ScoreData(0, 0, listOf(20)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        0, 0
                    ),

            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.MaxScoreRule(2),
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(0, 0, listOf(1)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                    ScoreGroup(
                        ScoreData(0, 0, listOf(20)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        0, 0
                    )
        )
        run `increase player 1 by 1`@{

            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("1"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase player 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("2"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase player 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("0"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            verify(exactly = 1) { sut.timeUpdateListener?.invoke(0L) }
            verify(exactly = 1) { sut.intervalIndexUpdateListener?.invoke(1) }
            verify(exactly = 1) {
                sut.primaryIncrementListUpdateListener?.invoke(
                    listOf(
                        listOf(1),
                        listOf(20),
                    )
                )
            }
            verify(exactly = 1) { sut.teamSizeUpdateListener?.invoke(2) }
            resetMocks()
        }

        run `increase player 2 by 20`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("0"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            verify(exactly = 1) { sut.timeUpdateListener?.invoke(0) }
            verify(exactly = 1) { sut.intervalIndexUpdateListener?.invoke(2) }
            verify(exactly = 1) {
                sut.primaryIncrementListUpdateListener?.invoke(
                    listOf(
                        listOf(1),
                        listOf(20),
                    )
                )
            }
            verify(exactly = 1) { sut.teamSizeUpdateListener?.invoke(2) }
            resetMocks()
        }
    }

    @Test
    fun testDeuceAdvantageScoreRule() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule(2),
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(0, 0, listOf(1)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                    ScoreGroup(
                        ScoreData(0, 0, listOf(1)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        0, 0
                    ),

            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule(2),
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(1, 1, listOf(1)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                    ScoreGroup(
                        ScoreData(1, 1, listOf(1)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                )
            ) to
                    IntervalData(
                        0, 0
                    ),
            ScoreInfo(
                ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule(2),
                mapOf(),
                listOf(
                    ScoreGroup(
                        ScoreData(0, 0, listOf(2)),
                        ScoreData(0, 0, listOf(1)),
                    ),
                    ScoreGroup(
                        ScoreData(0, 0, listOf(2)),
                        ScoreData(0, 0, listOf(1)),
                    )
                )
            ) to
                    IntervalData(
                        5, 5
                    ),
        )
        run `increase player 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("1"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase player 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("2"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase player 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("2"),
                            DisplayedScore.CustomDisplayedScore("1"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase player 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Blank,
                            DisplayedScore.Blank,
                        ),
                        overallDisplayedScore = DisplayedScore.Deuce
                    )
                )
            }
            resetMocks()
        }

        run `increase player 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Advantage,
                            DisplayedScore.Blank,
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase player 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Blank,
                            DisplayedScore.Blank,
                        ),
                        overallDisplayedScore = DisplayedScore.Deuce
                    )
                )
            }
            resetMocks()
        }
        run `increase player 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Blank,
                            DisplayedScore.Advantage,
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase player 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("1"),
                            DisplayedScore.CustomDisplayedScore("1"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            verify(exactly = 1) { sut.timeUpdateListener?.invoke(0L) }
            verify(exactly = 1) { sut.intervalIndexUpdateListener?.invoke(1) }
            verify(exactly = 1) {
                sut.primaryIncrementListUpdateListener?.invoke(
                    listOf(
                        listOf(1),
                        listOf(1),
                    )
                )
            }
            verify(exactly = 1) { sut.teamSizeUpdateListener?.invoke(2) }
            resetMocks()
        }
        run `increase player 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("1"),
                            DisplayedScore.CustomDisplayedScore("2"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase player 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.CustomDisplayedScore("0"),
                            DisplayedScore.CustomDisplayedScore("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            verify(exactly = 1) { sut.timeUpdateListener?.invoke(5L) }
            verify(exactly = 1) { sut.intervalIndexUpdateListener?.invoke(2) }
            verify(exactly = 1) {
                sut.primaryIncrementListUpdateListener?.invoke(
                    listOf(
                        listOf(2),
                        listOf(2),
                    )
                )
            }
            verify(exactly = 1) { sut.teamSizeUpdateListener?.invoke(2) }
            resetMocks()
        }

    }

}