package com.sachin_singh_dighan.newsapp.data.model.newsources

import com.google.gson.annotations.SerializedName

data class NewSourceResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("sources")
    val sources: List<Sources> = ArrayList(),
)
