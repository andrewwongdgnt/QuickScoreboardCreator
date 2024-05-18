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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.dgnt.quickScoreboardCreator.common.util.putExtra
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.ui.common.NavDestination
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
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickScoreboardCreatorTheme {
                val navItemLists = listOf(NavItem.ScoreboardList, NavItem.TeamList, NavItem.Contact)

                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                navItemLists.forEach { screen ->
                                    NavigationBarItem(
                                        selected = currentDestination?.route == screen.navDestination::class.qualifiedName,
                                        onClick = {
                                            navController.commonNavigate(currentDestination, screen.navDestination)
                                        },
                                        label = {
                                            Text(text = stringResource(id = screen.titleRes))
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentDestination?.route == screen.navDestination::class.qualifiedName) {
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
            startDestination = NavDestination.ScoreboardList,
            modifier = modifier
        ) {
            composable<NavDestination.ScoreboardList> {
                ScoreboardListContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            is UiEvent.ScoreboardDetails -> navController.commonNavigate(navDestination = NavDestination.ScoreboardDetails(uiEvent.id, uiEvent.scoreboardType))

                            is UiEvent.LaunchScoreboard -> context.startActivity(Intent(context, ScoreboardActivity::class.java).also { intent ->
                                intent.putExtra(ID, uiEvent.id)
                                intent.putExtra(uiEvent.scoreboardType)
                            })

                            else -> Unit
                        }
                    }
                )
            }
            dialog<NavDestination.ScoreboardDetails>(
                typeMap = mapOf(typeOf<ScoreboardType>() to NavType.EnumType(ScoreboardType::class.java))
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
            composable<NavDestination.TeamList> {
                TeamListContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            is UiEvent.TeamDetails -> navController.commonNavigate(navDestination = NavDestination.TeamDetails(uiEvent.id))
                            else -> Unit
                        }

                    }
                )
            }
            dialog<NavDestination.TeamDetails> {
                TeamDetailsDialogContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            UiEvent.Done -> navController.popBackStack()
                            else -> Unit
                        }
                    }
                )
            }
            composable<NavDestination.Contact> {
                ContactContent(

                )
            }
        }
    }

}


