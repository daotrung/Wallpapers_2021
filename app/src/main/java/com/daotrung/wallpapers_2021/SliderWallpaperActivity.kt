package com.daotrung.wallpapers_2021

import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.app.WallpaperManager
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
import androidx.room.Room
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.adapter.WallpaperListColorMainAdapter
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

const val url_get_img_preview = "http://hdwalls.wallzapps.com/upload/"

class SliderWallpaperActivity : AppCompatActivity() {

    private  var mMyPicViewModel : MyPicViewModel? = null
    private  var mMyFavoriteModel : MyFavoriteModel? = null
    private lateinit var database: MyWallPaperDatabase
    private lateinit var dao: IDao
    private var list: MaterialWapaper = MaterialWapaper(ArrayList<Trending>())
    private var listCate: MaterialWallpaperCatList = MaterialWallpaperCatList(ArrayList<CatList>())
    private var id: Int = 1
    private var img: String? = ""
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
        mMyPicViewModel = ViewModelProvider(this)[MyPicViewModel::class.java]
        mMyFavoriteModel = ViewModelProvider(this)[MyFavoriteModel::class.java]

        setFullScreenMode()
        setContentView(R.layout.activity_slider_wallpaper)

        val intent = intent

        inFinViewById()

        if (intent.getIntExtra("pos_img_trend", 0) >= 1) {


            list = intent.getSerializableExtra("list_img_trend") as MaterialWapaper
            id = intent.getIntExtra("pos_img_trend", 0) - 1
            img = url_get_img_preview + list.MaterialWallpaper[id].image

//            insertDataToDatabase(img!!)
            setIconHeart(img!!)

            Glide.with(this).load(img).into(img_layout)
            img_icon_heart.setOnClickListener {
                setIconHeart(img!!)
            }
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                if (id == 0) {
                    id = list.MaterialWallpaper.size - 1
                    img = url_get_img_preview + list.MaterialWallpaper[id].image
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                } else {
                    id--
                    img = url_get_img_preview + list.MaterialWallpaper[id].image
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                }
            }
            img_right_arrow.setOnClickListener {

                if (id == list.MaterialWallpaper.size - 1) {
                    id = 0
                    img = url_get_img_preview + list.MaterialWallpaper[id].image
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                } else {
                    id++
                    img = url_get_img_preview + list.MaterialWallpaper[id].image
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                }
            }
            img_save_btn.setOnClickListener {
                setDowloadDilog(img!!)
            }
            img_share_btn.setOnClickListener {
                shareImg()
            }
        }
        if (intent.getIntExtra("pos_img_categories", 0) >= 1) {
            listCate =
                intent.getSerializableExtra("list_img_categories") as MaterialWallpaperCatList
            id = intent.getIntExtra("pos_img_categories", 0) - 1

            img = url_get_img_preview + listCate.MaterialWallpaper[id].images

            setIconHeart(img!!)

            Glide.with(this).load(img).into(img_layout)

            // can fix
            img_icon_heart.setOnClickListener {
                setIconHeart(img!!)

            }
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {

                if (id == 0) {
                    id = listCate.MaterialWallpaper.size - 1
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                } else {
                    id--
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                }

            }
            img_right_arrow.setOnClickListener {
                if (id == listCate.MaterialWallpaper.size - 1) {
                    id = 0
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                } else {
                    id++
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                }

            }
            img_share_btn.setOnClickListener {
                shareImg()
            }
            img_save_btn.setOnClickListener {
                setDowloadDilog(img!!)

            }
        }
        if (intent.getIntExtra("pos_img_color", 0) >= 1) {
            listCate = intent.getSerializableExtra("list_img_color") as MaterialWallpaperCatList
            id = intent.getIntExtra("pos_img_color", 0) - 1
            img = url_get_img_preview + listCate.MaterialWallpaper.get(id).images


            setIconHeart(img!!)
            img_icon_heart.setOnClickListener {
                setIconHeart(img!!)

            }
            Glide.with(this).load(img).into(img_layout)
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                if (id == 0) {
                    id = listCate.MaterialWallpaper.size - 1
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                } else {
                    id--
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                }

            }
            img_right_arrow.setOnClickListener {
                if (id == listCate.MaterialWallpaper.size - 1) {
                    id = 0
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                } else {
                    id++
                    img = url_get_img_preview + listCate.MaterialWallpaper[id].images
                    Glide.with(this).load(img).into(img_layout)
                    setIconHeart(img!!)
                }

            }
            img_share_btn.setOnClickListener {
               shareImg()
            }
            img_save_btn.setOnClickListener {
                setDowloadDilog(img!!)
            }
        }

    }

    private fun shareImg() {

        var file: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        PRDownloader.download(img, file.path, URLUtil.guessFileName(img, null, null))
            .build()
            .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnCancelListener(object : OnCancelListener {
                override fun onCancel() {}
            })
            .setOnProgressListener(object : OnProgressListener {
                override fun onProgress(progress: Progress?) {
                }

            })
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val bitmapDrawale: BitmapDrawable = img_layout.drawable as BitmapDrawable
                    val bitmap: Bitmap = bitmapDrawale.bitmap
                    val bitmapPath: String =
                        MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Some title", null)
                    val bitmapUri: Uri = Uri.parse(bitmapPath)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setType("image/*")
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                    startActivity(Intent.createChooser(intent, "Share Image to Another App"))

                }

                override fun onError(error: com.downloader.Error?) {
                    Toast.makeText(this@SliderWallpaperActivity, "Error", Toast.LENGTH_SHORT)
                }

                fun onError(error: Error?) {}
            })


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

    private fun insertDataToDatabase(img: String) {
        if(inputCheck(img) && !dao.isExistPic(img)){
            val myPicturePaper = MyPicturePaper(img)
            mMyPicViewModel!!.addPicPaper(myPicturePaper)
        }

    }

    private fun insertDataToFavorite(img:String){
        if(inputCheck(img)&&!dao.isExistFavor(img)) {
            val myFavoritePicture = MyFavoritePicture(img)
            mMyFavoriteModel!!.addFavorite(myFavoritePicture)
            mMyFavoriteModel!!.deleteFavorite(myFavoritePicture)
        }
    }

    private fun deleteDataToFavorite(img:String){
        if(inputCheck(img)&&!dao.isExistFavor(img)) {
            val myFavoritePicture = MyFavoritePicture(img)
            mMyFavoriteModel!!.deleteFavorite(myFavoritePicture)
        }
    }
    private fun setIconHeart(img:String){
         if(dao.isExistFavor(img)){
             img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
             img_icon_heart.setOnClickListener {
                 img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_unselect_max))
                 deleteDataToFavorite(img)
                 Toast.makeText(this,"Đã xóa ảnh khỏi danh sách yêu thích ",Toast.LENGTH_SHORT).show()
             }
         }else{
             img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_unselect_max))
             img_icon_heart.setOnClickListener {
                 img_icon_heart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
                 insertDataToFavorite(img)
                 Toast.makeText(this,"Đã thích ảnh",Toast.LENGTH_SHORT).show()
             }

         }
    }

    private fun inputCheck(pathPic: String): Boolean {
         return !(TextUtils.isEmpty(pathPic))
    }

    // show dialog with wallpaper
    private fun setWalpaperDialog() {
        val builderWallpaer = AlertDialog.Builder(this)
        builderWallpaer.setTitle("Set Wallpaper")
        builderWallpaer.setMessage("DO you want set up wallpaper ?")
        builderWallpaer.setPositiveButton("No") { dialogInterface: DialogInterface, i: Int ->
            finish()

        }
        builderWallpaer.setNegativeButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            setBackgroundWallpaper()
        }
        builderWallpaer.show()
    }

    // dowload img
    private fun setDowloadDilog(img: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Download Image")
        builder.setMessage("Do you want download ?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.SET_WALLPAPER
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        /* ... */ if (report.areAllPermissionsGranted()) {
                            dowloadImage(img)
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
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            setWalpaperDialog()

        }
        builder.show()
    }

    // set background wallpaper
    private fun setBackgroundWallpaper() {
        var bitmapDrawale: BitmapDrawable = img_layout.drawable as BitmapDrawable
        var bitmap: Bitmap = bitmapDrawale.bitmap
        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        wallpaperManager.setBitmap(bitmap)
        Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
    }


    private fun dowloadImage(img: String) {
        var pd = ProgressDialog(this)
        pd.setMessage("Downloading....")
        pd.setCancelable(false)
        pd.show()
        var file: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        PRDownloader.download(img, file.path, URLUtil.guessFileName(img, null, null))
            .build()
            .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnCancelListener(object : OnCancelListener {
                override fun onCancel() {}
            })
            .setOnProgressListener(object : OnProgressListener {
                override fun onProgress(progress: Progress?) {
                    var per = progress!!.currentBytes * 100 / progress.totalBytes
                    pd.setMessage("Downloading : $per %")
                }

            })
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    pd.dismiss()
                    Toast.makeText(
                        this@SliderWallpaperActivity,
                        "Downloading Completed",
                        Toast.LENGTH_SHORT
                    )
                    setWalpaperDialog()
                }

                override fun onError(error: com.downloader.Error?) {
                    pd.dismiss()
                    Toast.makeText(this@SliderWallpaperActivity, "Error", Toast.LENGTH_SHORT)
                }

                fun onError(error: Error?) {}
            })


    }


}