package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist


import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.SnackBar
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportListItem
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.CategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.DeleteSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.GetSportListUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.InsertSportListUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent.LaunchScoreboard
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent.SportDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SportListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var getSportListUseCase: GetSportListUseCase

    @MockK
    private lateinit var insertSportListUseCase: InsertSportListUseCase

    @MockK
    private lateinit var deleteSportUseCase: DeleteSportUseCase

    @MockK
    private lateinit var categorizeSportUseCase: CategorizeSportUseCase

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: SportListViewModel

    private val mockSportList = listOf(
        SportModel(
            sportIdentifier = SportIdentifier.Custom(1),
            "scoreboard name",
            "scoreboard desc",
            WinRule.Count,
            SportIcon.SOCCER,
            intervalLabel = "game",
            intervalList = listOf()
        ),
        SportModel(
            sportIdentifier = SportIdentifier.Custom(2),
            "scoreboard name 2",
            "scoreboard desc 2",
            WinRule.Count,
            SportIcon.SOCCER,
            intervalLabel = "game",
            intervalList = listOf()
        )
    )
    private lateinit var mockFlowScoreboardList: Flow<List<SportModel>>
    private val mockCategorizedScoreboardList =

        listOf(
            SportType.BASKETBALL,
            SportType.HOCKEY,
            SportType.TENNIS,
        ) to listOf(
            SportListItem(
                SportIdentifier.Custom(1),
                title = "A team name",
                description = "team desc",
                icon = SportIcon.TENNIS
            ),
            SportListItem(
                SportIdentifier.Custom(2),
                title = "Next team name",
                description = "team desc",
                icon = SportIcon.TENNIS
            )
        )


    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        mockFlowScoreboardList = flow { emit(mockSportList) }
        every { getSportListUseCase() } answers { mockFlowScoreboardList }
        every {
            categorizeSportUseCase(
                listOf(
                    SportType.BASKETBALL,
                    SportType.HOCKEY,
                    SportType.SPIKEBALL,
                    SportType.TENNIS
                ), mockSportList
            )
        } answers { mockCategorizedScoreboardList }
        coEvery { insertSportListUseCase(mockSportList) } returns listOf(1, 2)
        sut = SportListViewModel(
            getSportListUseCase,
            insertSportListUseCase,
            deleteSportUseCase,
            categorizeSportUseCase,
            uiEventHandler
        )
    }

    @Test
    fun testOnAdd() = runTest {
        sut.onAction(SportListAction.Add)
        verify(exactly = 1) {
            sut.sendUiEvent(SportDetails())
        }
    }

    @Test
    fun testOnEdit() = runTest {
        sut.onAction(SportListAction.Edit(SportIdentifier.Default(SportType.TENNIS)))
        verify(exactly = 1) {
            sut.sendUiEvent(SportDetails(SportIdentifier.Default(SportType.TENNIS)))
        }
    }

    @Test
    fun testOnDeleteAndUndo() = runTest {
        sut.onAction(SportListAction.Delete(1))
        verify(exactly = 1) {
            sut.sendUiEvent(
                SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedSportMsg,
                    quantity = 1,
                    action = R.string.undo
                )
            )
        }
        sut.onAction(SportListAction.Delete(2))
        verify(exactly = 1) {
            sut.sendUiEvent(
                SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedSportMsg,
                    quantity = 2,
                    action = R.string.undo
                )
            )
        }
        sut.onAction(SportListAction.UndoDelete)
        coVerify(exactly = 1) { deleteSportUseCase(mockSportList[0]) }
        coVerify(exactly = 1) { deleteSportUseCase(mockSportList[1]) }
        coVerify(exactly = 1) { insertSportListUseCase(mockSportList) }
    }

    @Test
    fun testNoUndo() = runTest {
        sut.onAction(SportListAction.Delete(1))
        verify(exactly = 1) {
            sut.sendUiEvent(
                SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedSportMsg,
                    quantity = 1,
                    action = R.string.undo
                )
            )
        }
        sut.onAction(SportListAction.Delete(2))
        verify(exactly = 1) {
            sut.sendUiEvent(
                SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedSportMsg,
                    quantity = 2,
                    action = R.string.undo
                )
            )
        }
        sut.onAction(SportListAction.ClearDeletedSportList)
        sut.onAction(SportListAction.UndoDelete)
        coVerify(exactly = 1) { deleteSportUseCase(mockSportList[0]) }
        coVerify(exactly = 1) { deleteSportUseCase(mockSportList[1]) }
        coVerify(exactly = 0) { insertSportListUseCase(mockSportList) }
    }

    @Test
    fun testOnLaunch() = runTest {
        sut.onAction(SportListAction.Launch(SportIdentifier.Default(SportType.TENNIS)))
        verify(exactly = 1) {
            sut.sendUiEvent(LaunchScoreboard(SportIdentifier.Default(SportType.TENNIS)))
        }
    }
}