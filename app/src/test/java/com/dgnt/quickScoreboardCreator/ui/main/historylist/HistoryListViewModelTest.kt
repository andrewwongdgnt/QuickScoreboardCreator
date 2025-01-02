package com.dgnt.quickScoreboardCreator.ui.main.historylist

import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.HistoryCategorizer
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryItemData
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryListUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryListUseCase
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.presentation.historylist.HistoryListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var getHistoryListUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryListUseCase

    @MockK
    private lateinit var insertHistoryListUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryListUseCase

    @MockK
    private lateinit var deleteHistoryUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase

    @MockK
    private lateinit var historyCategorizer: com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.HistoryCategorizer

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: HistoryListViewModel

    private val mockHistoryList = listOf(
        HistoryEntity(
            id = 1,
            title = "Basketball",
            description = "BBall",
            icon = ScoreboardIcon.BASKETBALL,
            lastModified = DateTime(2024, 1, 15, 12, 10),
            createdAt = DateTime(2024, 1, 15, 12, 10),
            historicalScoreboard = HistoricalScoreboardData(mapOf()),
            temporary = false
        ),
        HistoryEntity(
            id = 2,
            title = "Spikeball",
            description = "ss",
            icon = ScoreboardIcon.TENNIS,
            lastModified = DateTime(2024, 2, 14, 11, 0),
            createdAt = DateTime(2024, 2, 14, 11, 0),
            historicalScoreboard = HistoricalScoreboardData(mapOf()),
            temporary = false
        ),
    )
    private val mockCategorizedHistoryList = listOf(
        com.dgnt.quickScoreboardCreator.feature.history.domain.model.CategorizedHistoryItemData(
            DateTime(2024, 6, 1, 0, 0),
            listOf(
                com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryItemData(1, "Tennis", "22", ScoreboardIcon.TENNIS, DateTime(2024, 6, 2, 10, 0), DateTime(2024, 6, 2, 10, 0)),
                com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryItemData(2, "Tennis", "3443", ScoreboardIcon.TENNIS, DateTime(2024, 6, 1, 9, 0), DateTime(2024, 6, 1, 9, 0)),
            )
        ),
        com.dgnt.quickScoreboardCreator.feature.history.domain.model.CategorizedHistoryItemData(
            DateTime(2024, 5, 1, 0, 0),
            listOf(
                com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryItemData(4, "Basketball NBA", "something", ScoreboardIcon.BASKETBALL, DateTime(2024, 5, 2, 14, 0), DateTime(2024, 5, 2, 14, 0)),
                com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryItemData(7, "Hockey NHL", "something again", ScoreboardIcon.HOCKEY, DateTime(2024, 5, 2, 10, 0), DateTime(2024, 5, 2, 10, 0)),
            )
        )
    )
    private lateinit var mockFlowHistoryList: Flow<List<HistoryEntity>>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        mockFlowHistoryList = flow { emit(mockHistoryList) }
        every { getHistoryListUseCase() } answers { mockFlowHistoryList }
        every {
            historyCategorizer(mockHistoryList)
        } answers { mockCategorizedHistoryList }
        coEvery { insertHistoryListUseCase(mockHistoryList) } returns listOf(1, 2)
        sut = HistoryListViewModel(
            getHistoryListUseCase,
            insertHistoryListUseCase,
            deleteHistoryUseCase,
            historyCategorizer,
            uiEventHandler
        )
    }

    @Test
    fun testOnEdit() = runTest {
        sut.onEdit(1)
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.HistoryDetails(1))
        }
    }

    @Test
    fun testOnDeleteAndUndo() = runTest {
        sut.onDelete(1)
        verify(exactly = 1) {
            sut.sendUiEvent(
                UiEvent.SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedHistoryMsg,
                    quantity = 1,
                    action = R.string.undo
                )
            )
        }
        sut.onDelete(2)
        verify(exactly = 1) {
            sut.sendUiEvent(
                UiEvent.SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedHistoryMsg,
                    quantity = 2,
                    action = R.string.undo
                )
            )
        }
        sut.onUndoDelete()
        coVerify(exactly = 1) { deleteHistoryUseCase(mockHistoryList[0]) }
        coVerify(exactly = 1) { deleteHistoryUseCase(mockHistoryList[1]) }
        coVerify(exactly = 1) { insertHistoryListUseCase(mockHistoryList) }
    }

    @Test
    fun testNoUndo() = runTest {
        sut.onDelete(1)
        verify(exactly = 1) {
            sut.sendUiEvent(
                UiEvent.SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedHistoryMsg,
                    quantity = 1,
                    action = R.string.undo
                )
            )
        }
        sut.onDelete(2)
        verify(exactly = 1) {
            sut.sendUiEvent(
                UiEvent.SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedHistoryMsg,
                    quantity = 2,
                    action = R.string.undo
                )
            )
        }
        sut.onClearDeletedHistoryList()
        sut.onUndoDelete()
        coVerify(exactly = 1) { deleteHistoryUseCase(mockHistoryList[0]) }
        coVerify(exactly = 1) { deleteHistoryUseCase(mockHistoryList[1]) }
        coVerify(exactly = 0) { insertHistoryListUseCase(any()) }
    }

    @Test
    fun testOnLaunch() = runTest {
        sut.onLaunch(1)
        //TODO test this when method has been implemented
    }

}