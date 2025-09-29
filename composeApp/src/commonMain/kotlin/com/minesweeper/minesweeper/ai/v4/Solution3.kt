package com.minesweeper.minesweeper.ai.v4

import com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.board.BoardOld


/*abstract class GameException(message: String) : Exception()
class GameWon : GameException("")
class GameLost(message: String) : GameException(message)*/
sealed class GameOutcome(message: String? = null) : Exception(message) {
    class Lost(message: String) : GameOutcome(message)
    data object Won: GameOutcome()
}


class MineSweeperSolverV3(
    val boardString: String,
    val nMines: Int,
    val openSquare: (row: Int, col: Int) -> Char
) {

    private var board = createBoard(boardString, nMines)
    var minesPut = 0


    fun solve(): String {
      // val newBoard = createBoard(boardString, nMines)
       // EppStrategy(board=newBoard,openSquare={row,col ->open(row,col)}, getNeighbors = ::getNeighbors)
        return ""
    }

    /*
 1. If the number of mines adjacent to a square is equal to the value in that square, then we know that all
 the neighbouring unknown squares can be probed (opened) since they contain no mines.
 2. If the number of unknown squares plus the number of mines adjacent to a square equals the value in that
 square, then we know that all the unknown neighbours of that squares can be marked (with a mine).
 Refer to Figure 3 for an example
 */
    fun solveWithBsStrategy(): String {

        val safeSquaresToProbe = ArrayDeque<Cell>().also { it.addAll(getStartingPoints()) }
        val probedSquares = mutableSetOf<Cell>()
        while (minesPut <= board.nMines) {
            val probedSquare =
                if (safeSquaresToProbe.isNotEmpty()) safeSquaresToProbe.removeFirst() else random()

            var hasBomb = false
            val neighboringSquares = getNeighbors(probedSquare)
            val unrevealedNeighboringSquares = getUnrevealedNeighbors(neighboringSquares)
            val markedNeighboringSquares = getMarkedAsMineNeighbors(neighboringSquares)

            if (board[probedSquare.row, probedSquare.col] == '?') {
                //before opening check for a mine.

                for (neighbor in neighboringSquares) {
                    if (label(neighbor) == '?' || label(neighbor) == 'x')
                        continue

                    val neighborsUnreleavedSquares =
                        getUnrevealedNeighbors(getNeighbors(neighbor)).size
                    if (neighborsUnreleavedSquares == label(neighbor).digitToInt()) {
                        markAllAsMine(listOf(probedSquare))
                        hasBomb = true
                    }
                }
                if (hasBomb)
                    continue

                open(probedSquare.row, probedSquare.col)
            }
            probedSquares.add(probedSquare)

            val valueOfProbedSquare = label(probedSquare).digitToInt()

            //apply theorem 1


            if (unrevealedNeighboringSquares.size == valueOfProbedSquare - markedNeighboringSquares.size) {
                markAllAsMine(unrevealedNeighboringSquares)
            }

            //apply theorem 2
            if (markedNeighboringSquares.size == valueOfProbedSquare) {
                unrevealedNeighboringSquares.forEach {
                    if (!probedSquares.contains(it)) {
                        safeSquaresToProbe.add(it)
                    }
                }
            }
        }

        openUnopenedSquares()
        return boardAsOutput(board)

    }


    fun openUnopenedSquares() {
        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                if (board[row, col] == MineSweeperField.HIDDEN) {
                    val value = openSquare(row, col)
                    board[row, col] = value
                }
            }
        }
    }

    fun random(): Cell {
        val unOpenedCells = mutableListOf<Cell>()
        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                if (board[row, col] == '?')
                    unOpenedCells.add(Cell(row, col))
            }
        }
        //if (unOpenedCells.isEmpty())
        //  throw ()
        return unOpenedCells.random()
    }

    private fun open(row: Int, col: Int): Char {
        val value = openSquare(row, col)
        board[row, col] = 'B'
        if (value == 'x')
            throw GameOutcome.Lost(
                "Mine opened row:$row col:$col squaresOpened:$minesPut minesLeft:${board.nMines - minesPut} \n${
                    boardAsOutput(
                        board
                    )
                }"
            )
        board[row, col] = value
        return value
    }

    private fun markAllAsMine(mines: List<Cell>) {
        mines.forEach {
            minesPut++
            board[it.row, it.col] = 'x'
        }
    }

    private fun getNeighbors(cell: Cell): List<Cell> {
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

    private fun getUnrevealedNeighbors(squares: List<Cell>): List<Cell> {
        val unrevealedNeighbors = mutableListOf<Cell>()

        for (square in squares) {
            if (board[square.row, square.col] == '?')
                unrevealedNeighbors.add(Cell(square.row, square.col))
        }
        return unrevealedNeighbors
    }

    private fun getMarkedAsMineNeighbors(squares: List<Cell>): List<Cell> {
        val markedMines = mutableListOf<Cell>()

        for (square in squares) {
            if (board[square.row, square.col] == 'x')
                markedMines.add(Cell(square.row, square.col))
        }
        return markedMines
    }

    private fun label(cell: Cell) = board[cell.row, cell.col]

    private fun createBoard(board: String, nMines: Int): BoardOld {
        val rows = board.count { it == '\n' } + 1
        var cols: Int

        val newBoard = board.replace(" ", "")
            .also { cols = it.takeWhile { it != '\n' }.count() }.replace("\n", "")

        return BoardOld(StringBuilder(newBoard), nMines, rows, cols)
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


    private fun getStartingPoints(): List<Cell> {
        val startingPoints = mutableListOf<Cell>()

        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                val value = board[row, col]
                if (value != '?')
                    startingPoints.add(Cell(row, col))
            }
        }

        return startingPoints
    }

}


