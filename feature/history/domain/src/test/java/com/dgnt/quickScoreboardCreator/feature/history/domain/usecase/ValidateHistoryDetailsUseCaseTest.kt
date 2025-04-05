package com.dgnt.quickScoreboardCreator.feature.history.domain.usecase


import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class ValidateHistoryDetailsUseCaseTest {

    @InjectMockKs
    lateinit var sut: ValidateHistoryDetailsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testNonBlankAsValid() {
        Assert.assertTrue(sut("Something"))
        Assert.assertTrue(sut("Something "))
        Assert.assertTrue(sut(" Something"))
        Assert.assertTrue(sut(" Something "))
        Assert.assertTrue(sut("Some  thing"))
    }

    @Test
    fun testWhiteSpaceAsInvalid() {
        Assert.assertFalse(sut(" "))
        Assert.assertFalse(sut("   "))
    }

    @Test
    fun testNothingAsInvalid() {
        Assert.assertFalse(sut(""))
    }
}