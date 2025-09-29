package com.minesweeper.di

import minesweeper.MineSweeper
import org.koin.dsl.module

val commonModule = module {
    factory  { MineSweeper() }
}