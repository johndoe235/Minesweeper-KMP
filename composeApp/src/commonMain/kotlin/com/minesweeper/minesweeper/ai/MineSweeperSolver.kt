package com.minesweeper.minesweeper.ai

import  com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.minesweeper.board.AreAllNeighborsOpenedUseCase
import com.minesweeper.minesweeper.board.Board
import com.minesweeper.minesweeper.board.deepCopy


class MineSweeperSolver() {

    private val bestMovesCache: MutableList<Cell> = mutableListOf()
    private var nodesToProcess: MutableSet<Cell> = mutableSetOf()
    private var foundMines = 0

    fun solve(boardToSolve: Board, open: (Int, Int) -> Char): Board {
        val solvedBoard = boardToSolve.deepCopy()
        foundMines = 0
        nodesToProcess = getOpenSquares(solvedBoard).toMutableSet()
        //1.find all mines
        do {
            getNextMove(solvedBoard, nodesToProcess)?.let { nextSafeMove ->
                val value = open(nextSafeMove.row, nextSafeMove.col)
                solvedBoard[nextSafeMove.row, nextSafeMove.col] = value
                    nodesToProcess.add(nextSafeMove)
            }


        } while (foundMines < boardToSolve.nMines)

        //open all squares that aren't flagged
        for (row in 0 until solvedBoard.rows) {
            for (col in 0 until solvedBoard.cols) {
                if (solvedBoard[row, col] == MineSweeperField.HIDDEN) solvedBoard[row, col] =
                    open(row, col)
            }
        }

        return solvedBoard
    }

    private fun getNextMove(board: Board, nodes: Set<Cell>): Cell? {
        if (bestMovesCache.isNotEmpty()) {

            return  bestMovesCache.removeFirst()
        }

        if (nodes.isEmpty()) {
            throw IllegalStateException("Nodes to process  were empty")
        }

        val probabilities = GetProbabilitiesForAZoneUseCase().execute(nodes.toList(), board)
        val sortedProbabilities = probabilities.sortedBy { it.second }

        sortedProbabilities.forEach {
            if (it.second > 0.999f) {
                flag(board, it.first)
            } else if (it.second < 0.0001f) bestMovesCache.add(it.first)
        }

        //removeInnerNodes(board)
        return if (sortedProbabilities.isEmpty() || sortedProbabilities.first().second == 1f) null
        else sortedProbabilities.first().first
    }

    private fun flag(board: Board, cell: Cell) {
        foundMines++
        board[cell.row, cell.col] = MineSweeperField.MINE
    }


    private fun getOpenSquares(board: Board) = mutableListOf<Cell>().also { startingPoints ->
        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                val value = board[row, col]
                if (value != '?') startingPoints.add(Cell(row, col))
            }
        }
    }

    fun removeInnerNodes(board: Board) {
        val processedNodes = mutableSetOf<Cell>()
        val usecase = AreAllNeighborsOpenedUseCase()
        nodesToProcess.forEach {
            if (usecase.execute(it,board)) {
                processedNodes.add(it)
            }
        }
        nodesToProcess.removeAll(processedNodes)
    }


}