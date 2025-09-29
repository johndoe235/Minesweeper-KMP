package com.minesweeper.logger

class DesktopLogger: Logger {
    override fun log(string: String) {
        println(string)
    }
}