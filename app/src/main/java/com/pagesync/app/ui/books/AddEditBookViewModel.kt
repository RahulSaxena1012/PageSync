package com.pagesync.app.ui.books

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pagesync.app.data.model.Book
import com.pagesync.app.data.model.ReadingStatus
import com.pagesync.app.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditBookViewModel @Inject constructor(
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: String? = savedStateHandle.get<String>("bookId")

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book.asStateFlow()

    val isEditing: Boolean = bookId != null

    init {
        bookId?.let { id ->
            viewModelScope.launch {
                _book.value = repository.getBookById(id)
            }
        }
    }

    fun saveBook(
        title: String,
        author: String,
        isbn: String,
        coverUrl: String,
        totalPages: Int,
        currentPage: Int,
        rating: Float,
        notes: String,
        status: ReadingStatus
    ) {
        viewModelScope.launch {
            val existing = _book.value
            val book = if (existing != null) {
                existing.copy(
                    title = title,
                    author = author,
                    isbn = isbn,
                    coverUrl = coverUrl,
                    totalPages = totalPages,
                    currentPage = currentPage,
                    rating = rating,
                    notes = notes,
                    status = status,
                    lastUpdated = System.currentTimeMillis()
                )
            } else {
                Book(
                    title = title,
                    author = author,
                    isbn = isbn,
                    coverUrl = coverUrl,
                    totalPages = totalPages,
                    currentPage = currentPage,
                    rating = rating,
                    notes = notes,
                    status = status
                )
            }
            if (existing != null) {
                repository.updateBook(book)
            } else {
                repository.insertBook(book)
            }
        }
    }

    fun deleteBook() {
        bookId?.let { id ->
            viewModelScope.launch {
                repository.deleteBookById(id)
            }
        }
    }
}
