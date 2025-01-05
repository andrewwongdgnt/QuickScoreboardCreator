package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase



import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class CategorizeSportUseCaseTest {

    @InjectMockKs
    private lateinit var sut: CategorizeSportUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testCategories() {

        sut(
            listOf(SportType.BASKETBALL, SportType.HOCKEY, SportType.SPIKEBALL),
            listOf(
                SportModel(sportIdentifier = SportIdentifier.Custom(1), title = "gg", description = "GG Desc", winRule = WinRule.Count, icon = SportIcon.TENNIS, intervalLabel = "game", intervalList = listOf()),
                SportModel(sportIdentifier = SportIdentifier.Custom(2), title = "ZZ", description = "GG Desc", winRule = WinRule.Count, icon = SportIcon.TENNIS, intervalLabel = "game", intervalList = listOf()),
                SportModel(sportIdentifier = SportIdentifier.Custom(3), title = "a", description = "GG Desc", winRule = WinRule.Count, icon = SportIcon.TENNIS, intervalLabel = "game", intervalList = listOf()),
                SportModel(sportIdentifier = SportIdentifier.Custom(4), title = "RRR", description = "GG Desc", winRule = WinRule.Count, icon = SportIcon.TENNIS, intervalLabel = "game", intervalList = listOf()),
                SportModel(sportIdentifier = SportIdentifier.Custom(5), title = "CC", description = "GG Desc", winRule = WinRule.Count, icon = SportIcon.TENNIS, intervalLabel = "game", intervalList = listOf()),
            )
        ).let {
            val defaults = it.first
            Assert.assertEquals(3, defaults.sportTypeList.size)
            Assert.assertEquals(SportType.BASKETBALL, defaults.sportTypeList[0])
            Assert.assertEquals(SportType.HOCKEY, defaults.sportTypeList[1])
            Assert.assertEquals(SportType.SPIKEBALL, defaults.sportTypeList[2])

            val customs = it.second
            Assert.assertEquals(5, customs.sportListItemList.size)
            Assert.assertEquals(3, (customs.sportListItemList[0].sportIdentifier as SportIdentifier.Custom).id)
            Assert.assertEquals(5, (customs.sportListItemList[1].sportIdentifier as SportIdentifier.Custom).id)
            Assert.assertEquals(1, (customs.sportListItemList[2].sportIdentifier as SportIdentifier.Custom).id)
            Assert.assertEquals(4, (customs.sportListItemList[3].sportIdentifier as SportIdentifier.Custom).id)
            Assert.assertEquals(2, (customs.sportListItemList[4].sportIdentifier as SportIdentifier.Custom).id)
        }


    }


}