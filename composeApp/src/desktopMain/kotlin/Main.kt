package com.minesweeper
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

import com.minesweeper.di.commonModule
import org.koin.core.context.GlobalContext.startKoin


fun main() = application {
    startKoin {
        modules(commonModule,)
    }
    Window(
        state =  rememberWindowState(size = DpSize(500.dp, 800.dp)),
        onCloseRequest = ::exitApplication,
        title = "MineSweeperMultiPlatform",
    ) {
        MineSweeperApp()
    }
}
