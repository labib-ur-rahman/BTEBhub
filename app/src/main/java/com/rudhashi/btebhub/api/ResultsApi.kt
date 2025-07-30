package com.rudhashi.btebhub.api

import com.rudhashi.btebhub.model.GroupResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ResultsApi {
    @GET("results/group")
    fun getGroupResults(
        @Query("semester") semester: Int,
        @Query("rollComb") rollComb: String,
        @Query("exam") exam: String,
        @Query("regulation") regulation: String
    ): Call<GroupResultResponse>
}
