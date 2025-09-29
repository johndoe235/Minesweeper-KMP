package com.minesweeper.di

import com.minesweeper.features.menu.ui.MenuViewModel
import com.minesweeper.features.minesweeper.ui.MineSweeperViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {

    viewModelOf(::MineSweeperViewModel)
    viewModelOf(::MenuViewModel)
}