package ui

import com.minesweeper.minesweeper.ui.UiBoard
import minesweeper.GameState

data class MineSweeperUiState(val board: UiBoard, val gameState: GameState, val flagOn: Boolean, val scale: Float = 2f)