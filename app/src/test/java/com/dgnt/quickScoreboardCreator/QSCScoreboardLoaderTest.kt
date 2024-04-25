package com.dgnt.quickScoreboardCreator

import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.loader.QSCScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.common.util.GsonProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCScoreboardLoaderTest {

    private lateinit var sut: com.dgnt.quickScoreboardCreator.domain.scoreboard.business.loader.QSCScoreboardLoader

    @Before
    fun setup() {
        sut = com.dgnt.quickScoreboardCreator.domain.scoreboard.business.loader.QSCScoreboardLoader(GsonProvider.gson)
    }

    @Test
    fun testLoadDefault() {
        val exampleDefault = javaClass.classLoader?.getResourceAsStream("example_default.json")?.let { ins ->
            sut.load(ins)
        } as DefaultScoreboardConfig

        Assert.assertEquals(ScoreboardType.BASKETBALL, exampleDefault.scoreboardType)
        Assert.assertTrue(exampleDefault.scoreCarriesOver)
        Assert.assertEquals(4, exampleDefault.intervalList.size)
    }

    @Test
    fun testLoadCustom() {
        val exampleCustom = javaClass.classLoader?.getResourceAsStream("example_custom.json")?.let { ins ->
            sut.load(ins)
        } as CustomScoreboardConfig

        Assert.assertEquals("Spike Ball", exampleCustom.name)
        Assert.assertEquals("Covid sport", exampleCustom.description)
        Assert.assertFalse(exampleCustom.scoreCarriesOver)
        Assert.assertEquals(3, exampleCustom.intervalList.size)
    }


}