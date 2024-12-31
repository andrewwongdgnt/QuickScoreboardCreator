package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.CategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardType
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.DeleteSportUseCase
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.GetSportListUseCase
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.InsertSportListUseCase
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIdentifier
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

    private lateinit var sut: com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist.SportListViewModel

    private val mockScoreboardList = listOf(
        ScoreboardEntity(1, "scoreboard name", "scoreboard desc", WinRule.Count, ScoreboardIcon.SOCCER, intervalLabel = "game"),
        ScoreboardEntity(2, "scoreboard name 2", "scoreboard desc 2", WinRule.Count, ScoreboardIcon.SOCCER, intervalLabel = "game")
    )
    private lateinit var mockFlowScoreboardList: Flow<List<ScoreboardEntity>>
    private val mockCategorizedScoreboardList =
        CategorizedScoreboardType(
            listOf(
                ScoreboardType.BASKETBALL,
                ScoreboardType.HOCKEY,
                ScoreboardType.TENNIS,
            )
        ) to CategorizedScoreboardItemData(
            listOf(
                com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportListItem(
                    ScoreboardIdentifier.Custom(1),
                    title = "A team name",
                    description = "team desc",
                    icon = ScoreboardIcon.TENNIS
                ),
                com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportListItem(
                    ScoreboardIdentifier.Custom(2),
                    title = "Next team name",
                    description = "team desc",
                    icon = ScoreboardIcon.TENNIS
                )
            )
        )


    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        mockFlowScoreboardList = flow { emit(mockScoreboardList) }
        every { getSportListUseCase() } answers { mockFlowScoreboardList }
        every {
            categorizeSportUseCase(
                listOf(
                    ScoreboardType.BASKETBALL,
                    ScoreboardType.HOCKEY,
                    ScoreboardType.SPIKEBALL,
                    ScoreboardType.TENNIS
                ), mockScoreboardList
            )
        } answers { mockCategorizedScoreboardList }
        coEvery { insertSportListUseCase(mockScoreboardList) } returns listOf(1, 2)
        sut = com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist.SportListViewModel(
            getSportListUseCase,
            insertSportListUseCase,
            deleteSportUseCase,
            categorizeSportUseCase,
            uiEventHandler
        )
    }

    @Test
    fun testOnAdd() = runTest {
        sut.onAdd()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.SportDetails())
        }
    }

    @Test
    fun testOnEdit() = runTest {
        sut.onEdit(ScoreboardIdentifier.Default(ScoreboardType.TENNIS))
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.SportDetails(ScoreboardIdentifier.Default(ScoreboardType.TENNIS)))
        }
    }

    @Test
    fun testOnDeleteAndUndo() = runTest {
        sut.onDelete(1)
        verify(exactly = 1) {
            sut.sendUiEvent(
                UiEvent.SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedScoreboardMsg,
                    quantity = 1,
                    action = R.string.undo
                )
            )
        }
        sut.onDelete(2)
        verify(exactly = 1) {
            sut.sendUiEvent(
                UiEvent.SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedScoreboardMsg,
                    quantity = 2,
                    action = R.string.undo
                )
            )
        }
        sut.onUndoDelete()
        coVerify(exactly = 1) { deleteSportUseCase(mockScoreboardList[0]) }
        coVerify(exactly = 1) { deleteSportUseCase(mockScoreboardList[1]) }
        coVerify(exactly = 1) { insertSportListUseCase(mockScoreboardList) }
    }

    @Test
    fun testNoUndo() = runTest {
        sut.onDelete(1)
        verify(exactly = 1) {
            sut.sendUiEvent(
                UiEvent.SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedScoreboardMsg,
                    quantity = 1,
                    action = R.string.undo
                )
            )
        }
        sut.onDelete(2)
        verify(exactly = 1) {
            sut.sendUiEvent(
                UiEvent.SnackBar.QuantitySnackBar(
                    message = R.plurals.deletedScoreboardMsg,
                    quantity = 2,
                    action = R.string.undo
                )
            )
        }
        sut.onClearDeletedSportList()
        sut.onUndoDelete()
        coVerify(exactly = 1) { deleteSportUseCase(mockScoreboardList[0]) }
        coVerify(exactly = 1) { deleteSportUseCase(mockScoreboardList[1]) }
        coVerify(exactly = 0) { insertSportListUseCase(mockScoreboardList) }
    }

    @Test
    fun testOnLaunch() = runTest {
        sut.onLaunch(ScoreboardIdentifier.Default(ScoreboardType.TENNIS))
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.LaunchScoreboard(ScoreboardIdentifier.Default(ScoreboardType.TENNIS)))
        }
    }
}