package com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamItemData
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.team.presentation.teampicker.TeamPickerViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeamPickerViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var teamCategorizer: com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.TeamCategorizer

    @MockK
    private lateinit var getTeamListUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamListUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: TeamPickerViewModel

    private val mockTeamList = listOf(TeamEntity(1, "team name", "team desc", TeamIcon.ALIEN))
    private val mockCategorizedTeamList = listOf(
        com.dgnt.quickScoreboardCreator.feature.team.domain.model.CategorizedTeamItemData(
            "A",
            listOf(
                com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamItemData(
                    id = 1,
                    title = "A team name",
                    description = "team desc",
                    icon = TeamIcon.ALIEN
                ),
                com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamItemData(
                    id = 2,
                    title = "a team name 2",
                    description = "team desc 2",
                    icon = TeamIcon.ALIEN
                )
            )
        ),
        com.dgnt.quickScoreboardCreator.feature.team.domain.model.CategorizedTeamItemData(
            "F",
            listOf(
                com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamItemData(
                    id = 4,
                    title = "F team name",
                    description = "team desc",
                    icon = TeamIcon.ANT
                ),
                com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamItemData(
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
            uiEventHandler
        )
    }

    @Test
    fun testOnDismiss() = runTest {
        sut.onDismiss()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
    }

    @Test
    fun testTeamPicked() = runTest {
        sut.onTeamPicked(2)
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.TeamUpdated(1, 2))
        }
    }

}