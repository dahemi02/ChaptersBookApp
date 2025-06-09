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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.chaptersbookapp.R
import com.example.chaptersbookapp.ui.model.Data

@Composable
fun BookDetailsScreen(
  bookId: Int,
  favoriteBookIds: MutableList<Int>,
  navController: NavHostController
){
    //Get screen orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //Find the selected book
    val book = Data.books.find { it.id == bookId } ?: return

    //Track whether the selected book is marked as favorite
    var isFavorite by remember {
        mutableStateOf (bookId in favoriteBookIds)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Back Button
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.goBack)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Landscape
        if (isLandscape) {
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
                            painter = painterResource(id = book.coverImage),
                            contentDescription = stringResource(id = book.title),
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
                    modifier = Modifier
                        .weight(1f)
                ) {
                    // Book Title
                    Text(
                        text = stringResource(book.title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    //Book Author
                    Text(
                        text = stringResource(R.string.by),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = stringResource(book.author),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Add to Favorites Button
                    Button(
                        onClick = {
                            if (isFavorite) {
                                favoriteBookIds.remove(bookId)
                            } else {
                                favoriteBookIds.add(bookId)
                            }
                            isFavorite = !isFavorite
                        }
                    ) {
                        //Favorite icon
                        Icon(
                            if (isFavorite)
                                Icons.Filled.Favorite
                            else
                                Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.addToFavorites)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        //Favorite label
                        Text(
                            if (isFavorite)
                                stringResource(R.string.removeFromFavorites)
                            else
                                stringResource(R.string.addToFavorites)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Book Description
                    Text(
                        text = stringResource(book.description),
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }
            }
        } else {
            //Portrait
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                //Book cover
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
                            painter = painterResource(id = book.coverImage),
                            contentDescription = stringResource(id = book.title),
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
                    //Book title
                    Text(
                        text = stringResource(book.title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    //Author
                    Text(
                        text = stringResource(R.string.by),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = stringResource(book.author),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Add to Favorites Button
                    Button(
                        onClick = {
                            if (isFavorite) {
                                favoriteBookIds.remove(bookId)
                            } else {
                                favoriteBookIds.add(bookId)
                            }
                            isFavorite = !isFavorite
                        }
                    ) {
                        //Favorites icon
                        Icon(
                            if (isFavorite)
                                Icons.Filled.Favorite
                            else
                                Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.addToFavorites)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        //Favorites label
                        Text(
                            if (isFavorite)
                                stringResource(R.string.removeFromFavorites)
                            else
                                stringResource(R.string.addToFavorites)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Book description
            Text(
                text = stringResource(book.description),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }
    }

}