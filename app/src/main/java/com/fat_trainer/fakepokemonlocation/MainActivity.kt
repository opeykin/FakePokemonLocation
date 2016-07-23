package com.fat_trainer.fakepokemonlocation


import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.location.LocationProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        addFakeGpsProvider(locationManager)
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, getNewYorkLocation())
        startService(Intent(this, LocationListenerService::class.java))
    }

    private fun addFakeGpsProvider(locationManager: LocationManager) {
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                false, false, false, false, false, false, false,
                Criteria.POWER_LOW,
                Criteria.ACCURACY_FINE)

        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)

        locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER,
                LocationProvider.AVAILABLE, null, System.currentTimeMillis())
    }

    class MyLocation(provider: String) : Location(provider) {
        override fun isFromMockProvider(): Boolean {
            return false
        }
    }

    private fun getNewYorkLocation(): Location {
        val loc = MyLocation(LocationManager.GPS_PROVIDER)
        loc.latitude = 40.692817
        loc.longitude = -63.916240
        loc.altitude = 0.0
        loc.bearing = 0.0f
        loc.accuracy = 1.0f
        loc.time = System.currentTimeMillis()
        loc.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        return loc
    }
}
