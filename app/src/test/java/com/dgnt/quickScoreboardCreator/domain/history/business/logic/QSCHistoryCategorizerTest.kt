package com.dgnt.quickScoreboardCreator.core.domain.history.business.logic


import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCHistoryCategorizerTest {

    @InjectMockKs
    lateinit var sut: QSCHistoryCategorizer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test2CategoriesWith2Entities() {

        sut(
            listOf(
                HistoryEntity(id = 1, title = "Basketball", description = "34", icon = ScoreboardIcon.BASKETBALL, lastModified = DateTime(2024, 1, 15, 12, 10), createdAt = DateTime(2024, 1, 15, 12, 10), historicalScoreboard = HistoricalScoreboardData(mapOf()), temporary = false),
                HistoryEntity(id = 2, title = "Spikeball", description = "34", icon = ScoreboardIcon.BASKETBALL, lastModified = DateTime(2024, 2, 14, 11, 0), createdAt = DateTime(2024, 2, 14, 11, 0), historicalScoreboard = HistoricalScoreboardData(mapOf()), temporary = false),
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
                HistoryEntity(id = 1, title = "Basketball", description = "34", icon = ScoreboardIcon.BASKETBALL, lastModified = DateTime(2024, 1, 4, 12, 10), createdAt = DateTime(2024, 1, 4, 12, 10), historicalScoreboard = HistoricalScoreboardData(mapOf()), temporary = false),
                HistoryEntity(id = 2, title = "Spikeball", description = "34", icon = ScoreboardIcon.BASKETBALL, lastModified = DateTime(2024, 2, 16, 11, 0), createdAt = DateTime(2024, 2, 16, 11, 0), historicalScoreboard = HistoricalScoreboardData(mapOf()), temporary = false),
                HistoryEntity(id = 3, title = "Volleyball", description = "34", icon = ScoreboardIcon.VOLLEYBALL, lastModified = DateTime(2024, 1, 19, 12, 10), createdAt = DateTime(2024, 1, 19, 12, 10), historicalScoreboard = HistoricalScoreboardData(mapOf()), temporary = false),
                HistoryEntity(id = 4, title = "Tennis", description = "34", icon = ScoreboardIcon.TENNIS, lastModified = DateTime(2024, 2, 1, 5, 13),createdAt = DateTime(2024, 2, 1, 5, 13), historicalScoreboard = HistoricalScoreboardData(mapOf()), temporary = false),
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