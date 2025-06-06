package com.dgnt.quickScoreboardCreator.feature.team.domain.usecase



import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class CategorizeTeamUseCaseTest {

    @InjectMockKs
    lateinit var sut: CategorizeTeamUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test2CategoriesWith2Entities() {

        sut(
            listOf(
                TeamModel(id = 1, title = "Team A", description = "Team A Description", icon = TeamIcon.ALIEN),
                TeamModel(id = 2, title = "Solo A", description = "Solo A Description", icon = TeamIcon.ALIEN),
            )
        ).let {
            Assert.assertEquals(2, it.size)
            Assert.assertEquals("S", it[0].title)
            Assert.assertEquals(1, it[0].teamItemDataList.size)
            Assert.assertEquals(2, it[0].teamItemDataList[0].id)
            Assert.assertEquals("T", it[1].title)
            Assert.assertEquals(1, it[1].teamItemDataList.size)
            Assert.assertEquals(1, it[1].teamItemDataList[0].id)
        }


    }
    @Test
    fun test2CategoriesWith4Entities() {

        sut(
            listOf(
                TeamModel(id = 1, title = "Solo B", description = "Solo B Description", icon = TeamIcon.ALIEN),
                TeamModel(id = 2, title = "Team A", description = "Team A Description", icon = TeamIcon.ALIEN),
                TeamModel(id = 3, title = "solo A", description = "Solo A Description", icon = TeamIcon.ALIEN),
                TeamModel(id = 4, title = "Team B", description = "Team B Description", icon = TeamIcon.ALIEN),
            )
        ).let {
            Assert.assertEquals(2, it.size)
            Assert.assertEquals("S", it[0].title)
            Assert.assertEquals(2, it[0].teamItemDataList.size)
            Assert.assertEquals(3, it[0].teamItemDataList[0].id)
            Assert.assertEquals(1, it[0].teamItemDataList[1].id)
            Assert.assertEquals("T", it[1].title)
            Assert.assertEquals(2, it[1].teamItemDataList.size)
            Assert.assertEquals(2, it[1].teamItemDataList[0].id)
            Assert.assertEquals(4, it[1].teamItemDataList[1].id)
        }


    }


}