package com.pagesync.app.ui.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pagesync.app.data.model.Book
import com.pagesync.app.data.model.ReadingStatus
import com.pagesync.app.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    val books: StateFlow<List<Book>> = repository.getBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedStatus = MutableStateFlow<ReadingStatus?>(null)
    val selectedStatus: StateFlow<ReadingStatus?> = _selectedStatus.asStateFlow()

    val displayBooks: StateFlow<List<Book>> = combine(books, _selectedStatus) { list, status ->
        if (status == null) list else list.filter { it.status == status }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun filterByStatus(status: ReadingStatus?) {
        _selectedStatus.value = status
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }
}
