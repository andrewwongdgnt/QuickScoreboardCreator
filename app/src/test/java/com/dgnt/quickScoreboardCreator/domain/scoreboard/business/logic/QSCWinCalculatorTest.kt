package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.QSCWinCalculator
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class QSCWinCalculatorTest {

    @InjectMockKs
    private lateinit var sut: QSCWinCalculator

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testWinnerByFinal() {

        val mockScoreInfoList = listOf(
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(2, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(3, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(20, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(31, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(25, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(54, 0, listOf(1)),
                        null
                    )
                )
            )
        )

        sut.store(mockScoreInfoList[0], 0)
        sut.store(mockScoreInfoList[1], 1)
        sut.store(mockScoreInfoList[2], 2)

        Assert.assertEquals(setOf(1), sut.calculate(WinRule.Final))
    }

    @Test
    fun testTieByFinal() {

        val mockScoreInfoList = listOf(
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(2, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(3, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(20, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(31, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(50, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(50, 0, listOf(1)),
                        null
                    )
                )
            )
        )

        sut.store(mockScoreInfoList[0], 0)
        sut.store(mockScoreInfoList[1], 1)
        sut.store(mockScoreInfoList[2], 2)

        Assert.assertEquals(setOf(0, 1), sut.calculate(WinRule.Final))
    }

    @Test
    fun testWinnerByCount() {
        val mockScoreInfoList = listOf(
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(2, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(3, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(5, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(3, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(8, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(7, 0, listOf(1)),
                        null
                    )
                )
            )
        )

        sut.store(mockScoreInfoList[0], 0)
        sut.store(mockScoreInfoList[1], 1)
        sut.store(mockScoreInfoList[2], 2)

        Assert.assertEquals(setOf(0), sut.calculate(WinRule.Count))
    }

    @Test
    fun testTieByCount() {
        val mockScoreInfoList = listOf(
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(2, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(3, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(5, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(3, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(8, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(8, 0, listOf(1)),
                        null
                    )
                )
            )
        )

        sut.store(mockScoreInfoList[0], 0)
        sut.store(mockScoreInfoList[1], 1)
        sut.store(mockScoreInfoList[2], 2)

        Assert.assertEquals(setOf(0, 1), sut.calculate(WinRule.Count))
    }

    @Test
    fun testWinnerBySum() {
        val mockScoreInfoList = listOf(
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(10, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(9, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(10, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(9, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(10, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(9, 0, listOf(1)),
                        null
                    )
                )
            )
        )

        sut.store(mockScoreInfoList[0], 0)
        sut.store(mockScoreInfoList[1], 1)
        sut.store(mockScoreInfoList[2], 2)

        Assert.assertEquals(setOf(0), sut.calculate(WinRule.Sum))
    }

    @Test
    fun testTieBySum() {
        val mockScoreInfoList = listOf(
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(10, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(9, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(10, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(9, 0, listOf(1)),
                        null
                    )
                )
            ),
            ScoreInfo(
                ScoreRule.None,
                mapOf(),
                "",
                listOf(
                    ScoreGroup(
                        ScoreData(8, 0, listOf(1)),
                        null
                    ),
                    ScoreGroup(
                        ScoreData(10, 0, listOf(1)),
                        null
                    )
                )
            )
        )

        sut.store(mockScoreInfoList[0], 0)
        sut.store(mockScoreInfoList[1], 1)
        sut.store(mockScoreInfoList[2], 2)

        Assert.assertEquals(setOf(0, 1), sut.calculate(WinRule.Sum))
    }
}