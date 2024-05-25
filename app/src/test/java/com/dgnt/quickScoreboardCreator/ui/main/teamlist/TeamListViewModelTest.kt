package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.domain.team.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamItemData
import com.dgnt.quickScoreboardCreator.domain.team.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.InsertTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var getTeamListUseCase: GetTeamListUseCase

    @MockK
    private lateinit var insertTeamListUseCase: InsertTeamListUseCase

    @MockK
    private lateinit var deleteTeamUseCase: DeleteTeamUseCase

    @MockK
    private lateinit var teamCategorizer: TeamCategorizer

    private lateinit var sut: TeamListViewModel

    private val mockTeamList = listOf(
        TeamEntity(1, "team name", "team desc", TeamIcon.ALIEN),
        TeamEntity(2, "team name 2", "team desc 2", TeamIcon.CYBORG),
    )
    private val mockCategorizedTeamList = listOf(
        CategorizedTeamItemData(
            "A",
            listOf(
                TeamItemData(
                    id = 1,
                    title = "A team name",
                    description = "team desc",
                    teamIcon = TeamIcon.ALIEN
                ),
                TeamItemData(
                    id = 2,
                    title = "a team name 2",
                    description = "team desc 2",
                    teamIcon = TeamIcon.ALIEN
                )
            )
        ),
        CategorizedTeamItemData(
            "F",
            listOf(
                TeamItemData(
                    id = 4,
                    title = "F team name",
                    description = "team desc",
                    teamIcon = TeamIcon.ANT
                ),
                TeamItemData(
                    id = 6,
                    title = "F team name 2",
                    description = "team desc 2",
                    teamIcon = TeamIcon.BOOK
                )
            )
        )
    )
    private lateinit var mockFlowTeamList: Flow<List<TeamEntity>>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        mockFlowTeamList = flow { emit(mockTeamList) }
        every { getTeamListUseCase() } answers { mockFlowTeamList }
        every {
            teamCategorizer(mockTeamList)
        } answers { mockCategorizedTeamList }
        coEvery { insertTeamListUseCase(mockTeamList) } returns listOf(1, 2)
        sut = TeamListViewModel(
            getTeamListUseCase,
            insertTeamListUseCase,
            deleteTeamUseCase,
            teamCategorizer
        )
    }

    @Test
    fun testOnAdd() = runTest {
        sut.onEvent(TeamListEvent.OnAdd)
        Assert.assertEquals(UiEvent.TeamDetails(), sut.uiEvent.first())
    }

    @Test
    fun testOnEdit() = runTest {
        sut.onEvent(TeamListEvent.OnEdit(1))
        Assert.assertEquals(UiEvent.TeamDetails(1), sut.uiEvent.first())
    }

    @Test
    fun testOnDeleteAndUndo() = runTest {
        sut.onEvent(TeamListEvent.OnDelete(1))
        Assert.assertEquals(
            UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                message = R.plurals.deletedTeamMsg,
                quantity = 1,
                action = R.string.undo
            ), sut.uiEvent.first()
        )
        sut.onEvent(TeamListEvent.OnDelete(2))
        Assert.assertEquals(
            UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                message = R.plurals.deletedTeamMsg,
                quantity = 2,
                action = R.string.undo
            ), sut.uiEvent.first()
        )
        sut.onEvent(TeamListEvent.OnUndoDelete)
        coVerify(exactly = 1) { deleteTeamUseCase(mockTeamList[0]) }
        coVerify(exactly = 1) { deleteTeamUseCase(mockTeamList[1]) }
        coVerify(exactly = 1) { insertTeamListUseCase(mockTeamList) }
    }

    @Test
    fun testNoUndo() = runTest {
        sut.onEvent(TeamListEvent.OnDelete(1))
        Assert.assertEquals(
            UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                message = R.plurals.deletedTeamMsg,
                quantity = 1,
                action = R.string.undo
            ), sut.uiEvent.first()
        )
        sut.onEvent(TeamListEvent.OnDelete(2))
        Assert.assertEquals(
            UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                message = R.plurals.deletedTeamMsg,
                quantity = 2,
                action = R.string.undo
            ), sut.uiEvent.first()
        )
        sut.onEvent(TeamListEvent.OnClearDeletedTeamList)
        sut.onEvent(TeamListEvent.OnUndoDelete)
        coVerify(exactly = 1) { deleteTeamUseCase(mockTeamList[0]) }
        coVerify(exactly = 1) { deleteTeamUseCase(mockTeamList[1]) }
        coVerify(exactly = 0) { insertTeamListUseCase(any()) }
    }

}