package ru.brauer.weather

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val someButton = findViewById<Button>(R.id.some_button)
        someButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "The button has pressed", Toast.LENGTH_LONG).show()
        }
    }
}