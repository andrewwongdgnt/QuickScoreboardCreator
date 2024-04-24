package com.dgnt.quickScoreboardCreator.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.common.util.Routes
import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails.ScoreboardDetailsContent
import com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist.ScoreboardListContent
import com.dgnt.quickScoreboardCreator.ui.theme.QuickScoreboardCreatorTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickScoreboardCreatorTheme {
                val items = listOf(
                    BottomNavigationItemData(
                        title = stringResource(id = R.string.scoreboardListNavTitle),
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                    ),
                    BottomNavigationItemData(
                        title = stringResource(id = R.string.teamListNavTitle),
                        selectedIcon = Icons.Filled.Person,
                        unselectedIcon = Icons.Outlined.Person,
                    ),
                    BottomNavigationItemData(
                        title = stringResource(id = R.string.contactNavTitle),
                        selectedIcon = Icons.Filled.Email,
                        unselectedIcon = Icons.Outlined.Email,
                    ),
                )
                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
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
            startDestination = Routes.SCOREBOARD_LIST,
            modifier = modifier
        ) {
            composable(Routes.SCOREBOARD_LIST) {
                ScoreboardListContent(
                    toScoreboardDetails = {
                        navController.navigate("${Routes.SCOREBOARD_DETAILS}?id=${it.id}&type=${it.scoreboardType}")
                    }
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
                ScoreboardDetailsContent(
                    onDone = {
                        navController.popBackStack()
                    })
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        QuickScoreboardCreatorTheme {
            ScoreboardListContent({})
        }
    }
}


