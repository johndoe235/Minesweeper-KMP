package com.minesweeper.minesweeper.ai.v4

import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.board.BoardOld

class MineSweeperSolverV4(
    val boardString: String,
    val nMines: Int,
    val openSquare: (row: Int, col: Int) -> Char
) {

    fun solve(): String {
        val mutableBoard = createBoard(boardString, nMines)
        var result = ""
        EppStrategy(
            mutableBoard,
            openSquare = { row, col -> open(row, col, mutableBoard) },
            getNeighbors = { cell -> getNeighbors(cell, mutableBoard) }
        ).solve().onSuccess {
            result = boardAsOutput(it)
        }

        return result
    }

    private fun createBoard(board: String, nMines: Int): BoardOld {
        val rows = board.count { it == '\n' } + 1
        var cols: Int

        val newBoard = board.replace(" ", "")
            .also { cols = it.takeWhile { it != '\n' }.count() }.replace("\n", "")

        return BoardOld(StringBuilder(newBoard), nMines, rows, cols)
    }

    private fun getNeighbors(cell: Cell, board: BoardOld): List<Cell> {
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

            neighbors.add(Cell(wantedRow, wantedCol))

        }
        return neighbors
    }

    private fun open(row: Int, col: Int, board: BoardOld): Char {
        val value = openSquare(row, col)
        board[row, col] = 'B'
        if (value == 'x')
            throw GameOutcome.Lost(
                "Mine opened row:$row col:$col  \n${
                    boardAsOutput(
                        board
                    )
                }"
            )
        board[row, col] = value
        return value
    }

    private fun boardAsOutput(board: BoardOld): String = StringBuilder("").let { stringBuilder ->

        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                stringBuilder.append(board[row, col])
                if (col != board.cols - 1)
                    stringBuilder.append(" ")
            }
            if (row != board.rows - 1)
                stringBuilder.append('\n')
        }

        return stringBuilder.toString()
    }
}