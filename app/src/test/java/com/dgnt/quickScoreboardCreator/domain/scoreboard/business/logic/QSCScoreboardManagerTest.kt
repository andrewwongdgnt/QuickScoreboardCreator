package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.core.domain.history.business.logic.HistoryCreator
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule
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
    private lateinit var winCalculator: com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.manager.WinCalculator

    @MockK
    private lateinit var historyCreator: HistoryCreator

    @InjectMockKs
    private lateinit var sut: com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.manager.QSCScoreboardManager

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

        every { historyCreator.init(any()) } answers { }
        every { historyCreator.addEntry(any(), any(), any(), any(), any(), any()) } answers { }
        every { historyCreator.create(any(), any()) } answers { HistoricalScoreboard(mapOf()) }
    }

    @Test
    fun testPrimaryScoreUpdates() {

        sut.intervalList = listOf(
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
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

        run `increase team 1 by 2`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("2"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 1 by 3`@{
            sut.updateScore(true, 0, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("5"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 by 3`@{
            sut.updateScore(true, 1, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("5"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("3"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 by 3`@{
            sut.updateScore(true, 1, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("5"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("6"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
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
                ScoreRule.None,
                mapOf(),
                "",
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

        run `increase team 1 secondary by 1`@{
            sut.updateScore(false, 0, 0)
            verify(exactly = 1) {
                sut.secondaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 secondary by 1`@{
            sut.updateScore(false, 1, 0)
            verify(exactly = 1) {
                sut.secondaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
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
                ScoreRule.None,
                mapOf(),
                "",
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
                ScoreRule.None,
                mapOf(),
                "",
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
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("2"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("3"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
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
                ScoreRule.Trigger.Max(2),
                mapOf(),
                "",
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
                ScoreRule.Trigger.Max(2),
                mapOf(),
                "",
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
                ScoreRule.Trigger.Max(2),
                mapOf(),
                "",
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
        run `increase team 1 by 1`@{

            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("2"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
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

        run `increase team 2 by 20`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
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
                ScoreRule.Trigger.DeuceAdvantage(2),
                mapOf(),
                "",
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
                ScoreRule.Trigger.DeuceAdvantage(2),
                mapOf(),
                "",
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
                ScoreRule.Trigger.DeuceAdvantage(2),
                mapOf(),
                "",
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
        run `increase team 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("2"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("2"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank,
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank,
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Deuce
                    )
                )
            }
            resetMocks()
        }

        run `increase team 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Advantage,
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank,
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank,
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank,
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Deuce
                    )
                )
            }
            resetMocks()
        }
        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank,
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Advantage,
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
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
        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("1"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("2"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(
                        displayedScores = listOf(
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                            com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank
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