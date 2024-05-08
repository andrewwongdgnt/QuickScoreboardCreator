package com.dgnt.quickScoreboardCreator.domain.team.business.logic


import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCTeamCategorizerTest {

    @InjectMockKs
    lateinit var sut: QSCTeamCategorizer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test2CategoriesWith2Entities() {

        sut(
            listOf(
                TeamEntity(id = 1, title = "Team A", description = "Team A Description", teamIcon = TeamIcon.ALIEN),
                TeamEntity(id = 2, title = "Solo A", description = "Solo A Description", teamIcon = TeamIcon.ALIEN),
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
                TeamEntity(id = 1, title = "Solo B", description = "Solo B Description", teamIcon = TeamIcon.ALIEN),
                TeamEntity(id = 2, title = "Team A", description = "Team A Description", teamIcon = TeamIcon.ALIEN),
                TeamEntity(id = 3, title = "Solo A", description = "Solo A Description", teamIcon = TeamIcon.ALIEN),
                TeamEntity(id = 4, title = "Team B", description = "Team B Description", teamIcon = TeamIcon.ALIEN),
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