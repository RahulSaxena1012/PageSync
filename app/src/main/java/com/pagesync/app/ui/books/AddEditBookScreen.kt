package com.pagesync.app.ui.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pagesync.app.data.model.ReadingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookScreen(
    onNavigateBack: () -> Unit,
    bookId: String?,
    viewModel: AddEditBookViewModel = hiltViewModel()
) {
    val book by viewModel.book.collectAsState()
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var coverUrl by remember { mutableStateOf("") }
    var totalPages by remember { mutableIntStateOf(0) }
    var currentPage by remember { mutableIntStateOf(0) }
    var rating by remember { mutableFloatStateOf(0f) }
    var notes by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ReadingStatus.WANT_TO_READ) }

    LaunchedEffect(book) {
        book?.let { b ->
            title = b.title
            author = b.author
            isbn = b.isbn
            coverUrl = b.coverUrl
            totalPages = b.totalPages
            currentPage = b.currentPage
            rating = b.rating
            notes = b.notes
            status = b.status
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (bookId != null) "Edit Book" else "Add Book") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = isbn,
                onValueChange = { isbn = it },
                label = { Text("ISBN") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = coverUrl,
                onValueChange = { coverUrl = it },
                label = { Text("Cover URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = totalPages.toString().takeIf { it != "0" } ?: "",
                onValueChange = { totalPages = it.toIntOrNull() ?: 0 },
                label = { Text("Total Pages") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = currentPage.toString().takeIf { it != "0" } ?: "",
                onValueChange = { currentPage = it.toIntOrNull() ?: 0 },
                label = { Text("Current Page") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Rating: %.1f".format(rating), style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = rating,
                    onValueChange = { rating = it },
                    valueRange = 0f..5f,
                    steps = 9
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Status", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ReadingStatus.entries.forEach { s ->
                        FilterChip(
                            selected = status == s,
                            onClick = { status = s },
                            label = { Text(s.name.replace("_", " ")) }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )
            Button(
                onClick = {
                    viewModel.saveBook(
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
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Text(if (bookId != null) "Save" else "Add Book")
            }
            if (bookId != null) {
                Button(
                    onClick = {
                        viewModel.deleteBook()
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
