package com.manekelsa.app.navigation

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.manekelsa.app.ui.screens.AboutScreen
import com.manekelsa.app.ui.screens.ResidentFeedScreen
import com.manekelsa.app.ui.screens.WorkerProfileScreen
import com.manekelsa.app.ui.theme.DeepSaffron

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Feed : Screen("feed", "ಕೆಲಸಗಾರರು", Icons.Filled.Groups)
    object Profile : Screen("profile", "ನನ್ನ ಪ್ರೊಫೈಲ್", Icons.Filled.Person)
    object About : Screen("about", "ಬಗ್ಗೆ", Icons.Filled.Info)
}

val bottomNavItems = listOf(Screen.Feed, Screen.Profile, Screen.About)

/**
 * ManeKelsaNavGraph — root navigation with a 3-tab bottom nav bar.
 * Simple enough for semi-literate users: icons + short Kannada labels.
 */
@Composable
fun ManeKelsaNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
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
                                contentDescription = screen.label
                            )
                        },
                        label = {
                            Text(
                                text = screen.label,
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
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
