package com.example.chaptersbookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chaptersbookapp.ui.data.BottomNavItem
import com.example.chaptersbookapp.ui.data.repository.BookRepository
import com.example.chaptersbookapp.ui.screen.AboutScreen
import com.example.chaptersbookapp.ui.screen.ApiLibraryScreen
import com.example.chaptersbookapp.ui.screen.AuthorDetailsScreen
import com.example.chaptersbookapp.ui.screen.AuthorsScreen
import com.example.chaptersbookapp.ui.screen.BookDetailsScreen
import com.example.chaptersbookapp.ui.screen.FavoritesScreen
import com.example.chaptersbookapp.ui.screen.HomeScreen
import com.example.chaptersbookapp.ui.screen.LogInScreen
import com.example.chaptersbookapp.ui.theme.ChaptersBookAppTheme
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize Firebase
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            ChaptersBookAppTheme {
                ChaptersApp()
            }
        }
    }
}


@Composable
fun ChaptersApp (){
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoggedIn by remember { mutableStateOf(false) }
    var isDataLoaded by remember { mutableStateOf(false) }

    val repository = remember { BookRepository(context) }

    // Load data when logged in
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn && !isDataLoaded) {
            android.util.Log.d("MainActivity", "Fetching data...")
            scope.launch {
                repository.fetchBooks()
                repository.fetchAuthors()
                isDataLoaded = true
                android.util.Log.d("MainActivity", "Data fetched")
            }
        }
    }

    //If not logged in, show Login Screen
    if (!isLoggedIn){
        LogInScreen (onLoginSuccess = {isLoggedIn = true})
    } else {
        if (!isDataLoaded) {
            // Show loading screen
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
                android.util.Log.d("MainActivity", "Loading data...")
            }
        } else {
            //Else show the navigation
            ChaptersNav(
                navController = rememberNavController(),
                repository = repository,
                onLogout = {
                    isLoggedIn = false
                    isDataLoaded = false
                }
            )
        }
    }

}

//Navigation
@Composable
fun ChaptersNav(
    navController: NavHostController,
    repository: BookRepository,
    onLogout: () -> Unit,
){
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Scaffold (
        bottomBar = {
            //Show bottom navigation bar
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            //List of favorite book ID's
            val favoriteBookIds = remember { mutableStateListOf<Int>() }

            //Navigation to switch between screens
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier
                    .weight(1f)
            ) {
                //Home Screen
                composable("home") {
                    HomeScreen(
                        navController = navController,
                        repository = repository,
                        onLogout = {
                            onLogout()
                        }
                    )
                }

                //Favorites Screen
                composable("favorites") { FavoritesScreen(
                    favoriteBookIds = favoriteBookIds,
                    onRemoveFavorite = {id ->
                        favoriteBookIds.remove(id)
                        // Update in database
                        kotlinx.coroutines.GlobalScope.launch {
                            repository.updateFavoriteStatus(id, false)
                        }
                    },
                    repository = repository
                )
                }

                //Authors Screen
                composable("authors") {
                    AuthorsScreen(navController, repository)
                }

                //Book Details Screen
                composable("book_detail/{bookId}") { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 1
                    BookDetailsScreen(
                        bookId = bookId,
                        favoriteBookIds = favoriteBookIds,
                        navController = navController,
                        repository = repository
                    )
                }

                //Author Details Screen
                composable("author_details/{authorId}") { backStackEntry ->
                    val authorId = backStackEntry.arguments?.getString("authorId")?.toIntOrNull() ?: 1
                    AuthorDetailsScreen(
                        authorId = authorId,
                        navController = navController,
                        repository = repository
                    )
                }

                //About Us Screen
                composable("about") { AboutScreen() }

                // Open Library API screen
                composable("api_library") {
                    ApiLibraryScreen(navController = navController)
                }
            }
        }
    }
}

//Bottom Navigation Bar
@Composable
fun BottomNavigationBar(
    navController: NavHostController
){
    //Define items in the bottom navigation bar
    val items = listOf(
        BottomNavItem("home", R.string.home, Icons.Filled.Home),
        BottomNavItem("favorites", R.string.favorites, Icons.Filled.Favorite),
        BottomNavItem("authors", R.string.authors, Icons.Filled.Person),
        BottomNavItem("about", R.string.about, Icons.Filled.Info)
    )

    //Navigation bar
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        //Get the current route
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                //Icons on the navigation bar
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.title),
                        tint =
                            if (currentRoute == item.route)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
                },
                //Labels on the navigation bar
                label = {
                    Text(
                        text = stringResource(item.title),
                        color =
                            if (currentRoute == item.route)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    //Navigate to selected route
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true //Only display one copy of the selected destination
                    }
                },
                //Colors of the navigation items
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

