package com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.manager



import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.CreateHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.usecase.CalculateWinUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule
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
    private lateinit var calculateWinUseCase: CalculateWinUseCase

    @MockK
    private lateinit var createHistoryUseCase: CreateHistoryUseCase

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

        every { calculateWinUseCase.store(any(), any()) } answers { }
        every { calculateWinUseCase.calculate(any()) } answers { setOf(0) }

        every { createHistoryUseCase.init(any()) } answers { }
        every { createHistoryUseCase.addEntry(any(), any(), any(), any(), any(), any()) } answers { }
        every { createHistoryUseCase.create(any(), any()) } answers { HistoricalScoreboard(mapOf()) }
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
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("2"),
                            DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 1 by 3`@{
            sut.updateScore(true, 0, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("5"),
                            DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 by 3`@{
            sut.updateScore(true, 1, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("5"),
                            DisplayedScore.Custom("3"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 by 3`@{
            sut.updateScore(true, 1, 1)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("5"),
                            DisplayedScore.Custom("6"),
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
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("1"),
                            DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 secondary by 1`@{
            sut.updateScore(false, 1, 0)
            verify(exactly = 1) {
                sut.secondaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("1"),
                            DisplayedScore.Custom("1"),
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
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("2"),
                            DisplayedScore.Custom("3"),
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
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("1"),
                            DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("2"),
                            DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("0"),
                            DisplayedScore.Custom("0"),
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

        run `increase team 2 by 20`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("0"),
                            DisplayedScore.Custom("0"),
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
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("1"),
                            DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 1 by 1`@{
            sut.updateScore(true, 0, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("2"),
                            DisplayedScore.Custom("0"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("2"),
                            DisplayedScore.Custom("1"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }
        run `increase team 2 by 1`@{
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

        run `increase team 1 by 1`@{
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

        run `increase team 2 by 1`@{
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
        run `increase team 2 by 1`@{
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

        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("1"),
                            DisplayedScore.Custom("1"),
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
        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("1"),
                            DisplayedScore.Custom("2"),
                        ),
                        overallDisplayedScore = DisplayedScore.Blank
                    )
                )
            }
            resetMocks()
        }

        run `increase team 2 by 1`@{
            sut.updateScore(true, 1, 0)
            verify(exactly = 1) {
                sut.primaryScoresUpdateListener?.invoke(
                    DisplayedScoreInfo(
                        displayedScores = listOf(
                            DisplayedScore.Custom("0"),
                            DisplayedScore.Custom("0"),
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