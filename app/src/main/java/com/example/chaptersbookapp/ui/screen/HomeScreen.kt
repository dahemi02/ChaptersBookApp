package com.example.chaptersbookapp.ui.screen

import android.icu.text.SymbolTable
import android.widget.Space
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.chaptersbookapp.R
import com.example.chaptersbookapp.ui.data.Book
import com.example.chaptersbookapp.ui.model.Data
import com.example.chaptersbookapp.ui.model.Data.books

@Composable
fun HomeScreen(navController: NavHostController) {

    //Landscape orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    //Scrollable list
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search Bar
        item {
            SearchBar()
        }

        // Recommended Books
        item {
            SectionHeader(stringResource(R.string.recommended))
        }

        // Romance Books
        item {
            CategorySection(
                title = R.string.romance,
                books = Data.books.filter { it.category == R.string.romance },
                navController = navController,
                isLandscape = isLandscape
            )
        }

        //Mystery Books
        item {
            CategorySection(
                title = R.string.mystery,
                books = Data.books.filter { it.category == R.string.mystery},
                navController = navController,
                isLandscape = isLandscape
            )
        }

        //Science Fiction Books
        item {
            CategorySection(
                title = R.string.scienceFiction,
                books = Data.books.filter { it.category == R.string.scienceFiction },
                navController = navController,
                isLandscape = isLandscape
            )
        }

        //Fantasy Books
        item {
            CategorySection(
                title = R.string.fantasy,
                books = Data.books.filter { it.category == R.string.fantasy },
                navController = navController,
                isLandscape = isLandscape
            )
        }
    }
}


//Search Bar
@Composable
fun SearchBar(){
    var searchText by remember { mutableStateOf("") }

    //Text input
    OutlinedTextField(
        value = searchText,
        onValueChange = {searchText = it},
        placeholder = {Text(stringResource(R.string.searchBooks))},
        leadingIcon = {
            //Search icon
            Icon(
                Icons.Filled.Search,
                contentDescription = stringResource(R.string.search)
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun SectionHeader(title: String){
    Card (
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        //Section header
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Composable
fun CategorySection(
    title: Int,
    books: List<Book>,
    navController: NavHostController,
    isLandscape: Boolean
){
    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Category title
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            //Arrow icon
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = (stringResource(R.string.seeMore))
            )
        }

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        //Horizontal scrollable list
        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(books) { book ->
                BookCard(
                    book = book,
                    onClick = {navController.navigate("book_detail/${book.id}")},
                    isLandscape = isLandscape
                )
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit,
    isLandscape: Boolean = false
){
    val cardWidth = when{
        isLandscape -> 140.dp
        else -> 120.dp
    }

    var isPressed by remember { mutableStateOf(false) }

    //Scale animation
    val scale by animateFloatAsState(
        targetValue =
            if (isPressed)
                0.95f
            else
                1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card (
        modifier = Modifier
            .width(cardWidth)
            .scale(scale)
            .clickable{
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        //Book Cover
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    if (isLandscape)
                        160.dp
                    else
                        140.dp
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = book.coverImage),
                    contentDescription = stringResource(id = book.title),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            if (isLandscape)
                                160.dp
                            else
                                140.dp
                        )
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        //Book Title
        Text(
            text = stringResource(book.title),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        //Author
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = stringResource(book.author),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}