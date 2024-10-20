package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.WinRuleType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.DeleteScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
class ScoreboardDetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var resources: Resources

    @MockK
    private lateinit var scoreboardLoader: ScoreboardLoader

    @MockK
    private lateinit var insertScoreboardUseCase: InsertScoreboardUseCase

    @MockK
    private lateinit var getScoreboardUseCase: GetScoreboardUseCase

    @MockK
    private lateinit var deleteScoreboardUseCase: DeleteScoreboardUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    @MockK
    private lateinit var timeTransformer: TimeTransformer

    private lateinit var sut: ScoreboardDetailsViewModel

    private fun initSut() {
        sut = ScoreboardDetailsViewModel(
            resources,
            insertScoreboardUseCase,
            getScoreboardUseCase,
            deleteScoreboardUseCase,
            scoreboardLoader,
            timeTransformer,
            savedStateHandle,
            uiEventHandler
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { insertScoreboardUseCase.invoke(any()) } answers { 1 }
        every { timeTransformer.toTimeData(0) } answers { TimeData(0, 0, 0) }
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns null
    }

    @Test
    fun testInitializingACustomScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.Custom(1)
        coEvery { getScoreboardUseCase(1) } coAnswers { ScoreboardEntity(1, "scoreboard name", "scoreboard desc", winRule = WinRule.Count, ScoreboardIcon.TENNIS) }
        initSut()

        Assert.assertEquals("scoreboard name", sut.title.value)
        Assert.assertEquals("scoreboard desc", sut.description.value)
        Assert.assertEquals(WinRule.Count, sut.winRule.value)
        Assert.assertEquals(ScoreboardIcon.TENNIS, sut.icon.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingADefaultScoreboard() = runTest {
        val inputStream = mockk<InputStream>()
        val scoreboardConfig = mockk<DefaultScoreboardConfig>()

        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.Default(ScoreboardType.BASKETBALL)
        every { resources.openRawResource(ScoreboardType.BASKETBALL.rawRes) } returns inputStream
        every { resources.getString(ScoreboardType.BASKETBALL.titleRes) } returns "Basketball"
        every { resources.getString(ScoreboardType.BASKETBALL.descriptionRes) } returns "Basketball Desc"
        every { scoreboardLoader.invoke(inputStream) } returns scoreboardConfig
        every { scoreboardConfig.winRuleType } returns WinRuleType.COUNT
        //TODO add actual list and then add some assertions
        every { scoreboardConfig.intervalList } returns listOf()
        initSut()

        Assert.assertEquals("Basketball", sut.title.value)
        Assert.assertEquals("Basketball Desc", sut.description.value)
        Assert.assertEquals(ScoreboardIcon.BASKETBALL, sut.icon.value)
        Assert.assertEquals(WinRule.Count, sut.winRule.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingNoScoreboard() = runTest {
        initSut()

        Assert.assertTrue(sut.title.value.isEmpty())
        Assert.assertTrue(sut.description.value.isEmpty())
        Assert.assertNotNull(sut.icon.value)
        Assert.assertEquals(WinRule.Final, sut.winRule.value)
        Assert.assertFalse(sut.valid.value)
    }

    @Test
    fun testValidation() = runTest {
        initSut()

        Assert.assertFalse(sut.valid.value)
        sut.onDescriptionChange("Some value")
        Assert.assertFalse(sut.valid.value)
        sut.onTitleChange("Some value")
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testScoreboardIconChange() = runTest {
        initSut()

        sut.onIconEdit()
        Assert.assertTrue(sut.iconChanging.value)
        sut.onIconChange(ScoreboardIcon.VOLLEYBALL)
        Assert.assertEquals(ScoreboardIcon.VOLLEYBALL, sut.icon.value)
        Assert.assertFalse(sut.iconChanging.value)
    }

    @Test
    fun testOnDismiss() = runTest {
        initSut()

        sut.onDismiss()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
    }

    @Test
    fun testInsertingNewScoreboard() = runTest {
        initSut()

        sut.onTitleChange("new scoreboard")
        sut.onDescriptionChange("new scoreboard desc")
        sut.onWinRuleChange(WinRule.Sum)
        sut.onIconChange(ScoreboardIcon.SOCCER)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            insertScoreboardUseCase.invoke(
                ScoreboardEntity(
                    id = null,
                    title = "new scoreboard",
                    description = "new scoreboard desc",
                    winRule = WinRule.Sum,
                    icon = ScoreboardIcon.SOCCER
                )
            )
        }
    }

    @Test
    fun testEditingAScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.Custom(2)
        coEvery { getScoreboardUseCase(2) } coAnswers { ScoreboardEntity(2, "scoreboard name 2", "scoreboard desc 2", WinRule.Sum, ScoreboardIcon.TENNIS) }
        initSut()

        sut.onTitleChange("new scoreboard")
        sut.onDescriptionChange("new scoreboard desc")
        sut.onWinRuleChange(WinRule.Final)
        sut.onIconChange(ScoreboardIcon.BOXING)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            insertScoreboardUseCase.invoke(
                ScoreboardEntity(
                    id = 2,
                    title = "new scoreboard",
                    description = "new scoreboard desc",
                    winRule = WinRule.Final,
                    icon = ScoreboardIcon.BOXING
                )
            )
        }
    }

    @Test
    fun testDeletingAScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.Custom(2)
        coEvery { getScoreboardUseCase(2) } coAnswers { ScoreboardEntity(2, "scoreboard name 2", "scoreboard desc 2", winRule = WinRule.Count, ScoreboardIcon.TENNIS) }
        initSut()

        sut.onDelete()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            deleteScoreboardUseCase.invoke(
                ScoreboardEntity(
                    id = 2,
                    title = "scoreboard name 2",
                    description = "scoreboard desc 2",
                    winRule = WinRule.Count,
                    icon = ScoreboardIcon.TENNIS
                )

            )
        }
    }

    @Test
    fun testInitialInterval() = runTest {
        initSut()

        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testAddingAnInterval() = runTest {
        initSut()

        sut.onIntervalAdd()
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                )
            ), sut.intervalList.value
        )
    }

    @Test
    fun testRemovingAnInterval() = runTest {
        initSut()

        sut.onIntervalRemove(0)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )

        sut.onIntervalAdd()
        sut.onIntervalRemove(1)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testMovingAnInterval() = runTest {
        initSut()

        sut.onIntervalAdd()
        sut.onIntervalEditForTimeIsIncreasing(0, true)
        sut.onIntervalEditForTimeIsIncreasing(1, false)
        sut.onIntervalMove(true, 1)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )

        sut.onIntervalMove(false, 0)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )

        sut.onIntervalMove(true, 0)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )

        sut.onIntervalMove(false, 1)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testEditingIsTimeIncreasing() = runTest {
        initSut()
        sut.onIntervalEditForTimeIsIncreasing(0, true)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testEditingMinute() = runTest {
        every { timeTransformer.fromTimeData(TimeData(8,0,0)) } answers { 480000 }
        initSut()
        sut.onIntervalEditForMinute(0, "8")
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 480000,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("8", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testEditingSecond() = runTest {
        every { timeTransformer.fromTimeData(TimeData(0,9,0)) } answers { 9000 }
        initSut()
        sut.onIntervalEditForSecond(0, "9")
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 9000,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "9"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testEditingForAllowDeuceAdv() = runTest {
        initSut()
        sut.onIntervalEditForAllowDeuceAdv(0, true)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.Trigger.DeuceAdvantage(0),
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = ""
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testEditingForMaxScoreInput() = runTest {
        initSut()
        sut.onIntervalEditForMaxScoreInput(0, "77")
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "77"
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testEditingForMaxScoreInputWithDeuce() = runTest {
        initSut()
        sut.onIntervalEditForMaxScoreInput(0, "77")
        sut.onIntervalEditForAllowDeuceAdv(0, true)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.Trigger.DeuceAdvantage(77),
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "77"
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testEditingForMaxScoreInputWithJustMax() = runTest {
        initSut()
        sut.onIntervalEditForMaxScoreInput(0, "20")
        sut.onIntervalEditForAllowDeuceAdv(0, false)
        Assert.assertEquals(
            listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.Trigger.Max(20),
                        scoreToDisplayScoreMap = mapOf(),
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "20"
                ),
            ), sut.intervalList.value
        )
    }
}