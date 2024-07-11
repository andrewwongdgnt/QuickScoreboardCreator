package com.dgnt.quickScoreboardCreator.domain.history.business.logic


import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
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
    fun testSequentialCreation() {
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

        val historicalScoreboard = sut.create(
            IntervalLabel.CustomIntervalLabel("Quarter"),
            listOf(
                TeamLabel.CustomTeamLabel("Whoaly", TeamIcon.SHARK),
                TeamLabel.CustomTeamLabel("DGNT", TeamIcon.FIREBALL),
            )
        )

        Assert.assertEquals(2, historicalScoreboard.historicalIntervalMap.size)
        val interval1 = historicalScoreboard.historicalIntervalMap[0]!!
        interval1.let { interval ->
            Assert.assertEquals(IntervalLabel.CustomIntervalLabel("Quarter", 0), interval.intervalLabel)
        }
        Assert.assertEquals(2, interval1.historicalScoreGroupList.size)
        val scorer1AtInterval1 = interval1.historicalScoreGroupList[0]!!
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
        scorer1AtInterval1.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.CustomTeamLabel("Whoaly", TeamIcon.SHARK), teamLabel)
        }

        val scorer2AtInterval1 = interval1.historicalScoreGroupList[1]!!
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
        scorer2AtInterval1.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.CustomTeamLabel("DGNT", TeamIcon.FIREBALL), teamLabel)
        }

        val interval2 = historicalScoreboard.historicalIntervalMap[1]!!
        interval2.let { interval ->
            Assert.assertEquals(IntervalLabel.CustomIntervalLabel("Quarter", 1), interval.intervalLabel)
        }
        Assert.assertEquals(2, interval2.historicalScoreGroupList.size)
        val scorer1AtInterval2 = interval2.historicalScoreGroupList[0]!!
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
        scorer1AtInterval2.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.CustomTeamLabel("Whoaly", TeamIcon.SHARK), teamLabel)
        }

        val scorer2AtInterval2 = interval2.historicalScoreGroupList[1]!!
        scorer2AtInterval2.primaryScoreList.let { primaryScoreList ->
            primaryScoreList[0].let {
                Assert.assertEquals(3, it.score)
                Assert.assertEquals("3", it.displayedScore)
                Assert.assertEquals(14, it.time)
            }
        }
        scorer2AtInterval2.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.CustomTeamLabel("DGNT", TeamIcon.FIREBALL), teamLabel)
        }
    }

    @Test
    fun testGapAndUnorderedCreation() {
        sut.addEntry(
            intervalIndex = 2,
            currentTime = 0,
            isPrimary = true,
            scoreIndex = 0,
            currentScore = 2,
            currentDisplayedScore = "2"
        )
        sut.addEntry(
            intervalIndex = 2,
            currentTime = 4,
            isPrimary = true,
            scoreIndex = 0,
            currentScore = 4,
            currentDisplayedScore = "4"
        )
        sut.addEntry(
            intervalIndex = 2,
            currentTime = 12,
            isPrimary = true,
            scoreIndex = 1,
            currentScore = 1,
            currentDisplayedScore = "1"
        )
        sut.addEntry(
            intervalIndex = 2,
            currentTime = 16,
            isPrimary = true,
            scoreIndex = 0,
            currentScore = 7,
            currentDisplayedScore = "7"
        )
        sut.addEntry(
            intervalIndex = 2,
            currentTime = 3,
            isPrimary = true,
            scoreIndex = 1,
            currentScore = 3,
            currentDisplayedScore = "3"
        )

        sut.addEntry(
            intervalIndex = 1,
            currentTime = 12,
            isPrimary = true,
            scoreIndex = 1,
            currentScore = 2,
            currentDisplayedScore = "2"
        )
        sut.addEntry(
            intervalIndex = 1,
            currentTime = 5,
            isPrimary = true,
            scoreIndex = 1,
            currentScore = 4,
            currentDisplayedScore = "4"
        )

        val historicalScoreboard = sut.create(
            IntervalLabel.ResourceIntervalLabel(R.string.quarter),
            listOf(
                TeamLabel.NoTeamLabel,
                TeamLabel.CustomTeamLabel("KYRA", TeamIcon.TANK),
            ))

        Assert.assertEquals(2, historicalScoreboard.historicalIntervalMap.size)
        val interval3 = historicalScoreboard.historicalIntervalMap[2]!!
        interval3.let { interval ->
            Assert.assertEquals(IntervalLabel.ResourceIntervalLabel(R.string.quarter, 2), interval.intervalLabel)
        }
        Assert.assertEquals(2, interval3.historicalScoreGroupList.size)
        val scorer1AtInterval3 = interval3.historicalScoreGroupList[0]!!
        scorer1AtInterval3.primaryScoreList.let { primaryScoreList ->
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
        scorer1AtInterval3.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.NoTeamLabel, teamLabel)
        }

        val scorer2AtInterval3 = interval3.historicalScoreGroupList[1]!!
        scorer2AtInterval3.primaryScoreList.let { primaryScoreList ->
            primaryScoreList[0].let {
                Assert.assertEquals(1, it.score)
                Assert.assertEquals("1", it.displayedScore)
                Assert.assertEquals(12, it.time)
            }
            primaryScoreList[1].let {
                Assert.assertEquals(3, it.score)
                Assert.assertEquals("3", it.displayedScore)
                Assert.assertEquals(3, it.time)
            }
        }
        scorer2AtInterval3.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.CustomTeamLabel("KYRA", TeamIcon.TANK), teamLabel)
        }

        val interval2 = historicalScoreboard.historicalIntervalMap[1]!!
        interval2.let { interval ->
            Assert.assertEquals(IntervalLabel.ResourceIntervalLabel(R.string.quarter, 1), interval.intervalLabel)
        }
        Assert.assertEquals(1, interval2.historicalScoreGroupList.size)

        val scorer2AtInterval2 = interval2.historicalScoreGroupList[1]!!
        scorer2AtInterval2.primaryScoreList.let { primaryScoreList ->
            primaryScoreList[0].let {
                Assert.assertEquals(2, it.score)
                Assert.assertEquals("2", it.displayedScore)
                Assert.assertEquals(12, it.time)
            }
            primaryScoreList[1].let {
                Assert.assertEquals(4, it.score)
                Assert.assertEquals("4", it.displayedScore)
                Assert.assertEquals(5, it.time)
            }
        }
        scorer2AtInterval2.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.CustomTeamLabel("KYRA", TeamIcon.TANK), teamLabel)
        }
    }

}