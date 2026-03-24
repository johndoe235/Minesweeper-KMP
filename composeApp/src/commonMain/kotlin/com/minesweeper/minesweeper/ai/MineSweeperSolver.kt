package com.minesweeper.minesweeper.ai

import  com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.board.Board
import com.minesweeper.minesweeper.board.deepCopy


class MineSweeperSolver() {

    private val bestMovesCache: MutableList<Cell> = mutableListOf()
    private val nodesProcessed: MutableList<Cell> = mutableListOf<Cell>()
    private var nodesToProcess: MutableList<Cell> = mutableListOf<Cell>()
    private var foundMines = 0

    private var _open: ((Int, Int) -> Char)? = null
    private fun open(row: Int, col: Int): Char {
        return _open!!.invoke(row, col)
    }

    fun solve(boardToSolve: Board, openSquare: (Int, Int) -> Char): Board {
        _open = openSquare
        val solvedBoard = boardToSolve.deepCopy()
        var turns = 0
        foundMines = 0
        var hasWonTheGame = false
        nodesToProcess = getOpenSquares(solvedBoard)


        while (!hasWonTheGame) {
            println(solvedBoard.format())
            println("turns ${turns++}  mines found: $foundMines")

            getNextMove(solvedBoard, nodesToProcess)?.let { nextSafeMove ->
                val value = open(nextSafeMove.row, nextSafeMove.col)
                solvedBoard[nextSafeMove.row, nextSafeMove.col] = value

                if (!nodesProcessed.contains(nextSafeMove))
                    nodesToProcess.add(nextSafeMove)
            }


            if (foundMines == boardToSolve.nMines)
                break

        }

        openHiddenSquares(solvedBoard)

        _open = null
        return solvedBoard
    }


    fun openHiddenSquares(solvedBoard: Board) {
        for (row in 0 until solvedBoard.rows) {
            for (col in 0 until solvedBoard.cols) {
                if (solvedBoard[row, col] == MineSweeperField.HIDDEN)
                    solvedBoard[row, col] = open(row, col)
            }
        }
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
            } else if (it.second < 0.0001f)
                bestMovesCache.add(it.first)
        }

        println(sortedProbabilities.first())
        return if (sortedProbabilities.isEmpty() || sortedProbabilities.first().second  == 1f)
            null
        else
            sortedProbabilities.first().first
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


}