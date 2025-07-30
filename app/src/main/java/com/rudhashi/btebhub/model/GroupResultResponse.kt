package com.rudhashi.btebhub.model

data class GroupResultResponse(
    val semester: String,
    val exam: String,
    val regulation: Int,
    val query_rolls: String,
    val results: List<ResultItem>
)