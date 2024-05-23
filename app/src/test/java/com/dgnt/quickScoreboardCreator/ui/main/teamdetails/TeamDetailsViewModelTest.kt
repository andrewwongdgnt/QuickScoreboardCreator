package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.InsertTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
    private lateinit var insertTeamListUseCase: InsertTeamListUseCase

    @MockK
    private lateinit var getTeamUseCase: GetTeamUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var sut: TeamDetailsViewModel

    private fun initSut() {
        sut = TeamDetailsViewModel(
            insertTeamListUseCase,
            getTeamUseCase,
            savedStateHandle,
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { insertTeamListUseCase.invoke(any()) } answers { listOf(1) }
    }

    @Test
    fun testInitializingATeam() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getTeamUseCase(1) } coAnswers { TeamEntity(1, "team name", "team desc", TeamIcon.ALIEN) }
        initSut()

        Assert.assertEquals("team name", sut.title.value)
        Assert.assertEquals("team desc", sut.description.value)
        Assert.assertEquals(TeamIcon.ALIEN, sut.teamIcon.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingNoTeam() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        Assert.assertTrue(sut.title.value.isEmpty())
        Assert.assertTrue(sut.description.value.isEmpty())
        Assert.assertNotNull(sut.teamIcon.value)
        Assert.assertFalse(sut.valid.value)
    }

    @Test
    fun testValidation() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        Assert.assertFalse(sut.valid.value)
        sut.onEvent(TeamDetailsEvent.OnDescriptionChange("Some value"))
        Assert.assertFalse(sut.valid.value)
        sut.onEvent(TeamDetailsEvent.OnTitleChange("Some value"))
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testTeamIconChange() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        sut.onEvent(TeamDetailsEvent.OnTeamIconEdit)
        Assert.assertTrue(sut.teamIconChanging.value)
        sut.onEvent(TeamDetailsEvent.OnNewTeamIcon(TeamIcon.BEAR))
        Assert.assertEquals(TeamIcon.BEAR, sut.teamIcon.value)
        Assert.assertFalse(sut.teamIconChanging.value)
    }

    @Test
    fun testOnDismiss() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        sut.onEvent(TeamDetailsEvent.OnDismiss)
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
        sut.onEvent(TeamDetailsEvent.OnConfirm)
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
    }

    @Test
    fun testInsertingNewTeam() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        initSut()

        sut.onEvent(TeamDetailsEvent.OnTitleChange("new team"))
        sut.onEvent(TeamDetailsEvent.OnDescriptionChange("new team desc"))
        sut.onEvent(TeamDetailsEvent.OnNewTeamIcon(TeamIcon.DRAGON))
        sut.onEvent(TeamDetailsEvent.OnConfirm)
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
        coVerify(exactly = 1) {
            insertTeamListUseCase.invoke(
                listOf(
                    TeamEntity(
                        id = null,
                        title = "new team",
                        description = "new team desc",
                        teamIcon = TeamIcon.DRAGON
                    )
                )
            )
        }
    }

    @Test
    fun testEditingATeam() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 2
        coEvery { getTeamUseCase(2) } coAnswers { TeamEntity(2, "team name 2", "team desc 2", TeamIcon.TREE) }
        initSut()

        sut.onEvent(TeamDetailsEvent.OnTitleChange("new team"))
        sut.onEvent(TeamDetailsEvent.OnDescriptionChange("new team desc"))
        sut.onEvent(TeamDetailsEvent.OnNewTeamIcon(TeamIcon.DRAGON))
        sut.onEvent(TeamDetailsEvent.OnConfirm)
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
        coVerify(exactly = 1) {
            insertTeamListUseCase.invoke(
                listOf(
                    TeamEntity(
                        id = 2,
                        title = "new team",
                        description = "new team desc",
                        teamIcon = TeamIcon.DRAGON
                    )
                )
            )
        }
    }

}