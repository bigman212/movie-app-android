package ru.redmadrobot.core.network.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WithPages<E>(
    @field:Json(name = "page") val page: Int,
    @field:Json(name = "total_results") val totalResults: Int,
    @field:Json(name = "total_pages") val totalPages: Int,
    @field:Json(name = "results") val results: List<E>
) {
    companion object {
        fun <E> createFromResults(totalPages: Int, results: List<E>): WithPages<E> {
            return WithPages(
                page = 1,
                totalResults = results.size,
                totalPages = totalPages,
                results = results
            )
        }
    }
}
