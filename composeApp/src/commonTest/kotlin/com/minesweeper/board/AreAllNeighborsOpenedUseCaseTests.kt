package com.minesweeper.board

import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.board.AreAllNeighborsOpenedUseCase
import com.minesweeper.minesweeper.board.Board
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AreAllNeighborsOpenedUseCaseTests {

    private val useCase = AreAllNeighborsOpenedUseCase()

    @Test
    fun `should return true when all neighbors are opened`() {
        val boardString = """
            |1 2 3
            |4 5 6
            |7 8 9
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 3
        )

        val cell = Cell(1, 1) // center cell '5'
        val result = useCase.execute(cell, board)

        assertTrue(result)
    }

    @Test
    fun `should return false when a neighbor is hidden`() {
        val boardString = """
            |1 2 3
            |4 5 ?
            |7 8 9
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 3
        )

        val cell = Cell(1, 1) // center cell '5'
        val result = useCase.execute(cell, board)

        assertFalse(result)
    }

    @Test
    fun `should return true when a neighbor is a mine`() {
        val boardString = """
            |1 2 3
            |4 5 x
            |7 8 9
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 1,
            rows = 3,
            cols = 3
        )

        val cell = Cell(1, 1) // center cell '5'
        val result = useCase.execute(cell, board)

        assertTrue(result)
    }

    @Test
    fun `should return true for corner cell when all neighbors are opened`() {
        val boardString = """
            |1 2 3
            |4 5 6
            |7 8 9
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 3
        )

        val cell = Cell(0, 0) // top-left corner '1'
        val result = useCase.execute(cell, board)

        assertTrue(result)
    }

    @Test
    fun `should return false for corner cell when a neighbor is hidden`() {
        val boardString = """
            |1 ? 3
            |4 5 6
            |7 8 9
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 3
        )

        val cell = Cell(0, 0) // top-left corner '1'
        val result = useCase.execute(cell, board)

        assertFalse(result)
    }

    @Test
    fun `should return true for bottom-right corner when all neighbors are opened`() {
        val boardString = """
            |1 2 3
            |4 5 6
            |7 8 9
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 3
        )

        val cell = Cell(2, 2) // bottom-right corner '9'
        val result = useCase.execute(cell, board)

        assertTrue(result)
    }

    @Test
    fun `should return false for edge cell when a neighbor is hidden`() {
        val boardString = """
            |1 2 3
            |? 5 6
            |7 8 9
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 3
        )

        val cell = Cell(0, 1) // top-edge cell '2'
        val result = useCase.execute(cell, board)

        assertFalse(result)
    }

    @Test
    fun `should return true for edge cell when all neighbors are opened`() {
        val boardString = """
            |1 2 3
            |4 5 6
            |7 8 9
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 3
        )

        val cell = Cell(0, 1) // top-edge cell '2'
        val result = useCase.execute(cell, board)

        assertTrue(result)
    }

    @Test
    fun `should return false when multiple neighbors are hidden`() {
        val boardString = """
            |? ? ?
            |? 5 ?
            |? ? ?
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 3
        )

        val cell = Cell(1, 1) // center cell '5'
        val result = useCase.execute(cell, board)

        assertFalse(result)
    }

    @Test
    fun `should return true for 1x1 board with no neighbors`() {
        val boardString = "1"

        val board = Board(
            board = boardString,
            nMines = 0,
            rows = 1,
            cols = 1
        )

        val cell = Cell(0, 0)
        val result = useCase.execute(cell, board)

        assertTrue(result)
    }

    @Test
    fun `should handle rectangular board correctly`() {
        val boardString = """
            |1 2 3 4
            |5 6 7 8
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 2,
            cols = 4
        )

        val cell = Cell(0, 2) // cell '3'
        val result = useCase.execute(cell, board)

        assertTrue(result)
    }

    @Test
    fun `should return false for rectangular board when neighbor is hidden`() {
        val boardString = """
            |1 2 3 4
            |5 6 ? 8
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 2,
            cols = 4
        )

        val cell = Cell(0, 2) // cell '3'
        val result = useCase.execute(cell, board)

        assertFalse(result)
    }
}

