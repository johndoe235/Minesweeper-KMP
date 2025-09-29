package minesweeper

import com.minesweeper.minesweeper.board.BoardGenerator
import com.minesweeper.minesweeper.board.GameBoard
import com.minesweeper.minesweeper.MineSweeperField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import com.minesweeper.minesweeper.board.GameBoard.Companion.EmptyGameBoard
import com.minesweeper.minesweeper.MapDifficulty
import ui.MineSweeperUiState
import com.minesweeper.minesweeper.ui.UiBoard
import kotlinx.coroutines.flow.asStateFlow


enum class GameState {
    NOT_STARTED,
    IN_PROGRESS,
    WON,
    LOST,

}

fun GameBoard.toUiBoard() = UiBoard(this.getBoard(), rows, cols)

class MineSweeper {
    private var board: GameBoard = EmptyGameBoard
    private var currentDifficulty = MapDifficulty(0,0,0)

    private val _uiState =
        MutableStateFlow(
            MineSweeperUiState(
                board = EmptyGameBoard.toUiBoard(), GameState.NOT_STARTED,
                flagOn = false,
            )
        )
    val mineSweeperUiState = _uiState.asStateFlow()


    fun toggleFlag() {
        _uiState.update { currentUiState -> currentUiState.copy(flagOn = !currentUiState.flagOn) }
    }

    fun start(mapDifficulty: MapDifficulty) {
        currentDifficulty = mapDifficulty
        board = BoardGenerator.generateGameBoard(mapDifficulty)

        _uiState.update { currentUiState -> currentUiState.copy(board.toUiBoard(), gameState = GameState.IN_PROGRESS) }
    }

    fun open(row: Int, column: Int) {
        if (_uiState.value.gameState != GameState.IN_PROGRESS)
            return

        board.open(row, column).let { fieldUnderneath ->
            if (fieldUnderneath == MineSweeperField.MINE) {
                _uiState.update { currentUiState -> currentUiState.copy(gameState = GameState.LOST) }
            } else if (fieldUnderneath == MineSweeperField.EMPTY) {
                board.openAdjacentEmptySquares(row, column)
            }

            if (fieldUnderneath != MineSweeperField.FLAG) {
                _uiState.update { currentUiState -> currentUiState.copy(board.toUiBoard()) }
            }

        }

    }


    fun restart() {
        start(currentDifficulty)
    }

    fun flag(row: Int, column: Int) {
        if (_uiState.value.gameState != GameState.IN_PROGRESS)
            return

        board.flag(row, column)

        _uiState.update { currentUiState -> currentUiState.copy(board.toUiBoard()) }
    }
}




