package com.dgnt.quickScoreboardCreator.ui.scoreboard

import android.os.Bundle
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.IntentCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.core.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.SCOREBOARD_IDENTIFIER
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.TIMELINE_VIEWER_IDENTIFIER
import com.dgnt.quickScoreboardCreator.ui.common.NavDestination
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.TimelineViewerIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.commonNavigate
import com.dgnt.quickScoreboardCreator.ui.common.parcelableType
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor.IntervalEditorDialogContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.ScoreboardInteractionContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker.TeamPickerDialogContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer.TimelineViewerContent
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class ScoreboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startDestination = IntentCompat.getParcelableExtra(intent, SCOREBOARD_IDENTIFIER, ScoreboardIdentifier::class.java)?.let { scoreboardIdentifier ->
            //TODO temporary logic flow because we can't handle custom scoreboards yet
            if (scoreboardIdentifier is ScoreboardIdentifier.Custom) {
                finish()
                Toast.makeText(this, getString(R.string.defaultInvalid), Toast.LENGTH_LONG).show()
                return
            }
            NavDestination.ScoreboardInteraction(scoreboardIdentifier)
        } ?: IntentCompat.getParcelableExtra(intent, TIMELINE_VIEWER_IDENTIFIER, TimelineViewerIdentifier::class.java)?.let { timelineViewerIdentifier ->
            NavDestination.TimelineViewer(timelineViewerIdentifier.id, timelineViewerIdentifier.index)
        } ?: run {
            finish()
            Toast.makeText(this, getString(R.string.defaultInvalid), Toast.LENGTH_LONG).show()
            return
        }


        setContent {
            QuickScoreboardCreatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavDestination.Start,
                    ) {
                        navigation<NavDestination.Start>(
                            startDestination = startDestination
                        ) {
                            composable<NavDestination.ScoreboardInteraction>(
                                typeMap = mapOf(
                                    typeOf<ScoreboardIdentifier>() to parcelableType<ScoreboardIdentifier>()
                                )
                            ) { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                val updatedTeamData by viewModel.updatedTeamData.collectAsStateWithLifecycle()
                                val updatedIntervalData by viewModel.updatedIntervalData.collectAsStateWithLifecycle()
                                ScoreboardInteractionContent(
                                    updatedTeamData,
                                    updatedIntervalData,
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            is UiEvent.TeamPicker -> navController.commonNavigate(navDestination = NavDestination.TeamPicker(uiEvent.index))
                                            is UiEvent.IntervalEditor -> navController.commonNavigate(navDestination = NavDestination.IntervalEditor(uiEvent.currentTimeValue, uiEvent.index, uiEvent.scoreboardIdentifier))
                                            is UiEvent.TimelineViewer -> navController.commonNavigate(navDestination = NavDestination.TimelineViewer(uiEvent.id, uiEvent.index))
                                            else -> Unit
                                        }

                                    }
                                )

                                //reset these so any recompose won't set the real values again
                                viewModel.onTeamDataUpdate(null)
                                viewModel.onIntervalDataUpdate(null)
                            }
                            dialog<NavDestination.TeamPicker> { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                TeamPickerDialogContent(
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            UiEvent.Done -> navController.navigateUp()

                                            is UiEvent.TeamUpdated -> {
                                                viewModel.onTeamDataUpdate(UpdatedTeamData(uiEvent.index, uiEvent.id))
                                                navController.navigateUp()
                                            }

                                            else -> Unit
                                        }
                                    },
                                )

                            }
                            dialog<NavDestination.IntervalEditor>(
                                typeMap = mapOf(
                                    typeOf<ScoreboardIdentifier>() to parcelableType<ScoreboardIdentifier>()
                                )
                            ) { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                IntervalEditorDialogContent(
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            UiEvent.Done -> navController.navigateUp()

                                            is UiEvent.IntervalUpdated -> {
                                                viewModel.onIntervalDataUpdate(UpdatedIntervalData(uiEvent.timeValue, uiEvent.index))
                                                navController.navigateUp()
                                            }

                                            else -> Unit
                                        }
                                    },
                                )

                            }
                            composable<NavDestination.TimelineViewer> {
                                TimelineViewerContent(
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            UiEvent.Done -> navController.navigateUp()
                                            else -> Unit
                                        }
                                    },
                                )

                            }
                        }
                    }
                }
            }
        }

        hideSystemUI()
    }

    private fun hideSystemUI() {

        //Hides the ugly action bar at the top
        actionBar?.hide()

        //Hide the status bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.insetsController?.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}
