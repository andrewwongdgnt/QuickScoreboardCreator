package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.core.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardType
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.presentation.timelineviewer.TimelineViewerViewModel
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
    private lateinit var getHistoryUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase

    @MockK
    private lateinit var historyScoreboardDomainMapper: Mapper<HistoricalScoreboardData, HistoricalScoreboard>

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: TimelineViewerViewModel

    private val mockHistoricalInterval1 = com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval(
        range = com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange.CountDown(72000),
        intervalLabel = IntervalLabel.Custom("Quarter", 0),
        historicalScoreGroupList = mapOf(
            0 to com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup(
                teamLabel = TeamLabel.Custom("DGNT", TeamIcon.AXE),
                primaryScoreList = listOf(
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(0, "0", 720000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(1, "1", 660000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(2, "2", 630000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(3, "3", 480000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(4, "4", 330000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(7, "7", 300000),
                ),
                secondaryScoreList = listOf(
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(1, "1", 720000)
                )
            ),
            1 to com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup(
                teamLabel = TeamLabel.None,
                primaryScoreList = listOf(
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(0, "0", 0),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(1, "1", 2000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(2, "2", 4400),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(3, "3", 5655),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(4, "4", 9800),
                ),
                secondaryScoreList = listOf()
            )
        )
    )

    private val mockHistoricalInterval2 = com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval(
        range = com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange.Infinite,
        intervalLabel = IntervalLabel.DefaultSport(ScoreboardType.SPIKEBALL, 1),
        historicalScoreGroupList = mapOf()
    )

    private val mockHistoricalInterval3 = com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval(
        range = com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange.CountDown(72000),
        intervalLabel = IntervalLabel.Custom("Quarter", 0),
        historicalScoreGroupList = mapOf(
            0 to com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup(
                teamLabel = TeamLabel.Custom("DGNT", TeamIcon.AXE),
                primaryScoreList = listOf(
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(0, "10", 720000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(1, "11", 660000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(2, "12", 640000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(3, "13", 480000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(4, "14", 330000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(7, "17", 320000),
                ),
                secondaryScoreList = listOf(
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(1, "11", 2)
                )
            ),
            1 to com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup(
                teamLabel = TeamLabel.None,
                primaryScoreList = listOf(
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(0, "0", 0),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(1, "1", 2000),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(2, "2", 4400),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(3, "3", 5655),
                    com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore(4, "4", 9800),
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
        every { savedStateHandle.get<Int>(Arguments.INDEX) } returns 0
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getHistoryUseCase(1) } coAnswers {
            com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel(
                id = 1,
                title = "Spike",
                description = "Craziness",
                icon = ScoreboardIcon.TENNIS,
                lastModified = DateTime(2024, 5, 12, 15, 15),
                createdAt = DateTime(2024, 5, 10, 12, 10),
                historicalScoreboard = HistoricalScoreboard(mapOf()),
                temporary = false
            )
        }
        every { historyScoreboardDomainMapper.map(any())} returns HistoricalScoreboard(
            mapOf(
                0 to mockHistoricalInterval1,
                1 to mockHistoricalInterval2,
                2 to mockHistoricalInterval3,
            )
        ) 

    }

    @Test
    fun testInit() = runTest {
        initSut()
        Assert.assertEquals(mockHistoricalInterval1, sut.historicalInterval.value)
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
    fun testOnNewInterval() = runTest {
        initSut()
        sut.onNewInterval(true)
        Assert.assertEquals(mockHistoricalInterval2, sut.historicalInterval.value)
        sut.onNewInterval(true)
        Assert.assertEquals(mockHistoricalInterval3, sut.historicalInterval.value)
        sut.onNewInterval(false)
        Assert.assertEquals(mockHistoricalInterval2, sut.historicalInterval.value)
        sut.onNewInterval(true)
        sut.onNewInterval(true)
        Assert.assertEquals(mockHistoricalInterval1, sut.historicalInterval.value)
        sut.onNewInterval(false)
        Assert.assertEquals(mockHistoricalInterval3, sut.historicalInterval.value)
    }
}