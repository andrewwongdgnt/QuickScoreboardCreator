package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.TimeTransformer
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardType
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.WinRuleType
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.DeleteSportUseCase
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.GetSportUseCase
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.InsertSportUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.ui.main.sportdetails.IntervalEditingInfo
import com.dgnt.quickScoreboardCreator.ui.main.sportdetails.SportDetailsViewModel
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
class SportDetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var resources: Resources

    @MockK
    private lateinit var scoreboardLoader: ScoreboardLoader

    @MockK
    private lateinit var insertSportUseCase: InsertSportUseCase

    @MockK
    private lateinit var getSportUseCase: GetSportUseCase

    @MockK
    private lateinit var deleteSportUseCase: DeleteSportUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    @MockK
    private lateinit var timeTransformer: TimeTransformer

    private lateinit var sut: SportDetailsViewModel

    private fun initSut() {
        sut = SportDetailsViewModel(
            resources,
            insertSportUseCase,
            getSportUseCase,
            deleteSportUseCase,
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
        coEvery { insertSportUseCase.invoke(any()) } answers { 1 }
        every { timeTransformer.toTimeData(0) } answers { TimeData(0, 0, 0) }
        every { timeTransformer.fromTimeData(TimeData(10, 0, 0)) } answers { 10000 }
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SPORT_IDENTIFIER) } returns null
    }

    @Test
    fun testInitializingACustomScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SPORT_IDENTIFIER) } returns ScoreboardIdentifier.Custom(1)
        coEvery { getSportUseCase(1) } coAnswers {
            ScoreboardEntity(
                id = 1,
                title = "scoreboard name",
                description = "scoreboard desc",
                winRule = WinRule.Count,
                intervalLabel = "scoreboard interval",
                icon = ScoreboardIcon.TENNIS
            )
        }
        initSut()

        Assert.assertEquals("scoreboard name", sut.title.value)
        Assert.assertEquals("scoreboard desc", sut.description.value)
        Assert.assertEquals(WinRule.Count, sut.winRule.value)
        Assert.assertEquals(ScoreboardIcon.TENNIS, sut.icon.value)
        Assert.assertEquals("scoreboard interval", sut.intervalLabel.value)
        Assert.assertFalse(sut.valid.value)
    }

    @Test
    fun testInitializingADefaultScoreboard() = runTest {
        val inputStream = mockk<InputStream>()
        val scoreboardConfig = mockk<DefaultScoreboardConfig>()

        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SPORT_IDENTIFIER) } returns ScoreboardIdentifier.Default(ScoreboardType.BASKETBALL)
        every { resources.openRawResource(ScoreboardType.BASKETBALL.rawRes) } returns inputStream
        every { resources.getString(ScoreboardType.BASKETBALL.titleRes) } returns "Basketball"
        every { resources.getString(ScoreboardType.BASKETBALL.descriptionRes) } returns "Basketball Desc"
        every { resources.getString(ScoreboardType.BASKETBALL.intervalLabelRes) } returns "Basketball Interval Label"
        every { scoreboardLoader.invoke(inputStream) } returns scoreboardConfig
        every { scoreboardConfig.winRuleType } returns WinRuleType.COUNT
        //TODO add actual list and then add some assertions
        every { scoreboardConfig.intervalList } returns listOf()
        initSut()

        Assert.assertEquals("Basketball", sut.title.value)
        Assert.assertEquals("Basketball Desc", sut.description.value)
        Assert.assertEquals(ScoreboardIcon.BASKETBALL, sut.icon.value)
        Assert.assertEquals("Basketball Interval Label", sut.intervalLabel.value)
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
        Assert.assertFalse(sut.valid.value)
        sut.onIntervalLabelChange("Some value")
        Assert.assertFalse(sut.valid.value)
        sut.onIntervalEditForMinute(0, "10")
        Assert.assertTrue(sut.valid.value)
        sut.onIntervalEditForInitialScoreInput(0, "")
        Assert.assertFalse(sut.valid.value)
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
        sut.onIntervalLabelChange("new interval label")
        sut.onIntervalEditForMinute(0, "10")
        sut.onIntervalEditForInitialScoreInput(0, "0")
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            insertSportUseCase.invoke(
                ScoreboardEntity(
                    id = null,
                    title = "new scoreboard",
                    description = "new scoreboard desc",
                    winRule = WinRule.Sum,
                    icon = ScoreboardIcon.SOCCER,
                    intervalLabel = "new interval label"
                )
            )
        }
    }

    @Test
    fun testEditingAScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SPORT_IDENTIFIER) } returns ScoreboardIdentifier.Custom(2)
        coEvery { getSportUseCase(2) } coAnswers {
            ScoreboardEntity(
                id = 2,
                title = "scoreboard name 2",
                description = "scoreboard desc 2",
                winRule = WinRule.Sum,
                icon = ScoreboardIcon.TENNIS,
                intervalLabel = "scoreboard interval label 2"
            )
        }
        initSut()

        sut.onTitleChange("new scoreboard")
        sut.onDescriptionChange("new scoreboard desc")
        sut.onWinRuleChange(WinRule.Final)
        sut.onIconChange(ScoreboardIcon.BOXING)
        sut.onIntervalLabelChange("new interval label")
        sut.onIntervalEditForMinute(0, "10")
        sut.onIntervalEditForInitialScoreInput(0, "0")
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            insertSportUseCase.invoke(
                ScoreboardEntity(
                    id = 2,
                    title = "new scoreboard",
                    description = "new scoreboard desc",
                    winRule = WinRule.Final,
                    icon = ScoreboardIcon.BOXING,
                    intervalLabel = "new interval label"
                )
            )
        }
    }

    @Test
    fun testDeletingAScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SPORT_IDENTIFIER) } returns ScoreboardIdentifier.Custom(2)
        coEvery { getSportUseCase(2) } coAnswers {
            ScoreboardEntity(
                id = 2,
                title = "scoreboard name 2",
                description = "scoreboard desc 2",
                winRule = WinRule.Count,
                icon = ScoreboardIcon.TENNIS,
                intervalLabel = "scoreboard interval label 2",
            )
        }
        initSut()

        sut.onDelete()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            deleteSportUseCase.invoke(
                ScoreboardEntity(
                    id = 2,
                    title = "scoreboard name 2",
                    description = "scoreboard desc 2",
                    winRule = WinRule.Count,
                    icon = ScoreboardIcon.TENNIS,
                    intervalLabel = "scoreboard interval label 2",
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
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
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
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
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
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
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
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
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
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
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
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
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
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
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
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = true
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
                ),
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        secondaryScoreLabel = "",
                        dataList = listOf(
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            ),
                            ScoreGroup(
                                primary = ScoreData(
                                    current = 0,
                                    initial = 0,
                                    increments = listOf(1)
                                ),
                                secondary = null
                            )
                        )
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false
                    ),
                    timeRepresentationPair = Pair("0", "0"),
                    maxScoreInput = "10",
                    initialScoreInput = "0",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf("0" to "0"),
                    allowSecondaryScore = false
                ),
            ), sut.intervalList.value
        )
    }

    @Test
    fun testEditingSoundEffect() = runTest {
        initSut()
        sut.onIntervalEditForSoundEffect(0, IntervalEndSound.LowBuzzer)
        Assert.assertEquals(
            IntervalEndSound.LowBuzzer, sut.intervalList.value[0].intervalData.soundEffect
        )
    }

    @Test
    fun testEditingIsTimeIncreasing() = runTest {
        initSut()
        sut.onIntervalEditForTimeIsIncreasing(0, true)
        Assert.assertTrue(sut.intervalList.value[0].intervalData.increasing)
    }

    @Test
    fun testEditingMinute() = runTest {
        initSut()
        sut.onIntervalEditForMinute(0, "8")
        Assert.assertEquals(Pair("8", "0"), sut.intervalList.value[0].timeRepresentationPair)
    }

    @Test
    fun testEditingSecond() = runTest {
        initSut()
        sut.onIntervalEditForSecond(0, "9")
        Assert.assertEquals(Pair("0", "9"), sut.intervalList.value[0].timeRepresentationPair)
    }

    @Test
    fun testEditingForAllowDeuceAdv() = runTest {
        initSut()
        sut.onIntervalEditForAllowDeuceAdv(0, true)
        Assert.assertEquals(ScoreRule.Trigger.DeuceAdvantage(10), sut.intervalList.value[0].scoreInfo.scoreRule)
    }

    @Test
    fun testEditingForMaxScoreInput() = runTest {
        initSut()
        sut.onIntervalEditForMaxScoreInput(0, "77")
        Assert.assertEquals("77", sut.intervalList.value[0].maxScoreInput)
    }

    @Test
    fun testEditingForMaxScoreInputWithDeuce() = runTest {
        initSut()
        sut.onIntervalEditForMaxScoreInput(0, "77")
        sut.onIntervalEditForAllowDeuceAdv(0, true)
        Assert.assertEquals(ScoreRule.Trigger.DeuceAdvantage(77), sut.intervalList.value[0].scoreInfo.scoreRule)
        Assert.assertEquals("77", sut.intervalList.value[0].maxScoreInput)
    }

    @Test
    fun testEditingForMaxScoreInputWithJustMax() = runTest {
        initSut()
        sut.onIntervalEditForMaxScoreInput(0, "20")
        sut.onIntervalEditForAllowDeuceAdv(0, false)
        Assert.assertEquals(ScoreRule.Trigger.Max(20), sut.intervalList.value[0].scoreInfo.scoreRule)
        Assert.assertEquals("20", sut.intervalList.value[0].maxScoreInput)
    }

    @Test
    fun testEditingForTeamCount() = runTest {
        initSut()
        // already has 1 team
        sut.onIntervalEditForTeamCount(0, 4)
        Assert.assertEquals(
            listOf(
                ScoreGroup(
                    primary = ScoreData(
                        current = 0,
                        initial = 0,
                        increments = listOf(1)
                    ),
                    secondary = null
                ),
                ScoreGroup(
                    primary = ScoreData(
                        current = 0,
                        initial = 0,
                        increments = listOf(1)
                    ),
                    secondary = null
                ),
                ScoreGroup(
                    primary = ScoreData(
                        current = 0,
                        initial = 0,
                        increments = listOf(1)
                    ),
                    secondary = null
                ),
                ScoreGroup(
                    primary = ScoreData(
                        current = 0,
                        initial = 0,
                        increments = listOf(1)
                    ),
                    secondary = null
                )
            ), sut.intervalList.value[0].scoreInfo.dataList
        )
        sut.onIntervalEditForTeamCount(0, 2)
        Assert.assertEquals(
            listOf(
                ScoreGroup(
                    primary = ScoreData(
                        current = 0,
                        initial = 0,
                        increments = listOf(1)
                    ),
                    secondary = null
                ),
                ScoreGroup(
                    primary = ScoreData(
                        current = 0,
                        initial = 0,
                        increments = listOf(1)
                    ),
                    secondary = null
                )
            ), sut.intervalList.value[0].scoreInfo.dataList
        )
        sut.onIntervalEditForTeamCount(0, 2)
        Assert.assertEquals(
            listOf(
                ScoreGroup(
                    primary = ScoreData(
                        current = 0,
                        initial = 0,
                        increments = listOf(1)
                    ),
                    secondary = null
                ),
                ScoreGroup(
                    primary = ScoreData(
                        current = 0,
                        initial = 0,
                        increments = listOf(1)
                    ),
                    secondary = null
                )
            ), sut.intervalList.value[0].scoreInfo.dataList
        )
    }

    @Test
    fun testEditingForPrimaryIncrementAdd() = runTest {
        initSut()
        val list1 = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1"), list1)
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        val list2 = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+1"), list2)
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        val list3 = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+1", "+1"), list3)
    }

    @Test
    fun testEditingForInitialScoreInput() = runTest {
        initSut()
        sut.onIntervalEditForInitialScoreInput(0, "50")
        val initialScoreInput = sut.intervalList.value[0].initialScoreInput
        Assert.assertEquals("50", initialScoreInput)
    }

    @Test
    fun testEditingForPrimaryIncrement() = runTest {
        initSut()
        val list1 = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1"), list1)
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        val list2 = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+1"), list2)
        sut.onIntervalEditForPrimaryIncrement(0, 1, "2")
        val list3 = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "2"), list3)
        sut.onIntervalEditForPrimaryIncrement(0, 0, "-1")
        val list4 = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("-1", "2"), list4)
    }

    @Test
    fun testEditingForPrimaryIncrementMove() = runTest {
        initSut()
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        sut.onIntervalEditForPrimaryIncrement(0, 1, "2")
        sut.onIntervalEditForPrimaryIncrementMove(0, 0, false)
        val list = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("2", "+1", "+1"), list)
    }

    @Test
    fun testEditingForPrimaryIncrementRemove() = runTest {
        initSut()
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        sut.onIntervalEditForPrimaryIncrement(0, 1, "2")
        sut.onIntervalEditForPrimaryIncrement(0, 2, "3")
        sut.onIntervalEditForPrimaryIncrementRemove(0, 1)
        val list = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "3"), list)
    }

    @Test
    fun testEditingForPrimaryIncrementRefresh() = runTest {
        initSut()
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        sut.onIntervalEditForPrimaryIncrement(0, 1, "2")
        sut.onIntervalEditForPrimaryIncrementRefresh(0, 1)
        val list = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+2"), list)
        sut.onIntervalEditForPrimaryIncrementAdd(0)
        sut.onIntervalEditForPrimaryIncrement(0, 2, "-2")
        sut.onIntervalEditForPrimaryIncrementRefresh(0, 2)
        val list2 = sut.intervalList.value[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+2", "-2"), list2)
    }

    @Test
    fun testEditingForPrimaryMappingAllowed() = runTest {
        initSut()
        sut.intervalList.value[0].allowPrimaryMapping.let {
            Assert.assertFalse(it)
        }
        sut.onIntervalEditForPrimaryMappingAllowed(0, true)
        sut.intervalList.value[0].allowPrimaryMapping.let {
            Assert.assertTrue(it)
        }
        sut.onIntervalEditForPrimaryMappingAllowed(0, false)
        sut.intervalList.value[0].allowPrimaryMapping.let {
            Assert.assertFalse(it)
        }
    }

    @Test
    fun testEditingForPrimaryMappingAdd() = runTest {
        initSut()
        sut.onIntervalEditForPrimaryMappingAdd(0)
        sut.onIntervalEditForPrimaryMappingAdd(0)
        sut.intervalList.value[0].primaryMappingInputList.let {
            Assert.assertEquals(
                listOf(
                    "0" to "0",
                    "1" to "",
                    "2" to "",
                ), it
            )
        }
    }

    @Test
    fun testEditingForPrimaryMappingOriginalScore() = runTest {
        initSut()
        sut.onIntervalEditForPrimaryMappingAdd(0)
        sut.onIntervalEditForPrimaryMappingAdd(0)
        sut.onIntervalEditForPrimaryMappingOriginalScore(0, 0, "3")
        sut.onIntervalEditForPrimaryMappingOriginalScore(0, 1, "4")
        sut.intervalList.value[0].primaryMappingInputList.let {
            Assert.assertEquals(
                listOf(
                    "3" to "0",
                    "4" to "",
                    "2" to "",
                ), it
            )
        }
    }

    @Test
    fun testEditingForPrimaryMappingDisplayScore() = runTest {
        initSut()
        sut.onIntervalEditForPrimaryMappingAdd(0)
        sut.onIntervalEditForPrimaryMappingAdd(0)
        sut.onIntervalEditForPrimaryMappingDisplayScore(0, 0, "0")
        sut.onIntervalEditForPrimaryMappingDisplayScore(0, 1, "15")
        sut.intervalList.value[0].primaryMappingInputList.let {
            Assert.assertEquals(
                listOf(
                    "0" to "0",
                    "1" to "15",
                    "2" to ""
                ), it
            )
        }
    }

    @Test
    fun testEditingForPrimaryMappingRemove() = runTest {
        initSut()
        sut.onIntervalEditForPrimaryMappingAdd(0)
        sut.onIntervalEditForPrimaryMappingAdd(0)
        sut.onIntervalEditForPrimaryMappingRemove(0, 1)
        sut.intervalList.value[0].primaryMappingInputList.let {
            Assert.assertEquals(
                listOf(
                    "0" to "0",
                    "2" to "",
                ), it
            )
        }
    }

    @Test
    fun testEditingForSecondaryScoreAllowed() = runTest {
        initSut()
        sut.intervalList.value[0].allowSecondaryScore.let {
            Assert.assertFalse(it)
        }
        sut.onIntervalEditForSecondaryScoreAllowed(0, true)
        sut.intervalList.value[0].allowSecondaryScore.let {
            Assert.assertTrue(it)
        }
        sut.onIntervalEditForSecondaryScoreAllowed(0, false)
        sut.intervalList.value[0].allowSecondaryScore.let {
            Assert.assertFalse(it)
        }
    }

    @Test
    fun testEditingForSecondaryScoreLabel() = runTest {
        initSut()
        sut.intervalList.value[0].scoreInfo.secondaryScoreLabel.let {
            Assert.assertTrue(it.isEmpty())
        }
        sut.onIntervalEditForSecondaryScoreLabel(0, "Fouls")
        sut.intervalList.value[0].scoreInfo.secondaryScoreLabel.let {
            Assert.assertEquals("Fouls", it)
        }

    }
}