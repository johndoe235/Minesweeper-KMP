package com.minesweeper.network.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("joke/Any")
    fun getWorkAppropriateJokes(
        @Query("type") type: String = "twopart",
        @Query("amount") amount: Int = 6,
        @Query("blacklistFlags") blacklistFlags: String = "nsfw,religious,political,racist,sexist,explicit"
    ):List<JokeResponse>
}



data class JokeResponse(val joke:String)