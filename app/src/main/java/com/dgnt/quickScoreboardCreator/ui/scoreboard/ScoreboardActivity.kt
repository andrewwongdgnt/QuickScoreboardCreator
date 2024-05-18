package com.dgnt.quickScoreboardCreator.ui.scoreboard

import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dgnt.quickScoreboardCreator.common.util.getEnumExtra
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.ui.common.NavDestination
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.commonNavigate
import com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor.IntervalEditorDialogContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.ScoreboardInteractionContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker.TeamPickerDialogContent
import com.dgnt.quickScoreboardCreator.ui.theme.QuickScoreboardCreatorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class ScoreboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.extras?.getInt(ID, -1) ?: -1
        val scoreboardType = intent.getEnumExtra<ScoreboardType>() ?: ScoreboardType.NONE
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
                            startDestination = NavDestination.ScoreboardInteraction(id, scoreboardType)
                        ) {
                            composable<NavDestination.ScoreboardInteraction>(
                                typeMap = mapOf(typeOf<ScoreboardType>() to NavType.EnumType(ScoreboardType::class.java))
                            ) { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                val updatedTeamData = viewModel.updatedTeamData
                                val updatedIntervalData = viewModel.updatedIntervalData
                                ScoreboardInteractionContent(
                                    updatedTeamData,
                                    updatedIntervalData,
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            is UiEvent.TeamPicker -> navController.commonNavigate(navDestination = NavDestination.TeamPicker(uiEvent.scoreIndex))
                                            is UiEvent.IntervalEditor -> navController.commonNavigate(navDestination = NavDestination.IntervalEditor(uiEvent.currentTimeValue, uiEvent.intervalIndex, uiEvent.id, uiEvent.scoreboardType))
                                            else -> Unit
                                        }

                                    }
                                )

                                //reset these so any recompose won't set the real values again
                                viewModel.updatedTeamData = null
                                viewModel.updatedIntervalData = null
                            }
                            dialog<NavDestination.TeamPicker> { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                TeamPickerDialogContent(
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            UiEvent.Done -> navController.popBackStack()

                                            is UiEvent.TeamUpdated -> {
                                                viewModel.updatedTeamData = UpdatedTeamData(uiEvent.scoreIndex, uiEvent.teamId)
                                                navController.popBackStack()
                                            }

                                            else -> Unit
                                        }
                                    },
                                )

                            }
                            dialog<NavDestination.IntervalEditor>(
                                typeMap = mapOf(typeOf<ScoreboardType>() to NavType.EnumType(ScoreboardType::class.java))
                            ) { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                IntervalEditorDialogContent(
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            UiEvent.Done -> navController.popBackStack()

                                            is UiEvent.IntervalUpdated -> {
                                                viewModel.updatedIntervalData = UpdatedIntervalData(uiEvent.timeValue, uiEvent.intervalIndex)
                                                navController.popBackStack()
                                            }

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