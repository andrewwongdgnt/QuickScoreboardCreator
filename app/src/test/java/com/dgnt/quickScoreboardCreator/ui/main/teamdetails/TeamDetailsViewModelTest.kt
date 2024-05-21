package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.InsertTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    }

    @Test
    fun testInitializingATeam() {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getTeamUseCase(1) } coAnswers { TeamEntity(1, "team name", "team desc", TeamIcon.ALIEN) }
        initSut()

        Assert.assertEquals("team name", sut.title)
        Assert.assertEquals("team desc", sut.description)
        Assert.assertEquals(TeamIcon.ALIEN, sut.teamIcon)
    }

}