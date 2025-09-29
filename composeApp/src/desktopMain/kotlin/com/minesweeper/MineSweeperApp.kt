package com.minesweeper

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.minesweeper.minesweeper.ui.MineSweeperGame
import com.minesweeper.minesweeper.MapDifficulty
import minesweeper.MineSweeper

@Composable
fun MineSweeperApp() {

    val game = remember { MineSweeper() }
    game.start(MapDifficulty(9,9,10))

    MaterialTheme {
        MineSweeperGame(
            game.mineSweeperUiState.collectAsState().value,
            onClick = { row, col ->
                if (!game.mineSweeperUiState.value.flagOn)
                    game.open(row, col)
                else
                    game.flag(row, col)
            },
            onSmileyClick = { game.restart() }, onFlagClick = game::toggleFlag
        )
    }
}