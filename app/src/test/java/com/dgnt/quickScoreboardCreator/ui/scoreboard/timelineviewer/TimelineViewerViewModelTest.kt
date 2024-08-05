package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.common.mapper.Mapper
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.history.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimelineViewerViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var getHistoryUseCase: GetHistoryUseCase

    @MockK
    private lateinit var historyScoreboardDomainMapper: Mapper<HistoricalScoreboardData, HistoricalScoreboard>

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: TimelineViewerViewModel

    private fun initSut() {
        sut = TimelineViewerViewModel(
            savedStateHandle,
            getHistoryUseCase,
            historyScoreboardDomainMapper,
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
            HistoryEntity(
                id = 1,
                title = "Spike",
                description = "Craziness",
                icon = ScoreboardIcon.TENNIS,
                lastModified = DateTime(2024, 5, 12, 15, 15),
                createdAt = DateTime(2024, 5, 10, 12, 10),
                historicalScoreboard = HistoricalScoreboardData(mapOf()),
                temporary = false
            )
        }
        every { historyScoreboardDomainMapper.map(any())} returns HistoricalScoreboard(mapOf()) //TODO create a proper mock object for the return

    }

    @Test
    fun testOnDismiss() = runTest {
        initSut()
        sut.onDismiss()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
    }
}