package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCScoreboardCategorizerTest {

    @InjectMockKs
    private lateinit var sut: QSCScoreboardCategorizer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testCategories() {

        sut(
            listOf(ScoreboardType.BASKETBALL, ScoreboardType.HOCKEY, ScoreboardType.SPIKEBALL),
            listOf(
                ScoreboardEntity(id = 1, title = "gg", description = "GG Desc", icon = ScoreboardIcon.TENNIS),
                ScoreboardEntity(id = 2, title = "ZZ", description = "GG Desc", icon = ScoreboardIcon.TENNIS),
                ScoreboardEntity(id = 3, title = "a", description = "GG Desc", icon = ScoreboardIcon.TENNIS),
                ScoreboardEntity(id = 4, title = "RRR", description = "GG Desc", icon = ScoreboardIcon.TENNIS),
                ScoreboardEntity(id = 5, title = "CC", description = "GG Desc", icon = ScoreboardIcon.TENNIS),
            )
        ).let {
            val defaults = it.first
            Assert.assertEquals(3, defaults.scoreboardTypeList.size)
            Assert.assertEquals(ScoreboardType.BASKETBALL, defaults.scoreboardTypeList[0])
            Assert.assertEquals(ScoreboardType.HOCKEY, defaults.scoreboardTypeList[1])
            Assert.assertEquals(ScoreboardType.SPIKEBALL, defaults.scoreboardTypeList[2])

            val customs = it.second
            Assert.assertEquals(5, customs.scoreboardItemDataList.size)
            Assert.assertEquals(3, customs.scoreboardItemDataList[0].id)
            Assert.assertEquals(5, customs.scoreboardItemDataList[1].id)
            Assert.assertEquals(1, customs.scoreboardItemDataList[2].id)
            Assert.assertEquals(4, customs.scoreboardItemDataList[3].id)
            Assert.assertEquals(2, customs.scoreboardItemDataList[4].id)
        }


    }



}