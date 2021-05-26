package com.example.reckeyboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.reckeyboard.activities.*
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLanguages: Button = findViewById(R.id.btnLanguages)
        val btnDatabase: Button = findViewById(R.id.btnDatabase)
        val btnHelp: ImageButton = findViewById(R.id.btnHelp)
        var intent: Intent

        btnLanguages.setOnClickListener {
            intent = Intent(this, KeyboardSettingsActivity::class.java)
            startActivity(intent)
        }
        btnDatabase.setOnClickListener {
            intent = Intent(this, DatabaseActivity::class.java)
            startActivity(intent)
        }
        btnHelp.setOnClickListener {
            intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
    }
}