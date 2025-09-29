package com.minesweeper.minesweeper.board


data class BoardOld(
    private val _board: StringBuilder,
    val nMines: Int,
    val rows: Int,
    val cols: Int
) {
    operator fun get(row: Int, col: Int) =
        _board[row * cols + col]


    operator fun set(row: Int, col: Int, value: Char) {
        _board[row * cols + col] = value
    }

    val board
        get() = _board.toString()
}


class Board(board: String, val nMines: Int, val rows: Int, val cols: Int) {
    private val _board: StringBuilder = StringBuilder(board)
    val board: String
        get() = _board.toString()


    operator fun get(row: Int, col: Int) =
        _board[row * cols + col]


    operator fun set(row: Int, col: Int, value: Char) {
        _board[row * cols + col] = value
    }


    fun format(): String {

         StringBuilder().let { output ->
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    output.append(this[row, col])
                    if(col < cols -1)
                    output.append(' ')
                }
                if(row < rows -1)
                output.append("\n")
            }
             return output.toString()
        }
    }

}

fun Board.deepCopy(): Board = Board(board, nMines, rows, cols)