package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardCategorizer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.DeleteScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardListUseCase
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

    private lateinit var sut: ScoreboardListViewModel

    private val mockScoreboardList = listOf(
        ScoreboardEntity(1, "team name", "team desc", true),
        ScoreboardEntity(2, "team name", "team desc", true)
    )
    private lateinit var mockFlowScoreboardList: Flow<List<ScoreboardEntity>>
    private val mockCategorizedScoreboards =
        CategorizedScoreboardType(
            listOf(
                ScoreboardType.BASKETBALL,
                ScoreboardType.HOCKEY,
                ScoreboardType.TENNIS,
            )
        ) to CategorizedScoreboardItemData(
            listOf(
                ScoreboardItemData(
                    id = 1,
                    type = ScoreboardType.NONE,
                    title = "A team name",
                    description = "team desc"
                ),
                ScoreboardItemData(
                    id = 2,
                    type = ScoreboardType.NONE,
                    title = "Next team name",
                    description = "team desc"
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
        } answers { mockCategorizedScoreboards }
        coEvery { insertScoreboardListUseCase(mockScoreboardList) } returns listOf(1, 2)
        sut = ScoreboardListViewModel(
            getScoreboardListUseCase,
            insertScoreboardListUseCase,
            deleteScoreboardUseCase,
            scoreboardCategorizer
        )
    }

    @Test
    fun testOnAdd() = runTest {
        sut.onEvent(ScoreboardListEvent.OnAdd)
        Assert.assertEquals(UiEvent.ScoreboardDetails(), sut.uiEvent.first())
    }

    @Test
    fun testOnEdit() = runTest {
        sut.onEvent(ScoreboardListEvent.OnEdit(1, ScoreboardType.TENNIS))
        Assert.assertEquals(UiEvent.ScoreboardDetails(1, ScoreboardType.TENNIS), sut.uiEvent.first())
    }

    @Test
    fun testOnDeleteAndUndo() = runTest {
        sut.onEvent(ScoreboardListEvent.OnDelete(1))
        Assert.assertEquals(
            UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                message = R.plurals.deletedScoreboardMsg,
                quantity = 1,
                action = R.string.undo
            ), sut.uiEvent.first()
        )
        sut.onEvent(ScoreboardListEvent.OnDelete(2))
        Assert.assertEquals(
            UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                message = R.plurals.deletedScoreboardMsg,
                quantity = 2,
                action = R.string.undo
            ), sut.uiEvent.first()
        )
        sut.onEvent(ScoreboardListEvent.OnUndoDelete)
        coVerify(exactly = 1) { deleteScoreboardUseCase(mockScoreboardList[0]) }
        coVerify(exactly = 1) { deleteScoreboardUseCase(mockScoreboardList[1]) }
        coVerify(exactly = 1) { insertScoreboardListUseCase(mockScoreboardList) }
    }

    @Test
    fun testOnLaunch() = runTest {
        sut.onEvent(ScoreboardListEvent.OnLaunch(1, ScoreboardType.TENNIS))
        Assert.assertEquals(UiEvent.LaunchScoreboard(1, ScoreboardType.TENNIS), sut.uiEvent.first())
    }
}