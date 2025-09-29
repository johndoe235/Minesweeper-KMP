package com.minesweeper.di

import com.minesweeper.data.QuotesRepository
import com.minesweeper.data.QuotesRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule)
    single { ::QuotesRepositoryImpl as QuotesRepository }
}