package com.example.chaptersbookapp.ui.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


data class BookApiResponse(
    @SerializedName("books") val books: List<BookApi>
)

data class BookApi(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverImage") val coverImage: String,
)

data class AuthorApiResponse(
    @SerializedName("authors") val authors: List<AuthorApi>
)

data class AuthorApi(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("profileImage") val profileImage: String,
    @SerializedName("isPopular") val isPopular: Boolean = false,
)

interface BooksApiService {

    // Fetch books from external JSON file
    @GET
    suspend fun getBooks(@Url url: String): BookApiResponse

    // Fetch authors from external JSON file
    @GET
    suspend fun getAuthors(@Url url: String): AuthorApiResponse

    companion object{

        private const val BASE_URL = "https://gist.github.com/dahemi02/73e59374abde5e63c1d2c2b645792e00/"

        const val BOOKS_URL = "https://gist.githubusercontent.com/dahemi02/73e59374abde5e63c1d2c2b645792e00/raw/dc61bf98f9ec4ce31f4351ed11bc5c94702d3ccc/books.json"
        const val AUTHORS_URL = "https://gist.githubusercontent.com/dahemi02/73e59374abde5e63c1d2c2b645792e00/raw/dc61bf98f9ec4ce31f4351ed11bc5c94702d3ccc/authors.json"

        fun create(): BooksApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BooksApiService::class.java)
        }

    }

}
