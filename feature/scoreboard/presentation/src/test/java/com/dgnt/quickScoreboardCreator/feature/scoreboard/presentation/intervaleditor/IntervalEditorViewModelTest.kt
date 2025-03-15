package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.intervaleditor


import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.IntervalUpdated
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.GetSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.TimeConversionUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.rawRes
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class IntervalEditorViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var resources: Resources

    @MockK
    private lateinit var getSportUseCase: GetSportUseCase

    @MockK
    private lateinit var timeConversionUseCase: TimeConversionUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: IntervalEditorViewModel

    private val mockScoreInfo = ScoreInfo(
        ScoreRule.None,
        mapOf(),
        "",
        listOf()
    )

    private val interval1Time = 720000L
    private val interval1TimeData = TimeData(12, 0, 0)
    private val interval2Time = 700000L
    private val interval3Time = 0L

    private val mockIntervalList = listOf(
        Pair(
            mockScoreInfo,
            IntervalData(
                interval1Time,
                interval1Time,
                false
            )
        ),
        Pair(
            mockScoreInfo,
            IntervalData(
                interval2Time,
                interval2Time,
                false
            )
        ),
        Pair(
            mockScoreInfo,
            IntervalData(
                interval3Time,
                interval3Time,
                true
            )
        ),
    )

    private val mockInitialCurrentTimeValue = 665000L
    private val mockInitialCurrentTimeData = TimeData(11, 5, 0)
    private val mockInitialCurrentIntervalIndex = 0

    private fun initSut() {
        sut = IntervalEditorViewModel(
            resources,
            getSportUseCase,
            timeConversionUseCase,
            savedStateHandle,
            uiEventHandler
        )
    }

    private fun initWithSportType() {
        val inputStream = mockk<InputStream>()
        val sportModel = mockk<SportModel>()

        every { savedStateHandle.get<SportIdentifier?>(NavArguments.SPORT_IDENTIFIER) } returns SportIdentifier.Default(SportType.BASKETBALL)
        every { resources.openRawResource(SportType.BASKETBALL.rawRes()) } returns inputStream
        every { getSportUseCase.invoke(inputStream) } returns sportModel
        every { sportModel.intervalList } returns mockIntervalList

        initSut()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        every { savedStateHandle.get<Long>(NavArguments.VALUE) } returns mockInitialCurrentTimeValue
        every { timeConversionUseCase.toTimeData(mockInitialCurrentTimeValue) } returns mockInitialCurrentTimeData
        every { savedStateHandle.get<Int>(NavArguments.INDEX) } returns mockInitialCurrentIntervalIndex
        every { timeConversionUseCase.toTimeData(interval1Time) } returns interval1TimeData
    }

    @Test
    fun testInitializingTime() = runTest {
        initWithSportType()

        sut.state.value.run {
            Assert.assertEquals(mockInitialCurrentTimeData.minute.toString(), minuteString)
            Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), secondString)
        }
    }

    @Test
    fun testInitializingIntervalIndex() = runTest {
        initWithSportType()

        Assert.assertEquals("1", sut.state.value.intervalString)
    }

    @Test
    fun testInitializingADefaultScoreboard() = runTest {
        initWithSportType()
        sut.state.value.run {
            Assert.assertEquals(Label.Resource(R.string.quarter), label)
            Assert.assertTrue(errors.isEmpty())
        }
    }

    @Test
    fun testOnDismiss() = runTest {
        initWithSportType()
        sut.onAction(IntervalEditorAction.Dismiss)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
    }

    @Test
    fun testOnConfirmInitialValues() = runTest {
        initWithSportType()
        sut.onAction(IntervalEditorAction.Confirm)
        verify(exactly = 1) {
            sut.sendUiEvent(IntervalUpdated(mockInitialCurrentTimeValue, mockInitialCurrentIntervalIndex))
        }
    }

    @Test
    fun testOnMinuteChange() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(10, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 1000
        sut.onAction(IntervalEditorAction.MinuteChange("10"))
        sut.state.value.run {
            Assert.assertEquals("10", minuteString)
            Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), secondString)
            Assert.assertEquals("1", intervalString)
            Assert.assertTrue(errors.isEmpty())
        }
    }

    @Test
    fun testOnSecondChange() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(mockInitialCurrentTimeData.minute, 10, mockInitialCurrentTimeData.centiSecond)) } returns 1000
        sut.onAction(IntervalEditorAction.SecondChange("10"))
        sut.state.value.run {
            Assert.assertEquals(mockInitialCurrentTimeData.minute.toString(), minuteString)
            Assert.assertEquals("10", secondString)
            Assert.assertEquals("1", intervalString)
            Assert.assertTrue(errors.isEmpty())
        }
    }

    @Test
    fun testOnIntervalChange() = runTest {
        initWithSportType()
        sut.onAction(IntervalEditorAction.IntervalChange("2"))
        sut.state.value.run {
            Assert.assertEquals(mockInitialCurrentTimeData.minute.toString(), minuteString)
            Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), secondString)
            Assert.assertEquals("2", intervalString)
            Assert.assertTrue(errors.isEmpty())
        }
    }

    @Test
    fun testOnInvalidMinuteChange() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(13, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 740000
        sut.onAction(IntervalEditorAction.MinuteChange("13"))
        sut.state.value.run {
            Assert.assertEquals("13", minuteString)
            Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), secondString)
            Assert.assertEquals("1", intervalString)
            Assert.assertEquals(1, errors.size)
            Assert.assertEquals(IntervalEditorErrorType.Time.Invalid(interval1TimeData.minute, interval1TimeData.second), errors.first())
        }
    }

    @Test
    fun testOnInvalidMinuteChange_Zero() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(0, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 0
        sut.onAction(IntervalEditorAction.MinuteChange("0"))
        sut.state.value.run {
            Assert.assertEquals("0", minuteString)
            Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), secondString)
            Assert.assertEquals("1", intervalString)
            Assert.assertEquals(1, errors.size)
            Assert.assertEquals(IntervalEditorErrorType.Time.Zero, errors.first())
        }
    }

    @Test
    fun testOnInvalidIntervalChange() = runTest {
        initWithSportType()
        sut.onAction(IntervalEditorAction.IntervalChange("313"))
        sut.state.value.run {
            Assert.assertEquals(mockInitialCurrentTimeData.minute.toString(), minuteString)
            Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), secondString)
            Assert.assertEquals("313", intervalString)
            Assert.assertEquals(1, errors.size)
            Assert.assertEquals(IntervalEditorErrorType.Interval.Invalid(3), errors.first())
        }
    }

    @Test
    fun testOnInvalidIntervalAndTimeChange() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(13, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 740000
        sut.onAction(IntervalEditorAction.MinuteChange("13"))
        sut.onAction(IntervalEditorAction.IntervalChange("313"))
        sut.state.value.run {
            Assert.assertEquals(1, errors.size)
            Assert.assertEquals(IntervalEditorErrorType.Interval.Invalid(3), errors.first())
        }
    }

    @Test
    fun testHighMinChangeOnIncreasingTimeInterval() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(13, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 740000
        sut.onAction(IntervalEditorAction.MinuteChange("13"))
        sut.onAction(IntervalEditorAction.IntervalChange("3"))
        Assert.assertTrue(sut.state.value.errors.isEmpty())
    }

    @Test
    fun testEmptyTimeAndIntervalError() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(0, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 123
        sut.onAction(IntervalEditorAction.MinuteChange(""))
        sut.onAction(IntervalEditorAction.IntervalChange(""))
        sut.state.value.run {
            Assert.assertTrue(minuteString.isEmpty())
            Assert.assertTrue(intervalString.isEmpty())
            Assert.assertEquals(2, errors.size)
            Assert.assertTrue(errors.contains(IntervalEditorErrorType.Time.Empty))
            Assert.assertTrue(errors.contains(IntervalEditorErrorType.Interval.Empty))
        }
    }

    /**
     * Since interval is out of range, we can't tell if zero time is allowed or not. Therefore there is no error about zero time
     *
     */
    @Test
    fun testZeroTimeAndIntervalError() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(0, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 0
        sut.onAction(IntervalEditorAction.MinuteChange("0"))
        sut.onAction(IntervalEditorAction.IntervalChange("0"))
        sut.state.value.run {
            Assert.assertEquals(1, errors.size)
            Assert.assertTrue(errors.contains(IntervalEditorErrorType.Interval.Invalid(3)))
        }
    }

    @Test
    fun testZeroTimeAllowed() = runTest {
        initWithSportType()
        every { timeConversionUseCase.fromTimeData(TimeData(0, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 0
        sut.onAction(IntervalEditorAction.MinuteChange("0"))
        sut.onAction(IntervalEditorAction.IntervalChange("3"))
        Assert.assertTrue(sut.state.value.errors.isEmpty())
    }


}