package com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.domain.team.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamItemData
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamPickerViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var teamCategorizer: TeamCategorizer

    @MockK
    private lateinit var getTeamListUseCase: GetTeamListUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var sut: TeamPickerViewModel

    private val mockTeamList = listOf(TeamEntity(1, "team name", "team desc", TeamIcon.ALIEN))
    private val mockCategorizedTeamList = listOf(
        CategorizedTeamItemData(
            "A",
            listOf(
                TeamItemData(
                    id = 1,
                    title = "A team name",
                    description = "team desc",
                    icon = TeamIcon.ALIEN
                ),
                TeamItemData(
                    id = 2,
                    title = "a team name 2",
                    description = "team desc 2",
                    icon = TeamIcon.ALIEN
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
                    icon = TeamIcon.ANT
                ),
                TeamItemData(
                    id = 6,
                    title = "F team name 2",
                    description = "team desc 2",
                    icon = TeamIcon.BOOK
                )
            )
        )
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        every { savedStateHandle.get<Int>(Arguments.INDEX) } returns 1
        coEvery { getTeamListUseCase() } coAnswers { flow { mockTeamList } }
        coEvery { teamCategorizer(mockTeamList) } coAnswers { mockCategorizedTeamList }
        sut = TeamPickerViewModel(
            teamCategorizer,
            getTeamListUseCase,
            savedStateHandle,
        )
    }

    @Test
    fun testOnDismiss() = runTest {
        sut.onDismiss()
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
    }

    @Test
    fun testTeamPicked() = runTest {
        sut.onTeamPicked(2)
        Assert.assertEquals(UiEvent.TeamUpdated(1, 2), sut.uiEvent.first())
    }

}