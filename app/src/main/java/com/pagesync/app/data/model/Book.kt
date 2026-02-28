package com.pagesync.app.data.model

import java.util.UUID

data class Book(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val author: String = "",
    val isbn: String = "",
    val coverUrl: String = "",
    val totalPages: Int = 0,
    val currentPage: Int = 0,
    val rating: Float = 0f,
    val notes: String = "",
    val status: ReadingStatus = ReadingStatus.WANT_TO_READ,
    val dateAdded: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis(),
    val userId: String = ""
)

enum class ReadingStatus {
    WANT_TO_READ,
    READING,
    FINISHED
}
