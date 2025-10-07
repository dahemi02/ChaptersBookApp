package com.example.chaptersbookapp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

// ============================================
// 1. GEOLOCATION MANAGER
// ============================================
class LocationManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    /**
     * Get current device location
     * Requires ACCESS_FINE_LOCATION permission
     */
    suspend fun getCurrentLocation(): Location? {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get location as formatted string
     */
    fun getLocationString(): String {
        val loc = _location.value ?: return "Location unavailable"
        return "Lat: %.4f, Lon: %.4f".format(loc.latitude, loc.longitude)
    }
}

// ============================================
// 2. AMBIENT LIGHT SENSOR MANAGER
// ============================================
class LightSensorManager(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    private val _lightLevel = MutableStateFlow(0f)
    val lightLevel: StateFlow<Float> = _lightLevel

    /**
     * Start listening to light sensor
     */
    fun start() {
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    /**
     * Stop listening to light sensor
     */
    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                _lightLevel.value = it.values[0]
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for light sensor
    }

    /**
     * Get light level as formatted string
     */
    fun getLightLevelString(): String {
        return "%.2f lux".format(_lightLevel.value)
    }
}

// ============================================
// 3. BATTERY STATUS MANAGER
// ============================================
class BatteryManager(private val context: Context) {

    /**
     * Get battery level percentage (0-100)
     */
    fun getBatteryLevel(): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        return batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    /**
     * Check if device is charging
     */
    fun isCharging(): Boolean {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        val status = batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_STATUS)
        return status == android.os.BatteryManager.BATTERY_STATUS_CHARGING ||
                status == android.os.BatteryManager.BATTERY_STATUS_FULL
    }

    /**
     * Get battery status as formatted string
     */
    fun getBatteryStatus(): String {
        val level = getBatteryLevel()
        val charging = if (isCharging()) "Charging" else "Not Charging"
        return "$level% - $charging"
    }
}

// ============================================
// COMBINED DEVICE CAPABILITIES MANAGER
// ============================================
/**
 * Main manager that provides access to all device capabilities
 * Usage:
 *   val deviceManager = DeviceCapabilitiesManager(context)
 *   deviceManager.lightSensorManager.start()
 *   val battery = deviceManager.batteryManager.getBatteryStatus()
 */
class DeviceCapabilitiesManager(private val context: Context) {
    val locationManager = LocationManager(context)
    val lightSensorManager = LightSensorManager(context)
    val batteryManager = BatteryManager(context)
}

