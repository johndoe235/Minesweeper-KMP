package com.minesweeper.board

import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.board.Board
import com.minesweeper.minesweeper.board.GetNeighborsUseCase
import com.minesweeper.minesweeper.board.GetNeighborsUseCase.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetNeighborsUseCaseTests {

    val boardString = """
            |1 2 3 4
            |5 6 7 8
            |9 A B C
        """.trimMargin()

    val board = Board(
        board = boardString.replace(" ", "").replace("\n", ""),
        nMines = 0,
        rows = 3,
        cols = 4
    )

    val getNeighborsUseCase = GetNeighborsUseCase()

    @Test
    fun `test 1 should return correct neighboring cells for square 5`() {

        val cell = Cell(1, 0) // 5
        val result = getNeighborsUseCase.execute(
            cell = cell,
            board = board
        ).map { board[it.row, it.col] }

        assertEquals(listOf('1', '2', '6', '9', 'A'), result)
    }

    @Test
    fun `test 2 should return correct neighboring cells for square 6`() {

        val cell = Cell(1, 1) // 6
        val result = getNeighborsUseCase.execute(
            cell = cell,
            board = board
        ).map { board[it.row, it.col] }

        assertEquals(listOf('1', '2', '3', '5', '7', '9', 'A', 'B'), result)
    }

    @Test
    fun `test 2 should return correct neighboring cells for square 1`() {

        val cell = Cell(0, 0) // 1
        val result = getNeighborsUseCase.execute(
            cell = cell,
            board = board
        ).map { board[it.row, it.col] }

        assertEquals(listOf('2', '5', '6'), result)
    }

    @Test
    fun `test 2 should return correct neighboring cells for square C`() {

        val cell = Cell(2, 3) // C
        val result = getNeighborsUseCase.execute(
            cell = cell,
            board = board
        ).map { board[it.row, it.col] }

        assertEquals(listOf('7', '8', 'B'), result)
    }

    @Test
    fun `should return only opened squares`() {
        val cell = Cell(2, 3) // C
        val result = getNeighborsUseCase.execute(
            cell = cell,
            board = board,
            filterOptions = FilterOptions.ONLY_OPENED
        ).map { board[it.row, it.col] }

        assertEquals(listOf('7', '8', 'B'), result)
    }

    @Test
    fun `should return only unopened squares for square B`() {
        val boardString = """
            |1 2 3 4
            |5 6 7 ?
            |9 A B ?
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 4
        )

        val result = getNeighborsUseCase.execute(Cell(2, 2), board, FilterOptions.ONLY_UNOPENED)

        assertEquals(listOf(Cell(1, 3), Cell(2, 3)), result)
    }

    @Test
    fun `should return only opened squares for square B `() {
        val boardString = """
            |1 2 3 4
            |5 6 7 ?
            |9 A B ?
        """.trimMargin()

        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 3,
            cols = 4
        )

        val result = getNeighborsUseCase.execute(Cell(2, 2), board, FilterOptions.ONLY_OPENED)
            .map { board[it.row, it.col] }

        assertEquals(listOf('6', '7', 'A'), result)
    }


}