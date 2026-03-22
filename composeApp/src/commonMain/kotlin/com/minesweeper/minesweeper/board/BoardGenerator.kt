package com.minesweeper.minesweeper.board

import com.minesweeper.minesweeper.MapDifficulty
import com.minesweeper.minesweeper.MineSweeperField
import kotlin.random.Random



object BoardGenerator {

    fun generateGameBoard(mapDifficulty: MapDifficulty): GameBoard {
        createEmptyMineGrid(mapDifficulty.rows * mapDifficulty.cols).let { emptyGrid ->
            val mineLocations = generateMinedLocations(
                numberOfMines = mapDifficulty.numberOfMines,
                rows = mapDifficulty.rows,
                cols = mapDifficulty.cols
            )
            addMinesToGrid(
                emptyGrid, mineLocations,
                rows = mapDifficulty.rows,
                cols = mapDifficulty.cols
            ).let { minedGrid ->
                addHintsToMinedGrid(
                    minedGrid,
                    mineLocations = mineLocations,
                    rows = mapDifficulty.rows,
                    cols = mapDifficulty.cols
                ).let { completedGrid: String ->
                    val unmaskedBoard = Board(
                        board = completedGrid,
                        nMines = mapDifficulty.numberOfMines,
                        rows = mapDifficulty.rows,
                        cols = mapDifficulty.cols
                    )
                    val maskedBoard = Board(
                        board = MineSweeperField.HIDDEN.toString().repeat(mapDifficulty.rows * mapDifficulty.cols),
                        nMines = mapDifficulty.numberOfMines,
                        rows = mapDifficulty.rows,
                        cols = mapDifficulty.cols
                    )
                    return GameBoard(
                        unmaskedBoard = unmaskedBoard,
                        maskedBoard = maskedBoard,
                        rows = mapDifficulty.rows,
                        cols = mapDifficulty.cols
                    )
                }
            }
        }
    }


    fun createEmptyMineGrid(gridSize: Int): String = StringBuilder("0".repeat(gridSize)).toString()

    fun addMinesToGrid(
        grid: String,
        mineLocations: List<Pair<Int, Int>>,
        rows: Int,
        cols: Int
    ): String {
        if (grid.length != rows * cols)
            return ""

        StringBuilder(grid).let { stringBuilder ->

            mineLocations.forEach {
                stringBuilder[it.first * cols + it.second] = MineSweeperField.MINE
            }

            return stringBuilder.toString()
        }
    }

    fun addHintsToMinedGrid(
        grid: String,
        mineLocations: List<Pair<Int, Int>>,
        rows: Int,
        cols: Int
    ): String {
        if (grid.length != rows * cols)
            throw IllegalArgumentException("Field length should be $rows * $cols but was ${grid.length}")

        StringBuilder(grid).let { stringBuilder ->

            mineLocations.forEach { mineLocation ->
                for (adjacentOffset in listOf(
                    0 to 1, //row ; col +1
                    0 to -1, //row ; col -1
                    1 to 0, //row + 1 ; col
                    1 to 1, // row +1 ; col +1
                    1 to -1, // row +1 ; col -1
                    -1 to 0, // row - 1 ; col
                    -1 to -1, //row -1 ; col -1
                    -1 to 1, // row -1 ; col +1
                )) {
                    val row = (mineLocation.first + adjacentOffset.first)
                    if (row < 0 || row >= rows)
                        continue
                    val col = mineLocation.second + adjacentOffset.second
                    if (col < 0 || col >= cols)
                        continue

                    val index = row * cols + col
                    if (stringBuilder[index] != MineSweeperField.MINE)
                        stringBuilder[index] = stringBuilder[index] + 1


                }
            }
            return stringBuilder.toString()
        }

    }


    private fun generateMinedLocations(
        numberOfMines: Int,
        rows: Int,
        cols: Int
    ): List<Pair<Int, Int>> {
        val mineLocations = mutableListOf<Pair<Int, Int>>()

        while (mineLocations.size != numberOfMines) {
            val row = Random.nextInt(0, rows)
            val col = Random.nextInt(0, cols)

            if (!mineLocations.contains(row to col))
                mineLocations.add(row to col)
        }
        return mineLocations
    }

}