package com.minesweeper.board

import com.minesweeper.minesweeper.ai.Cell
import com.minesweeper.minesweeper.board.Board
import com.minesweeper.minesweeper.board.GetNeighborsOfZoneUseCase
import kotlin.test.Test
import kotlin.test.assertEquals


class GetNeighborsOfZoneUseCaseTests {

    /*



    zone of interest
    x1 = A
    x2 = B
    x3 = C
    x4 = D
    x5 = E
    x6 = F
    x7 = G
    x8 = H
    x9 = I
    x10 = J
    x11 = K
    x12 = L
     */
    @Test
    fun `should return correct zone of interest`() {
        val boardString = """
            |? ? ? ? ? ? 
            |? A B C ? ? 
            |? D 1 E F ? 
            |? G 3 2 H ? 
            |? I J K L ? 
            |? ? ? ? ? ? 
        """.trimMargin()

        val obfuscatedBoardString = """
            |? ? ? ? ? ? 
            |? ? ? ? ? ? 
            |? ? 1 ? ? ? 
            |? ? 3 2 ? ? 
            |? ? ? ? ? ? 
            |? ? ? ? ? ? 
        """.trimMargin()

        val obfuscatedBoard = Board(
            board = obfuscatedBoardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 6,
            cols = 6
        )
        val board = Board(
            board = boardString.replace(" ", "").replace("\n", ""),
            nMines = 0,
            rows = 6,
            cols = 6
        )

        val result =
            GetNeighborsOfZoneUseCase().execute(
                openSquares = listOf(Cell(2, 2), Cell(3, 2), Cell(3, 3)),
                board = obfuscatedBoard,
                sortByRow = true
            )
                .map { board[it.row, it.col] }
        assertEquals(('A'..'L').toList(), result)
    }

}