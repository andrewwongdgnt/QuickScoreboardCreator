package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
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
    private lateinit var insertTeamUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamUseCase

    @MockK
    private lateinit var getTeamUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamUseCase

    @MockK
    private lateinit var deleteTeamUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase

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
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getTeamUseCase(1) } coAnswers { TeamEntity(1, "team name", "team desc", TeamIcon.ALIEN) }
        initSut()

        Assert.assertEquals("team name", sut.title.value)
        Assert.assertEquals("team desc", sut.description.value)
        Assert.assertEquals(TeamIcon.ALIEN, sut.icon.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingNoTeam() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        Assert.assertTrue(sut.title.value.isEmpty())
        Assert.assertTrue(sut.description.value.isEmpty())
        Assert.assertNotNull(sut.icon.value)
        Assert.assertFalse(sut.valid.value)
    }

    @Test
    fun testValidation() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        Assert.assertFalse(sut.valid.value)
        sut.onDescriptionChange("Some value")
        Assert.assertFalse(sut.valid.value)
        sut.onTitleChange("Some value")
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testTeamIconChange() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        sut.onIconEdit()
        Assert.assertTrue(sut.iconChanging.value)
        sut.onIconChange(TeamIcon.BEAR)
        Assert.assertEquals(TeamIcon.BEAR, sut.icon.value)
        Assert.assertFalse(sut.iconChanging.value)
    }

    @Test
    fun testOnDismiss() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        sut.onDismiss()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
    }

    @Test
    fun testInsertingNewTeam() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        sut.onTitleChange("new team")
        sut.onDescriptionChange("new team desc")
        sut.onIconChange(TeamIcon.DRAGON)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            insertTeamUseCase.invoke(
                TeamEntity(
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
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 2
        coEvery { getTeamUseCase(2) } coAnswers { TeamEntity(2, "team name 2", "team desc 2", TeamIcon.TREE) }
        initSut()

        sut.onTitleChange("new team")
        sut.onDescriptionChange("new team desc")
        sut.onIconChange(TeamIcon.DRAGON)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            insertTeamUseCase.invoke(
                TeamEntity(
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
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 2
        coEvery { getTeamUseCase(2) } coAnswers { TeamEntity(2, "team name 2", "team desc 2", TeamIcon.TREE) }
        initSut()

        sut.onDelete()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            deleteTeamUseCase.invoke(
                TeamEntity(
                    id = 2,
                    title = "team name 2",
                    description = "team desc 2",
                    icon = TeamIcon.TREE
                )

            )
        }
    }

}