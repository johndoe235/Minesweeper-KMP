package com.minesweeper.minesweeper

typealias BoardRaw = String

fun BoardRaw.checkAndGet(row: Int, column: Int, rows: Int, cols: Int): Char? =
    if (row < 0 || column < 0 || row >= rows || column >= cols || row * cols + column < 0 || row * cols + column >= this.length)
        null
    else
        this[row * cols + column]



