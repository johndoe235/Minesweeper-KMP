package com.minesweeper.minesweeper.ui

data class UiBoard(val board: String, val rows: Int, val cols: Int) {
    operator fun get(row: Int, col: Int): Char = board[row * cols + col]
}
