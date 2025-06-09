package com.example.chaptersbookapp.ui.screen

import android.accessibilityservice.GestureDescription
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.chaptersbookapp.R

@Composable
fun AboutScreen(){

    //LAndscape orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    //Scrollable list
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //"About Us" Title
        item {
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        //Features
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    //Features title
                    Text(
                        text = stringResource(R.string.features),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )

                    //Personal Favorites
                    FeatureItem(
                        icon = Icons.Filled.Favorite,
                        title = stringResource(R.string.personalFavorites),
                        description = stringResource(R.string.personalFavorites_des)
                    )

                    //Author Profile
                    FeatureItem(
                        icon = Icons.Filled.Person,
                        title = stringResource(R.string.authorProfiles),
                        description = stringResource(R.string.authorProfiles_des)
                    )

                    //Genre Category
                    FeatureItem(
                        icon = Icons.Filled.Category,
                        title = stringResource(R.string.genreCategories),
                        description = stringResource(R.string.genreCategories_des)
                    )
                }
            }
        }

        //Contact Form
        item {
            ContactForm()
        }
    }
}

//Feature Item
@Composable
fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        //Feature Icon
        Icon(
            icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier
                .width(16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            //Feature Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            //Feature description
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

//Contact Form
@Composable
fun ContactForm(){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            //"Contact Us" Title
            Text(
                text = stringResource(R.string.contactUs),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            //Name Field
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = {Text(stringResource(R.string.email))},
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )

            //Message Field
            OutlinedTextField(
                value = message,
                onValueChange = {message = it},
                label = {Text(stringResource(R.string.message))},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            //Submit Button
            Button(
                onClick = {
                    //Handle form submission
                    name = ""
                    email = ""
                    message = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.sendMessage))
            }
        }
    }
}