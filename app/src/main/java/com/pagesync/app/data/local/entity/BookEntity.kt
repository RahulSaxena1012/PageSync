package com.pagesync.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pagesync.app.data.model.Book
import com.pagesync.app.data.model.ReadingStatus

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val coverUrl: String,
    val totalPages: Int,
    val currentPage: Int,
    val rating: Float,
    val notes: String,
    val status: String,
    val dateAdded: Long,
    val lastUpdated: Long,
    val userId: String,
    val isSynced: Boolean = false
) {
    fun toBook(): Book = Book(
        id = id,
        title = title,
        author = author,
        isbn = isbn,
        coverUrl = coverUrl,
        totalPages = totalPages,
        currentPage = currentPage,
        rating = rating,
        notes = notes,
        status = ReadingStatus.valueOf(status),
        dateAdded = dateAdded,
        lastUpdated = lastUpdated,
        userId = userId
    )

    companion object {
        fun fromBook(book: Book, isSynced: Boolean = false): BookEntity = BookEntity(
            id = book.id,
            title = book.title,
            author = book.author,
            isbn = book.isbn,
            coverUrl = book.coverUrl,
            totalPages = book.totalPages,
            currentPage = book.currentPage,
            rating = book.rating,
            notes = book.notes,
            status = book.status.name,
            dateAdded = book.dateAdded,
            lastUpdated = book.lastUpdated,
            userId = book.userId,
            isSynced = isSynced
        )
    }
}
