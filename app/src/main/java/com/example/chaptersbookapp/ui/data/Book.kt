package com.example.chaptersbookapp.ui.data

import android.icu.text.CaseMap

data class Book(
    val id: Int,
    val title: Int,
    val author: Int,
    val description: Int,
    val category: Int,
    val coverImage: Int,
    val isFavorite: Boolean = false
)
