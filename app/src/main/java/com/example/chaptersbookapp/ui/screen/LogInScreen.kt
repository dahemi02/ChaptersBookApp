package com.example.chaptersbookapp.ui.screen

import android.R.style
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chaptersbookapp.R

@Composable
fun LogInScreen (
    onLoginSuccess: () -> Unit
){
    //Track whether it's in login or user mode
    var isLogin by remember { mutableStateOf(true) }

    Surface (
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Title
            Text(
                text = stringResource(R.string.chapters),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            // Login/Register Form
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column (
                    modifier = Modifier.padding(24.dp)
                ) {
                    //Welcome Back / Create Account Text
                    Text(
                        text =
                            if (isLogin)
                                stringResource(R.string.welcome)
                            else
                                stringResource(R.string.createAccount),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                    )

                    // Email Field
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = {Text(stringResource(id = R.string.email))},
                        placeholder = {Text(stringResource(R.string.enterEmail))},
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )

                    // Password Field
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = {Text(stringResource(R.string.password))},
                        modifier = Modifier
                            .fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                    )

                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                    )

                    // Login/register Button
                    Button(
                        onClick = onLoginSuccess,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            if (isLogin)
                                stringResource(R.string.login)
                            else
                                stringResource(R.string.register)
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )

                    // Switch between Login/register
                    TextButton(
                        onClick = {isLogin = !isLogin},
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            if (isLogin)
                                stringResource(R.string.register_link)
                            else
                                stringResource(R.string.login_link)
                        )
                    }
                }
            }

        }
    }
}