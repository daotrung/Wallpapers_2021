package com.daotrung.wallpapers_2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class SettingActivity : AppCompatActivity() {

    private lateinit var radioGroup : RadioGroup
    private lateinit var themeTV : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        radioGroup = RadioGroup(this)
        themeTV = TextView(this)
        radioGroup.findViewById<RadioGroup>(R.id.idRGgroup)
        themeTV.findViewById<TextView>(R.id.idtvTheme)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.idRBLight){
                themeTV.text = "Light Theme"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            if(checkedId == R.id.idRBDark){
                themeTV.text = "Dark Theme"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}