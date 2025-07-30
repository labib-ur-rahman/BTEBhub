package com.rudhashi.btebhub.api

import com.rudhashi.btebhub.model.NoticeResponse
import com.rudhashi.btebhub.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("result")
    suspend fun getResults(
        @Query("rollNumber") rollNumber: String,
        @Query("technology") technology: String = "diploma in engineering"
    ): List<Result>

    @GET("api/notice?type=all")
    fun getNotices(): Call<NoticeResponse>
}