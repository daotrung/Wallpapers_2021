package com.daotrung.wallpapers_2021

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.downloader.PRDownloader

class SetWallpaperActivity : AppCompatActivity() {

    private lateinit var btn_save : Button
    private lateinit var img_wall_paper : ImageView
    private lateinit var url_img : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        PRDownloader.initialize(applicationContext)
        setContentView(R.layout.activity_set_wallpaper)
         btn_save =  findViewById(R.id.btn_set_wall)
         img_wall_paper  = findViewById(R.id.img_preview_image)

            val intent = intent
            url_img = intent.getStringExtra("mw_2").toString()
            Glide.with(this).load(url_img).into(img_wall_paper)

        btn_save.setOnClickListener {
            var bitmapDrawale: BitmapDrawable = img_wall_paper.drawable as BitmapDrawable
            var bitmap: Bitmap = bitmapDrawale.bitmap
            val wallpaperManager = WallpaperManager.getInstance(applicationContext)
            wallpaperManager.setBitmap(bitmap)
            btn_save.isEnabled = false
            Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }
}