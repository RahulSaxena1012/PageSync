package com.pagesync.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pagesync.app.ui.books.AddEditBookScreen
import com.pagesync.app.ui.books.BookListScreen

sealed class Screen(val route: String) {
    data object List : Screen("books")
    data object Add : Screen("books/add")
    data object Edit : Screen("books/{bookId}") {
        fun createRoute(bookId: String) = "books/$bookId"
    }
}

@Composable
fun PageSyncNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            BookListScreen(
                onAddBook = { navController.navigate(Screen.Add.route) },
                onBookClick = { navController.navigate(Screen.Edit.createRoute(it.id)) }
            )
        }
        composable(Screen.Add.route) {
            AddEditBookScreen(
                onNavigateBack = { navController.popBackStack() },
                bookId = null
            )
        }
        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            AddEditBookScreen(
                onNavigateBack = { navController.popBackStack() },
                bookId = bookId
            )
        }
    }
}
