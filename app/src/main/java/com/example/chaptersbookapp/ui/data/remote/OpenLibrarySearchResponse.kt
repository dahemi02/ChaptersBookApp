package com.example.chaptersbookapp.ui.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// Response models for Open Library API
data class OpenLibrarySearchResponse(
    @SerializedName("docs") val docs: List<OpenLibraryBook>,
    @SerializedName("numFound") val numFound: Int,
    @SerializedName("start") val start: Int
)

data class OpenLibraryBook(
    @SerializedName("key") val key: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("isbn") val isbn: List<String>?,
    @SerializedName("cover_i") val coverId: Long?,
    @SerializedName("publisher") val publisher: List<String>?,
    @SerializedName("language") val language: List<String>?,
    @SerializedName("number_of_pages_median") val numberOfPages: Int?,
    @SerializedName("subject") val subjects: List<String>?,
    @SerializedName("ratings_average") val ratingsAverage: Double?
)

// Trending Books Response
data class OpenLibraryTrendingResponse(
    @SerializedName("works") val works: List<OpenLibraryWork>
)

data class OpenLibraryWork(
    @SerializedName("key") val key: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("cover_id") val coverId: Long?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?
)


interface OpenLibraryApiService {

    //Search books by query
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,isbn,cover_i,publisher,language,number_of_pages_median,subject,ratings_average"
    ): OpenLibrarySearchResponse

    //Get books by subject
    @GET("subjects/fiction.json")
    suspend fun getTrendingBooks(
        @Query("limit") limit: Int = 20
    ): OpenLibraryTrendingResponse

    //Search by author
    @GET("search.json")
    suspend fun searchByAuthor(
        @Query("author") author: String,
        @Query("limit") limit: Int = 20
    ): OpenLibrarySearchResponse

    //Search by ISBN
    @GET("search.json")
    suspend fun searchByISBN(
        @Query("isbn") isbn: String
    ): OpenLibrarySearchResponse

    companion object {
        private const val BASE_URL = "https://openlibrary.org/"

        //Create Retrofit instance
        fun create(): OpenLibraryApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenLibraryApiService::class.java)
        }

        //Get cover image URL
        fun getCoverUrl(coverId: Long?, size: String = "M"): String? {
            return if (coverId != null) {
                "https://covers.openlibrary.org/b/id/$coverId-$size.jpg"
            } else null
        }

        //Get cover URL by ISBN
        fun getCoverUrlByISBN(isbn: String, size: String = "M"): String {
            return "https://covers.openlibrary.org/b/isbn/$isbn-$size.jpg"
        }
    }
}