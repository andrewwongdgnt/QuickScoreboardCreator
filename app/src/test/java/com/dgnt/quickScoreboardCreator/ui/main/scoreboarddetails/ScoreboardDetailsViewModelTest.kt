package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.WinRuleType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.DeleteScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var deleteScoreboardUseCase: DeleteScoreboardUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var uiEventHandler: UiEventHandler

    private lateinit var sut: ScoreboardDetailsViewModel

    private fun initSut() {
        sut = ScoreboardDetailsViewModel(
            resources,
            insertScoreboardListUseCase,
            getScoreboardUseCase,
            deleteScoreboardUseCase,
            scoreboardLoader,
            savedStateHandle,
            uiEventHandler
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { insertScoreboardListUseCase.invoke(any()) } answers { listOf(1) }
    }

    @Test
    fun testInitializingACustomScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.Custom(1)
        coEvery { getScoreboardUseCase(1) } coAnswers { ScoreboardEntity(1, "scoreboard name", "scoreboard desc", ScoreboardIcon.TENNIS) }
        initSut()

        Assert.assertEquals("scoreboard name", sut.title.value)
        Assert.assertEquals("scoreboard desc", sut.description.value)
        Assert.assertEquals(ScoreboardIcon.TENNIS, sut.icon.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingADefaultScoreboard() = runTest {
        val inputStream = mockk<InputStream>()
        val scoreboardConfig = mockk<DefaultScoreboardConfig>()

        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.Default(ScoreboardType.BASKETBALL)
        every { resources.openRawResource(ScoreboardType.BASKETBALL.rawRes) } returns inputStream
        every { resources.getString(ScoreboardType.BASKETBALL.titleRes) } returns "Basketball"
        every { resources.getString(ScoreboardType.BASKETBALL.descriptionRes) } returns "Basketball Desc"
        every { scoreboardLoader.invoke(inputStream) } returns scoreboardConfig
        every { scoreboardConfig.winRuleType } returns WinRuleType.COUNT
        initSut()

        Assert.assertEquals("Basketball", sut.title.value)
        Assert.assertEquals("Basketball Desc", sut.description.value)
        Assert.assertEquals(ScoreboardIcon.BASKETBALL, sut.icon.value)
        Assert.assertEquals(WinRule.Count, sut.winRule.value)
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testInitializingNoScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns null
        initSut()

        Assert.assertTrue(sut.title.value.isEmpty())
        Assert.assertTrue(sut.description.value.isEmpty())
        Assert.assertNotNull(sut.icon.value)
        Assert.assertEquals(WinRule.Count, sut.winRule.value)
        Assert.assertFalse(sut.valid.value)
    }

    @Test
    fun testValidation() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns null
        initSut()

        Assert.assertFalse(sut.valid.value)
        sut.onDescriptionChange("Some value")
        Assert.assertFalse(sut.valid.value)
        sut.onTitleChange("Some value")
        Assert.assertTrue(sut.valid.value)
    }

    @Test
    fun testScoreboardIconChange() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns null
        initSut()

        sut.onIconEdit()
        Assert.assertTrue(sut.iconChanging.value)
        sut.onIconChange(ScoreboardIcon.VOLLEYBALL)
        Assert.assertEquals(ScoreboardIcon.VOLLEYBALL, sut.icon.value)
        Assert.assertFalse(sut.iconChanging.value)
    }

    @Test
    fun testOnDismiss() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns null
        initSut()

        sut.onDismiss()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
    }

    @Test
    fun testInsertingNewScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns null
        initSut()

        sut.onTitleChange("new scoreboard")
        sut.onDescriptionChange("new scoreboard desc")
        sut.onIconChange(ScoreboardIcon.SOCCER)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            insertScoreboardListUseCase.invoke(
                listOf(
                    ScoreboardEntity(
                        id = null,
                        title = "new scoreboard",
                        description = "new scoreboard desc",
                        icon = ScoreboardIcon.SOCCER
                    )
                )
            )
        }
    }

    @Test
    fun testEditingAScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.Custom(2)
        coEvery { getScoreboardUseCase(2) } coAnswers { ScoreboardEntity(2, "scoreboard name 2", "scoreboard desc 2", ScoreboardIcon.TENNIS) }
        initSut()

        sut.onTitleChange("new scoreboard")
        sut.onDescriptionChange("new scoreboard desc")
        sut.onIconChange(ScoreboardIcon.BOXING)
        sut.onConfirm()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            insertScoreboardListUseCase.invoke(
                listOf(
                    ScoreboardEntity(
                        id = 2,
                        title = "new scoreboard",
                        description = "new scoreboard desc",
                        icon = ScoreboardIcon.BOXING
                    )
                )
            )
        }
    }

    @Test
    fun testDeletingAScoreboard() = runTest {
        every { savedStateHandle.get<ScoreboardIdentifier?>(Arguments.SCOREBOARD_IDENTIFIER) } returns ScoreboardIdentifier.Custom(2)
        coEvery { getScoreboardUseCase(2) } coAnswers { ScoreboardEntity(2, "scoreboard name 2", "scoreboard desc 2", ScoreboardIcon.TENNIS) }
        initSut()

        sut.onDelete()
        verify(exactly = 1) {
            sut.sendUiEvent(UiEvent.Done)
        }
        coVerify(exactly = 1) {
            deleteScoreboardUseCase.invoke(
                ScoreboardEntity(
                    id = 2,
                    title = "scoreboard name 2",
                    description = "scoreboard desc 2",
                    icon = ScoreboardIcon.TENNIS
                )

            )
        }
    }

}