package com.example.chaptersbookapp.ui.data.repository

import android.content.Context
import com.example.chaptersbookapp.ui.data.local.AppDatabase
import com.example.chaptersbookapp.ui.data.local.AuthorEntity
import com.example.chaptersbookapp.ui.data.local.BookEntity
import com.example.chaptersbookapp.ui.data.remote.AuthorApiResponse
import com.example.chaptersbookapp.ui.data.remote.BookApiResponse
import com.example.chaptersbookapp.ui.data.remote.BooksApiService
import com.example.chaptersbookapp.util.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import kotlin.jvm.java

class BookRepository (private val context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val bookDao = database.bookDao()
    private val authorDao = database.authorDao()
    private val apiService = BooksApiService.create()

    // Fetch Books (Online/Offline)
    suspend fun fetchBooks(): Result<Unit> = withContext(Dispatchers.IO) {

        android.util.Log.d("BookRepository", " Starting fetchBooks ")

        try {

            if (NetworkUtils.isNetworkAvailable(context)) {
                android.util.Log.d("BookRepository", "Network available, trying API")

                // Fetch from API online
                try {
                    android.util.Log.d("BookRepository", "Calling API: ${BooksApiService.BOOKS_URL}")
                    val response = apiService.getBooks(BooksApiService.BOOKS_URL)
                    android.util.Log.d("BookRepository", "API Success! ${response.books.size} books")

                    val bookEntities = response.books.map { book ->
                        BookEntity(
                            id = book.id,
                            title = book.title,
                            author = book.author,
                            category = book.category,
                            description = book.description,
                            coverImageUrl = book.coverImage
                        )
                    }

                    android.util.Log.d("BookRepository", "Saving ${bookEntities.size} books to database")

                    // Write data to local data source (Room Database)
                    bookDao.insertBooks(bookEntities)
                    android.util.Log.d("BookRepository", "Books saved successfully")
                    Result.success(Unit)

                } catch (e: Exception) {

                    // Fallback to local JSON in the case of API failure
                    android.util.Log.e("BookRepository", "API failed: ${e.message}")
                    e.printStackTrace()
                    loadBooksFromLocalJson()
                    Result.success(Unit)

                }

            }
            else {

                // Read data from local data source (offline)
                android.util.Log.d("BookRepository", "Offline, loading from assets")
                loadBooksFromLocalJson()
                Result.success(Unit)

            }

        } catch (e: Exception) {

            // Final fallback to local JSON
            android.util.Log.e("BookRepository", "Error: ${e.message}")
            e.printStackTrace()
            loadBooksFromLocalJson()
            Result.failure(e)

        }

    }

    // Read data from local data source (assets/books.json)
    private suspend fun loadBooksFromLocalJson() {
        try {
            // Open the JSON file from assets folder
            val inputStream = context.assets.open("books.json")
            val reader = InputStreamReader(inputStream)

            // Parse JSON using Gson
            val response = Gson().fromJson(reader, BookApiResponse::class.java)

            // Convert to Room entities
            val bookEntities = response.books.map { book ->
                BookEntity(
                    id = book.id,
                    title = book.title,
                    author = book.author,
                    category = book.category,
                    description = book.description,
                    coverImageUrl = book.coverImage
                )
            }

            // Write data to local data source (Room Database)
            bookDao.insertBooks(bookEntities)
            android.util.Log.d("BookRepository", "Loaded ${bookEntities.size} books from local JSON")

            // Close the reader
            reader.close()

        } catch (e: Exception) {
            android.util.Log.e("BookRepository", "Error loading local books: ${e.message}")
            e.printStackTrace()
        }
    }

    // Fetch Authors (Online/Offline)
    suspend fun fetchAuthors(): Result<Unit> = withContext(Dispatchers.IO) {

        try {

            if (NetworkUtils.isNetworkAvailable(context)) {

                // ONLINE: Fetch from API
                try {

                    val response = apiService.getAuthors(BooksApiService.AUTHORS_URL)

                    val authorEntities = response.authors.map { author ->
                        AuthorEntity(
                            id = author.id,
                            name = author.name,
                            description = author.description,
                            profileImageUrl = author.profileImage,
                            isPopular = author.isPopular
                        )
                    }

                    // Write data to local data source
                    authorDao.insertAuthors(authorEntities)
                    Result.success(Unit)

                } catch (e: Exception) {

                    // API failed, fallback to local JSON
                    loadAuthorsFromLocalJson()
                    Result.success(Unit)

                }
            }
            else {

                // Read data from local data source
                loadAuthorsFromLocalJson()
                Result.success(Unit)

            }
        } catch (e: Exception) {

            // Final fallback to local JSON
            loadAuthorsFromLocalJson()
            Result.failure(e)

        }
    }

    // Read data from local data source (assets/authors.json)
    private suspend fun loadAuthorsFromLocalJson() {
        try {
            // Open the JSON file from assets folder
            val inputStream = context.assets.open("authors.json")
            val reader = InputStreamReader(inputStream)

            // Parse JSON using Gson
            val response = Gson().fromJson(reader, AuthorApiResponse::class.java)

            // Convert API models to Room entities
            val authorEntities = response.authors.map { author ->
                AuthorEntity(
                    id = author.id,
                    name = author.name,
                    description = author.description,
                    profileImageUrl = author.profileImage,
                    isPopular = author.isPopular
                )
            }

            // Write data to local data source
            authorDao.insertAuthors(authorEntities)

            // Close the reader
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Read data from local data source (Room Database Operations)

    // Get all books (for HomeScreen)
    fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()

    // Get favorite books (for FavoritesScreen)
    fun getFavoriteBooks(): Flow<List<BookEntity>> = bookDao.getFavoriteBooks()

    // Get book by ID from Room database
    suspend fun getBookById(bookId: Int): BookEntity? = bookDao.getBookById(bookId)

    // Update favorite status
    suspend fun updateFavoriteStatus(bookId: Int, isFavorite: Boolean) {
        bookDao.updateFavoriteStatus(bookId, isFavorite)
    }

    // Get all authors (for AuthorsScreen)
    fun getAllAuthors(): Flow<List<AuthorEntity>> = authorDao.getAllAuthors()

    // Get author by ID (for AuthorDetailsScreen)
    suspend fun getAuthorById(authorId: Int) = authorDao.getAuthorById(authorId)

}