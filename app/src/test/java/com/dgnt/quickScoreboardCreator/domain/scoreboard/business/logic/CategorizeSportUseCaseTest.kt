package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.QSCCategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardType
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIdentifier
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class CategorizeSportUseCaseTest {

    @InjectMockKs
    private lateinit var sut: QSCCategorizeSportUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testCategories() {

        sut(
            listOf(ScoreboardType.BASKETBALL, ScoreboardType.HOCKEY, ScoreboardType.SPIKEBALL),
            listOf(
                ScoreboardEntity(id = 1, title = "gg", description = "GG Desc", winRule = WinRule.Count, icon = ScoreboardIcon.TENNIS, intervalLabel = "game"),
                ScoreboardEntity(id = 2, title = "ZZ", description = "GG Desc", winRule = WinRule.Count, icon = ScoreboardIcon.TENNIS, intervalLabel = "game"),
                ScoreboardEntity(id = 3, title = "a", description = "GG Desc", winRule = WinRule.Count, icon = ScoreboardIcon.TENNIS, intervalLabel = "game"),
                ScoreboardEntity(id = 4, title = "RRR", description = "GG Desc", winRule = WinRule.Count, icon = ScoreboardIcon.TENNIS, intervalLabel = "game"),
                ScoreboardEntity(id = 5, title = "CC", description = "GG Desc", winRule = WinRule.Count, icon = ScoreboardIcon.TENNIS, intervalLabel = "game"),
            )
        ).let {
            val defaults = it.first
            Assert.assertEquals(3, defaults.scoreboardTypeList.size)
            Assert.assertEquals(ScoreboardType.BASKETBALL, defaults.scoreboardTypeList[0])
            Assert.assertEquals(ScoreboardType.HOCKEY, defaults.scoreboardTypeList[1])
            Assert.assertEquals(ScoreboardType.SPIKEBALL, defaults.scoreboardTypeList[2])

            val customs = it.second
            Assert.assertEquals(5, customs.scoreboardItemDataList.size)
            Assert.assertEquals(3, (customs.scoreboardItemDataList[0].scoreboardIdentifier as ScoreboardIdentifier.Custom).id)
            Assert.assertEquals(5, (customs.scoreboardItemDataList[1].scoreboardIdentifier as ScoreboardIdentifier.Custom).id)
            Assert.assertEquals(1, (customs.scoreboardItemDataList[2].scoreboardIdentifier as ScoreboardIdentifier.Custom).id)
            Assert.assertEquals(4, (customs.scoreboardItemDataList[3].scoreboardIdentifier as ScoreboardIdentifier.Custom).id)
            Assert.assertEquals(2, (customs.scoreboardItemDataList[4].scoreboardIdentifier as ScoreboardIdentifier.Custom).id)
        }


    }


}