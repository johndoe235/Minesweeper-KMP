package com.minesweeper.minesweeper.ai

import com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.board.Board
import com.minesweeper.minesweeper.board.GetNeighborsUseCase
import com.minesweeper.minesweeper.board.GetNeighborsUseCase.*
import com.minesweeper.minesweeper.board.GetNeighborsOfZoneUseCase
import com.minesweeper.minesweeper.board.deepCopy


class ExhaustiveSearch {
    private val getNeighborsUseCase = GetNeighborsUseCase()
    fun execute(openSquaresOfInterest: List<Cell>, board: Board): List<Board> {

        val ecsBoard = board.deepCopy()

        //zone on  which ECS will compute.
        val zoneOfInterest =
            GetNeighborsOfZoneUseCase().execute(openSquaresOfInterest, ecsBoard, sortByRow = true)


        val result = mutableListOf<Board>()
        search(zoneOfInterest, ecsBoard, 0, result)
        return result
    }



    private fun search(
        zones: List<Cell>,
        board: Board,
        currentIndex: Int,
        foundBoards: MutableList<Board>
    ) {
        if (currentIndex >= zones.size)
            return

        if (canPlaceBomb(zones[currentIndex], board)) {
            val newBoard = board.deepCopy().apply { placeMine(zones[currentIndex]) }

            if (isConfigurationASolution(board = newBoard))
                foundBoards.add(newBoard)

            search(zones, newBoard, currentIndex + 1, foundBoards)
        }

        search(zones, board, currentIndex + 1, foundBoards)

    }

    /*
    A solution is defined as a board which approximates all mines in a given area.
     */
    fun isConfigurationASolution(squares: List<Cell>? = null, board: Board): Boolean {
        val squaresBeingTested = squares ?: getAllOpenedSquares(board)

        squaresBeingTested.forEach { square ->
            val amountOfNeighboringMines =
                getNeighborsUseCase.execute(square, board, FilterOptions.ONLY_OPENED)
                    .filter { board[it.row, it.col] == MineSweeperField.MINE }.size
            val value = board[square.row, square.col].digitToInt()

            if (amountOfNeighboringMines != value)
                return false
        }
        return true
    }

    private fun getAllOpenedSquares(board: Board): List<Cell> {
        val result = mutableListOf<Cell>()
        for (row in 0 until board.rows)
            for (col in 0 until board.cols) {
                if (board[row, col] in MineSweeperField.NUMBER)
                    result.add(Cell(row, col))
            }
        return result
    }


    fun canPlaceBomb(cell: Cell, board: Board): Boolean {
        val numberedNeighbors = getNeighborsUseCase.execute(cell, board, FilterOptions.ONLY_OPENED)
            .filter { board[it.row,it.col] == MineSweeperField.EMPTY || MineSweeperField.NUMBER.contains(board[it.row, it.col]) }

        numberedNeighbors.forEach { n ->
            val amountOfNeighboringMines =
                getNeighborsUseCase.execute(n, board, FilterOptions.ONLY_OPENED)
                    .filter { board[it.row, it.col] == MineSweeperField.MINE }.size
            val value = board[n.row, n.col].digitToInt()

            if (value <= amountOfNeighboringMines)
                return false
        }

        return true
    }


}

private fun Board.placeMine(cell: Cell) {
    this[cell.row, cell.col] = MineSweeperField.MINE
}


