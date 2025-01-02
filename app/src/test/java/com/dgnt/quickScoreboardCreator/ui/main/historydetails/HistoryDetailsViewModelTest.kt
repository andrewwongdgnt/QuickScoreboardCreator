package com.dgnt.quickScoreboardCreator.ui.main.historydetails

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryUseCase
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.presentation.historydetails.HistoryDetailsViewModel
import io.mockk.MockKAnnotations
import io.mockk.awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
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
class HistoryDetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var insertHistoryUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryUseCase

    @MockK
    private lateinit var getHistoryUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase

    @MockK
    private lateinit var deleteHistoryUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: HistoryDetailsViewModel

    private fun initSut() {
        sut = HistoryDetailsViewModel(
            insertHistoryUseCase,
            getHistoryUseCase,
            deleteHistoryUseCase,
            uiEventHandler,
            savedStateHandle,
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { insertHistoryUseCase(any()) } coAnswers { 1 }
    }

    @Test
    fun testInitializingAHistory() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryEntity(
                id = 1,
                title = "history name",
                description = "history desc",
                icon = ScoreboardIcon.TENNIS,
                lastModified = DateTime(2024, 1, 15, 12, 10),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboardData(mapOf()),
                temporary = false
            )
        }
        initSut()

        Assert.assertEquals("history name", sut.title.value)
        Assert.assertEquals("history desc", sut.description.value)
        Assert.assertEquals(ScoreboardIcon.TENNIS, sut.icon.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testValidation() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryEntity(
                id = 1,
                title = "history name",
                description = "history desc",
                icon = ScoreboardIcon.TENNIS,
                lastModified = DateTime(2024, 1, 15, 12, 10),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboardData(mapOf()),
                temporary = false
            )
        }
        initSut()

        sut.onTitleChange("")
        Assert.assertFalse(sut.valid.value)
        sut.onDescriptionChange("Some value")
        Assert.assertFalse(sut.valid.value)
        sut.onTitleChange("Some value")
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testHistoryIconChange() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryEntity(
                id = 1,
                title = "history name",
                description = "history desc",
                icon = ScoreboardIcon.TENNIS,
                lastModified = DateTime(2024, 1, 15, 12, 10),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboardData(mapOf()),
                temporary = false
            )
        }
        initSut()

        sut.onIconEdit()
        Assert.assertTrue(sut.iconChanging.value)
        sut.onIconChange(ScoreboardIcon.SOCCER)
        Assert.assertEquals(ScoreboardIcon.SOCCER, sut.icon.value)
        Assert.assertFalse(sut.iconChanging.value)
    }

    @Test
    fun testOnDismiss() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryEntity(
                id = 1,
                title = "history name",
                description = "history desc",
                icon = ScoreboardIcon.TENNIS,
                lastModified = DateTime(2024, 1, 15, 12, 10),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboardData(mapOf()),
                temporary = false
            )
        }
        initSut()

        sut.onDismiss()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
    }

    @Test
    fun testEditingAHistory() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 2
        coEvery { getHistoryUseCase(2) } coAnswers {
            HistoryEntity(
                id = 2,
                title = "history name 2",
                description = "history desc 2",
                icon = ScoreboardIcon.TENNIS,
                lastModified = DateTime(2024, 3, 12, 15, 15),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboardData(mapOf()),
                temporary = false
            )
        }
        val historyEntitySlot = slot<HistoryEntity>()
        coEvery { insertHistoryUseCase.invoke(capture(historyEntitySlot)) } just awaits
        initSut()


        sut.onTitleChange("new history")
        sut.onDescriptionChange("new history desc")
        sut.onIconChange(ScoreboardIcon.HOCKEY)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        historyEntitySlot.captured.let {
            Assert.assertEquals(2, it.id)
            Assert.assertEquals("new history", it.title)
            Assert.assertEquals("new history desc", it.description)
            Assert.assertEquals(ScoreboardIcon.HOCKEY, it.icon)
            Assert.assertEquals(DateTime(2024, 1, 15, 12, 10), it.createdAt)
            Assert.assertEquals(HistoricalScoreboardData(mapOf()), it.historicalScoreboard)
            Assert.assertFalse(it.temporary)
        }

    }

    @Test
    fun testDeletingAHistory() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 2
        coEvery { getHistoryUseCase(2) } coAnswers {
            HistoryEntity(
                id = 2,
                title = "history name 2",
                description = "history desc 2",
                icon = ScoreboardIcon.TENNIS,
                lastModified = DateTime(2024, 3, 12, 15, 15),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboardData(mapOf()),
                temporary = false
            )
        }
        initSut()

        sut.onDelete()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            deleteHistoryUseCase.invoke(
                HistoryEntity(
                    id = 2,
                    title = "history name 2",
                    description = "history desc 2",
                    icon = ScoreboardIcon.TENNIS,
                    lastModified = DateTime(2024, 3, 12, 15, 15),
                    createdAt = DateTime(2024, 1, 15, 12, 10),
                    historicalScoreboard = HistoricalScoreboardData(mapOf()),
                    temporary = false
                )

            )
        }
    }

}