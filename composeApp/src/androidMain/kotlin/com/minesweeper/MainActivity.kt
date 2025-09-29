package com.minesweeper

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.minesweeper.features.minesweeper.ui.MineSweeperViewModel

import com.minesweeper.minesweeper.ui.MineSweeperGame
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    val viewmodel: MineSweeperViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MineSweeperGame(viewmodel.mineSweeperUiState.collectAsState().value,
                    onClick = { row, col ->
                        if (!viewmodel.mineSweeperUiState.value.flagOn)
                            viewmodel.open(row, col)
                        else
                            viewmodel.flag(row, col)
                    },
                    onSmileyClick = { viewmodel.restart() }, onFlagClick = viewmodel::toggleFlag
                )
            }
        }
    }
}