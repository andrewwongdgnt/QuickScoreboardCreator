package com.dgnt.quickScoreboardCreator

import com.dgnt.quickScoreboardCreator.business.scoreboard.loader.QSBScoreboardLoader
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Before
import org.junit.Test


class QSBScoreboardLoaderTest {

    @InjectMockKs
    lateinit var sut: QSBScoreboardLoader

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testLoad() {


    }



}