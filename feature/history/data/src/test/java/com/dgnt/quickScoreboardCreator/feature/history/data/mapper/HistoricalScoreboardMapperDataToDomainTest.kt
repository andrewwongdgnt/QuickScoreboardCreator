package com.dgnt.quickScoreboardCreator.feature.history.data.mapper


import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreGroupData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HistoricalScoreboardMapperDataToDomainTest {


    private lateinit var sut: HistoricalScoreboardMapperDataToDomain

    @Before
    fun setup() {
        sut = HistoricalScoreboardMapperDataToDomain()
    }


    @Test
    fun testMapping() {

        sut.map(
            HistoricalScoreboardData(
                mapOf(
                    0 to HistoricalIntervalData(
                        range = HistoricalIntervalRangeData.CountDown(72000),
                        IntervalLabelData.Custom("Quarter", 0),
                        mapOf(
                            0 to HistoricalScoreGroupData(
                                teamLabel = TeamLabelData.Custom("DGNT", TeamIcon.AXE),
                                primaryScoreList = listOf(
                                    HistoricalScoreData(0, "0", 720000),
                                    HistoricalScoreData(1, "1", 660000),
                                    HistoricalScoreData(2, "2", 630000),
                                    HistoricalScoreData(3, "3", 480000),
                                    HistoricalScoreData(4, "4", 330000),
                                    HistoricalScoreData(7, "7", 300000),
                                ),
                                secondaryScoreList = listOf(
                                    HistoricalScoreData(1, "1", 720000)
                                )
                            ),
                            1 to HistoricalScoreGroupData(
                                teamLabel = TeamLabelData.None,
                                primaryScoreList = listOf(
                                    HistoricalScoreData(0, "0", 0),
                                    HistoricalScoreData(1, "1", 2000),
                                    HistoricalScoreData(2, "2", 4400),
                                    HistoricalScoreData(3, "3", 5655),
                                    HistoricalScoreData(4, "4", 9800),
                                ),
                                secondaryScoreList = listOf()
                            )
                        )
                    ),
                    1 to HistoricalIntervalData(
                        range = HistoricalIntervalRangeData.Infinite,
                        intervalLabel = IntervalLabelData.DefaultSport(SportType.SPIKEBALL, 1),
                        historicalScoreGroupList = mapOf()
                    )
                )
            )
        ).let { historicalScoreboard ->
            Assert.assertEquals(2, historicalScoreboard.historicalIntervalMap.size)
            historicalScoreboard.historicalIntervalMap[0]?.let { historicalInterval ->
                Assert.assertEquals(HistoricalIntervalRange.CountDown(72000), historicalInterval.range)
                Assert.assertEquals("Quarter", (historicalInterval.intervalLabel as? IntervalLabel.Custom)?.value)
                Assert.assertEquals(0, historicalInterval.intervalLabel.index)
                Assert.assertEquals(2, historicalInterval.historicalScoreGroupList.size)
                historicalInterval.historicalScoreGroupList[0]?.let { historicalScoreGroup ->
                    Assert.assertEquals(TeamLabel.Custom("DGNT", TeamIcon.AXE), historicalScoreGroup.teamLabel)
                    Assert.assertEquals(6, historicalScoreGroup.primaryScoreList.size)
                    historicalScoreGroup.primaryScoreList[0].let { historicalScoreData ->
                        Assert.assertEquals(0, historicalScoreData.score)
                        Assert.assertEquals("0", historicalScoreData.displayedScore)
                        Assert.assertEquals(720000, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[1].let { historicalScoreData ->
                        Assert.assertEquals(1, historicalScoreData.score)
                        Assert.assertEquals("1", historicalScoreData.displayedScore)
                        Assert.assertEquals(660000, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[2].let { historicalScoreData ->
                        Assert.assertEquals(2, historicalScoreData.score)
                        Assert.assertEquals("2", historicalScoreData.displayedScore)
                        Assert.assertEquals(630000, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[3].let { historicalScoreData ->
                        Assert.assertEquals(3, historicalScoreData.score)
                        Assert.assertEquals("3", historicalScoreData.displayedScore)
                        Assert.assertEquals(480000, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[4].let { historicalScoreData ->
                        Assert.assertEquals(4, historicalScoreData.score)
                        Assert.assertEquals("4", historicalScoreData.displayedScore)
                        Assert.assertEquals(330000, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[5].let { historicalScoreData ->
                        Assert.assertEquals(7, historicalScoreData.score)
                        Assert.assertEquals("7", historicalScoreData.displayedScore)
                        Assert.assertEquals(300000, historicalScoreData.time)
                    }
                    Assert.assertEquals(1, historicalScoreGroup.secondaryScoreList.size)
                    historicalScoreGroup.secondaryScoreList[0].let { historicalScoreData ->
                        Assert.assertEquals(1, historicalScoreData.score)
                        Assert.assertEquals("1", historicalScoreData.displayedScore)
                        Assert.assertEquals(720000, historicalScoreData.time)
                    }

                }
                historicalInterval.historicalScoreGroupList[1]?.let { historicalScoreGroup ->
                    Assert.assertEquals(TeamLabel.None, historicalScoreGroup.teamLabel)
                    Assert.assertEquals(5, historicalScoreGroup.primaryScoreList.size)
                    historicalScoreGroup.primaryScoreList[0].let { historicalScoreData ->
                        Assert.assertEquals(0, historicalScoreData.score)
                        Assert.assertEquals("0", historicalScoreData.displayedScore)
                        Assert.assertEquals(0, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[1].let { historicalScoreData ->
                        Assert.assertEquals(1, historicalScoreData.score)
                        Assert.assertEquals("1", historicalScoreData.displayedScore)
                        Assert.assertEquals(2000, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[2].let { historicalScoreData ->
                        Assert.assertEquals(2, historicalScoreData.score)
                        Assert.assertEquals("2", historicalScoreData.displayedScore)
                        Assert.assertEquals(4400, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[3].let { historicalScoreData ->
                        Assert.assertEquals(3, historicalScoreData.score)
                        Assert.assertEquals("3", historicalScoreData.displayedScore)
                        Assert.assertEquals(5655, historicalScoreData.time)
                    }
                    historicalScoreGroup.primaryScoreList[4].let { historicalScoreData ->
                        Assert.assertEquals(4, historicalScoreData.score)
                        Assert.assertEquals("4", historicalScoreData.displayedScore)
                        Assert.assertEquals(9800, historicalScoreData.time)
                    }
                    Assert.assertTrue(historicalScoreGroup.secondaryScoreList.isEmpty())
                }
            }
            historicalScoreboard.historicalIntervalMap[1]?.let { historicalInterval ->
                Assert.assertEquals(HistoricalIntervalRange.Infinite, historicalInterval.range)
                Assert.assertEquals(SportType.SPIKEBALL, (historicalInterval.intervalLabel as? IntervalLabel.DefaultSport)?.sportType)
                Assert.assertEquals(1, historicalInterval.intervalLabel.index)
                Assert.assertTrue(historicalInterval.historicalScoreGroupList.isEmpty())
            }

        }

    }
}