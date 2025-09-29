package com.minesweeper.di

import com.minesweeper.logger.DesktopLogger
import com.minesweeper.logger.Logger
import minesweeper.MineSweeper
import org.koin.dsl.module


val desktopModule = module {
    single<Logger> { DesktopLogger() } // Singleton instance
}