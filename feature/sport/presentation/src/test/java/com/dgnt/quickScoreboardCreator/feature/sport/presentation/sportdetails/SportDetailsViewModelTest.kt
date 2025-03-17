package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.DeleteSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.GetSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.InsertSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.TimeConversionUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.descriptionRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.intervalLabelRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.rawRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.titleRes
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
    private lateinit var timeConversionUseCase: TimeConversionUseCase

    private lateinit var sut: SportDetailsViewModel

    private fun initSut() {
        sut = SportDetailsViewModel(
            resources,
            insertSportUseCase,
            getSportUseCase,
            deleteSportUseCase,
            timeConversionUseCase,
            savedStateHandle,
            uiEventHandler
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { insertSportUseCase.invoke(any()) } answers { 1 }
        every { timeConversionUseCase.toTimeData(0) } answers { TimeData(0, 0, 0) }
        every { timeConversionUseCase.fromTimeData(TimeData(10, 0, 0)) } answers { 10000 }
        every { savedStateHandle.get<SportIdentifier?>(NavArguments.SPORT_IDENTIFIER) } returns null
    }

    @Test
    fun testInitializingACustomSport() = runTest {
        every { savedStateHandle.get<SportIdentifier?>(NavArguments.SPORT_IDENTIFIER) } returns SportIdentifier.Custom(1)
        coEvery { getSportUseCase(1) } coAnswers {
            SportModel(
                sportIdentifier = SportIdentifier.Custom(1),
                title = "sport name",
                description = "sport desc",
                winRule = WinRule.Count,
                icon = SportIcon.TENNIS,
                intervalLabel = "sport interval",
                intervalList = listOf()
            )
        }
        initSut()

        sut.state.value.run {
            Assert.assertEquals("sport name", title)
            Assert.assertEquals("sport desc", description)
            Assert.assertEquals(WinRule.Count, winRule)
            Assert.assertEquals(SportIcon.TENNIS, (iconState as SportIconState.Picked).sportIcon)
            Assert.assertEquals("sport interval", intervalLabel)
            Assert.assertFalse(valid)
        }
    }

    @Test
    fun testInitializingADefaultSport() = runTest {
        val inputStream = mockk<InputStream>()
        val sportModel = mockk<SportModel>()

        every { savedStateHandle.get<SportIdentifier?>(NavArguments.SPORT_IDENTIFIER) } returns SportIdentifier.Default(SportType.BASKETBALL)
        every { resources.openRawResource(SportType.BASKETBALL.rawRes()) } returns inputStream
        every { resources.getString(SportType.BASKETBALL.titleRes()) } returns "Basketball"
        every { resources.getString(SportType.BASKETBALL.descriptionRes()) } returns "Basketball Desc"
        every { resources.getString(SportType.BASKETBALL.intervalLabelRes()) } returns "Basketball Interval Label"
        every { getSportUseCase.invoke(inputStream) } returns sportModel
        every { sportModel.winRule } returns WinRule.Count
        every { sportModel.icon } returns SportType.BASKETBALL.icon
        //TODO add actual list and then add some assertions
        every { sportModel.intervalList } returns listOf()
        initSut()

        sut.state.value.run {
            Assert.assertEquals("Basketball", title)
            Assert.assertEquals("Basketball Desc", description)
            Assert.assertEquals(SportIcon.BASKETBALL, (iconState as SportIconState.Picked).sportIcon)
            Assert.assertEquals("Basketball Interval Label", intervalLabel)
            Assert.assertEquals(WinRule.Count, winRule)
            Assert.assertTrue(valid)
        }
    }

    @Test
    fun testInitializingNoSport() = runTest {
        initSut()

        sut.state.value.run {
            Assert.assertTrue(title.isEmpty())
            Assert.assertTrue(description.isEmpty())
            Assert.assertFalse(iconState is SportIconState.Initial)
            Assert.assertEquals(WinRule.Final, winRule)
            Assert.assertFalse(valid)
        }
    }

    @Test
    fun testValidation() = runTest {
        initSut()

        Assert.assertFalse(sut.state.value.valid)
        sut.onAction(SportDetailsAction.DescriptionChange("Some value"))
        Assert.assertFalse(sut.state.value.valid)
        sut.onAction(SportDetailsAction.TitleChange("Some value"))
        Assert.assertFalse(sut.state.value.valid)
        sut.onAction(SportDetailsAction.IntervalLabelChange("Some value"))
        Assert.assertFalse(sut.state.value.valid)
        sut.onAction(SportDetailsAction.IntervalEditForMinute(0, "10"))
        Assert.assertTrue(sut.state.value.valid)
        sut.onAction(SportDetailsAction.IntervalEditForInitialScoreInput(0, ""))
        Assert.assertFalse(sut.state.value.valid)

    }

    @Test
    fun testSportIconChange() = runTest {
        initSut()

        sut.onAction(SportDetailsAction.IconEdit(true))
        Assert.assertTrue(sut.state.value.iconState is SportIconState.Picked.Changing)
        sut.onAction(SportDetailsAction.IconChange(SportIcon.VOLLEYBALL))
        Assert.assertEquals(SportIcon.VOLLEYBALL, (sut.state.value.iconState as SportIconState.Picked.Displaying).sportIcon)

    }

    @Test
    fun testOnDismiss() = runTest {
        initSut()

        sut.onAction(SportDetailsAction.Dismiss)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
    }

    @Test
    fun testInsertingNewSport() = runTest {
        initSut()

        sut.onAction(SportDetailsAction.TitleChange("new sport"))
        sut.onAction(SportDetailsAction.DescriptionChange("new sport desc"))
        sut.onAction(SportDetailsAction.WinRuleChange(WinRule.Sum))
        sut.onAction(SportDetailsAction.IconChange(SportIcon.SOCCER))
        sut.onAction(SportDetailsAction.IntervalLabelChange("new interval label"))
        sut.onAction(SportDetailsAction.IntervalEditForMinute(0, "10"))
        sut.onAction(SportDetailsAction.IntervalEditForInitialScoreInput(0, "0"))
        sut.onAction(SportDetailsAction.Confirm)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
        coVerify(exactly = 1) {
            insertSportUseCase.invoke(
                SportModel(
                    sportIdentifier = null,
                    title = "new sport",
                    description = "new sport desc",
                    winRule = WinRule.Sum,
                    icon = SportIcon.SOCCER,
                    intervalLabel = "new interval label",
                    intervalList = listOf()
                )
            )
        }
    }

    @Test
    fun testEditingASport() = runTest {
        every { savedStateHandle.get<SportIdentifier?>(NavArguments.SPORT_IDENTIFIER) } returns SportIdentifier.Custom(2)
        coEvery { getSportUseCase(2) } coAnswers {
            SportModel(
                sportIdentifier = SportIdentifier.Custom(2),
                title = "sport name 2",
                description = "sport desc 2",
                winRule = WinRule.Sum,
                icon = SportIcon.TENNIS,
                intervalLabel = "sport interval label 2",
                intervalList = listOf()
            )
        }
        initSut()

        sut.onAction(SportDetailsAction.TitleChange("new sport"))
        sut.onAction(SportDetailsAction.DescriptionChange("new sport desc"))
        sut.onAction(SportDetailsAction.WinRuleChange(WinRule.Final))
        sut.onAction(SportDetailsAction.IconChange(SportIcon.BOXING))
        sut.onAction(SportDetailsAction.IntervalLabelChange("new interval label"))
        sut.onAction(SportDetailsAction.IntervalEditForMinute(0, "10"))
        sut.onAction(SportDetailsAction.IntervalEditForInitialScoreInput(0, "0"))
        sut.onAction(SportDetailsAction.Confirm)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
        coVerify(exactly = 1) {
            insertSportUseCase.invoke(
                SportModel(
                    sportIdentifier = SportIdentifier.Custom(2),
                    title = "new sport",
                    description = "new sport desc",
                    winRule = WinRule.Final,
                    icon = SportIcon.BOXING,
                    intervalLabel = "new interval label",
                    intervalList = listOf()
                )
            )
        }
    }

    @Test
    fun testDeletingASport() = runTest {
        every { savedStateHandle.get<SportIdentifier?>(NavArguments.SPORT_IDENTIFIER) } returns SportIdentifier.Custom(2)
        coEvery { getSportUseCase(2) } coAnswers {
            SportModel(
                sportIdentifier = SportIdentifier.Custom(2),
                title = "sport name 2",
                description = "sport desc 2",
                winRule = WinRule.Count,
                icon = SportIcon.TENNIS,
                intervalLabel = "sport interval label 2",
                intervalList = listOf()
            )
        }
        initSut()

        sut.onAction(SportDetailsAction.Delete)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
        coVerify(exactly = 1) {
            deleteSportUseCase.invoke(
                SportModel(
                    sportIdentifier = SportIdentifier.Custom(2),
                    title = "sport name 2",
                    description = "sport desc 2",
                    winRule = WinRule.Count,
                    icon = SportIcon.TENNIS,
                    intervalLabel = "sport interval label 2",
                    intervalList = listOf()
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
            ), sut.state.value.intervalList
        )
    }

    @Test
    fun testAddingAnInterval() = runTest {
        initSut()

        sut.onAction(SportDetailsAction.IntervalAdd())
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
            ), sut.state.value.intervalList
        )
    }

    @Test
    fun testRemovingAnInterval() = runTest {
        initSut()

        sut.onAction(SportDetailsAction.IntervalRemove(0))
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
            ), sut.state.value.intervalList
        )

        sut.onAction(SportDetailsAction.IntervalAdd())
        sut.onAction(SportDetailsAction.IntervalRemove(1))
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
            ), sut.state.value.intervalList
        )
    }

    @Test
    fun testMovingAnInterval() = runTest {
        initSut()

        sut.onAction(SportDetailsAction.IntervalAdd())
        sut.onAction(SportDetailsAction.IntervalEditForTimeIsIncreasing(0, true))
        sut.onAction(SportDetailsAction.IntervalEditForTimeIsIncreasing(1, false))
        sut.onAction(SportDetailsAction.IntervalMove(true, 1))
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
            ), sut.state.value.intervalList
        )

        sut.onAction(SportDetailsAction.IntervalMove(false, 0))
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
            ), sut.state.value.intervalList
        )

        sut.onAction(SportDetailsAction.IntervalMove(true, 0))
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
            ), sut.state.value.intervalList
        )

        sut.onAction(SportDetailsAction.IntervalMove(false, 1))
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
            ), sut.state.value.intervalList
        )
    }

    @Test
    fun testEditingSoundEffect() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForSoundEffect(0, IntervalEndSound.LowBuzzer))
        Assert.assertEquals(
            IntervalEndSound.LowBuzzer, sut.state.value.intervalList[0].intervalData.soundEffect
        )
    }

    @Test
    fun testEditingIsTimeIncreasing() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForTimeIsIncreasing(0, true))
        Assert.assertTrue(sut.state.value.intervalList[0].intervalData.increasing)
    }

    @Test
    fun testEditingMinute() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForMinute(0, "8"))
        Assert.assertEquals(Pair("8", "0"), sut.state.value.intervalList[0].timeRepresentationPair)
    }

    @Test
    fun testEditingSecond() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForSecond(0, "9"))
        Assert.assertEquals(Pair("0", "9"), sut.state.value.intervalList[0].timeRepresentationPair)
    }

    @Test
    fun testEditingForAllowDeuceAdv() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForAllowDeuceAdv(0, true))
        Assert.assertEquals(ScoreRule.Trigger.DeuceAdvantage(10), sut.state.value.intervalList[0].scoreInfo.scoreRule)
    }

    @Test
    fun testEditingForMaxScoreInput() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForMaxScoreInput(0, "77"))
        Assert.assertEquals("77", sut.state.value.intervalList[0].maxScoreInput)
    }

    @Test
    fun testEditingForMaxScoreInputWithDeuce() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForMaxScoreInput(0, "77"))
        sut.onAction(SportDetailsAction.IntervalEditForAllowDeuceAdv(0, true))
        sut.state.value.run {
            Assert.assertEquals(ScoreRule.Trigger.DeuceAdvantage(77), intervalList[0].scoreInfo.scoreRule)
            Assert.assertEquals("77", intervalList[0].maxScoreInput)
        }
    }

    @Test
    fun testEditingForMaxScoreInputWithJustMax() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForMaxScoreInput(0, "20"))
        sut.onAction(SportDetailsAction.IntervalEditForAllowDeuceAdv(0, false))
        sut.state.value.run {
            Assert.assertEquals(ScoreRule.Trigger.Max(20), intervalList[0].scoreInfo.scoreRule)
            Assert.assertEquals("20", intervalList[0].maxScoreInput)
        }
    }

    @Test
    fun testEditingForTeamCount() = runTest {
        initSut()


        // already has 1 team
        sut.onAction(SportDetailsAction.IntervalEditForTeamCount(0, 4))
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
            ), sut.state.value.intervalList[0].scoreInfo.dataList
        )
        sut.onAction(SportDetailsAction.IntervalEditForTeamCount(0, 2))
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
            ), sut.state.value.intervalList[0].scoreInfo.dataList
        )
        sut.onAction(SportDetailsAction.IntervalEditForTeamCount(0, 2))
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
            ), sut.state.value.intervalList[0].scoreInfo.dataList
        )

    }

    @Test
    fun testEditingForPrimaryIncrementAdd() = runTest {
        initSut()
        val list1 = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1"), list1)
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        val list2 = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+1"), list2)
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        val list3 = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+1", "+1"), list3)

    }

    @Test
    fun testEditingForInitialScoreInput() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForInitialScoreInput(0, "50"))
        val initialScoreInput = sut.state.value.intervalList[0].initialScoreInput
        Assert.assertEquals("50", initialScoreInput)
    }

    @Test
    fun testEditingForPrimaryIncrement() = runTest {
        initSut()

        val list1 = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1"), list1)
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        val list2 = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+1"), list2)
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrement(0, 1, "2"))
        val list3 = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "2"), list3)
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrement(0, 0, "-1"))
        val list4 = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("-1", "2"), list4)
    }

    @Test
    fun testEditingForPrimaryIncrementMove() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrement(0, 1, "2"))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementMove(0, 0, false))
        val list = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("2", "+1", "+1"), list)
    }

    @Test
    fun testEditingForPrimaryIncrementRemove() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrement(0, 1, "2"))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrement(0, 2, "3"))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementRemove(0, 1))
        val list = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "3"), list)
    }

    @Test
    fun testEditingForPrimaryIncrementRefresh() = runTest {
        initSut()

        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrement(0, 1, "2"))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementRefresh(0, 1))
        val list = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+2"), list)
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrement(0, 2, "-2"))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryIncrementRefresh(0, 2))
        val list2 = sut.state.value.intervalList[0].primaryIncrementInputList
        Assert.assertEquals(listOf("+1", "+2", "-2"), list2)
    }

    @Test
    fun testEditingForPrimaryMappingAllowed() = runTest {
        initSut()
        sut.state.value.intervalList[0].allowPrimaryMapping.let {
            Assert.assertFalse(it)
        }
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAllowed(0, true))
        sut.state.value.intervalList[0].allowPrimaryMapping.let {
            Assert.assertTrue(it)
        }
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAllowed(0, false))
        sut.state.value.intervalList[0].allowPrimaryMapping.let {
            Assert.assertFalse(it)
        }

    }

    @Test
    fun testEditingForPrimaryMappingAdd() = runTest {
        initSut()
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(0))
        sut.state.value.intervalList[0].primaryMappingInputList.let {
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
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingOriginalScore(0, 0, "3"))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingOriginalScore(0, 1, "4"))
        sut.state.value.intervalList[0].primaryMappingInputList.let {
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
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingDisplayScore(0, 0, "0"))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingDisplayScore(0, 1, "15"))
        sut.state.value.intervalList[0].primaryMappingInputList.let {
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
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(0))
        sut.onAction(SportDetailsAction.IntervalEditForPrimaryMappingRemove(0, 1))
        sut.state.value.intervalList[0].primaryMappingInputList.let {
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
        sut.state.value.intervalList[0].allowSecondaryScore.let {
            Assert.assertFalse(it)
        }
        sut.onAction(SportDetailsAction.IntervalEditForSecondaryScoreAllowed(0, true))
        sut.state.value.intervalList[0].allowSecondaryScore.let {
            Assert.assertTrue(it)
        }
        sut.onAction(SportDetailsAction.IntervalEditForSecondaryScoreAllowed(0, false))
        sut.state.value.intervalList[0].allowSecondaryScore.let {
            Assert.assertFalse(it)
        }

    }

    @Test
    fun testEditingForSecondaryScoreLabel() = runTest {
        initSut()

        sut.state.value.intervalList[0].scoreInfo.secondaryScoreLabel.let {
            Assert.assertTrue(it.isEmpty())
        }
        sut.onAction(SportDetailsAction.IntervalEditForSecondaryScoreLabel(0, "Fouls"))
        sut.state.value.intervalList[0].scoreInfo.secondaryScoreLabel.let {
            Assert.assertEquals("Fouls", it)
        }
    }
}