package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardCategorizer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.DeleteScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
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
class ScoreboardListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var getScoreboardListUseCase: GetScoreboardListUseCase

    @MockK
    private lateinit var insertScoreboardListUseCase: InsertScoreboardListUseCase

    @MockK
    private lateinit var deleteScoreboardUseCase: DeleteScoreboardUseCase

    @MockK
    private lateinit var scoreboardCategorizer: ScoreboardCategorizer

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: ScoreboardListViewModel

    private val mockScoreboardList = listOf(
        ScoreboardEntity(1, "scoreboard name", "scoreboard desc", WinRule.Count, ScoreboardIcon.SOCCER),
        ScoreboardEntity(2, "scoreboard name 2", "scoreboard desc 2", WinRule.Count, ScoreboardIcon.SOCCER)
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
                ScoreboardItemData(
                    ScoreboardIdentifier.Custom(1),
                    title = "A team name",
                    description = "team desc",
                    icon = ScoreboardIcon.TENNIS
                ),
                ScoreboardItemData(
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
        every { getScoreboardListUseCase() } answers { mockFlowScoreboardList }
        every {
            scoreboardCategorizer(
                listOf(
                    ScoreboardType.BASKETBALL,
                    ScoreboardType.HOCKEY,
                    ScoreboardType.SPIKEBALL,
                    ScoreboardType.TENNIS
                ), mockScoreboardList
            )
        } answers { mockCategorizedScoreboardList }
        coEvery { insertScoreboardListUseCase(mockScoreboardList) } returns listOf(1, 2)
        sut = ScoreboardListViewModel(
            getScoreboardListUseCase,
            insertScoreboardListUseCase,
            deleteScoreboardUseCase,
            scoreboardCategorizer,
            uiEventHandler
        )
    }

    @Test
    fun testOnAdd() = runTest {
        sut.onAdd()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.ScoreboardDetails())
        }
    }

    @Test
    fun testOnEdit() = runTest {
        sut.onEdit(ScoreboardIdentifier.Default(ScoreboardType.TENNIS))
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.ScoreboardDetails(ScoreboardIdentifier.Default(ScoreboardType.TENNIS)))
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
        coVerify(exactly = 1) { deleteScoreboardUseCase(mockScoreboardList[0]) }
        coVerify(exactly = 1) { deleteScoreboardUseCase(mockScoreboardList[1]) }
        coVerify(exactly = 1) { insertScoreboardListUseCase(mockScoreboardList) }
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
        sut.onClearDeletedScoreboardList()
        sut.onUndoDelete()
        coVerify(exactly = 1) { deleteScoreboardUseCase(mockScoreboardList[0]) }
        coVerify(exactly = 1) { deleteScoreboardUseCase(mockScoreboardList[1]) }
        coVerify(exactly = 0) { insertScoreboardListUseCase(mockScoreboardList) }
    }

    @Test
    fun testOnLaunch() = runTest {
        sut.onLaunch(ScoreboardIdentifier.Default(ScoreboardType.TENNIS))
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.LaunchScoreboard(ScoreboardIdentifier.Default(ScoreboardType.TENNIS)))
        }
    }
}