package com.shuttl.packagetest

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.shuttl.location_pings.callbacks.LocationPingServiceCallback
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.open_lib.LocationsHelper
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.service.LocationPingService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.name
    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val callback = object : LocationPingServiceCallback<GPSLocation> {
        override fun afterSyncLocations(locations: List<GPSLocation>?) {
            Log.i(TAG, "afterSyncLocations, number of locations synced: " + locations?.size)
        }

        override fun errorWhileSyncLocations(error: Exception?) {
            Log.i(TAG, "errorWhileSyncLocations" + error?.toString())
        }

        override fun serviceStarted() {
            Log.i(TAG, "serviceStarted")
        }

        override fun serviceStopped() {
            Log.i(TAG, "serviceStopped")
        }

        override fun serviceStoppedManually() {
            Log.i(TAG, "serviceStoppedManually")
            LocationsHelper.stopAndClearAll(application)
        }

        override fun beforeSyncLocations(locations: List<GPSLocation>?): List<GPSLocation> {
            return locations?: emptyList()
        }
    }

    fun getAllLocations() {
        GlobalScope.launch {
        Log.i("AllLocations", LocationsHelper.getAllLocations(application).await()?.toString()?: "None")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestLocationPermission()

        val intent = Intent(this, LocationPingService::class.java)
        intent.action = "STOP"

        LocationsHelper.initLocationsModule(
            app = application,
            locationConfigs = LocationConfigs(syncUrl = "http://10.191.6.177:3000/record", minSyncInterval = 5000, minDistanceInterval = 10, minTimeInterval = 1000), callback = callback, intent = intent)

    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            locationPermissions,
            1
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationsHelper.stopAndClearAll(application)
    }
}