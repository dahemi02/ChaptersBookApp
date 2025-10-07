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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.chaptersbookapp.R
import com.example.chaptersbookapp.ui.data.Book
import com.example.chaptersbookapp.ui.data.local.BookEntity
import com.example.chaptersbookapp.ui.data.repository.BookRepository
import com.example.chaptersbookapp.ui.model.Data
import com.example.chaptersbookapp.ui.model.Data.books
import com.example.chaptersbookapp.util.NetworkUtils
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController,
    repository: BookRepository,
    onLogout: () -> Unit
) {

    //Landscape orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val context = LocalContext.current

    val books by repository.getAllBooks().collectAsState(initial = emptyList())
    val isOnline = NetworkUtils.isNetworkAvailable(context)
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(books) {
        android.util.Log.d("HomeScreen", "Books count: ${books.size}")
        books.forEach { book ->
            android.util.Log.d("HomeScreen", "Book: ${book.title}")
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {

        AlertDialog(
            onDismissRequest = {showLogoutDialog = false},
            title = {Text(stringResource(R.string.logout))},
            text = {Text(stringResource(R.string.logout_confirm))},
            confirmButton = {
                TextButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text(
                        stringResource(R.string.logout),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {showLogoutDialog = false}
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )

    }

    //Scrollable list
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Network Service Indicator
        item {
            NetworkStatusBanner(isOnline)
        }

        // Search Bar with Logout Button
        item {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Bar
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    SearchBar()
                }

                // Logout Button
                IconButton(
                    onClick = { showLogoutDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(R.string.logout),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // API Library Access Card
        item {
            ApiLibraryAccessCard(
                navController = navController,
                isConnected = isOnline
            )
        }

        // Recommended Books
        item {
            SectionHeader(stringResource(R.string.recommended))
        }

        // Romance Books
        item {
            CategorySection(
                title = R.string.romance,
                books = books.filter { it.category == "Romance"},
                navController = navController,
                isLandscape = isLandscape
            )
        }

        //Mystery Books
        item {
            CategorySection(
                title = R.string.mystery,
                books = books.filter { it.category == "Mystery"},
                navController = navController,
                isLandscape = isLandscape
            )
        }

        //Science Fiction Books
        item {
            CategorySection(
                title = R.string.scienceFiction,
                books = books.filter { it.category == "Science Fiction" },
                navController = navController,
                isLandscape = isLandscape
            )
        }

        //Fantasy Books
        item {
            CategorySection(
                title = R.string.fantasy,
                books = books.filter { it.category == "Fantasy" },
                navController = navController,
                isLandscape = isLandscape
            )
        }
    }
}

@Composable
fun ApiLibraryAccessCard(
    navController: NavHostController,
    isConnected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("api_library") },
        colors = CardDefaults.cardColors(
            containerColor = if (isConnected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isConnected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isConnected) Icons.Filled.Cloud else Icons.Filled.CloudOff,
                        contentDescription = "API Library",
                        modifier = Modifier.size(32.dp),
                        tint = if (isConnected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Discover Online Library",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isConnected)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                    if (isConnected) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "LIVE",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                Text(
                    text = if (isConnected)
                        "Browse millions of books from Open Library API"
                    else
                        "Connect to internet to access online library",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isConnected)
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                if (isConnected) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Real-time search • Book covers • Ratings",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Arrow
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open",
                modifier = Modifier.size(32.dp),
                tint = if (isConnected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun NetworkStatusBanner(isOnline: Boolean) {
    if (!isOnline) {
        Card (
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = stringResource(R.string.network_status),
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium
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
    books: List<BookEntity>,
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
                    onClick = {navController.navigate("book_detail/${book.id}")},  //Navigate to Book Details Screen
                    isLandscape = isLandscape
                )
            }
        }
    }
}

@Composable
fun BookCard(
    book: BookEntity,
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

        //Animation behaviour
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,  //Bounce
            stiffness = Spring.StiffnessLow  //Soft spring
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
                    painter = rememberAsyncImagePainter(book.coverImageUrl),
                    contentDescription = book.title,
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
            text = book.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        //Author
        Text(
            text = book.author,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}