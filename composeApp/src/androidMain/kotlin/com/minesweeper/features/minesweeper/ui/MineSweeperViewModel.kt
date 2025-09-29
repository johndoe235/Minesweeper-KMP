package com.minesweeper.features.minesweeper.ui

import androidx.lifecycle.ViewModel
import com.minesweeper.minesweeper.MapDifficulty
import minesweeper.MineSweeper


class MineSweeperViewModel(private val mineSweeper: MineSweeper) : ViewModel() {


    init {
        start(mapDifficulty = MapDifficulty(9,8,10))
    }

    val mineSweeperUiState = mineSweeper.mineSweeperUiState

    fun start(mapDifficulty: MapDifficulty) = mineSweeper.start(mapDifficulty)

    fun open(row: Int, column: Int) = mineSweeper.open(row, column)

    fun restart() = mineSweeper.restart()

    fun flag(row: Int, column: Int) = mineSweeper.flag(row, column)

    fun toggleFlag() = mineSweeper.toggleFlag()


}