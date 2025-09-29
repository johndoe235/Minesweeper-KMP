package com.minesweeper.minesweeper.board

import com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.ai.Cell


class GetNeighborsUseCase {
    fun execute(
        cell: Cell,
        board: Board,
        filterOptions: FilterOptions = FilterOptions.ALL
    ): List<Cell> {
        val neighbors = mutableListOf<Cell>()
        val neighborCoordinates = listOf(
            //top adjacent squares
            -1 to -1, // row-1; col -1
            -1 to 0, // row-1 ; col
            -1 to 1, // row-1; col + 1
            //horizontal adjacent squares
            0 to -1, //row ; col -1
            0 to 1, //row ; col +1
            //bottom adjacent squares
            1 to -1, //row +1; col -1
            1 to 0,// row+1; col
            1 to 1, //row +1 ; col +1
        )

        for (neighborCoordinate in neighborCoordinates) {
            val wantedRow = cell.row + neighborCoordinate.first
            if (wantedRow < 0 || wantedRow >= board.rows)
                continue

            val wantedCol = cell.col + neighborCoordinate.second
            if (wantedCol < 0 || wantedCol >= board.cols)
                continue

            when (filterOptions) {
                FilterOptions.ALL -> neighbors.add(Cell(wantedRow, wantedCol))
                FilterOptions.ONLY_OPENED -> if (board[wantedRow, wantedCol] != MineSweeperField.HIDDEN) neighbors.add(
                    Cell(wantedRow, wantedCol)
                )

                FilterOptions.ONLY_UNOPENED -> if (board[wantedRow, wantedCol] == MineSweeperField.HIDDEN) neighbors.add(
                    Cell(wantedRow, wantedCol)
                )
            }


        }
        return neighbors
    }

    enum class FilterOptions {
        ONLY_OPENED,
        ONLY_UNOPENED,
        ALL,
    }
}