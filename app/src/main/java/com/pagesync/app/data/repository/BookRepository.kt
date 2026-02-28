package com.pagesync.app.data.repository

import com.pagesync.app.data.local.BookDao
import com.pagesync.app.data.local.entity.BookEntity
import com.pagesync.app.data.model.Book
import com.pagesync.app.data.model.ReadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    private val userId: String = "local_user"

    fun getBooks(userId: String = this.userId): Flow<List<Book>> =
        bookDao.getBooksByUser(userId).map { entities -> entities.map { it.toBook() } }

    fun getBooksByStatus(status: ReadingStatus, userId: String = this.userId): Flow<List<Book>> =
        bookDao.getBooksByStatus(userId, status.name).map { entities -> entities.map { it.toBook() } }

    suspend fun getBookById(bookId: String): Book? =
        bookDao.getBookById(bookId)?.toBook()

    suspend fun insertBook(book: Book) {
        val bookWithUserId = if (book.userId.isBlank()) book.copy(userId = userId) else book
        bookDao.insertBook(BookEntity.fromBook(bookWithUserId))
    }

    suspend fun updateBook(book: Book) {
        bookDao.updateBook(BookEntity.fromBook(book))
    }

    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(BookEntity.fromBook(book))
    }

    suspend fun deleteBookById(bookId: String) {
        bookDao.deleteBookById(bookId)
    }
}
