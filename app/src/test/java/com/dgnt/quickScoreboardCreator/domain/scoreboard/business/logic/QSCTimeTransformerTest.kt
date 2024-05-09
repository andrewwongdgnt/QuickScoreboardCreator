package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class QSCTimeTransformerTest {

    @InjectMockKs
    lateinit var sut: QSCTimeTransformer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test2Seconds() {

        sut(2000).let {
            Assert.assertEquals(0, it.minute)
            Assert.assertEquals(2, it.second)
            Assert.assertEquals(0, it.centiSecond)
        }

    }

    @Test
    fun test7_2Seconds() {

        sut(7211).let {
            Assert.assertEquals(0, it.minute)
            Assert.assertEquals(7, it.second)
            Assert.assertEquals(2, it.centiSecond)
        }

    }

    @Test
    fun test1min18_2Seconds() {

        sut(78250).let {
            Assert.assertEquals(1, it.minute)
            Assert.assertEquals(18, it.second)
            Assert.assertEquals(2, it.centiSecond)
        }

    }

}