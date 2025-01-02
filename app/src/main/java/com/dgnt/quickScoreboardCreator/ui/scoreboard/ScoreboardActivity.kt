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
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.SPORT_IDENTIFIER
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.TIMELINE_VIEWER_IDENTIFIER
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.IntervalUpdated
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TeamPicker
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TeamUpdated
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TimelineViewer
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.UpdatedTeamData
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.intervaleditor.IntervalEditorDialogContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.UpdatedIntervalData
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.ScoreboardContent
import com.dgnt.quickScoreboardCreator.feature.history.presentation.timelineviewer.TimelineViewerContent
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.uievent.IntervalEditor
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.team.presentation.teampicker.TeamPickerDialogContent
import com.dgnt.quickScoreboardCreator.ui.common.NavDestination
import com.dgnt.quickScoreboardCreator.ui.common.TimelineViewerIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.commonNavigate
import com.dgnt.quickScoreboardCreator.ui.common.customNavType
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class ScoreboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startDestination = IntentCompat.getSerializableExtra(intent, SPORT_IDENTIFIER, SportIdentifier::class.java)?.let { sportIdentifier ->
            //TODO temporary logic flow because we can't handle custom sports yet
            if (sportIdentifier is SportIdentifier.Custom) {
                finish()
                Toast.makeText(this, getString(R.string.defaultInvalid), Toast.LENGTH_LONG).show()
                return
            }
            NavDestination.Scoreboard(sportIdentifier)
        } ?: IntentCompat.getSerializableExtra(intent, TIMELINE_VIEWER_IDENTIFIER, TimelineViewerIdentifier::class.java)?.let { timelineViewerIdentifier ->
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
                            composable<NavDestination.Scoreboard>(
                                typeMap = mapOf(
                                    typeOf<SportIdentifier>() to customNavType<SportIdentifier>()
                                )
                            ) { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                val updatedTeamData by viewModel.updatedTeamData.collectAsStateWithLifecycle()
                                val updatedIntervalData by viewModel.updatedIntervalData.collectAsStateWithLifecycle()
                                ScoreboardContent(
                                    updatedTeamData,
                                    updatedIntervalData,
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            is TeamPicker -> navController.commonNavigate(navDestination = NavDestination.TeamPicker(uiEvent.index))
                                            is IntervalEditor -> navController.commonNavigate(navDestination = NavDestination.IntervalEditor(uiEvent.currentTimeValue, uiEvent.index, uiEvent.sportIdentifier))
                                            is TimelineViewer -> navController.commonNavigate(navDestination = NavDestination.TimelineViewer(uiEvent.id, uiEvent.index))
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
                                            Done -> navController.navigateUp()

                                            is TeamUpdated -> {
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
                                    typeOf<SportIdentifier>() to customNavType<SportIdentifier>()
                                )
                            ) { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                IntervalEditorDialogContent(
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            Done -> navController.navigateUp()

                                            is IntervalUpdated -> {
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
                                            Done -> navController.navigateUp()
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
