package com.dgnt.quickScoreboardCreator

import com.dgnt.quickScoreboardCreator.domain.scoreboard.loader.QSCScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardTemplate
import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.common.util.GsonProvider
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
    fun testLoadTemplate() {
        val exampleTemplate = javaClass.classLoader?.getResourceAsStream("example_template.json")?.let { ins ->
            sut.load(ins)
        } as ScoreboardTemplate

        Assert.assertEquals(ScoreboardType.BASKETBALL, exampleTemplate.scoreboardType)
        Assert.assertTrue(exampleTemplate.scoreCarriesOver)
        Assert.assertEquals(4, exampleTemplate.intervalList.size)
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