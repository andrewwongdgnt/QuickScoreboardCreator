package com.dgnt.quickScoreboardCreator.domain.history.business.logic


import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
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
        sut.init(
            listOf(
                ScoreInfo(
                    ScoreRule.NoRule,
                    mapOf(),
                    listOf()
                ) to
                        IntervalData(
                            1000,
                            1000
                        ),
                ScoreInfo(
                    ScoreRule.NoRule,
                    mapOf(),
                    listOf()
                ) to
                        IntervalData(
                            0,
                            0,
                            increasing = true
                        )
            )
        )

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
    }

    @Test
    fun testSequentialCreation() {
        val historicalScoreboard = sut.create(
            IntervalLabel.CustomIntervalLabel("Quarter"),
            listOf(
                TeamLabel.CustomTeamLabel("Whoaly", TeamIcon.SHARK),
                TeamLabel.CustomTeamLabel("DGNT", TeamIcon.FIREBALL),
            )
        )

        Assert.assertEquals(2, historicalScoreboard.historicalIntervalMap.size)
        val interval1 = historicalScoreboard.historicalIntervalMap[0]!!
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

        val interval2 = historicalScoreboard.historicalIntervalMap[1]!!
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

        val scorer2AtInterval2 = interval2.historicalScoreGroupList[1]!!
        scorer2AtInterval2.primaryScoreList.let { primaryScoreList ->
            primaryScoreList[0].let {
                Assert.assertEquals(3, it.score)
                Assert.assertEquals("3", it.displayedScore)
                Assert.assertEquals(14, it.time)
            }
        }
    }

    @Test
    fun testRange() {
        val historicalScoreboard = sut.create(
            IntervalLabel.CustomIntervalLabel("Quarter"),
            listOf(
                TeamLabel.NoTeamLabel,
                TeamLabel.CustomTeamLabel("DGNT", TeamIcon.FIREBALL),
            )
        )

        Assert.assertEquals(2, historicalScoreboard.historicalIntervalMap.size)
        val interval1 = historicalScoreboard.historicalIntervalMap[0]!!
        interval1.let { interval ->
            Assert.assertEquals(HistoricalIntervalRange.CountDown(1000), interval.range)
        }

        val interval2 = historicalScoreboard.historicalIntervalMap[1]!!
        interval2.let { interval ->
            Assert.assertEquals(HistoricalIntervalRange.Infinite, interval.range)
        }

    }

    @Test
    fun testLabels() {
        val historicalScoreboard = sut.create(
            IntervalLabel.CustomIntervalLabel("Quarter"),
            listOf(
                TeamLabel.NoTeamLabel,
                TeamLabel.CustomTeamLabel("DGNT", TeamIcon.FIREBALL),
            )
        )

        Assert.assertEquals(2, historicalScoreboard.historicalIntervalMap.size)
        val interval1 = historicalScoreboard.historicalIntervalMap[0]!!
        interval1.let { interval ->
            Assert.assertEquals(IntervalLabel.CustomIntervalLabel("Quarter", 0), interval.intervalLabel)
        }
        val scorer1AtInterval1 = interval1.historicalScoreGroupList[0]!!
        scorer1AtInterval1.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.NoTeamLabel, teamLabel)
        }

        val scorer2AtInterval1 = interval1.historicalScoreGroupList[1]!!
        scorer2AtInterval1.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.CustomTeamLabel("DGNT", TeamIcon.FIREBALL), teamLabel)
        }

        val interval2 = historicalScoreboard.historicalIntervalMap[1]!!
        interval2.let { interval ->
            Assert.assertEquals(IntervalLabel.CustomIntervalLabel("Quarter", 1), interval.intervalLabel)
        }
        val scorer1AtInterval2 = interval2.historicalScoreGroupList[0]!!
        scorer1AtInterval2.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.NoTeamLabel, teamLabel)
        }

        val scorer2AtInterval2 = interval2.historicalScoreGroupList[1]!!
        scorer2AtInterval2.teamLabel.let { teamLabel ->
            Assert.assertEquals(TeamLabel.CustomTeamLabel("DGNT", TeamIcon.FIREBALL), teamLabel)
        }
    }

    @Test
    fun testGapAndUnorderedCreation() {

        sut.init(
            listOf(
                ScoreInfo(
                    ScoreRule.NoRule,
                    mapOf(),
                    listOf()
                ) to
                        IntervalData(
                            1000,
                            1000
                        ),
                ScoreInfo(
                    ScoreRule.NoRule,
                    mapOf(),
                    listOf()
                ) to
                        IntervalData(
                            1000,
                            1000
                        ),
                ScoreInfo(
                    ScoreRule.NoRule,
                    mapOf(),
                    listOf()
                ) to
                        IntervalData(
                            1000,
                            1000
                        )
            )
        )

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
            IntervalLabel.ScoreboardTypeIntervalLabel(ScoreboardType.BASKETBALL),
            listOf(
                TeamLabel.NoTeamLabel,
                TeamLabel.CustomTeamLabel("KYRA", TeamIcon.TANK),
            )
        )

        Assert.assertEquals(2, historicalScoreboard.historicalIntervalMap.size)
        val interval3 = historicalScoreboard.historicalIntervalMap[2]!!
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

        val interval2 = historicalScoreboard.historicalIntervalMap[1]!!
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
    }

}