package com.minesweeper.minesweeper.board

import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.board.GetNeighborsUseCase.*

class GetNeighborsOfZoneUseCase {
    fun execute(openSquares: List<Cell>, board: Board, sortByRow: Boolean = false): List<Cell> {
        return openSquares.flatMap { cell ->
            GetNeighborsUseCase().execute(cell, board, FilterOptions.ONLY_UNOPENED)
        }.distinct().let { cells ->
            if (sortByRow) cells.sortedBy { it.row } else cells
        }
    }
}