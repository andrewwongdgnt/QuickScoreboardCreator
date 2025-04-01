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
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.ValidateTeamDetailsUseCase
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
    private lateinit var validateTeamDetailsUseCase: ValidateTeamDetailsUseCase

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
            validateTeamDetailsUseCase,
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
        every { validateTeamDetailsUseCase(any()) } returns true
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns 1
        coEvery { getTeamUseCase(1) } coAnswers { TeamModel(1, "team name", "team desc", TeamIcon.ALIEN) }
        initSut()

        Assert.assertEquals("team name", sut.state.value.title)
        Assert.assertEquals("team desc", sut.state.value.description)
        Assert.assertEquals(TeamIcon.ALIEN, (sut.state.value.iconState as TeamIconState.Picked.Displaying).teamIcon)
        Assert.assertTrue(sut.state.value.valid)
    }

    @Test
    fun testInitializingNoTeam() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        Assert.assertTrue(sut.state.value.title.isEmpty())
        Assert.assertTrue(sut.state.value.description.isEmpty())
        Assert.assertFalse(sut.state.value.iconState is TeamIconState.Initial)
        Assert.assertFalse(sut.state.value.valid)
    }

    @Test
    fun testValidation() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        every { validateTeamDetailsUseCase("") } returns false
        every { validateTeamDetailsUseCase("Some value") } returns true
        initSut()

        Assert.assertFalse(sut.state.value.valid)
        sut.onAction(TeamDetailsAction.TitleChange("Some value"))
        Assert.assertTrue(sut.state.value.valid)
        sut.onAction(TeamDetailsAction.TitleChange(""))
        Assert.assertFalse(sut.state.value.valid)
        verify(exactly = 2) { validateTeamDetailsUseCase(any()) }
    }

    @Test
    fun testTeamIconChange() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        sut.onAction(TeamDetailsAction.IconEdit(true))
        Assert.assertTrue(sut.state.value.iconState is TeamIconState.Picked.Changing)
        sut.onAction(TeamDetailsAction.IconChange(TeamIcon.BEAR))
        Assert.assertEquals(TeamIcon.BEAR, (sut.state.value.iconState as TeamIconState.Picked.Displaying).teamIcon)
    }

    @Test
    fun testOnDismiss() = runTest {
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        sut.onAction(TeamDetailsAction.Dismiss)
        verify(exactly = 1) {
            sut.sendUiEvent(Done)
        }
    }

    @Test
    fun testInsertingNewTeam() = runTest {
        every { validateTeamDetailsUseCase(any()) } returns true
        every { savedStateHandle.get<Int>(NavArguments.ID) } returns -1
        initSut()

        sut.onAction(TeamDetailsAction.TitleChange("new team"))
        sut.onAction(TeamDetailsAction.DescriptionChange("new team desc"))
        sut.onAction(TeamDetailsAction.IconChange(TeamIcon.DRAGON))
        sut.onAction(TeamDetailsAction.Confirm)
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
        every { validateTeamDetailsUseCase(any()) } returns true
        coEvery { getTeamUseCase(2) } coAnswers { TeamModel(2, "team name 2", "team desc 2", TeamIcon.TREE) }
        initSut()

        sut.onAction(TeamDetailsAction.TitleChange("new team"))
        sut.onAction(TeamDetailsAction.DescriptionChange("new team desc"))
        sut.onAction(TeamDetailsAction.IconChange(TeamIcon.DRAGON))
        sut.onAction(TeamDetailsAction.Confirm)
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
        every { validateTeamDetailsUseCase(any()) } returns true
        coEvery { getTeamUseCase(2) } coAnswers { TeamModel(2, "team name 2", "team desc 2", TeamIcon.TREE) }
        initSut()

        sut.onAction(TeamDetailsAction.Delete)
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