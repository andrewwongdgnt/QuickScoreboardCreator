package com.dgnt.quickScoreboardCreator.domain.history.business.logic


import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCHistoryCreatorTest {


    @InjectMockKs
    private lateinit var sut: QSCHistoryCreator

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testNormalCreation() {
        sut.addEntry(
            intervalIndex = 0,
            currentTime = 0,
            isPrimary = true,
            scoreIndex = 0,
            currentScore = 2,
            currentDisplayedScore = "2"
        )
        sut.addEntry(
            intervalIndex = 0,
            currentTime = 4,
            isPrimary = true,
            scoreIndex = 0,
            currentScore = 4,
            currentDisplayedScore = "4"
        )
        sut.addEntry(
            intervalIndex = 0,
            currentTime = 8,
            isPrimary = true,
            scoreIndex = 1,
            currentScore = 1,
            currentDisplayedScore = "1"
        )
        sut.addEntry(
            intervalIndex = 0,
            currentTime = 16,
            isPrimary = true,
            scoreIndex = 0,
            currentScore = 7,
            currentDisplayedScore = "7"
        )
        sut.addEntry(
            intervalIndex = 0,
            currentTime = 22,
            isPrimary = true,
            scoreIndex = 1,
            currentScore = 3,
            currentDisplayedScore = "3"
        )

        sut.addEntry(
            intervalIndex = 1,
            currentTime = 2,
            isPrimary = true,
            scoreIndex = 0,
            currentScore = 2,
            currentDisplayedScore = "2"
        )
        sut.addEntry(
            intervalIndex = 1,
            currentTime = 12,
            isPrimary = true,
            scoreIndex = 0,
            currentScore = 4,
            currentDisplayedScore = "4"
        )
        sut.addEntry(
            intervalIndex = 1,
            currentTime = 14,
            isPrimary = true,
            scoreIndex = 1,
            currentScore = 3,
            currentDisplayedScore = "3"
        )

        val historicalScoreboard = sut.create()

        Assert.assertEquals(2, historicalScoreboard.historicalIntervalList.size)
        val interval1 = historicalScoreboard.historicalIntervalList[0]
        Assert.assertEquals(2, interval1.historicalScoreGroupList.size)
        val scorer1AtInterval1 = interval1.historicalScoreGroupList[0]
        scorer1AtInterval1.primaryScoreList.let { primaryScoreList ->
            primaryScoreList[0].let {
                Assert.assertEquals(2, it.score)
                Assert.assertEquals("2", it.displayedScore)
                Assert.assertEquals(0, it.time)
            }
            primaryScoreList[1].let {
                Assert.assertEquals(4, it.score)
                Assert.assertEquals("4", it.displayedScore)
                Assert.assertEquals(4, it.time)
            }
            primaryScoreList[2].let {
                Assert.assertEquals(7, it.score)
                Assert.assertEquals("7", it.displayedScore)
                Assert.assertEquals(16, it.time)
            }
        }

        val scorer2AtInterval1 = interval1.historicalScoreGroupList[1]
        scorer2AtInterval1.primaryScoreList.let { primaryScoreList ->
            primaryScoreList[0].let {
                Assert.assertEquals(1, it.score)
                Assert.assertEquals("1", it.displayedScore)
                Assert.assertEquals(8, it.time)
            }
            primaryScoreList[1].let {
                Assert.assertEquals(3, it.score)
                Assert.assertEquals("3", it.displayedScore)
                Assert.assertEquals(22, it.time)
            }
        }

        val interval2 = historicalScoreboard.historicalIntervalList[1]
        Assert.assertEquals(2, interval2.historicalScoreGroupList.size)
        val scorer1AtInterval2 = interval2.historicalScoreGroupList[0]
        scorer1AtInterval2.primaryScoreList.let { primaryScoreList ->
            primaryScoreList[0].let {
                Assert.assertEquals(2, it.score)
                Assert.assertEquals("2", it.displayedScore)
                Assert.assertEquals(2, it.time)
            }
            primaryScoreList[1].let {
                Assert.assertEquals(4, it.score)
                Assert.assertEquals("4", it.displayedScore)
                Assert.assertEquals(12, it.time)
            }
        }

        val scorer2AtInterval2 = interval2.historicalScoreGroupList[1]
        scorer2AtInterval2.primaryScoreList.let { primaryScoreList ->
            primaryScoreList[0].let {
                Assert.assertEquals(3, it.score)
                Assert.assertEquals("3", it.displayedScore)
                Assert.assertEquals(14, it.time)
            }
        }
    }

}