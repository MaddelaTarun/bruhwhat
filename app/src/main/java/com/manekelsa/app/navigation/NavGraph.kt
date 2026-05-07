package com.manekelsa.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.manekelsa.app.R
import com.manekelsa.app.ui.screens.AboutScreen
import com.manekelsa.app.ui.screens.LanguageSelectionScreen
import com.manekelsa.app.ui.screens.ResidentFeedScreen
import com.manekelsa.app.ui.screens.WorkerProfileScreen
import com.manekelsa.app.ui.theme.DeepSaffron

sealed class Screen(val route: String, val labelResId: Int, val icon: ImageVector) {
    object LanguageSelection : Screen("language_selection", R.string.language_selection_title, Icons.Filled.Info)
    object Feed : Screen("feed", R.string.tab_workers, Icons.Filled.Groups)
    object Profile : Screen("profile", R.string.tab_profile, Icons.Filled.Person)
    object About : Screen("about", R.string.tab_about, Icons.Filled.Info)
}

val bottomNavItems = listOf(Screen.Feed, Screen.Profile, Screen.About)

/**
 * ManeKelsaNavGraph — root navigation with language selection and 3-tab bottom nav.
 * Shows language selection on first launch, then remembers preference.
 */
@Composable
fun ManeKelsaNavGraph(
    startDestination: String,
    onLanguageSelected: (String) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Hide bottom nav on language selection screen
    val showBottomNav = currentDestination?.route != Screen.LanguageSelection.route

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = stringResource(screen.labelResId)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(screen.labelResId),
                                    fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepSaffron,
                                selectedTextColor = DeepSaffron,
                                indicatorColor = DeepSaffron.copy(alpha = 0.12f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(if (showBottomNav) innerPadding else PaddingValues(0.dp))
        ) {
            composable(Screen.LanguageSelection.route) {
                LanguageSelectionScreen(
                    onLanguageSelected = { languageCode ->
                        onLanguageSelected(languageCode)
                        navController.navigate(Screen.Feed.route) {
                            popUpTo(Screen.LanguageSelection.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Feed.route) {
                ResidentFeedScreen()
            }
            composable(Screen.Profile.route) {
                // In production, pass the authenticated worker's ID here
                WorkerProfileScreen(workerId = null)
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
        }
    }
}
