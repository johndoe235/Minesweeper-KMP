package com.minesweeper.ai

import com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.ai.MineSweeperSolver
import com.minesweeper.minesweeper.board.Board
import kotlin.test.Test
import kotlin.test.assertEquals

class MineSweeperSolverTests {

    @Test
    fun `should solve board`() {
        val rows = 3
        val cols = 4
        val mines = 2

        val board = """
            |1 x 1 0
            |2 2 1 0
            |x 1 0 0
        """.trimMargin()

        val obfuscatedboard = """
                |? ? ? ?
                |? ? ? ?
                |? ? ? 0
            """.trimMargin()

        doTest(board, obfuscatedboard, rows, cols, mines) { actual ->
            assertEquals(expected = board, actual = actual.format())
        }
    }

    @Test
    fun `should solve a 5x6 field `() {
        val rows = 5
        val cols = 6
        val mines = 3

        val board = """
            |x 1 0 0 0 0
            |1 2 1 1 0 0
            |0 1 x 1 0 0
            |0 1 1 1 1 1
            |0 0 0 0 1 x
        """.trimMargin()

        val obfuscationBoard = """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? ? ? ? 1
            |? ? ? ? ? ?
        """.trimMargin()

        doTest(
            board, obfuscationBoard,
            rows = rows,
            cols = cols,
            mines = mines,
        ) { actual ->
            assertEquals(expected = board, actual.format())
        }
    }

    @Test
    fun `should solve a 6x6 field `() {
        val rows = 6
        val cols = 6
        val mines = 4
        val board = """
            |0 0 0 0 0 0
            |0 1 2 2 1 0
            |0 1 x x 1 0
            |0 1 2 3 2 1
            |1 1 0 1 x 1
            |x 1 0 1 1 1
        """.trimMargin()

        val obfuscationBoard = """
            |? ? ? ? ? 0
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? ? ? ? 1
            |? ? ? ? ? ?
            |? ? ? ? ? ?
        """.trimMargin()

        doTest(board, obfuscationBoard, rows, cols, mines) { actual ->
            assertEquals(board, actual.format())
        }
    }

    @Test
    fun `simple map 1 `() {
        val rows = 6
        val cols = 6
        val mines = 6
        val board = """
            |1 x 1 1 x 1
            |2 2 2 1 2 2
            |2 x 2 0 1 x
            |2 x 2 1 2 2
            |1 1 1 1 x 1
            |0 0 0 1 1 1
            """.trimMargin()


        val obfuscatedBoard = """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? ? 0 ? ?
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |0 0 0 ? ? ?
            """.trimMargin()

        doTest(board, obfuscatedBoard, rows, cols, mines) { actual ->
            assertEquals(board, actual.format())
        }

    }


    @Test
    fun `should return question mark  when board has no solution `() {

        val rows = 2
        val cols = 3
        val mines = 1
        val board = """
            |0 1 1
            |0 1 x
        """.trimMargin()

        val obfuscationBoard = """
            |0 ? ? 
            |0 ? ? 
        """.trimMargin()

        doTest(board, obfuscationBoard, rows, cols, mines) { actual ->
            assertEquals(board, actual.format())
        }
    }

    @Test
    fun `should return question mark  when board has no solution  2`() {

        val rows = 2
        val cols = 3
        val mines = 1
        val board = """
            |0 1 x
            |0 1 1
        """.trimMargin()

        val obfuscationBoard = """
            |0 ? ? 
            |0 ? ? 
        """.trimMargin()

        doTest(board, obfuscationBoard, rows, cols, mines) { actual ->
            assertEquals(board, actual.format())
        }
    }


}

private fun doTest(
    boardString: String,
    obfuscationBoardString: String,
    rows: Int,
    cols: Int,
    mines: Int,
    assertion: (actual: Board) -> Unit
) {

    val board = Board(
        board = boardString.replace(" ", "").replace("\n", ""),
        nMines = mines,
        rows = rows,
        cols = cols,
    )
    val obfuscatedboard = Board(
        board = obfuscationBoardString.replace(" ", "").replace("\n", ""),
        nMines = board.nMines,
        rows = board.rows,
        cols = board.cols
    )

    val actual = MineSweeperSolver().solve(
        obfuscatedboard,
        { row, col -> board[row, col].also { if (it == MineSweeperField.MINE) throw Exception("Boom at row:$row col:$col") } })

    assertion.invoke(actual)
}

