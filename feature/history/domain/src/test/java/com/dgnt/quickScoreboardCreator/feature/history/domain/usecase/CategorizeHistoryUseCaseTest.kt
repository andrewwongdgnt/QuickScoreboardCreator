package com.dgnt.quickScoreboardCreator.feature.history.domain.usecase


import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class CategorizeHistoryUseCaseTest {

    @InjectMockKs
    lateinit var sut: CategorizeHistoryUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test2CategoriesWith2Entities() {

        sut(
            listOf(
                HistoryModel(id = 1, title = "Basketball", description = "34", icon = SportIcon.BASKETBALL, lastModified = DateTime(2024, 1, 15, 12, 10), createdAt = DateTime(2024, 1, 15, 12, 10), historicalScoreboard = HistoricalScoreboard(mapOf()), temporary = false),
                HistoryModel(id = 2, title = "Spikeball", description = "34", icon = SportIcon.BASKETBALL, lastModified = DateTime(2024, 2, 14, 11, 0), createdAt = DateTime(2024, 2, 14, 11, 0), historicalScoreboard = HistoricalScoreboard(mapOf()), temporary = false),
            )
        ).let {
            Assert.assertEquals(2, it.size)
            Assert.assertEquals(DateTime(2024, 2, 1, 0, 0), it[0].dateTime)
            Assert.assertEquals(1, it[0].historyItemDataList.size)
            Assert.assertEquals(2, it[0].historyItemDataList[0].id)
            Assert.assertEquals(DateTime(2024, 1, 1, 0, 0), it[1].dateTime)
            Assert.assertEquals(1, it[1].historyItemDataList.size)
            Assert.assertEquals(1, it[1].historyItemDataList[0].id)
        }


    }

    @Test
    fun test2CategoriesWith4Entities() {

        sut(
            listOf(
                HistoryModel(id = 1, title = "Basketball", description = "34", icon = SportIcon.BASKETBALL, lastModified = DateTime(2024, 1, 4, 12, 10), createdAt = DateTime(2024, 1, 4, 12, 10), historicalScoreboard = HistoricalScoreboard(mapOf()), temporary = false),
                HistoryModel(id = 2, title = "Spikeball", description = "34", icon = SportIcon.BASKETBALL, lastModified = DateTime(2024, 2, 16, 11, 0), createdAt = DateTime(2024, 2, 16, 11, 0), historicalScoreboard = HistoricalScoreboard(mapOf()), temporary = false),
                HistoryModel(id = 3, title = "Volleyball", description = "34", icon = SportIcon.VOLLEYBALL, lastModified = DateTime(2024, 1, 19, 12, 10), createdAt = DateTime(2024, 1, 19, 12, 10), historicalScoreboard = HistoricalScoreboard(mapOf()), temporary = false),
                HistoryModel(id = 4, title = "Tennis", description = "34", icon = SportIcon.TENNIS, lastModified = DateTime(2024, 2, 1, 5, 13),createdAt = DateTime(2024, 2, 1, 5, 13), historicalScoreboard = HistoricalScoreboard(mapOf()), temporary = false),
            )
        ).let {
            Assert.assertEquals(2, it.size)
            Assert.assertEquals(DateTime(2024, 2, 1, 0, 0), it[0].dateTime)
            Assert.assertEquals(2, it[0].historyItemDataList.size)
            Assert.assertEquals(2, it[0].historyItemDataList[0].id)
            Assert.assertEquals(4, it[0].historyItemDataList[1].id)
            Assert.assertEquals(DateTime(2024, 1, 1, 0, 0), it[1].dateTime)
            Assert.assertEquals(2, it[1].historyItemDataList.size)
            Assert.assertEquals(3, it[1].historyItemDataList[0].id)
            Assert.assertEquals(1, it[1].historyItemDataList[1].id)
        }


    }


}