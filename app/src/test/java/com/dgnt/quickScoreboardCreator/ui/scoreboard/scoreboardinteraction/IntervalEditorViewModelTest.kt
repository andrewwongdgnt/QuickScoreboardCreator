package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalDataConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreInfoConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreRuleConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreRuleType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.composable.Label
import com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor.IntervalEditorErrorType
import com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor.IntervalEditorViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
    private lateinit var getScoreboardUseCase: GetScoreboardUseCase

    @MockK
    private lateinit var timeTransformer: TimeTransformer

    @MockK
    private lateinit var scoreboardLoader: ScoreboardLoader

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var sut: IntervalEditorViewModel

    private val defaultScoreInfoConfig = ScoreInfoConfig(
        ScoreRuleConfig(
            ScoreRuleType.NO_RULE,
            0,
        ),
        mapOf(),
        null,
        listOf()
    )

    private val interval1Time = 720000L
    private val interval1TimeData = TimeData(12, 0, 0)
    private val interval2Time = 700000L
    private val interval3Time = 0L

    private val mockIntervalList = listOf(
        IntervalConfig(
            defaultScoreInfoConfig,
            IntervalDataConfig(
                interval1Time,
                false
            )
        ),
        IntervalConfig(
            defaultScoreInfoConfig,
            IntervalDataConfig(
                interval2Time,
                false
            )
        ),
        IntervalConfig(
            defaultScoreInfoConfig,
            IntervalDataConfig(
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
            getScoreboardUseCase,
            timeTransformer,
            scoreboardLoader,
            savedStateHandle,
        )
    }

    private fun initDefaultScoreboardConfig() {
        val inputStream = mockk<InputStream>()
        val scoreboardConfig = mockk<DefaultScoreboardConfig>()

        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.DefaultScoreboard(ScoreboardType.BASKETBALL)
        every { resources.openRawResource(ScoreboardType.BASKETBALL.rawRes) } returns inputStream
        every { scoreboardLoader.invoke(inputStream) } returns scoreboardConfig
        every { scoreboardConfig.intervalList } returns mockIntervalList

        initSut()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        every { savedStateHandle.get<Long>(Arguments.VALUE) } returns mockInitialCurrentTimeValue
        every { timeTransformer.toTimeData(mockInitialCurrentTimeValue) } returns mockInitialCurrentTimeData
        every { savedStateHandle.get<Int>(Arguments.INDEX) } returns mockInitialCurrentIntervalIndex
        every { timeTransformer.toTimeData(interval1Time) } returns interval1TimeData
    }

    @Test
    fun testInitializingTime() = runTest {
        initDefaultScoreboardConfig()

        Assert.assertEquals(mockInitialCurrentTimeData.minute.toString(), sut.minuteString.value)
        Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), sut.secondString.value)
    }

    @Test
    fun testInitializingIntervalIndex() = runTest {
        initDefaultScoreboardConfig()

        Assert.assertEquals("1", sut.intervalString.value)
    }

    @Test
    fun testInitializingADefaultScoreboard() = runTest {
        initDefaultScoreboardConfig()

        Assert.assertEquals(Label.ResourceLabel(R.string.quarter), sut.label.value)
        Assert.assertTrue(sut.errors.value.isEmpty())
    }

    @Test
    fun testOnDismiss() = runTest {
        initDefaultScoreboardConfig()
        sut.onDismiss()
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
    }

    @Test
    fun testOnConfirmInitialValues() = runTest {
        initDefaultScoreboardConfig()
        sut.onConfirm()
        Assert.assertEquals(UiEvent.IntervalUpdated(mockInitialCurrentTimeValue, mockInitialCurrentIntervalIndex), sut.uiEvent.first())
    }

    @Test
    fun testOnMinuteChange() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(10, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 1000
        sut.onMinuteChange("10")
        Assert.assertEquals("10", sut.minuteString.value)
        Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), sut.secondString.value)
        Assert.assertEquals("1", sut.intervalString.value)
        Assert.assertTrue(sut.errors.value.isEmpty())
    }

    @Test
    fun testOnSecondChange() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(mockInitialCurrentTimeData.minute, 10, mockInitialCurrentTimeData.centiSecond)) } returns 1000
        sut.onSecondChange("10")
        Assert.assertEquals(mockInitialCurrentTimeData.minute.toString(), sut.minuteString.value)
        Assert.assertEquals("10", sut.secondString.value)
        Assert.assertEquals("1", sut.intervalString.value)
        Assert.assertTrue(sut.errors.value.isEmpty())
    }

    @Test
    fun testOnIntervalChange() = runTest {
        initDefaultScoreboardConfig()
        sut.onIntervalChange("2")
        Assert.assertEquals(mockInitialCurrentTimeData.minute.toString(), sut.minuteString.value)
        Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), sut.secondString.value)
        Assert.assertEquals("2", sut.intervalString.value)
        Assert.assertTrue(sut.errors.value.isEmpty())
    }

    @Test
    fun testOnInvalidMinuteChange() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(13, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 740000
        sut.onMinuteChange("13")
        Assert.assertEquals("13", sut.minuteString.value)
        Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), sut.secondString.value)
        Assert.assertEquals("1", sut.intervalString.value)
        Assert.assertEquals(1, sut.errors.value.size)
        Assert.assertEquals(IntervalEditorErrorType.TimeErrorType.Time(interval1TimeData.minute, interval1TimeData.second), sut.errors.value.first())
    }

    @Test
    fun testOnInvalidMinuteChange_Zero() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(0, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 0
        sut.onMinuteChange("0")
        Assert.assertEquals("0", sut.minuteString.value)
        Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), sut.secondString.value)
        Assert.assertEquals("1", sut.intervalString.value)
        Assert.assertEquals(1, sut.errors.value.size)
        Assert.assertEquals(IntervalEditorErrorType.TimeErrorType.ZeroTime, sut.errors.value.first())
    }

    @Test
    fun testOnInvalidIntervalChange() = runTest {
        initDefaultScoreboardConfig()
        sut.onIntervalChange("313")
        Assert.assertEquals(mockInitialCurrentTimeData.minute.toString(), sut.minuteString.value)
        Assert.assertEquals(mockInitialCurrentTimeData.second.toString(), sut.secondString.value)
        Assert.assertEquals("313", sut.intervalString.value)
        Assert.assertEquals(1, sut.errors.value.size)
        Assert.assertEquals(IntervalEditorErrorType.IntervalErrorType.Interval(3), sut.errors.value.first())
    }

    @Test
    fun testOnInvalidIntervalAndTimeChange() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(13, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 740000
        sut.onMinuteChange("13")
        sut.onIntervalChange("313")
        Assert.assertEquals(1, sut.errors.value.size)
        Assert.assertEquals(IntervalEditorErrorType.IntervalErrorType.Interval(3), sut.errors.value.first())
    }

    @Test
    fun testHighMinChangeOnIncreasingTimeInterval() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(13, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 740000
        sut.onMinuteChange("13")
        sut.onIntervalChange("3")
        Assert.assertTrue(sut.errors.value.isEmpty())
    }

    @Test
    fun testEmptyTimeAndIntervalError() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(0, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 123
        sut.onMinuteChange("")
        sut.onIntervalChange("")
        Assert.assertTrue(sut.minuteString.value.isEmpty())
        Assert.assertTrue(sut.intervalString.value.isEmpty())
        Assert.assertEquals(2, sut.errors.value.size)
        Assert.assertTrue(sut.errors.value.contains(IntervalEditorErrorType.TimeErrorType.EmptyTime))
        Assert.assertTrue(sut.errors.value.contains(IntervalEditorErrorType.IntervalErrorType.EmptyInterval))
    }

    /**
     * Since interval is out of range, we can't tell if zero time is allowed or not. Therefore there is no error about zero time
     *
     */
    @Test
    fun testZeroTimeAndIntervalError() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(0, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 0
        sut.onMinuteChange("0")
        sut.onIntervalChange("0")
        Assert.assertEquals(1, sut.errors.value.size)
        Assert.assertTrue(sut.errors.value.contains(IntervalEditorErrorType.IntervalErrorType.Interval(3)))
    }

    @Test
    fun testZeroTimeAllowed() = runTest {
        initDefaultScoreboardConfig()
        every { timeTransformer.fromTimeData(TimeData(0, mockInitialCurrentTimeData.second, mockInitialCurrentTimeData.centiSecond)) } returns 0
        sut.onMinuteChange("0")
        sut.onIntervalChange("3")
        Assert.assertEquals(0, sut.errors.value.size)
    }


}