package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.WinRuleType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class ScoreboardDetailsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var resources: Resources

    @MockK
    private lateinit var scoreboardLoader: ScoreboardLoader

    @MockK
    private lateinit var insertScoreboardListUseCase: InsertScoreboardListUseCase

    @MockK
    private lateinit var getScoreboardUseCase: GetScoreboardUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var sut: ScoreboardDetailsViewModel

    private fun initSut() {
        sut = ScoreboardDetailsViewModel(
            resources,
            insertScoreboardListUseCase,
            getScoreboardUseCase,
            scoreboardLoader,
            savedStateHandle,
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { insertScoreboardListUseCase.invoke(any()) } answers { listOf(1) }
        every { resources.getString(ScoreboardType.NONE.titleRes) } returns ""
        every { resources.getString(ScoreboardType.NONE.descriptionRes) } returns ""
    }

    @Test
    fun testInitializingACustomScoreboard() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 1
        coEvery { getScoreboardUseCase(1) } coAnswers { ScoreboardEntity(1, "scoreboard name", "scoreboard desc") }
        initSut()

        Assert.assertEquals("scoreboard name", sut.title.value)
        Assert.assertEquals("scoreboard desc", sut.description.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingADefaultScoreboard() = runTest {
        val inputStream = mockk<InputStream>()
        val scoreboardConfig = mockk<DefaultScoreboardConfig>()

        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        every { savedStateHandle.get<ScoreboardType>(Arguments.TYPE) } returns ScoreboardType.BASKETBALL
        every { resources.openRawResource(ScoreboardType.BASKETBALL.rawRes!!) } returns inputStream
        every { resources.getString(ScoreboardType.BASKETBALL.titleRes) } returns "Basketball"
        every { resources.getString(ScoreboardType.BASKETBALL.descriptionRes) } returns "Basketball Desc"
        every { scoreboardLoader.invoke(inputStream) } returns scoreboardConfig
        every { scoreboardConfig.winRuleType} returns WinRuleType.COUNT
        initSut()

        Assert.assertEquals("Basketball", sut.title.value)
        Assert.assertEquals("Basketball Desc", sut.description.value)
        Assert.assertEquals(WinRule.Count, sut.winRule.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingNoScoreboard() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        every { savedStateHandle.get<ScoreboardType>(Arguments.TYPE) } returns ScoreboardType.NONE
        initSut()

        Assert.assertTrue(sut.title.value.isEmpty())
        Assert.assertTrue(sut.description.value.isEmpty())
        Assert.assertEquals(WinRule.Count, sut.winRule.value)
        Assert.assertFalse(sut.valid.value)
    }

    @Test
    fun testValidation() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        every { savedStateHandle.get<ScoreboardType>(Arguments.TYPE) } returns ScoreboardType.NONE
        initSut()

        Assert.assertFalse(sut.valid.value)
        sut.onEvent(ScoreboardDetailsEvent.OnDescriptionChange("Some value"))
        Assert.assertFalse(sut.valid.value)
        sut.onEvent(ScoreboardDetailsEvent.OnTitleChange("Some value"))
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testOnDismiss() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        every { savedStateHandle.get<ScoreboardType>(Arguments.TYPE) } returns ScoreboardType.NONE
        initSut()

        sut.onEvent(ScoreboardDetailsEvent.OnDismiss)
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
        sut.onEvent(ScoreboardDetailsEvent.OnConfirm)
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
    }

    @Test
    fun testInsertingNewScoreboard() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns -1
        every { savedStateHandle.get<ScoreboardType>(Arguments.TYPE) } returns ScoreboardType.NONE
        initSut()

        sut.onEvent(ScoreboardDetailsEvent.OnTitleChange("new scoreboard"))
        sut.onEvent(ScoreboardDetailsEvent.OnDescriptionChange("new scoreboard desc"))
        sut.onEvent(ScoreboardDetailsEvent.OnConfirm)
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
        coVerify(exactly = 1) {
            insertScoreboardListUseCase.invoke(
                listOf(
                    ScoreboardEntity(
                        id = null,
                        title = "new scoreboard",
                        description = "new scoreboard desc",
                    )
                )
            )
        }
    }

    @Test
    fun testEditingAScoreboard() = runTest {
        every { savedStateHandle.get<Int>(Arguments.ID) } returns 2
        coEvery { getScoreboardUseCase(2) } coAnswers { ScoreboardEntity(2, "scoreboard name 2", "scoreboard desc 2") }
        initSut()

        sut.onEvent(ScoreboardDetailsEvent.OnTitleChange("new scoreboard"))
        sut.onEvent(ScoreboardDetailsEvent.OnDescriptionChange("new scoreboard desc"))
        sut.onEvent(ScoreboardDetailsEvent.OnConfirm)
        Assert.assertEquals(UiEvent.Done, sut.uiEvent.first())
        coVerify(exactly = 1) {
            insertScoreboardListUseCase.invoke(
                listOf(
                    ScoreboardEntity(
                        id = 2,
                        title = "new scoreboard",
                        description = "new scoreboard desc",
                    )
                )
            )
        }
    }

}