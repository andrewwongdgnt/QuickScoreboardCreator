package com.dgnt.quickScoreboardCreator.core.domain.history.mapper

import com.dgnt.quickScoreboardCreator.core.gson.GsonProvider
import com.dgnt.quickScoreboardCreator.core.gson.fromJson
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalIntervalData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreGroupData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.IntervalLabelData
import com.dgnt.quickScoreboardCreator.data.history.entity.TeamLabelData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HistoricalScoreboardDataJsonMappingTest {

    private lateinit var historicalScoreboardData: HistoricalScoreboardData

    @Before
    fun setup() {
        historicalScoreboardData = HistoricalScoreboardData(
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
                    intervalLabel = IntervalLabelData.ScoreboardType(ScoreboardType.SPIKEBALL, 1),
                    historicalScoreGroupList = mapOf()
                )
            )
        )
    }

    @Test
    fun testMapping() {
        val serialized = GsonProvider.gson.toJson(historicalScoreboardData)
        GsonProvider.gson.fromJson<HistoricalScoreboardData>(serialized).let { historicalScoreboardData ->
            Assert.assertEquals(2, historicalScoreboardData.historicalIntervalMap.size)
            historicalScoreboardData.historicalIntervalMap[0]?.let { historicalIntervalData ->
                Assert.assertEquals(HistoricalIntervalRangeData.CountDown(72000), historicalIntervalData.range)
                Assert.assertEquals("Quarter", (historicalIntervalData.intervalLabel as? IntervalLabelData.Custom)?.value)
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
                Assert.assertEquals(ScoreboardType.SPIKEBALL, (historicalIntervalData.intervalLabel as? IntervalLabelData.ScoreboardType)?.scoreboardType)
                Assert.assertEquals(IntervalLabelData.Type.SCOREBOARD_TYPE.name, historicalIntervalData.intervalLabel.type)
                Assert.assertEquals(1, historicalIntervalData.intervalLabel.index)
                Assert.assertTrue(historicalIntervalData.historicalScoreGroupList.isEmpty())
            }

        }

    }
}