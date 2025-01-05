package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamdetails

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamDetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var insertTeamUseCase: InsertTeamUseCase

    @MockK
    private lateinit var getTeamUseCase: GetTeamUseCase

    @MockK
    private lateinit var deleteTeamUseCase: DeleteTeamUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: TeamDetailsViewModel

    private fun initSut() {
        sut = TeamDetailsViewModel(
            insertTeamUseCase,
            getTeamUseCase,
            deleteTeamUseCase,
            uiEventHandler,
            savedStateHandle,
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { insertTeamUseCase(any()) } coAnswers { 1 }
    }

    @Test
    fun testInitializingATeam() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 1
        coEvery { getTeamUseCase(1) } coAnswers { TeamModel(1, "team name", "team desc", TeamIcon.ALIEN) }
        initSut()

        Assert.assertEquals("team name", sut.title.value)
        Assert.assertEquals("team desc", sut.description.value)
        Assert.assertEquals(TeamIcon.ALIEN, sut.icon.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingNoTeam() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        Assert.assertTrue(sut.title.value.isEmpty())
        Assert.assertTrue(sut.description.value.isEmpty())
        Assert.assertNotNull(sut.icon.value)
        Assert.assertFalse(sut.valid.value)
    }

    @Test
    fun testValidation() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        Assert.assertFalse(sut.valid.value)
        sut.onDescriptionChange("Some value")
        Assert.assertFalse(sut.valid.value)
        sut.onTitleChange("Some value")
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testTeamIconChange() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        sut.onIconEdit()
        Assert.assertTrue(sut.iconChanging.value)
        sut.onIconChange(TeamIcon.BEAR)
        Assert.assertEquals(TeamIcon.BEAR, sut.icon.value)
        Assert.assertFalse(sut.iconChanging.value)
    }

    @Test
    fun testOnDismiss() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        sut.onDismiss()
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
    }

    @Test
    fun testInsertingNewTeam() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        sut.onTitleChange("new team")
        sut.onDescriptionChange("new team desc")
        sut.onIconChange(TeamIcon.DRAGON)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
        coVerify(exactly = 1) {
            insertTeamUseCase.invoke(
                TeamModel(
                    id = null,
                    title = "new team",
                    description = "new team desc",
                    icon = TeamIcon.DRAGON
                )
            )
        }
    }

    @Test
    fun testEditingATeam() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 2
        coEvery { getTeamUseCase(2) } coAnswers { TeamModel(2, "team name 2", "team desc 2", TeamIcon.TREE) }
        initSut()

        sut.onTitleChange("new team")
        sut.onDescriptionChange("new team desc")
        sut.onIconChange(TeamIcon.DRAGON)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
        coVerify(exactly = 1) {
            insertTeamUseCase.invoke(
                TeamModel(
                    id = 2,
                    title = "new team",
                    description = "new team desc",
                    icon = TeamIcon.DRAGON
                )
            )
        }
    }

    @Test
    fun testDeletingATeam() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 2
        coEvery { getTeamUseCase(2) } coAnswers { TeamModel(2, "team name 2", "team desc 2", TeamIcon.TREE) }
        initSut()

        sut.onDelete()
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
        coVerify(exactly = 1) {
            deleteTeamUseCase.invoke(
                TeamModel(
                    id = 2,
                    title = "team name 2",
                    description = "team desc 2",
                    icon = TeamIcon.TREE
                )

            )
        }
    }

}