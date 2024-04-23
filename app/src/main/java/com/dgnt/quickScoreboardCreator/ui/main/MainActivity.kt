package com.dgnt.quickScoreboardCreator.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.common.util.UiEvent
import com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails.ScoreboardDetailsContent
import com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist.ScoreboardListContent
import com.dgnt.quickScoreboardCreator.ui.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.common.util.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                        title = stringResource(id = R.string.settingsNavTitle),
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                    ),
                )
                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    var scoreboardDetails by rememberSaveable {
                        mutableStateOf<UiEvent.ScoreboardDetails?>(null)
                    }
                    val scope = rememberCoroutineScope()
                    scoreboardDetails?.let { details ->
                        ModalBottomSheet(
                            sheetState = sheetState,
                            onDismissRequest = {
                                scoreboardDetails = null
                            }
                        ) {
                            ScoreboardDetailsContent(
                                details,
                                onDone = {
                                    scope.launch {
                                        sheetState.hide()
                                        scoreboardDetails = null
                                    }
                                }
                            )
                        }
                    }
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
                            toScoreboardDetails = { scoreboardDetails = it },
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
        toScoreboardDetails: (UiEvent.ScoreboardDetails) -> Unit,
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
                        toScoreboardDetails(it)
                    }
                )
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


