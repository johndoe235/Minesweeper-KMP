package com.minesweeper

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform