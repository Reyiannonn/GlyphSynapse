package com.glyphsynapse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.glyphsynapse.app.ui.screens.AdvancedScreen
import com.glyphsynapse.app.ui.screens.AnimationScreen
import com.glyphsynapse.app.ui.screens.HomeScreen
import com.glyphsynapse.app.ui.screens.PresetsScreen
import com.glyphsynapse.app.ui.screens.SettingsScreen
import com.glyphsynapse.app.ui.theme.Background
import com.glyphsynapse.app.ui.theme.AccentWhite
import com.glyphsynapse.app.ui.theme.Divider
import com.glyphsynapse.app.ui.theme.GlyphSynapseTheme
import com.glyphsynapse.app.ui.theme.IconInactive
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String, val label: String, val iconRes: Int) {
    object Home       : Screen("home",       "Home",       R.drawable.ic_nav_home)
    object Animations : Screen("animations", "Animations", R.drawable.ic_nav_animations)
    object Presets    : Screen("presets",    "Presets",    R.drawable.ic_nav_presets)
    object Settings   : Screen("settings",   "Settings",   R.drawable.ic_nav_settings)
}

private val bottomNavScreens = listOf(
    Screen.Home, Screen.Animations, Screen.Presets, Screen.Settings
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlyphSynapseTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
private fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentDest = navBackStack?.destination

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(
                containerColor = Background,
                tonalElevation = 0.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                bottomNavScreens.forEach { screen ->
                    val selected = currentDest?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(screen.iconRes),
                                contentDescription = screen.label,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = null,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentWhite,
                            unselectedIconColor = IconInactive,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route)       { HomeScreen() }
            composable(Screen.Animations.route) { AnimationScreen() }
            composable(Screen.Presets.route)    { PresetsScreen() }
            composable(Screen.Settings.route)   {
                SettingsScreen(onNavigateToAdvanced = { navController.navigate("advanced") })
            }
            composable("advanced") { AdvancedScreen() }
        }
    }
}
