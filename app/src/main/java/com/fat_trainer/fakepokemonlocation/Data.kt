package com.fat_trainer.fakepokemonlocation


class LatLon(val lat: Double, val lon: Double) {
    override fun toString(): String = "$lat,$lon"
}
object LatLonUtil {
    fun tryParse(str: String): LatLon? {
        try {
            val parts = str.split(',')
            return LatLon(parts[0].trim().toDouble(), parts[1].trim().toDouble())
        } catch (ex: Exception) {
            return null
        }
    }
}


