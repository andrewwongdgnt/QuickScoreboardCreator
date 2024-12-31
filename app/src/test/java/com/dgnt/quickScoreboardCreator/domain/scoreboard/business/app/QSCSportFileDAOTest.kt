package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app

import com.dgnt.quickScoreboardCreator.feature.sport.data.filedao.SportFileDao
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.CustomSportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.DefaultSportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.ScoreRuleFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.ScoreRuleType
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.WinRuleType
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportType
import com.dgnt.quickScoreboardCreator.core.serializer.QSCSerializer
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCSportFileDAOTest {

    private lateinit var sut: SportFileDao

    @Before
    fun setup() {
        sut = SportFileDao(QSCSerializer())
    }

    @Test
    fun testLoadDefault() {
        val exampleDefault = javaClass.classLoader?.getResourceAsStream("example_default.json")?.let { ins ->
            sut.import(ins)
        } as DefaultSportFileDTO

        Assert.assertEquals(SportType.BASKETBALL, exampleDefault.sportType)
        Assert.assertEquals(WinRuleType.FINAL, exampleDefault.winRuleType)
        Assert.assertEquals(6, exampleDefault.intervalList.size)
        (0 until 4).forEach {
            val intervalInfo = exampleDefault.intervalList[it]
            val scoreInfo = intervalInfo.scoreInfo
            Assert.assertEquals(ScoreRuleFileDTO(ScoreRuleType.DEUCE_ADVANTAGE, 3), scoreInfo.scoreRule)
            Assert.assertEquals("0", scoreInfo.scoreMapping?.get("0"))
            Assert.assertEquals("15", scoreInfo.scoreMapping?.get("1"))
            Assert.assertEquals("30", scoreInfo.scoreMapping?.get("2"))
            Assert.assertEquals("40", scoreInfo.scoreMapping?.get("3"))
            Assert.assertTrue(intervalInfo.intervalData.increasing)
        }

        (4 until 6).forEach {
            val intervalInfo = exampleDefault.intervalList[it]
            val scoreInfo = intervalInfo.scoreInfo
            Assert.assertEquals(ScoreRuleFileDTO(ScoreRuleType.NO_RULE, 0), scoreInfo.scoreRule)
            Assert.assertNull(scoreInfo.scoreMapping)
            Assert.assertFalse(intervalInfo.intervalData.increasing)
        }

    }

    @Test
    fun testLoadCustom() {
        val exampleCustom = javaClass.classLoader?.getResourceAsStream("example_custom.json")?.let { ins ->
            sut.import(ins)
        } as CustomSportFileDTO

        Assert.assertEquals("Spike Ball", exampleCustom.title)
        Assert.assertEquals("Covid sport", exampleCustom.description)
        Assert.assertEquals("Game", exampleCustom.intervalLabel)
        Assert.assertEquals(WinRuleType.COUNT, exampleCustom.winRuleType)
        Assert.assertEquals(2, exampleCustom.intervalList.size)
        (0 until 2).forEach {
            val intervalInfo = exampleCustom.intervalList[it]
            Assert.assertEquals("Fouls", intervalInfo.scoreInfo.secondaryScoreLabel)
        }

    }


}