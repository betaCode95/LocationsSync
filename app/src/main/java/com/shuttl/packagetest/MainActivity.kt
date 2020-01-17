package com.shuttl.packagetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.open_lib.LocationsHelper
import com.shuttl.location_pings.config.open_lib.MockLocationHelper


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            MockLocationHelper.startMockLocationService(app = application)
        } else {
            LocationsHelper.initLocationsModule(app = application, locationConfigs = LocationConfigs(syncUrl = "https://gps.shuttlstage.com/streams/driver/record"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            MockLocationHelper.stopMockLocationService(application)
        } else {
            LocationsHelper.stop(application)
        }
    }
}