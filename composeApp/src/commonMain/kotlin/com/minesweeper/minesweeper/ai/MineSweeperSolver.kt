package com.minesweeper.minesweeper.ai

import com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.board.Board
import com.minesweeper.minesweeper.board.deepCopy

class MineSweeperSolver {

    private val bestMovesCache: MutableList<Cell> = mutableListOf()

    private val nodesProcessed: MutableList<Cell> = mutableListOf<Cell>()
    private var nodesToProcess: MutableList<Cell> = mutableListOf<Cell>()
    private var foundMines = 0

    fun solve(boardToSolve: Board, open: (Int, Int) -> Char): Board {
        val solvedBoard = boardToSolve.deepCopy()
        foundMines = 0
        var hasWonTheGame = false
        nodesToProcess = getOpenSquares(solvedBoard)

        while (!hasWonTheGame) {
            getNextMove(solvedBoard, nodesToProcess)?.let { nextSafeMove ->
                val value = open(nextSafeMove.row, nextSafeMove.col)
                solvedBoard[nextSafeMove.row, nextSafeMove.col] = value

                if (!nodesProcessed.contains(Cell(nextSafeMove.row, nextSafeMove.col)))
                    nodesToProcess.add(Cell(nextSafeMove.row, nextSafeMove.col))
            }


            if (foundMines == boardToSolve.nMines)
                break


            //  println(solvedBoard.format())
            //  println()
        }


        for (row in 0 until solvedBoard.rows) {
            for (col in 0 until solvedBoard.cols) {
                if (solvedBoard[row, col] == MineSweeperField.HIDDEN)
                    solvedBoard[row, col] = open(row, col)
            }
        }

        return solvedBoard
    }


    private fun getNextMove(board: Board, nodes: List<Cell>): Cell? {
        if (bestMovesCache.isNotEmpty()) {
            val bestMovePopped = bestMovesCache.first()
            bestMovesCache.removeFirst()
            return bestMovePopped
        }

        if (nodes.isEmpty()) {
            throw IllegalStateException("Nodes to process  were empty")
        }

        val probabilities = GetProbabilitiesForAZoneUseCase().execute(nodes, board)
        val sortedProbabilities = probabilities.sortedBy { it.second }
        nodesProcessed.addAll(nodes)

        sortedProbabilities.forEach {
            if (it.second > 0.999f) {
                flag(board, it.first)
            } else if (it.second < 0.000f)
                bestMovesCache.add(it.first)

        }

        return sortedProbabilities.first().first
    }

    private fun flag(board: Board, cell: Cell) {
        foundMines++
        board[cell.row, cell.col] = MineSweeperField.MINE
    }


    private fun getOpenSquares(board: Board) =
        mutableListOf<Cell>().also { startingPoints ->

            for (row in 0 until board.rows) {
                for (col in 0 until board.cols) {
                    val value = board[row, col]
                    if (value != '?')
                        startingPoints.add(Cell(row, col))
                }
            }
        }

    private fun getRandomUnopenedCell(board: Board): Cell {
        val unOpenedCells = mutableListOf<Cell>()
        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                if (board[row, col] == '?')
                    unOpenedCells.add(Cell(row, col))
            }
        }
        return unOpenedCells.random()
    }


    private class ProbabilityBoard(board: Board) {
        private val rows: Int = board.rows
        private val cols: Int = board.cols
        private val _board = MutableList<Float>(rows * cols) { 1f }

        operator fun set(row: Int, col: Int, value: Float): Unit {
            _board[row * cols + col] = value
        }

        operator fun get(row: Int, col: Int): Float = _board[row * cols + col]

        val board: List<Float>
            get() = _board


    }
}