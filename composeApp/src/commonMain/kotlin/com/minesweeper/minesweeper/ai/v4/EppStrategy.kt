package com.minesweeper.minesweeper.ai.v4

import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.board.BoardOld


interface Strategy {
    @Throws(GameOutcome::class)
    fun solve(): Result<BoardOld>
}


data class ProbabilityBoard(val rows: Int, val cols: Int) {

    private val table = MutableList<Float>(rows*cols) { 1f}
    operator fun get(row: Int, col: Int) =
        table[row * cols + col]


    operator fun set(row: Int, col: Int, value: Float) {
        table[row * cols + col] = value
    }

    fun setToOnes() {
        table.fill(1f)
    }
}


class EppStrategy(
    val board: BoardOld,
    val openSquare: (row: Int, col: Int) -> Char,
    val getNeighbors: (cell: Cell) -> List<Cell>
) : Strategy {

    private val probabilityBoard = ProbabilityBoard(board.rows,board.cols)

    private val flaggedSquares = 0
    override fun solve(): Result<BoardOld> {



        Result.success(board)

        return Result.failure(GameOutcome.Lost(""))
    }

    /**
     *  Click a random square in the set of unrevealed squares on the board.
     * If a bomb is revealed, the game is over.
     * If a bomb is not revealed, go to step B.
     */
    fun A() {


    }



    /**
     * Apply the EPP algorithm to the board.
     * Flag all squares in the perimeter with probability of 1.
     * Flag all squares outside the perimeter with a probability of 1.
     * If the number of newly flagged squares plus the old flags equals the total
     * number of bombs on the board b, the player has found all the bombs and wins.
     * Click all squares in the perimeter with probability of O.
     * Click all squares outside the perimeter with a probability of O.
     * If any square with a probability of I or 0 was found, repeat step B.
     * If no square with a probability of I or 0 was found, go to step C. -15
     */
    fun B() {
    

    }


    /**
     *
     * If there is no perimeter, go to step A.
     * Click on the square in the perimeter (or the unknown square) with the lowest
     * probability. If any squares identically have the lowest probability, select one at
     * random.
     * If a bomb is revealed, the game is over.
     * If a bomb is not revealed, go to step B.
     */
    fun C() {

    }
}
