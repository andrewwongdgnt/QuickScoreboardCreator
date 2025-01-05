package com.dgnt.quickScoreboardCreator.feature.history.data.mapper


import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HistoricalScoreboardMapperDomainToDataTest {

    private lateinit var sut: HistoricalScoreboardMapperDomainToData

    @Before
    fun setup() {
        sut = HistoricalScoreboardMapperDomainToData()
    }

    @Test
    fun testMapping() {
        sut.map(
            HistoricalScoreboard(
                mapOf(
                    0 to HistoricalInterval(
                        range = HistoricalIntervalRange.CountDown(72000),
                        intervalLabel = IntervalLabel.Custom("Quarter", 0),
                        historicalScoreGroupList = mapOf(
                            0 to HistoricalScoreGroup(
                                teamLabel = TeamLabel.Custom("DGNT", TeamIcon.AXE),
                                primaryScoreList = listOf(
                                    HistoricalScore(0, "0", 720000),
                                    HistoricalScore(1, "1", 660000),
                                    HistoricalScore(2, "2", 630000),
                                    HistoricalScore(3, "3", 480000),
                                    HistoricalScore(4, "4", 330000),
                                    HistoricalScore(7, "7", 300000),
                                ),
                                secondaryScoreList = listOf(
                                    HistoricalScore(1, "1", 720000)
                                )
                            ),
                            1 to HistoricalScoreGroup(
                                teamLabel = TeamLabel.None,
                                primaryScoreList = listOf(
                                    HistoricalScore(0, "0", 0),
                                    HistoricalScore(1, "1", 2000),
                                    HistoricalScore(2, "2", 4400),
                                    HistoricalScore(3, "3", 5655),
                                    HistoricalScore(4, "4", 9800),
                                ),
                                secondaryScoreList = listOf()
                            )
                        )
                    ),
                    1 to HistoricalInterval(
                        range = HistoricalIntervalRange.Infinite,
                        intervalLabel = IntervalLabel.DefaultSport(SportType.SPIKEBALL, 1),
                        historicalScoreGroupList = mapOf()
                    )
                )
            )
        ).let { historicalScoreboardData ->
            Assert.assertEquals(2, historicalScoreboardData.historicalIntervalMap.size)
            historicalScoreboardData.historicalIntervalMap[0]?.let { historicalIntervalData ->
                Assert.assertEquals(HistoricalIntervalRangeData.CountDown(72000), historicalIntervalData.range)
                Assert.assertEquals(IntervalLabelData.Custom("Quarter", 0), historicalIntervalData.intervalLabel)
                Assert.assertEquals(IntervalLabelData.Type.CUSTOM.name, historicalIntervalData.intervalLabel.type)
                Assert.assertEquals(0, historicalIntervalData.intervalLabel.index)
                Assert.assertEquals(2, historicalIntervalData.historicalScoreGroupList.size)
                historicalIntervalData.historicalScoreGroupList[0]?.let { historicalScoreGroupData ->
                    Assert.assertEquals(TeamLabelData.Custom("DGNT", TeamIcon.AXE), historicalScoreGroupData.teamLabel)
                    Assert.assertEquals(TeamLabelData.Type.CUSTOM.name, historicalScoreGroupData.teamLabel.type)
                    Assert.assertEquals(6, historicalScoreGroupData.primaryScoreList.size)
                    historicalScoreGroupData.primaryScoreList[0].let { historicalScoreData ->
                        Assert.assertEquals(0, historicalScoreData.score)
                        Assert.assertEquals("0", historicalScoreData.displayedScore)
                        Assert.assertEquals(720000, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[1].let { historicalScoreData ->
                        Assert.assertEquals(1, historicalScoreData.score)
                        Assert.assertEquals("1", historicalScoreData.displayedScore)
                        Assert.assertEquals(660000, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[2].let { historicalScoreData ->
                        Assert.assertEquals(2, historicalScoreData.score)
                        Assert.assertEquals("2", historicalScoreData.displayedScore)
                        Assert.assertEquals(630000, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[3].let { historicalScoreData ->
                        Assert.assertEquals(3, historicalScoreData.score)
                        Assert.assertEquals("3", historicalScoreData.displayedScore)
                        Assert.assertEquals(480000, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[4].let { historicalScoreData ->
                        Assert.assertEquals(4, historicalScoreData.score)
                        Assert.assertEquals("4", historicalScoreData.displayedScore)
                        Assert.assertEquals(330000, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[5].let { historicalScoreData ->
                        Assert.assertEquals(7, historicalScoreData.score)
                        Assert.assertEquals("7", historicalScoreData.displayedScore)
                        Assert.assertEquals(300000, historicalScoreData.time)
                    }
                    Assert.assertEquals(1, historicalScoreGroupData.secondaryScoreList.size)
                    historicalScoreGroupData.secondaryScoreList[0].let { historicalScoreData ->
                        Assert.assertEquals(1, historicalScoreData.score)
                        Assert.assertEquals("1", historicalScoreData.displayedScore)
                        Assert.assertEquals(720000, historicalScoreData.time)
                    }

                }
                historicalIntervalData.historicalScoreGroupList[1]?.let { historicalScoreGroupData ->
                    Assert.assertEquals(TeamLabelData.None, historicalScoreGroupData.teamLabel)
                    Assert.assertEquals(TeamLabelData.Type.NONE.name, historicalScoreGroupData.teamLabel.type)
                    Assert.assertEquals(5, historicalScoreGroupData.primaryScoreList.size)
                    historicalScoreGroupData.primaryScoreList[0].let { historicalScoreData ->
                        Assert.assertEquals(0, historicalScoreData.score)
                        Assert.assertEquals("0", historicalScoreData.displayedScore)
                        Assert.assertEquals(0, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[1].let { historicalScoreData ->
                        Assert.assertEquals(1, historicalScoreData.score)
                        Assert.assertEquals("1", historicalScoreData.displayedScore)
                        Assert.assertEquals(2000, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[2].let { historicalScoreData ->
                        Assert.assertEquals(2, historicalScoreData.score)
                        Assert.assertEquals("2", historicalScoreData.displayedScore)
                        Assert.assertEquals(4400, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[3].let { historicalScoreData ->
                        Assert.assertEquals(3, historicalScoreData.score)
                        Assert.assertEquals("3", historicalScoreData.displayedScore)
                        Assert.assertEquals(5655, historicalScoreData.time)
                    }
                    historicalScoreGroupData.primaryScoreList[4].let { historicalScoreData ->
                        Assert.assertEquals(4, historicalScoreData.score)
                        Assert.assertEquals("4", historicalScoreData.displayedScore)
                        Assert.assertEquals(9800, historicalScoreData.time)
                    }
                    Assert.assertTrue(historicalScoreGroupData.secondaryScoreList.isEmpty())
                }
            }
            historicalScoreboardData.historicalIntervalMap[1]?.let { historicalIntervalData ->
                Assert.assertEquals(HistoricalIntervalRangeData.Infinite, historicalIntervalData.range)
                Assert.assertEquals(IntervalLabelData.DefaultSport(SportType.SPIKEBALL, 1), historicalIntervalData.intervalLabel)
                Assert.assertEquals(IntervalLabelData.Type.SPORT_TYPE.name, historicalIntervalData.intervalLabel.type)
                Assert.assertEquals(1, historicalIntervalData.intervalLabel.index)
                Assert.assertTrue(historicalIntervalData.historicalScoreGroupList.isEmpty())
            }

        }
    }

}

