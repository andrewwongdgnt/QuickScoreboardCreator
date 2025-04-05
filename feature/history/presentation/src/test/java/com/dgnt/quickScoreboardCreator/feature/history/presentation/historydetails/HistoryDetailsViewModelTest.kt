package com.dgnt.quickScoreboardCreator.feature.history.presentation.historydetails

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.ValidateHistoryDetailsUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
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
    private lateinit var insertHistoryUseCase: InsertHistoryUseCase

    @MockK
    private lateinit var getHistoryUseCase: GetHistoryUseCase

    @MockK
    private lateinit var deleteHistoryUseCase: DeleteHistoryUseCase

    @MockK
    private lateinit var validateHistoryDetailsUseCase: ValidateHistoryDetailsUseCase

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
            validateHistoryDetailsUseCase,
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
        every { validateHistoryDetailsUseCase(any()) } returns true
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 1
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryModel(
                id = 1,
                title = "history name",
                description = "history desc",
                icon = SportIcon.TENNIS,
                lastModified = DateTime(2024, 1, 15, 12, 10),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboard(mapOf()),
                temporary = false
            )
        }
        initSut()

        Assert.assertEquals("history name", sut.state.value.title)
        Assert.assertEquals("history desc", sut.state.value.description)
        Assert.assertEquals(SportIcon.TENNIS, (sut.state.value.iconState as HistoryIconState.Picked.Displaying).sportIcon)
        Assert.assertTrue(sut.state.value.valid)
    }

    @Test
    fun testValidation() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 1
        every { validateHistoryDetailsUseCase("history name") } returns true
        every { validateHistoryDetailsUseCase("") } returns false
        every { validateHistoryDetailsUseCase("Some value") } returns true
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryModel(
                id = 1,
                title = "history name",
                description = "history desc",
                icon = SportIcon.TENNIS,
                lastModified = DateTime(2024, 1, 15, 12, 10),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboard(mapOf()),
                temporary = false
            )
        }
        initSut()

        sut.onAction(HistoryDetailsAction.TitleChange(""))
        Assert.assertFalse(sut.state.value.valid)
        sut.onAction(HistoryDetailsAction.TitleChange("Some value"))
        Assert.assertTrue(sut.state.value.valid)
        verify(exactly = 3) { validateHistoryDetailsUseCase(any()) }
    }

    @Test
    fun testHistoryIconChange() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 1
        every { validateHistoryDetailsUseCase(any()) } returns true
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryModel(
                id = 1,
                title = "history name",
                description = "history desc",
                icon = SportIcon.TENNIS,
                lastModified = DateTime(2024, 1, 15, 12, 10),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboard(mapOf()),
                temporary = false
            )
        }
        initSut()

        sut.onAction(HistoryDetailsAction.IconEdit(true))
        Assert.assertTrue(sut.state.value.iconState is HistoryIconState.Picked.Changing)
        sut.onAction(HistoryDetailsAction.IconChange(SportIcon.SOCCER))
        Assert.assertEquals(SportIcon.SOCCER, (sut.state.value.iconState as HistoryIconState.Picked.Displaying).sportIcon)
    }

    @Test
    fun testOnDismiss() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 1
        every { validateHistoryDetailsUseCase(any()) } returns true
        coEvery { getHistoryUseCase(1) } coAnswers {
            HistoryModel(
                id = 1,
                title = "history name",
                description = "history desc",
                icon = SportIcon.TENNIS,
                lastModified = DateTime(2024, 1, 15, 12, 10),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboard(mapOf()),
                temporary = false
            )
        }
        initSut()

        sut.onAction(HistoryDetailsAction.Dismiss)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
    }

    @Test
    fun testEditingAHistory() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 2
        every { validateHistoryDetailsUseCase(any()) } returns true
        coEvery { getHistoryUseCase(2) } coAnswers {
            HistoryModel(
                id = 2,
                title = "history name 2",
                description = "history desc 2",
                icon = SportIcon.TENNIS,
                lastModified = DateTime(2024, 3, 12, 15, 15),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboard(mapOf()),
                temporary = false
            )
        }
        val HistoryModelSlot = slot<HistoryModel>()
        coEvery { insertHistoryUseCase.invoke(capture(HistoryModelSlot)) } just awaits
        initSut()


        sut.onAction(HistoryDetailsAction.TitleChange("new history"))
        sut.onAction(HistoryDetailsAction.DescriptionChange("new history desc"))
        sut.onAction(HistoryDetailsAction.IconChange(SportIcon.HOCKEY))
        sut.onAction(HistoryDetailsAction.Confirm)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
        HistoryModelSlot.captured.let {
            Assert.assertEquals(2, it.id)
            Assert.assertEquals("new history", it.title)
            Assert.assertEquals("new history desc", it.description)
            Assert.assertEquals(SportIcon.HOCKEY, it.icon)
            Assert.assertEquals(DateTime(2024, 1, 15, 12, 10), it.createdAt)
            Assert.assertEquals(HistoricalScoreboard(mapOf()), it.historicalScoreboard)
            Assert.assertFalse(it.temporary)
        }

    }

    @Test
    fun testDeletingAHistory() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 2
        every { validateHistoryDetailsUseCase(any()) } returns true
        coEvery { getHistoryUseCase(2) } coAnswers {
            HistoryModel(
                id = 2,
                title = "history name 2",
                description = "history desc 2",
                icon = SportIcon.TENNIS,
                lastModified = DateTime(2024, 3, 12, 15, 15),
                createdAt = DateTime(2024, 1, 15, 12, 10),
                historicalScoreboard = HistoricalScoreboard(mapOf()),
                temporary = false
            )
        }
        initSut()

        sut.onAction(HistoryDetailsAction.Delete)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
        coVerify(exactly = 1) {
            deleteHistoryUseCase.invoke(
                HistoryModel(
                    id = 2,
                    title = "history name 2",
                    description = "history desc 2",
                    icon = SportIcon.TENNIS,
                    lastModified = DateTime(2024, 3, 12, 15, 15),
                    createdAt = DateTime(2024, 1, 15, 12, 10),
                    historicalScoreboard = HistoricalScoreboard(mapOf()),
                    temporary = false
                )

            )
        }
    }

}