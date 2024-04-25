package com.dgnt.quickScoreboardCreator.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.common.Routes
import com.dgnt.quickScoreboardCreator.ui.common.Routes.CONTACT
import com.dgnt.quickScoreboardCreator.ui.common.Routes.SCOREBOARD_LIST
import com.dgnt.quickScoreboardCreator.ui.common.Routes.TEAM_LIST
import com.dgnt.quickScoreboardCreator.ui.main.contact.ContactContent
import com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails.ScoreboardDetailsDialogContent
import com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist.ScoreboardListContent
import com.dgnt.quickScoreboardCreator.ui.main.teamlist.TeamListContent
import com.dgnt.quickScoreboardCreator.ui.theme.QuickScoreboardCreatorTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickScoreboardCreatorTheme {
                val screenList = listOf(Screen.ScoreboardList, Screen.TeamList, Screen.Contact)

                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

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
                                            navController.commonNavigate(screen.route)
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
        NavHost(
            navController = navController,
            startDestination = SCOREBOARD_LIST,
            modifier = modifier
        ) {
            composable(SCOREBOARD_LIST) {
                ScoreboardListContent(
                    toScoreboardDetails = {
                        navController.commonNavigate("${Routes.SCOREBOARD_DETAILS}?id=${it.id}&type=${it.scoreboardType}")
                    }
                )
            }
            composable(TEAM_LIST) {
                TeamListContent(

                )
            }
            composable(CONTACT) {
                ContactContent(

                )
            }
            dialog(
                dialogProperties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
                route = Routes.SCOREBOARD_DETAILS + "?id={id}&type={type}",
                arguments = listOf(
                    navArgument(name = "id") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument(name = "type") {
                        type = NavType.EnumType(ScoreboardType::class.java)
                        defaultValue = ScoreboardType.NONE

                    }
                )
            ) {
                ScoreboardDetailsDialogContent(
                    onDone = {
                        navController.popBackStack()
                    })
            }
        }
    }

    private fun NavController.commonNavigate(route: String) = navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        QuickScoreboardCreatorTheme {
            ScoreboardListContent({})
        }
    }
}


