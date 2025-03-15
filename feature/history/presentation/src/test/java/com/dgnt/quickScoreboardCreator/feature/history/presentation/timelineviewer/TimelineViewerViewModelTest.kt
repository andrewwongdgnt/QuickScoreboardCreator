package com.dgnt.quickScoreboardCreator.feature.history.presentation.timelineviewer

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimelineViewerViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var getHistoryUseCase: GetHistoryUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: TimelineViewerViewModel

    private val mockHistoricalInterval1 = HistoricalInterval(
        range = HistoricalIntervalRange.CountDown(72000),
        intervalLabel = IntervalLabel.Custom("Quarter", 0),
        historicalScoreGroupList = mapOf(
            0 to HistoricalScoreGroup(
                teamLabel = TeamLabel.Custom("DGNT", TeamIcon.AXE),
                primaryScoreList = listOf(
                    HistoricalScore(0, "0", 720000),
                    HistoricalScore(1, "1", 660000),
                    HistoricalScore(2, "2", 630000),
                    HistoricalScore(3, "3", 480000),
                    HistoricalScore(4, "4", 330000),
                    HistoricalScore(7, "7", 300000),
                ),
                secondaryScoreList = listOf(
                    HistoricalScore(1, "1", 720000)
                )
            ),
            1 to HistoricalScoreGroup(
                teamLabel = TeamLabel.None,
                primaryScoreList = listOf(
                    HistoricalScore(0, "0", 0),
                    HistoricalScore(1, "1", 2000),
                    HistoricalScore(2, "2", 4400),
                    HistoricalScore(3, "3", 5655),
                    HistoricalScore(4, "4", 9800),
                ),
                secondaryScoreList = listOf()
            )
        )
    )

    private val mockHistoricalInterval2 = HistoricalInterval(
        range = HistoricalIntervalRange.Infinite,
        intervalLabel = IntervalLabel.DefaultSport(SportType.SPIKEBALL, 1),
        historicalScoreGroupList = mapOf()
    )

    private val mockHistoricalInterval3 = HistoricalInterval(
        range = HistoricalIntervalRange.CountDown(72000),
        intervalLabel = IntervalLabel.Custom("Quarter", 0),
        historicalScoreGroupList = mapOf(
            0 to HistoricalScoreGroup(
                teamLabel = TeamLabel.Custom("DGNT", TeamIcon.AXE),
                primaryScoreList = listOf(
                    HistoricalScore(0, "10", 720000),
                    HistoricalScore(1, "11", 660000),
                    HistoricalScore(2, "12", 640000),
                    HistoricalScore(3, "13", 480000),
                    HistoricalScore(4, "14", 330000),
                    HistoricalScore(7, "17", 320000),
                ),
                secondaryScoreList = listOf(
                    HistoricalScore(1, "11", 2)
                )
            ),
            1 to HistoricalScoreGroup(
                teamLabel = TeamLabel.None,
                primaryScoreList = listOf(
                    HistoricalScore(0, "0", 0),
                    HistoricalScore(1, "1", 2000),
                    HistoricalScore(2, "2", 4400),
                    HistoricalScore(3, "3", 5655),
                    HistoricalScore(4, "4", 9800),
                ),
                secondaryScoreList = listOf()
            )
        )
    )

    private fun initSut() {
        sut = TimelineViewerViewModel(
            savedStateHandle,
            getHistoryUseCase,
            uiEventHandler
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        every { savedStateHandle.get<Int>(NavArguments.INDEX) } returns 0
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 1
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryModel(
                id = 1,
                title = "Spike",
                description = "Craziness",
                icon = SportIcon.TENNIS,
                lastModified = DateTime(2024, 5, 12, 15, 15),
                createdAt = DateTime(2024, 5, 10, 12, 10),
                historicalScoreboard = HistoricalScoreboard(
                    mapOf(
                        0 to mockHistoricalInterval1,
                        1 to mockHistoricalInterval2,
                        2 to mockHistoricalInterval3,
                    )
                ),
                temporary = false
            )
        }
    }

    @Test
    fun testInit() = runTest {
        initSut()
        Assert.assertEquals(mockHistoricalInterval1, (sut.state.value.historicalIntervalState as HistoricalIntervalState.Loaded).historicalInterval)
    }

    @Test
    fun testOnDismiss() = runTest {
        initSut()
        sut.onAction(TimelineViewerAction.Dismiss)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
    }

    @Test
    fun testOnNewInterval() = runTest {
        initSut()
        sut.onAction(TimelineViewerAction.NewInterval(true))
        Assert.assertEquals(mockHistoricalInterval2, (sut.state.value.historicalIntervalState as HistoricalIntervalState.Loaded).historicalInterval)
        sut.onAction(TimelineViewerAction.NewInterval(true))
        Assert.assertEquals(mockHistoricalInterval3, (sut.state.value.historicalIntervalState as HistoricalIntervalState.Loaded).historicalInterval)
        sut.onAction(TimelineViewerAction.NewInterval(false))
        Assert.assertEquals(mockHistoricalInterval2, (sut.state.value.historicalIntervalState as HistoricalIntervalState.Loaded).historicalInterval)
        sut.onAction(TimelineViewerAction.NewInterval(true))
        sut.onAction(TimelineViewerAction.NewInterval(true))
        Assert.assertEquals(mockHistoricalInterval1, (sut.state.value.historicalIntervalState as HistoricalIntervalState.Loaded).historicalInterval)
        sut.onAction(TimelineViewerAction.NewInterval(false))
        Assert.assertEquals(mockHistoricalInterval3, (sut.state.value.historicalIntervalState as HistoricalIntervalState.Loaded).historicalInterval)
    }
}