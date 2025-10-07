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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chaptersbookapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LogInScreen (
    onLoginSuccess: () -> Unit
){
    //Track whether it's in login or register mode
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()

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
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = ""
                        },
                        label = {Text(stringResource(id = R.string.email))},
                        placeholder = {Text(stringResource(R.string.enterEmail))},
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = !isLoading,
                        isError = errorMessage.isNotEmpty()
                    )

                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = ""
                        },
                        label = {Text(stringResource(R.string.password))},
                        modifier = Modifier
                            .fillMaxWidth(),
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        enabled = !isLoading,
                        isError = errorMessage.isNotEmpty(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Filled.Visibility
                                    else
                                        Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordVisible)
                                        "Hide Password"
                                    else
                                        "Show Password"
                                )
                            }
                        }
                    )

                    // Confirm Password Field for Registration Only
                    if (!isLogin) {
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                errorMessage = ""
                            },
                            label = {Text(stringResource(R.string.confirmPassword))},
                            placeholder = {Text(stringResource(R.string.placeholder_confirmPass))},
                            modifier = Modifier
                                .fillMaxWidth(),
                            visualTransformation = if (confirmPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            enabled = !isLoading,
                            isError = errorMessage.isNotEmpty(),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        confirmPasswordVisible = !confirmPasswordVisible
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (confirmPasswordVisible)
                                            Icons.Filled.Visibility
                                        else
                                            Icons.Filled.VisibilityOff,
                                        contentDescription = if (confirmPasswordVisible)
                                            "Hide Password"
                                        else
                                            "Show Password"
                                    )
                                }
                            }
                        )
                    }

                    //Error Message
                    if (errorMessage.isNotEmpty()) {
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                    )

                    // Login/register Button
                    Button(
                        onClick = {
                            scope.launch {

                                // Validation
                                if (email.isBlank() || password.isBlank()) {
                                    errorMessage = "Please Fill in All Fields"
                                    return@launch
                                }

                                if (password.length < 6) {
                                    errorMessage = "Password must be at least 6 characters"
                                    return@launch
                                }

                                // Validate confirm password for registration
                                if (!isLogin) {
                                    if (confirmPassword.isBlank()) {
                                        errorMessage = "Please Confirm Your Password"
                                        return@launch
                                    }
                                    if (password != confirmPassword) {
                                        errorMessage = "Passwords Do Not Match"
                                        return@launch
                                    }
                                }

                                isLoading = true
                                errorMessage = ""

                                try {
                                    if (isLogin) {

                                        //Login
                                        auth.signInWithEmailAndPassword(email, password).await()
                                        onLoginSuccess()

                                    }
                                    else {

                                        //Register
                                        auth.createUserWithEmailAndPassword(email, password).await()
                                        onLoginSuccess()

                                    }
                                }
                                catch (e: Exception) {
                                    errorMessage = e.localizedMessage ?: "Authentication Failed"
                                }
                                finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                if (isLogin)
                                    stringResource(R.string.login)
                                else
                                    stringResource(R.string.register)
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )

                    // Switch between Login/register
                    TextButton(
                        onClick = {
                            isLogin = !isLogin     //Change isLogin from 'true' to 'false' or vice versa
                            errorMessage = ""
                            confirmPassword = "" // Clear confirm password when switching
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = !isLoading
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