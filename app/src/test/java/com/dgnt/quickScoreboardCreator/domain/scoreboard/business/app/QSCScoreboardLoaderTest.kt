package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app

import com.dgnt.quickScoreboardCreator.core.gson.GsonProvider
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreRuleConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreRuleType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.WinRuleType
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCScoreboardLoaderTest {

    private lateinit var sut: QSCScoreboardLoader

    @Before
    fun setup() {
        sut = QSCScoreboardLoader(GsonProvider.gson)
    }

    @Test
    fun testLoadDefault() {
        val exampleDefault = javaClass.classLoader?.getResourceAsStream("example_default.json")?.let { ins ->
            sut(ins)
        } as DefaultScoreboardConfig

        Assert.assertEquals(ScoreboardType.BASKETBALL, exampleDefault.scoreboardType)
        Assert.assertEquals(WinRuleType.FINAL, exampleDefault.winRuleType)
        Assert.assertEquals(6, exampleDefault.intervalList.size)
        (0 until 4).forEach {
            val intervalInfo = exampleDefault.intervalList[it]
            val scoreInfo = intervalInfo.scoreInfo
            Assert.assertEquals(ScoreRuleConfig(ScoreRuleType.DEUCE_ADVANTAGE, 3), scoreInfo.scoreRule)
            Assert.assertEquals("0", scoreInfo.scoreMapping?.get("0"))
            Assert.assertEquals("15", scoreInfo.scoreMapping?.get("1"))
            Assert.assertEquals("30", scoreInfo.scoreMapping?.get("2"))
            Assert.assertEquals("40", scoreInfo.scoreMapping?.get("3"))
            Assert.assertTrue(intervalInfo.intervalData.increasing)
        }

        (4 until 6).forEach {
            val intervalInfo = exampleDefault.intervalList[it]
            val scoreInfo = intervalInfo.scoreInfo
            Assert.assertEquals(ScoreRuleConfig(ScoreRuleType.NO_RULE, 0), scoreInfo.scoreRule)
            Assert.assertNull(scoreInfo.scoreMapping)
            Assert.assertFalse(intervalInfo.intervalData.increasing)
        }

    }

    @Test
    fun testLoadCustom() {
        val exampleCustom = javaClass.classLoader?.getResourceAsStream("example_custom.json")?.let { ins ->
            sut(ins)
        } as CustomScoreboardConfig

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