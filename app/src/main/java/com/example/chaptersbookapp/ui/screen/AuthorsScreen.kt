package com.example.chaptersbookapp.ui.screen

import android.R.attr.author
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.chaptersbookapp.R
import com.example.chaptersbookapp.ui.data.Author
import com.example.chaptersbookapp.ui.model.Data

@Composable
fun AuthorsScreen(
    navController: NavHostController
){

    //Landscape orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    //Scrollable list
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //'Author' Title
        item {
            Text(
                text = stringResource(R.string.authors),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        //Search Bar
        item {
            AuthorSearchBar()
        }

        //Popular Authors Section
        item {
            SectionHeader(stringResource(R.string.popularAuthors))
        }

        items (Data.authors.filter { it.isPopular }) { author ->
            AuthorCard(
                author = author,
                onClick = {navController.navigate("author_detail/${author.id}")},
                isLandscape = isLandscape
            )
        }

        //All Authors Section
        item {
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Text(
                text = stringResource(R.string.allAuthors),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        //Author Card
        items (Data.authors) { author ->
            AuthorCard(
                author = author,
                onClick = {navController.navigate("author_details/${author.id}")},
                isLandscape = isLandscape
            )
        }
    }
}


//Search bar
@Composable
fun AuthorSearchBar(){
    var searchText by remember { mutableStateOf("") }

    //Text input
    OutlinedTextField(
        value = searchText,
        onValueChange = {searchText = it},
        placeholder = {Text(stringResource(R.string.searchAuthors))},
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
fun AuthorCard(
    author: Author,
    onClick: () -> Unit,
    isLandscape: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }

    //Animation scale effect
    val scale by animateFloatAsState(
        targetValue =
            if (isPressed)
                0.98f
            else
                1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Author Profile Picture
            Card (
                modifier = Modifier
                    .size(60.dp),
                shape = CircleShape,
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
                        painter = painterResource(id = author.profileImage),
                        contentDescription = stringResource(id = author.name),
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
                    .width(16.dp)
            )

            //Author Info
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                //Author name
                Text(
                    text = stringResource(id = author.name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                )

                //Author description
                Text(
                    text = stringResource(id = author.description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines =
                        if (isLandscape)
                            1
                        else
                            2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            //Arrow Icon
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.view_AuthorDetails),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}