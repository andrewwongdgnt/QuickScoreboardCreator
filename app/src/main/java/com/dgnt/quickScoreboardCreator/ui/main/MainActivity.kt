package com.dgnt.quickScoreboardCreator.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dgnt.quickScoreboardCreator.common.util.putExtra
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.TYPE
import com.dgnt.quickScoreboardCreator.ui.common.Routes.CONTACT
import com.dgnt.quickScoreboardCreator.ui.common.Routes.SCOREBOARD_DETAILS
import com.dgnt.quickScoreboardCreator.ui.common.Routes.SCOREBOARD_LIST
import com.dgnt.quickScoreboardCreator.ui.common.Routes.TEAM_DETAILS
import com.dgnt.quickScoreboardCreator.ui.common.Routes.TEAM_LIST
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.commonNavigate
import com.dgnt.quickScoreboardCreator.ui.main.contact.ContactContent
import com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails.ScoreboardDetailsDialogContent
import com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist.ScoreboardListContent
import com.dgnt.quickScoreboardCreator.ui.main.teamdetails.TeamDetailsDialogContent
import com.dgnt.quickScoreboardCreator.ui.main.teamlist.TeamListContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.ScoreboardActivity
import com.dgnt.quickScoreboardCreator.ui.theme.QuickScoreboardCreatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickScoreboardCreatorTheme {
                val screenList = listOf(Screen.ScoreboardList, Screen.TeamList, Screen.Contact)

                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                screenList.forEach { screen ->
                                    NavigationBarItem(
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.commonNavigate(currentDestination, screen.route)
                                        },
                                        label = {
                                            Text(text = stringResource(id = screen.titleRes))
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                                    screen.selectedIcon
                                                } else screen.unselectedIcon,
                                                contentDescription = stringResource(id = screen.titleRes)
                                            )
                                        }
                                    )
                                }
                            }

                        }
                    ) { padding ->
                        NavigationContent(
                            navController = navController,
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun NavigationContent(
        navController: NavHostController,
        modifier: Modifier = Modifier
    ) {
        val context = LocalContext.current
        NavHost(
            navController = navController,
            startDestination = SCOREBOARD_LIST,
            modifier = modifier
        ) {
            composable(SCOREBOARD_LIST) {
                ScoreboardListContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            is UiEvent.ScoreboardDetails -> navController.commonNavigate(route = "$SCOREBOARD_DETAILS?$ID=${uiEvent.id}&$TYPE=${uiEvent.scoreboardType}")

                            is UiEvent.LaunchScoreboard -> context.startActivity(Intent(context, ScoreboardActivity::class.java).also { intent ->
                                intent.putExtra(ID, uiEvent.id)
                                (uiEvent.scoreboardType ?: ScoreboardType.NONE).let { scoreboardType -> intent.putExtra(scoreboardType) }
                            })

                            else -> Unit
                        }
                    }
                )
            }
            dialog(
                dialogProperties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
                route = "$SCOREBOARD_DETAILS?$ID={$ID}&$TYPE={$TYPE}",
                arguments = listOf(
                    navArgument(name = ID) {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument(name = TYPE) {
                        type = NavType.EnumType(ScoreboardType::class.java)
                        defaultValue = ScoreboardType.NONE

                    }
                )
            ) {
                ScoreboardDetailsDialogContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            UiEvent.Done -> navController.popBackStack()
                            else -> Unit
                        }
                    }
                )
            }
            composable(TEAM_LIST) {
                TeamListContent(
                    toTeamDetails = {
                        navController.commonNavigate(route = "$TEAM_DETAILS?$ID=${it.id}")
                    }
                )
            }
            dialog(
                dialogProperties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
                route = "$TEAM_DETAILS?$ID={$ID}",
                arguments = listOf(
                    navArgument(name = ID) {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                TeamDetailsDialogContent(
                    onDone = {
                        navController.popBackStack()
                    })
            }
            composable(CONTACT) {
                ContactContent(

                )
            }
        }
    }

}


