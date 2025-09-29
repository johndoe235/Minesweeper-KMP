package com.minesweeper.navigation

import androidx.compose.runtime.Composable


enum class Screen {
    HOME,
    LOGIN,
}
sealed class NavigationItem(val route: String) {
    object Menu : NavigationItem(Screen.HOME.name)
    object MineSweeper : NavigationItem(Screen.LOGIN.name)
}
@Composable
fun Navigation() {

}