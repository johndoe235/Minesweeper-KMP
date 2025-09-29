package com.minesweeper.features.minesweeper.ui

import androidx.compose.runtime.Composable
import com.minesweeper.minesweeper.ui.MineSweeperGame
import ui.MineSweeperUiState

@Composable
fun MineSweeperScreen(
    uiState: MineSweeperUiState,
    onClick: (Int, Int) -> Unit,
    onSmileyClick: () -> Unit,
    onFlagClick: () -> Unit
) {
    MineSweeperGame(uiState,onClick,onSmileyClick,onFlagClick)
}
