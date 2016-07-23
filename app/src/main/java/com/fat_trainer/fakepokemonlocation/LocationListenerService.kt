package com.fat_trainer.fakepokemonlocation


import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket

class LocationListenerService : IntentService("LocationListenerService") {
    var socket: DatagramSocket? = null
    var locationManager: LocationManager? = null

    companion object Constants {
        val TAG = "LocationListenerService"
    }

    override fun onCreate() {
        super.onCreate()
        socket = DatagramSocket(12345)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onDestroy() {
        socket?.close()
    }

    override fun onHandleIntent(intent: Intent?) {
        val buf = ByteArray(256)
        while (true) {
            val packet = DatagramPacket(buf, buf.size)
            socket!!.receive(packet)
            val response = String(packet.data, 0, packet.length)
            tryUpdateLocation(response)
        }
    }

    private fun tryUpdateLocation(locationStr: String): Boolean {
        val latLon = LatLonUtil.tryParse(locationStr)
        if (latLon == null) {
            Log.d(TAG, "Failed to parse location: $locationStr")
            return false
        }

        Log.v(TAG, "Update location to: $latLon")

        val value = setMockLocationSettings()
        try {
            locationManager!!.setTestProviderLocation(LocationManager.GPS_PROVIDER, latLon.toGPSLocation())
        } catch (e: SecurityException) {
            e.printStackTrace()
        } finally {
            restoreMockLocationSettings(value)
        }

        return true
    }

    private fun setMockLocationSettings(): Int {
        var result = 0

        try {
            result = Settings.Secure.getInt(contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION)
            Settings.Secure.putInt(contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION, 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    private fun restoreMockLocationSettings(restoreValue: Int) {
        try {
            Settings.Secure.putInt(contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION, restoreValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun LatLon.toGPSLocation(): Location {
        val loc = android.location.Location(android.location.LocationManager.GPS_PROVIDER)
        loc.latitude = lat
        loc.longitude = lon
        loc.altitude = 0.0
        loc.bearing = 0.0f
        loc.accuracy = 1.0f
        loc.time = java.lang.System.currentTimeMillis()
        loc.elapsedRealtimeNanos = android.os.SystemClock.elapsedRealtimeNanos()
        return loc
    }
}
