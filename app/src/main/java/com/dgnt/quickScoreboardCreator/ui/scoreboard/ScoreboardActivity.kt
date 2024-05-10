package com.dgnt.quickScoreboardCreator.ui.scoreboard

import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
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
import androidx.navigation.navArgument
import com.dgnt.quickScoreboardCreator.common.util.getEnumExtra
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.SCORE_INDEX
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.TYPE
import com.dgnt.quickScoreboardCreator.ui.common.Routes.SCOREBOARD_INTERACTION
import com.dgnt.quickScoreboardCreator.ui.common.Routes.TEAM_PICKER
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.commonNavigate
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.ScoreboardInteractionContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker.TeamPickerDialogContent
import com.dgnt.quickScoreboardCreator.ui.theme.QuickScoreboardCreatorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class ScoreboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.extras?.getInt(ID, -1)
        val scoreboardType = intent.getEnumExtra<ScoreboardType>()
        setContent {
            QuickScoreboardCreatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "start",
                    ) {
                        navigation(
                            startDestination = "$SCOREBOARD_INTERACTION/{$ID}/{$TYPE}",
                            route = "start"
                        ) {
                            composable(
                                route = "$SCOREBOARD_INTERACTION/{$ID}/{$TYPE}",
                                arguments = listOf(
                                    navArgument(name = ID) {
                                        type = NavType.IntType
                                        defaultValue = id
                                    },
                                    navArgument(name = TYPE) {
                                        type = NavType.EnumType(ScoreboardType::class.java)
                                        defaultValue = scoreboardType

                                    }
                                )
                            ) { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                val teamSelectedData = viewModel.teamSelectedData
                                ScoreboardInteractionContent(
                                    teamSelectedData,
                                    onUiEvent = { uiEvent ->
                                        when (uiEvent) {
                                            is UiEvent.TeamPicker -> navController.commonNavigate(route = "$TEAM_PICKER/${uiEvent.scoreIndex}")
                                            else -> Unit
                                        }

                                    }
                                )
                            }
                            dialog(
                                dialogProperties = DialogProperties(
                                    usePlatformDefaultWidth = false
                                ),
                                route = "$TEAM_PICKER/{$SCORE_INDEX}",
                                arguments = listOf(
                                    navArgument(name = SCORE_INDEX) {
                                        type = NavType.IntType
                                        defaultValue = 0
                                    }
                                )
                            ) { entry ->
                                val viewModel = entry.sharedViewModel<ScoreboardActivityViewModel>(navController)
                                TeamPickerDialogContent(
                                    onDone = {
                                        navController.popBackStack()
                                    },
                                    onTeamPicked = { scoreIndex, teamId ->

                                        viewModel.teamSelectedData = TeamSelectedData(scoreIndex, teamId)
                                        navController.popBackStack()


                                    })

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

@Composable
fun <T> Flow<T>.collectAsEffect(
    context: CoroutineContext = EmptyCoroutineContext,
    block: (T) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onEach(block).flowOn(context).launchIn(this)
    }
}