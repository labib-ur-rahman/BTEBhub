package com.rudhashi.btebhub.model

data class NoticeResponse(
    val notices: List<Notice>
)

data class Notice(
    val date: String,
    val mainTitle: String,
    val link: String
)