package com.minesweeper.ai

import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.ai.GetProbabilitiesForAZoneUseCase
import com.minesweeper.minesweeper.board.Board
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetProbabilitiesForAZoneUseCaseTests {

    @Test
    fun `should return the correct probabilities for a zone and board`() {
        val result = GetProbabilitiesForAZoneUseCase().execute(
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
        val expectedValues = listOf(
            0.0454F,
            0.0454F,
            0.0454F,
            0.352F,
            0.159F,
            0.182F,
            0.352F,
            0.182F,
            0.841F,
            0.648F,
            0.648F,
            0.182F
        )
        val epsilon = 0.0005
        expectedValues.forEachIndexed { index, expected ->
            val actual = result[index].second
            assertTrue("expected:$expected actual:$actual", { abs(expected - actual) < epsilon })
        }

    }

    @Test
    fun `should return all zeroes`() {
        val actual = GetProbabilitiesForAZoneUseCase().execute(
            openSquaresOfInterest = listOf(Cell(2, 3)),
            board = Board(
                board = """
                    |? ? ? ?
                    |? ? ? ?
                    |? ? ? 0
        """.trimMargin().replace(" ", "").replace("\n", ""),
                nMines = 11,
                rows = 3,
                cols = 4,
            )
        )
        val expected = listOf(0f, 0f, 0f)
        assertTrue { actual.size == 3 }
        assertEquals(expected, actual.map { it.second })
    }

    @Test
    fun `should return all ones`() {
        val actual = GetProbabilitiesForAZoneUseCase().execute(
            openSquaresOfInterest = listOf(Cell(2, 3)),
            board = Board(
                board = """
                    |? ? ? ?
                    |? ? ? ?
                    |? ? ? 3
        """.trimMargin().replace(" ", "").replace("\n", ""),
                nMines = 11,
                rows = 3,
                cols = 4,
            )
        )
        val expected = listOf(1f, 1f, 1f)
        assertTrue { actual.size == 3 }
        assertEquals(expected, actual.map { it.second })
    }
}