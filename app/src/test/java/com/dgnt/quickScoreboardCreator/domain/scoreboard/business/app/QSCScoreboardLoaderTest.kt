package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app

import com.dgnt.quickScoreboardCreator.common.util.GsonProvider
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreRuleConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreRuleType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
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
        Assert.assertTrue(exampleDefault.scoreCarriesOver)
        Assert.assertEquals(4, exampleDefault.intervalList.size)
        val scoreInfo1 = exampleDefault.intervalList[0].scoreInfo
        Assert.assertEquals(ScoreRuleConfig(ScoreRuleType.DEUCE_ADVANTAGE, 3),scoreInfo1.scoreRule)
        Assert.assertEquals("0",scoreInfo1.scoreMapping?.get("0"))
        Assert.assertEquals("15",scoreInfo1.scoreMapping?.get("1"))
        Assert.assertEquals("30",scoreInfo1.scoreMapping?.get("2"))
        Assert.assertEquals("40",scoreInfo1.scoreMapping?.get("3"))
    }

    @Test
    fun testLoadCustom() {
        val exampleCustom = javaClass.classLoader?.getResourceAsStream("example_custom.json")?.let { ins ->
            sut(ins)
        } as CustomScoreboardConfig

        Assert.assertEquals("Spike Ball", exampleCustom.title)
        Assert.assertEquals("Covid sport", exampleCustom.description)
        Assert.assertEquals("Game", exampleCustom.intervalLabel)
        Assert.assertFalse(exampleCustom.scoreCarriesOver)
        Assert.assertEquals(3, exampleCustom.intervalList.size)

    }


}