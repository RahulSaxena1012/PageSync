package com.pagesync.app.data.local

import androidx.room.*
import com.pagesync.app.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books WHERE userId = :userId ORDER BY lastUpdated DESC")
    fun getBooksByUser(userId: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Query("SELECT * FROM books WHERE userId = :userId AND status = :status ORDER BY lastUpdated DESC")
    fun getBooksByStatus(userId: String, status: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE isSynced = 0")
    suspend fun getUnsyncedBooks(): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: String)

    @Query("UPDATE books SET isSynced = 1 WHERE id = :bookId")
    suspend fun markAsSynced(bookId: String)
}
