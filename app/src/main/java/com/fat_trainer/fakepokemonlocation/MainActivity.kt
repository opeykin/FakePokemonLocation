package com.fat_trainer.fakepokemonlocation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, LocationListenerService::class.java))

        val button = findViewById(R.id.start_maps_button) as Button
        button.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }
}
