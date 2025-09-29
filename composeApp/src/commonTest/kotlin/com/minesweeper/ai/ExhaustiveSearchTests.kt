package com.minesweeper.ai

import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.ai.ExhaustiveSearch
import com.minesweeper.minesweeper.board.Board
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExhaustiveSearchTests {


    @Test
    fun `should find all 19  possible solutions in the specific board `() {


        val result = ExhaustiveSearch().execute(
            openSquaresOfInterest = listOf(Cell(2, 2), Cell(3, 2), Cell(3, 3)),
            board = Board(
                board = """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? 1 ? ? ?
            |? ? 3 2 ? ?
            |? ? ? ? ? ?
            |? ? ? ? ? ?
        """.trimMargin().replace(" ", "").replace("\n", ""),
                nMines = 11,
                rows = 6,
                cols = 6,
            )
        )
        val formatedResult = result.map { it.format() }
        assertEquals(19, result.size)
        assertTrue { result.map { it.board }.containsAll(solutionsWith3Mines) }
        assertTrue { result.map { it.board }.containsAll(solutionsWith4Mines_incomplete) }
    }

    @Test
    fun `bomb placement should be found possible 1`() {

        val board = Board(
            board = """
            |? ? ? 
            |? 1 ?
            |? ? ?
        """.trimMargin().replace(" ", "").replace("\n", ""),
            nMines = 1,
            rows = 3,
            cols = 3,
        )
        val result = ExhaustiveSearch().canPlaceBomb(Cell(0, 0), board)

        assertEquals(true, result)
    }

    @Test
    fun `bomb placement should be found impossible 1`() {

        val board = Board(
            board = """
            |? x ? 
            |? 1 ?
            |? ? ?
        """.trimMargin().replace(" ", "").replace("\n", ""),
            nMines = 1,
            rows = 3,
            cols = 3,
        )
        val result = ExhaustiveSearch().canPlaceBomb(Cell(0, 0), board)

        assertEquals(false, result)
    }

    @Test
    fun `bomb placement should be found possible 2`() {

        val board = Board(
            board = """
            |? x ? 
            |? 2 ?
            |? ? ?
        """.trimMargin().replace(" ", "").replace("\n", ""),
            nMines = 1,
            rows = 3,
            cols = 3,
        )
        val result = ExhaustiveSearch().canPlaceBomb(Cell(0, 0), board)

        assertEquals(true, result)
    }

    @Test
    fun `bomb placement should be found impossible 2`() {

        val board = Board(
            board = """
            |? x ? 
            |3 2 x
            |? ? ?
        """.trimMargin().replace(" ", "").replace("\n", ""),
            nMines = 1,
            rows = 3,
            cols = 3,
        )
        val result = ExhaustiveSearch().canPlaceBomb(Cell(0, 0), board)

        assertEquals(false, result)
    }

    @Test
    fun `should check if mines put solve  a given board `() {
        val board = Board(
            board = """
            |x x ? 
            |2 3 ?
            |? ? x
        """.trimMargin().replace(" ", "").replace("\n", ""),
            nMines = 1,
            rows = 3,
            cols = 3,
        )

        val result = ExhaustiveSearch().isConfigurationASolution(listOf(Cell(1, 0), Cell(1, 1)), board)

        assertEquals(true, result)
    }

    private val solutionsWith3Mines = listOf(
        """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? 1 ? ? ?
            |? x 3 2 ? ?
            |? ? x x ? ?
            |? ? ? ? ? ?
        """,
        """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? 1 x ? ?
            |? ? 3 2 ? ?
            |? x x ? ? ?
            |? ? ? ? ? ?
        """,
        """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? 1 x ? ?
            |? ? 3 2 ? ?
            |? x ? x ? ?
            |? ? ? ? ? ?
        """,
        """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? x 1 ? ? ?
            |? ? 3 2 x ?
            |? x x ? ? ?
            |? ? ? ? ? ?
        """

    ).map { it.trimMargin().replace(" ", "").replace("\n", "") }

    private val solutionsWith4Mines_incomplete =
        listOf(
            """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? x 1 ? ? ?
            |? ? 3 2 ? ?
            |? x ? x x ?
            |? ? ? ? ? ?
        """,
            """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? x 1 ? x ?
            |? ? 3 2 ? ?
            |? x x ? ? ?
            |? ? ? ? ? ?
        """,
            """
            |? ? ? ? ? ?
            |? ? ? ? ? ?
            |? ? 1 ? x ?
            |? x 3 2 ? ?
            |? x x ? ? ?
            |? ? ? ? ? ?
        """
        ).map { it.trimMargin().replace(" ", "").replace("\n", "") }
}

