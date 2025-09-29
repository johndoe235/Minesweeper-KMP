package com.minesweeper.data

import com.minesweeper.network.retrofit.ApiService

interface QuotesRepository {
   suspend fun getMineSweeperQuote(): List<String>
   suspend  fun getWorkAppropriateJoke(): List<String>
}


class QuotesRepositoryImpl(apiService: ApiService): QuotesRepository {


    override suspend fun getMineSweeperQuote(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkAppropriateJoke(): List<String> {
        TODO("Not yet implemented")
    }


}