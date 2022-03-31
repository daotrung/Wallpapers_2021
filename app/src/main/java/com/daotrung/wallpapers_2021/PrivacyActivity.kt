package com.daotrung.wallpapers_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PrivacyActivity : AppCompatActivity() {

    private lateinit var btnContact : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        btnContact = findViewById(R.id.contact_us_btn)

        btnContact.setOnClickListener{
            finish()
        }

    }

}