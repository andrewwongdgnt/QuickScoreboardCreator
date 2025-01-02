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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.SPORT_IDENTIFIER
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.TIMELINE_VIEWER_IDENTIFIER
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.HistoryDetails
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TimelineViewer
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails.SportDetailsDialogContent
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist.SportListContent
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent.LaunchScoreboard
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent.SportDetails
import com.dgnt.quickScoreboardCreator.feature.team.presentation.teamdetails.TeamDetailsDialogContent
import com.dgnt.quickScoreboardCreator.feature.team.presentation.teamlist.TeamListContent
import com.dgnt.quickScoreboardCreator.feature.team.presentation.uievent.TeamDetails
import com.dgnt.quickScoreboardCreator.ui.common.NavDestination
import com.dgnt.quickScoreboardCreator.ui.common.TimelineViewerIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.commonNavigate
import com.dgnt.quickScoreboardCreator.ui.common.customNavType
import com.dgnt.quickScoreboardCreator.ui.main.contact.ContactContent
import com.dgnt.quickScoreboardCreator.feature.history.presentation.historydetails.HistoryDetailsDialogContent
import com.dgnt.quickScoreboardCreator.feature.history.presentation.historylist.HistoryListContent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.ScoreboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickScoreboardCreatorTheme {
                val navItemLists = listOf(NavItem.SportList, NavItem.TeamList, NavItem.HistoryList, NavItem.Contact)

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
            startDestination = NavDestination.SportList,
            modifier = modifier
        ) {
            composable<NavDestination.SportList> {
                SportListContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            is SportDetails -> navController.commonNavigate(navDestination = NavDestination.SportDetails(uiEvent.sportIdentifier))

                            is LaunchScoreboard -> context.startActivity(Intent(context, ScoreboardActivity::class.java).also { intent ->
                                intent.putExtra(SPORT_IDENTIFIER, uiEvent.sportIdentifier)
                            })

                            else -> Unit
                        }
                    }
                )
            }
            dialog<NavDestination.SportDetails>(
                typeMap = mapOf(
                    typeOf<SportIdentifier?>() to customNavType<SportIdentifier?>(isNullableAllowed = true)
                )
            ) {
                SportDetailsDialogContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            Done -> navController.navigateUp()
                            else -> Unit
                        }
                    }
                )
            }
            composable<NavDestination.TeamList> {
                TeamListContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            is TeamDetails -> navController.commonNavigate(navDestination = NavDestination.TeamDetails(uiEvent.id))
                            else -> Unit
                        }

                    }
                )
            }
            dialog<NavDestination.TeamDetails> {
                TeamDetailsDialogContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            Done -> navController.navigateUp()
                            else -> Unit
                        }
                    }
                )
            }
            composable<NavDestination.HistoryList> {
                HistoryListContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            is HistoryDetails -> navController.commonNavigate(navDestination = NavDestination.HistoryDetails(uiEvent.id))
                            is TimelineViewer -> context.startActivity(Intent(context, ScoreboardActivity::class.java).also { intent ->
                                intent.putExtra(TIMELINE_VIEWER_IDENTIFIER, TimelineViewerIdentifier(uiEvent.id, uiEvent.index))
                            })
                            else -> Unit
                        }

                    }
                )
            }
            dialog<NavDestination.HistoryDetails> {
                HistoryDetailsDialogContent(
                    onUiEvent = { uiEvent ->
                        when (uiEvent) {
                            Done -> navController.navigateUp()
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


