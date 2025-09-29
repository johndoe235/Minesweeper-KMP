package com.minesweeper.di

import org.koin.dsl.module


val androidModule = module {
    includes(commonModule)
    includes(dataModule)
    includes(viewModelModule)
}