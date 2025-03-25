package com.sachin_singh_dighan.newsapp.data.model.topheadline

import com.google.gson.annotations.SerializedName
import com.sachin_singh_dighan.newsapp.data.local.entity.Source

data class ApiSource(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String = "",
)

fun ApiSource.toSourceEntity(): Source {
    return Source(
        id = this.id,
        name = this.name
    )
}
