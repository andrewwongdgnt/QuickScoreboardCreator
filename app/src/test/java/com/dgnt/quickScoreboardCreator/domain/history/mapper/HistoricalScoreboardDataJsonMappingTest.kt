package com.dgnt.quickScoreboardCreator.domain.history.mapper

import com.dgnt.quickScoreboardCreator.core.gson.GsonProvider
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalIntervalData
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreData
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreGroupData
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.data.IntervalLabelData
import com.dgnt.quickScoreboardCreator.data.history.data.TeamLabelData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
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
                )
            )
        )
    }

    @Test
    fun testMapping() {
        GsonProvider.gson.toJson(historicalScoreboardData).let {
            //TODO deserialize and test output
        }
    }
}