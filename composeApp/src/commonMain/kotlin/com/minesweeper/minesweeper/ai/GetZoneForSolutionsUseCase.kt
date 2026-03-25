package com.minesweeper.minesweeper.ai

import com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.board.Board
import com.minesweeper.minesweeper.board.GetNeighborsOfZoneUseCase


/**
 * EPP algorithm
 * Gets the probabilities that a square is a mine
 */
class GetProbabilitiesForAZoneUseCase {
    fun execute(openSquaresOfInterest: List<Cell>, board: Board): List<Pair<Cell, Float>> {
        val sortedSolutionsMap =
            sortSolutionsByMineCount(ExhaustiveSearch().execute(openSquaresOfInterest, board))
        val probabilities = mutableListOf<Pair<Cell, Float>>()
        val zonesOfInterest =
            GetNeighborsOfZoneUseCase().execute(openSquaresOfInterest, board, sortByRow = true)

        zonesOfInterest.forEach { currentCellOfInterest ->
            //calculate each cell
            val probabilityOfMine = sumSolutionsOfCell(
                currentCell = currentCellOfInterest,
                numberOfUnknownSquares = countUnknownSquares(board) - zonesOfInterest.size,
                totalNumberOfMines = board.nMines,
                solutions = sortedSolutionsMap
            ).toFloat()
            probabilities.add(currentCellOfInterest to probabilityOfMine)
        }
        return probabilities
    }


    private fun countUnknownSquares(board: Board): Int {
        var result = 0
        board.board.forEach { if (it == MineSweeperField.HIDDEN) result++ }
        return result
    }

    private fun sumSolutionsOfCell(
        currentCell: Cell,
        numberOfUnknownSquares: Int,
        totalNumberOfMines: Int,
        solutions: Map<Int, List<Board>>
    ): Double {
        var totalNumberOfArrangementsWhereSquareContainsAMine: Double = 0.0
        var totalNumberOfAllPossibleBombArrangements: Double = 0.0
        solutions.forEach { (numberOfPlacedMines, boardSolutions) ->
            val minesLeft = totalNumberOfMines - numberOfPlacedMines
            totalNumberOfArrangementsWhereSquareContainsAMine += numberOfSolutionsWhereCellIsAMine(
                currentCell,
                boardSolutions
            ) * calculateBinomial(numberOfUnknownSquares, minesLeft)


            totalNumberOfAllPossibleBombArrangements += boardSolutions.size * calculateBinomial(
                numberOfUnknownSquares,
                minesLeft
            )
        }

        if (totalNumberOfAllPossibleBombArrangements == 0.0)
            return 0.0

        return totalNumberOfArrangementsWhereSquareContainsAMine / totalNumberOfAllPossibleBombArrangements
    }

    private fun numberOfSolutionsWhereCellIsAMine(cell: Cell, list: List<Board>): Int {
        var result = 0
        list.forEach {

            if (it[cell.row, cell.col] == MineSweeperField.MINE)
                result++
        }
        return result
    }

    private fun sortSolutionsByMineCount(boards: List<Board>): Map<Int, List<Board>> =
        mutableMapOf<Int, MutableList<Board>>().also { mineCountMap ->
            for (entry in boards) {
                // Count mines in current board
                var mineCount = 0
                for (row in 0 until entry.rows) {
                    for (col in 0 until entry.cols) {
                        if (entry[row, col] == MineSweeperField.MINE) { // Assuming 'X' represents a mine
                            mineCount++
                        }
                    }
                }


                if (!mineCountMap.containsKey(mineCount)) {
                    mineCountMap[mineCount] = mutableListOf()
                }
                mineCountMap[mineCount]!!.add(entry)
            }
        }

}


fun calculateBinomial(n: Int, k: Int): Double {
    var k1 = k
    if (k1 > n - k1) k1 = n - k1

    var b = 1.0
    var i = 1
    var m = n
    while (i <= k1) {
        b = b * m / i
        i++
        m--
    }
    return b
}

