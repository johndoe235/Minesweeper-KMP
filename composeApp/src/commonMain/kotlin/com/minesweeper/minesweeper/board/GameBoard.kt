package com.minesweeper.minesweeper.board

import com.minesweeper.minesweeper.BoardRaw
import com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.checkAndGet


class GameBoard(
    private val unmaskedBoard: BoardRaw,
    private val maskedBoard: MutableList<Char> = MutableList(unmaskedBoard.length) { '?' },
    val rows: Int,
    val cols: Int,

    ) {



    fun flag(row: Int, column: Int) {
        if (maskedBoard[row * cols + column] == MineSweeperField.HIDDEN)
            maskedBoard[row * cols + column] = MineSweeperField.FLAG
        else if (maskedBoard[row * cols + column] == MineSweeperField.FLAG)
            maskedBoard[row * cols + column] = MineSweeperField.HIDDEN
    }

    fun open(row: Int, column: Int): Char {
        if(  maskedBoard[row * cols + column] == MineSweeperField.FLAG)
            return MineSweeperField.FLAG

        return unmaskedBoard[row * cols + column].let {
            maskedBoard[row * cols + column] = it
            it
        }
    }


    fun getBoard(): String = maskedBoard.joinToString(separator = "", prefix = "", postfix = "")

    fun openAdjacentEmptySquares(row: Int, column: Int) {
        findAdjacentEmptySquares(row, column).let { emptySquares ->
            emptySquares.forEach {
                open(it.first, it.second)
            }
        }
    }

    ///bfs
    private fun findAdjacentEmptySquares(row: Int, column: Int): List<Pair<Int, Int>> {
        val squaresThatHaveBeenVisited:MutableList<Pair<Int,Int>> = mutableListOf<Pair<Int, Int>>()
        val squaresToVisit = mutableListOf(row to column)
        mutableListOf<Pair<Int, Int>>()

        while (squaresToVisit.isNotEmpty()) {
            val currentSquare = squaresToVisit.removeAt(0)
            squaresThatHaveBeenVisited.add(currentSquare)

            for (it in listOf(
                0 to 1, //row ; col +1
                0 to -1, //row ; col -1
                1 to 0, //row + 1 ; col
                -1 to 0, // row - 1 ; col
            )) {
                val adjecentSquare = currentSquare.first + it.first to currentSquare.second + it.second
                val squareSymbol = unmaskedBoard.checkAndGet(adjecentSquare.first, adjecentSquare.second, rows, cols)

                if (squareSymbol == null || squaresToVisit.contains(adjecentSquare) || squaresThatHaveBeenVisited.contains(
                        adjecentSquare
                    )
                )
                    continue

                if (squareSymbol == MineSweeperField.EMPTY) {
                    squaresToVisit.add(adjecentSquare)
                } else if (MineSweeperField.NUMBER.contains(squareSymbol))
                    squaresThatHaveBeenVisited.add(adjecentSquare)
            }
        }
        return squaresThatHaveBeenVisited
    }


    companion object {
        val EmptyGameBoard = GameBoard(unmaskedBoard = "", maskedBoard = mutableListOf(), rows = 0, cols = 0)
    }
}





