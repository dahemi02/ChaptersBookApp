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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chaptersbookapp.R
import com.example.chaptersbookapp.ui.data.Book
import com.example.chaptersbookapp.ui.model.Data

@Composable
fun FavoritesScreen(
    //List of IDs of favorite books
    favoriteBookIds: List<Int>,
    //Remove favorite
    onRemoveFavorite: (Int) -> Unit
){
    //Landscape orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //Get only the favorite books
    val favoriteBooks = Data.books.filter { it.id in favoriteBookIds }

    //Scrollable list
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //'Favorites' Title
        item {
            Text(
                text = stringResource(R.string.favorites),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        //If there are no favorite books
        if (favoriteBooks.isEmpty()){
            item {
                Card (
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Favorite icon
                        Icon(
                            Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.noFavorites),
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        Spacer(
                            modifier = Modifier
                                .height(16.dp)
                        )

                        //Message
                        Text(
                            text = stringResource(R.string.noFavoritesYet),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(R.string.markedFavorites),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            //List of favorite books
            items(favoriteBooks) { book ->
                FavoriteBookItem(book = book) {
                    onRemoveFavorite(book.id)
                }
            }
        }
    }
}

@Composable
fun FavoriteBookItem(
    book: Book,
    onRemove: () -> Unit
) {
    Card (
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row (
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Book Cover
            Card(
                modifier = Modifier
                    .size(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = book.coverImage),
                        contentDescription = stringResource(id = book.title),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )

            //Book Info
            Column (
                modifier = Modifier
                    .weight(1f)
            ) {
                //Book title
                Text(
                    text = stringResource(book.title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                //Author
                Text(
                    text = stringResource(R.string.by),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(book.author),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            //Remove Button
            IconButton(
                onClick = onRemove
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = stringResource(R.string.removeFromFavorites),
                    tint = Color.Red
                )
            }
        }
    }
}