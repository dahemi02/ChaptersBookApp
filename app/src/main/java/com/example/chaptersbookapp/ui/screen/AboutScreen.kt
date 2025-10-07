package com.example.chaptersbookapp.ui.screen

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.chaptersbookapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.chaptersbookapp.util.DeviceCapabilitiesManager

// About Screen
@Composable
fun AboutScreen() {
    var showDeviceInfo by remember { mutableStateOf(false) }

    // Detect landscape or portrait
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tab selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(
                onClick = { showDeviceInfo = false },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!showDeviceInfo)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text("About App")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { showDeviceInfo = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showDeviceInfo)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text("Device Info")
            }
        }

        // Content
        if (showDeviceInfo) {
            DeviceInfoScreen()
        } else {
            AboutAppContent()
        }
    }
}

// About App Content

@Composable
fun AboutAppContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // "About Us" Title
        item {
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Features Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.features),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureItem(
                        icon = Icons.Filled.Favorite,
                        title = stringResource(R.string.personalFavorites),
                        description = stringResource(R.string.personalFavorites_des)
                    )

                    FeatureItem(
                        icon = Icons.Filled.Person,
                        title = stringResource(R.string.authorProfiles),
                        description = stringResource(R.string.authorProfiles_des)
                    )

                    FeatureItem(
                        icon = Icons.Filled.Category,
                        title = stringResource(R.string.genreCategories),
                        description = stringResource(R.string.genreCategories_des)
                    )
                }
            }
        }

        // Contact Form
        item {
            ContactForm()
        }
    }
}

// Feature Item

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
        Icon(
            icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

// ---------------- CONTACT FORM ----------------

@Composable
fun ContactForm() {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.contactUs),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(stringResource(R.string.message)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Handle form submission
                    email = ""
                    message = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.sendMessage))
            }
        }
    }
}

// ---------------- DEVICE INFO SCREEN ----------------

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DeviceInfoScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // These would come from your own DeviceCapabilitiesManager class
    val deviceManager = remember { DeviceCapabilitiesManager(context) }

    var batteryStatus by remember { mutableStateOf("Loading...") }
    var lightLevel by remember { mutableStateOf("0.00 lux") }
    var locationData by remember { mutableStateOf("Location unavailable") }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    DisposableEffect(Unit) {
        deviceManager.lightSensorManager.start()
        onDispose {
            deviceManager.lightSensorManager.stop()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            lightLevel = deviceManager.lightSensorManager.getLightLevelString()
            batteryStatus = deviceManager.batteryManager.getBatteryStatus()

            if (locationPermissions.allPermissionsGranted) {
                scope.launch {
                    val location = deviceManager.locationManager.getCurrentLocation()
                    locationData = location?.let {
                        "Lat: %.4f, Lon: %.4f".format(it.latitude, it.longitude)
                    } ?: "Location unavailable"
                }
            }

            delay(1000)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Device Information",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Location Info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Geolocation",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (locationPermissions.allPermissionsGranted) {
                        Text(
                            text = locationData,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Button(
                            onClick = { locationPermissions.launchMultiplePermissionRequest() }
                        ) {
                            Text("Grant Location Permission")
                        }
                    }
                }
            }
        }

        // Light Sensor
        item {
            DeviceInfoCard(
                title = "Ambient Light Sensor",
                icon = Icons.Filled.LightMode,
                items = listOf("Light Level" to lightLevel)
            )
        }

        // Battery
        item {
            DeviceInfoCard(
                title = "Battery Status",
                icon = Icons.Filled.BatteryFull,
                items = listOf("Status" to batteryStatus)
            )
        }
    }
}

@Composable
fun DeviceInfoCard(
    title: String,
    icon: ImageVector,
    items: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$label:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
