package com.example.chaptersbookapp.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.chaptersbookapp.R
import com.example.chaptersbookapp.ui.data.Book
import com.example.chaptersbookapp.ui.data.local.BookEntity
import com.example.chaptersbookapp.ui.data.repository.BookRepository
import com.example.chaptersbookapp.ui.model.Data
import kotlinx.coroutines.launch

@Composable
fun BookDetailsScreen(
  bookId: Int,
  favoriteBookIds: MutableList<Int>,
  navController: NavHostController,
  repository: BookRepository
){
    //Get screen orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scope = rememberCoroutineScope()

    val allBooks by repository.getAllBooks().collectAsState(initial = emptyList())
    //Find the selected book
    val book = allBooks.find { it.id == bookId }

    //Track whether the selected book is marked as favorite
    var isFavorite by remember { mutableStateOf (book?.isFavorite ?: false) }

    if (book == null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Back Button
        IconButton(
            onClick = { navController.popBackStack() }  //Navigate back to previous screen
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.goBack)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Landscape
        if (isLandscape) {
            LandscapeLayout(
                book = book,
                isFavorite = isFavorite,
                onFavoriteClick = {
                    isFavorite = !isFavorite
                    if (isFavorite) {
                        favoriteBookIds.add(bookId)
                    } else {
                        favoriteBookIds.remove(bookId)
                    }
                    scope.launch {
                        repository.updateFavoriteStatus(bookId, isFavorite)
                    }
                }
            )
        }
        else {
            // Portrait
            PortraitLayout(
                book = book,
                isFavorite = isFavorite,
                onFavoriteClick = {
                    isFavorite = !isFavorite
                    if (isFavorite) {
                        favoriteBookIds.add(bookId)
                    } else {
                        favoriteBookIds.remove(bookId)
                    }
                    scope.launch {
                        repository.updateFavoriteStatus(bookId, isFavorite)
                    }
                }
            )
        }
    }

}

@Composable
fun LandscapeLayout(
    book: com.example.chaptersbookapp.ui.data.local.BookEntity,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Book Cover
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(200.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(book.coverImageUrl),
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Book Info and Description
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "by ${book.author}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Chip(text = book.category)

            Spacer(modifier = Modifier.height(16.dp))

            // Favorite Button
            Button(onClick = onFavoriteClick) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Add to Favorites"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isFavorite) "Remove from Favorites" else "Add to Favorites"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }
    }

}

@Composable
fun PortraitLayout(
    book: com.example.chaptersbookapp.ui.data.local.BookEntity,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Book Cover
        Card(
            modifier = Modifier
                .width(120.dp)
                .height(160.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(book.coverImageUrl),
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Book Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "by ${book.author}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Chip(text = book.category)

            Spacer(modifier = Modifier.height(16.dp))

            // Favorite Button
            Button(
                onClick = onFavoriteClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Add to Favorites"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isFavorite) "Remove" else "Add to Favorites"
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Description
    Text(
        text = book.description,
        style = MaterialTheme.typography.bodyLarge,
        lineHeight = 24.sp
    )
}

@Composable
fun Chip(text: String) {
    Surface (
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}