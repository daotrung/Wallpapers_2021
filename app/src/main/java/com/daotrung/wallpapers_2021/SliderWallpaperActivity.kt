package com.daotrung.wallpapers_2021

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.model.MaterialWallpaperCatList
import com.daotrung.wallpapers_2021.model.MaterialWapaper
import com.daotrung.wallpapers_2021.model.MyWallpaperWall
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import java.io.Serializable
import java.util.ArrayList

const val urlImage ="https://hdwalls.wallzapps.com/upload/"
class SliderWallpaperActivity : AppCompatActivity() {

    private lateinit var list:MaterialWapaper
    private lateinit var listCate : MaterialWallpaperCatList
    private lateinit var listMyWallpaperWall : MyWallpaperWall
    private var id : Int = 0
    private lateinit var img : String
    private var flag : Boolean = false
    private lateinit var img_layout : ImageView
    private lateinit var img_close : ImageView
    private lateinit var img_left_arrow : ImageView
    private lateinit var img_right_arrow : ImageView
    private lateinit var img_save_btn : ImageView
    private lateinit var img_share_btn : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_slider_wallpaper)

        val intent = intent
        img_layout = findViewById(R.id.img_slider_wallpaper_last)
        img_close = findViewById(R.id.img_close_live_wallpaper)
        img_left_arrow = findViewById(R.id.img_arrow_left_wallpaper)
        img_right_arrow = findViewById(R.id.img_arrow_right_wallpaper)
        img_save_btn = findViewById(R.id.img_btn_save_wallpaper)
        img_share_btn = findViewById(R.id.img_share_wallpaper)

        if(intent.getIntExtra("pos_img_wallpaper",0)!=0) {
            list = intent.getSerializableExtra("list_img_wallpaper") as MaterialWapaper
            id = intent.getIntExtra("pos_img_wallpaper", 0)
            setImg(id)
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                id--
                if(id<=0){
                    id = list.MaterialWallpaper.size
                    id--
                    setImg(id)
                }
                setImg(id)
            }
            img_right_arrow.setOnClickListener {
                id++
                if(id>=list.MaterialWallpaper.size){
                    id=0
                    id++
                    setImg(id)
                }
                setImg(id)
            }
        }
        if(intent.getIntExtra("pos_img_categories",0)!=0){
            listCate = intent.getSerializableExtra("list_img_categories") as MaterialWallpaperCatList
            id = intent.getIntExtra("pos_img_categories",0)

            setImg(id)
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                id--
                if(id<=0){
                    id = listCate.MaterialWallpaper.size
                    id--
                    setImg(id)
                }
                setImg(id)
            }
            img_right_arrow.setOnClickListener {
                id++
                if(id>=listCate.MaterialWallpaper.size){
                    id=0
                    id++
                    setImg(id)
                }
                setImg(id)
            }
        }
        if(intent.getIntExtra("pos_img_color",0)!=0){
            listCate = intent.getSerializableExtra("list_img_color") as MaterialWallpaperCatList
            id = intent.getIntExtra("pos_img_color",0)

            setImg(id)
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                id--
                if(id<=0){
                    id = listCate.MaterialWallpaper.size
                    id--
                    setImg(id)
                }
                setImg(id)
            }
            img_right_arrow.setOnClickListener {
                id++
                if(id>=listCate.MaterialWallpaper.size){
                    id=0
                    id++
                    setImg(id)
                }
                setImg(id)
            }
        }
        if(intent.getIntExtra("pos_my_wallpaper",0)!=0){
            listMyWallpaperWall = intent.getSerializableExtra("list_img_my_wallpaper") as MyWallpaperWall
            id = intent.getIntExtra("pos_my_wallpaper",0)

            setImg(id)
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                id--
                if(id<=0){
                    id = listMyWallpaperWall.wallpapers.size
                    id--
                    setImg(id)
                }
                setImg(id)
            }
            img_right_arrow.setOnClickListener {
                id++
                if(id>=listMyWallpaperWall.wallpapers.size){
                    id=0
                    id++
                    setImg(id)
                }
                setImg(id)
            }
        }


//        img_share_btn.setOnClickListener {
//            val drwable: Drawable = img_layout.drawable
//        }
//        img_save_btn.setOnClickListener {
//            val dowloadTask: SliderLiveActivity.DowloadTask = DowloadTask(this)
//            dowloadTask.execute(list.get(this.id).image)
//        }


    }

    private fun setImg(id: Int) {
//        img = list.get(this.id).image
        if(intent.getIntExtra("pos_img_wallpaper",0)!=0) {
            img = list.MaterialWallpaper.get(this.id).image

            Glide.with(this).load(urlImage+img).into(img_layout)

        }
        if(intent.getIntExtra("pos_img_categories",0)!=0){
            img = listCate.MaterialWallpaper.get(this.id).images

            Glide.with(this).load(urlImage+img).into(img_layout)

        }
        if(intent.getIntExtra("pos_img_color",0)!=0){
            img = listCate.MaterialWallpaper.get(this.id).images
            Glide.with(this).load(urlImage+img).into(img_layout)

        }
        if(intent.getIntExtra("pos_my_wallpaper",0)!=0){
            img = listMyWallpaperWall.wallpapers.get(this.id).image
            Glide.with(this).load(img).into(img_layout)

        }

    }


}