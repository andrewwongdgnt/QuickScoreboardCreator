package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
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

        sut.toTimeData(2000).let {
            Assert.assertEquals(0, it.minute)
            Assert.assertEquals(2, it.second)
            Assert.assertEquals(0, it.centiSecond)
        }

        sut.fromTimeData(TimeData(0,2,0)).let {
            Assert.assertEquals(2000, it)
        }

    }

    @Test
    fun test7_2Seconds() {

        sut.toTimeData(7211).let {
            Assert.assertEquals(0, it.minute)
            Assert.assertEquals(7, it.second)
            Assert.assertEquals(2, it.centiSecond)
        }

        sut.fromTimeData(TimeData(0,7,2)).let {
            Assert.assertEquals(7200, it)
        }

    }

    @Test
    fun test1min18_2Seconds() {

        sut.toTimeData(78250).let {
            Assert.assertEquals(1, it.minute)
            Assert.assertEquals(18, it.second)
            Assert.assertEquals(2, it.centiSecond)
        }

        sut.fromTimeData(TimeData(1,18,2)).let {
            Assert.assertEquals(78200, it)
        }

    }

}