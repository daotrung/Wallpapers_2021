package com.daotrung.wallpapers_2021

import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.adapter.*
import com.daotrung.wallpapers_2021.model.*
import com.daotrung.wallpapers_2021.room.*
import com.downloader.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.Serializable
import java.lang.Error
import java.lang.Exception

const val url_get_img_preview = "http://hdwalls.wallzapps.com/upload/"

class SliderWallpaperActivity : AppCompatActivity() {

    private  var mMyFavoriteModel : MyFavoriteModel? = null
    private lateinit var database: MyWallPaperDatabase
    private lateinit var dao: IDao
    private var list: MaterialWapaper = MaterialWapaper(ArrayList<Trending>())
    private var listCate: MaterialWallpaperCatList = MaterialWallpaperCatList(ArrayList<CatList>())
    private var id: Int = 1
    private var img: String? = ""
    private var check : Boolean = false
    private lateinit var img_layout: ImageView
    private lateinit var img_close: ImageView
    private lateinit var img_left_arrow: ImageView
    private lateinit var img_right_arrow: ImageView
    private lateinit var img_save_btn: ImageView
    private lateinit var img_share_btn: ImageView
    private lateinit var img_icon_heart : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     // khởi tạo database , dao , viewModel
        database = Room.databaseBuilder(
            this,
            MyWallPaperDatabase::class.java,
            "mywall_database"
        ).allowMainThreadQueries().build()

        dao = database.getMyWallDao()
        mMyFavoriteModel = ViewModelProvider(this)[MyFavoriteModel::class.java]

        setFullScreenMode()
        setContentView(R.layout.activity_slider_wallpaper)

        val intent = intent

        inFinViewById()

        if (intent.getIntExtra(POST_IMG_TREND, 0) >= 1) {

            list = intent.getSerializableExtra(LIST_IMG_TREND) as MaterialWapaper
            id = intent.getIntExtra(POST_IMG_TREND, 0) - 1
            img = url_get_img_preview + list.MaterialWallpaper[id].image

            Glide.with(this).load(img).into(img_layout)
            img_icon_heart.setOnClickListener {
                setIconHeart(img!!,id)
            }
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                if (id == 0) {
                    id = list.MaterialWallpaper.size - 1
                    img = url_get_img_preview + list.MaterialWallpaper[id].image
                    Glide.with(this).load(img).into(img_layout)
                    list.MaterialWallpaper
                    setIconHeart(img!!,id)
                } else {
                    id--
                    img = url_get_img_preview + list.MaterialWallpaper[id].image
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                }
            }
            img_right_arrow.setOnClickListener {

                if (id == list.MaterialWallpaper.size - 1) {
                    id = 0
                    img = url_get_img_preview + list.MaterialWallpaper[id].image
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                } else {
                    id++
                    img = url_get_img_preview + list.MaterialWallpaper[id].image
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                }
            }
            img_save_btn.setOnClickListener {
                setDownloadDilog(img!!)
            }
            img_share_btn.setOnClickListener {
                shareImg()
            }
        }
        if (intent.getIntExtra(POST_IMG_CATEGORIES, 0) >= 1) {
            listCate =
                intent.getSerializableExtra(LIST_IMG_CATEGORIES) as MaterialWallpaperCatList
            id = intent.getIntExtra(POST_IMG_CATEGORIES, 0) - 1

            img = url_get_img_preview + listCate.MaterialWallpaper[id].images

            Glide.with(this).load(img).into(img_layout)
            setIconHeart(img!!,id)

            // can fix
            img_icon_heart.setOnClickListener {
                setIconHeart(img!!,id)

            }
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {

                if (id == 0) {
                    id = listCate.MaterialWallpaper.size - 1
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                } else {
                    id--
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                }

            }
            img_right_arrow.setOnClickListener {
                if (id == listCate.MaterialWallpaper.size - 1) {
                    id = 0
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                } else {
                    id++
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                }

            }
            img_share_btn.setOnClickListener {
                shareImg()
            }
            img_save_btn.setOnClickListener {
                setDownloadDilog(img!!)

            }
        }
        if (intent.getIntExtra(POST_IMG_CATEGORIES, 0) >= 1) {
            listCate = intent.getSerializableExtra(LIST_IMG_CATEGORIES) as MaterialWallpaperCatList
            id = intent.getIntExtra(POST_IMG_CATEGORIES, 0) - 1
            img = url_get_img_preview + listCate.MaterialWallpaper.get(id).images

            Glide.with(this).load(img).into(img_layout)
            setIconHeart(img!!,id)
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                if (id == 0) {
                    id = listCate.MaterialWallpaper.size - 1
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                } else {
                    id--
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                }

            }
            img_right_arrow.setOnClickListener {
                if (id == listCate.MaterialWallpaper.size - 1) {
                    id = 0
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                } else {
                    id++
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!,id)
                }

            }
            img_share_btn.setOnClickListener {
               shareImg()
            }
            img_save_btn.setOnClickListener {
                setDownloadDilog(img!!)
            }
        }

    }

    private fun shareImg() {
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.SET_WALLPAPER
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    /* ... */ if (report.areAllPermissionsGranted()) {
                        try {
                            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                            val downloadUri = Uri.parse(img)
                            val request = DownloadManager.Request(downloadUri)
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false)
                                .setTitle(img)
                                .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(
                                    Environment.DIRECTORY_PICTURES,
                                    File.separator + img.toString() + ".jpg"
                                )
                            dm.enqueue(request)

                        } catch (e: Exception) {
                            Toast.makeText(this@SliderWallpaperActivity, "Share img", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(this@SliderWallpaperActivity, "Image download started.", Toast.LENGTH_SHORT).show()
                        val bitmapDrawable: BitmapDrawable = img_layout.drawable as BitmapDrawable
                        val bitmap: Bitmap = bitmapDrawable.bitmap
                        val bitmapPath: String =
                            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Some title", null)
                        val bitmapUri: Uri = Uri.parse(bitmapPath)
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                        startActivity(Intent.createChooser(intent, "Share Image to Another App"))

                    } else {
                        Toast.makeText(
                            this@SliderWallpaperActivity,
                            "Please allow all permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()


    }

    private fun setFullScreenMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        PRDownloader.initialize(applicationContext)
    }

    private fun inFinViewById() {
        img_icon_heart = findViewById(R.id.img_icon_heart_big)
        img_layout = findViewById(R.id.img_slider_wallpaper_last)
        img_close = findViewById(R.id.img_close_live_wallpaper)
        img_left_arrow = findViewById(R.id.img_arrow_left_wallpaper)
        img_right_arrow = findViewById(R.id.img_arrow_right_wallpaper)
        img_save_btn = findViewById(R.id.img_btn_save_wallpaper)
        img_share_btn = findViewById(R.id.img_share_wallpaper)
    }

    private fun deleteFavorite(img: String) {
            dao.deleteItemWithUrl(img)
            img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_unselect_max))

    }

    private fun insertDataToFavorite(img:String){
        if(inputCheck(img)&&!dao.isExistFavor(img)) {
            val myFavoritePicture = MyFavoritePicture(img)
            mMyFavoriteModel!!.addFavorite(myFavoritePicture)
            img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
        }
    }

    private fun setIconHeart(img:String,pos:Int){
        if(dao.isExistFavor(img)){
            img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
            check = true
        }else{
            img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_unselect_max))
        }
        img_icon_heart.setOnClickListener {

             if(check){
                 img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_unselect_max))
                 deleteFavorite(img)
                 check = false
                 sendLocalBroadcastForInformation(pos)
             }else{
                 img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
                 insertDataToFavorite(img)
                 check = true
                 sendLocalBroadcastForInformation(pos)
             }
        }

    }

    private fun sendLocalBroadcastForInformation(pos: Int) {
        val intent = Intent("localBroadCastForData")
        intent.putExtra("pos_change",pos)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

    }


    private fun inputCheck(pathPic: String): Boolean {
         return !(TextUtils.isEmpty(pathPic))
    }


    // dowload img
    private fun setDownloadDilog(imgUrl: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Download Image")
        builder.setMessage("Do you want download ?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.SET_WALLPAPER
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        /* ... */ if (report.areAllPermissionsGranted()) {
                            downloadImage(imgUrl)
                        } else {
                            Toast.makeText(
                                this@SliderWallpaperActivity,
                                "Please allow all permission",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) { /* ... */
                    }
                }).check()


        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
              setWallpaperDialog(imgUrl)
        }
        builder.show()
    }

    private fun downloadImage(img: String) {
        try {
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(img)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(img)
                .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + img.toString() + ".jpg"
                )
            dm.enqueue(request)
            Toast.makeText(this, "Image download started.", Toast.LENGTH_SHORT).show()
            setWallpaperDialog(img)

        } catch (e: Exception) {
            Toast.makeText(this, "Image download failed.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setWallpaperDialog(img: String) {
         val builder = AlertDialog.Builder(this)
        builder.setTitle("Save Wallpaper")
        builder.setMessage("Do you want set image to wallpaper ?")
        builder.setPositiveButton("Yes"){_:DialogInterface,_:Int ->
             val intent = Intent(this,SetActivityWallpaper::class.java)
            intent.putExtra("mw_1",img)
            Log.e("mw__",img.toString())
            startActivity(intent)
        }

        builder.setNegativeButton("No"){_:DialogInterface,_:Int->
             builder.setOnDismissListener {
                 builder.setCancelable(true)
             }
        }
        builder.show()
    }


}