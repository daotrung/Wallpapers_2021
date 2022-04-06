package com.daotrung.wallpapers_2021

import android.app.AlertDialog
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
import com.daotrung.wallpapers_2021.room.*
import com.downloader.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.lang.Error

private var id: Int = 0
private var img: String? = ""
private lateinit var myList : List<MyFavoritePicture>


class MyWallSliderActivity : AppCompatActivity() {

    private var img_lay : ImageView? = null
    private lateinit var database: MyWallPaperDatabase
    private lateinit var dao: IDao
    private lateinit var myFavoriteModel: MyFavoriteModel
    private var img_icon_heart : ImageView? = null
    private var check : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // khởi tạo database , dao , viewModel
        database = Room.databaseBuilder(
            this,
            MyWallPaperDatabase::class.java,
            "mywall_database"
        ).allowMainThreadQueries().build()

        dao = database.getMyWallDao()

        myFavoriteModel = ViewModelProvider(this)[MyFavoriteModel::class.java]

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_slider_wallpaper)

       val img_layout: ImageView = findViewById<ImageView>(R.id.img_slider_wallpaper_last)
       val img_close : ImageView = findViewById<ImageView>(R.id.img_close_live_wallpaper)
       val  img_left_arrow:ImageView =  findViewById<ImageView>(R.id.img_arrow_left_wallpaper)
        val  img_right_arrow: ImageView = findViewById<ImageView>(R.id.img_arrow_right_wallpaper)
      val img_share_btn: ImageView= findViewById<ImageView>(R.id.img_share_wallpaper)
      val  img_save_btn: ImageView = findViewById<ImageView>(R.id.img_btn_save_wallpaper)
       img_icon_heart = findViewById<ImageView>(R.id.img_icon_heart_big)

        img_lay = img_layout

        img_close.setOnClickListener {
            finish()
        }

        // lấy list từ room database
        myFavoriteModel = ViewModelProvider(this).get(MyFavoriteModel::class.java)

        myFavoriteModel.allPicFavorite.observe(this, Observer {
            mypic-> myList = mypic

            val intent = intent

             id = intent.getIntExtra("id_picture",-1)
            Glide.with(this).load(myList[id].myUrlHeart).into(img_lay!!)
            img_icon_heart!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
            setIconHeart(img!!)

        })

        img_left_arrow.setOnClickListener {
            if(id == 0){
                id = myList.size-1
                img =  myList[id].myUrlHeart
                Glide.with(this).load(img).into(img_lay!!)
                setIconHeart(img!!)
            }else{
                id--
                img = myList[id].myUrlHeart
                Glide.with(this).load(img).into(img_lay!!)
                setIconHeart(img!!)
            }
        }
        img_right_arrow.setOnClickListener {
            if(id == myList.size-1){
                id = 0
                img = myList[id].myUrlHeart
                Glide.with(this).load(img).into(img_lay!!)
                setIconHeart(img!!)

            }else{
                id++
                img =  myList[id].myUrlHeart
                Glide.with(this).load(img).into(img_lay!!)
                setIconHeart(img!!)
            }
        }

        img_save_btn.setOnClickListener {
              setDowloadDialog(img!!)
        }

        img_share_btn.setOnClickListener {
            val bitmapDrawale: BitmapDrawable = img_lay!!.drawable as BitmapDrawable
            val bitmap: Bitmap = bitmapDrawale.bitmap
            val bitmapPath: String = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                "Some title",
                null
            )
            val bitmapUri: Uri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            startActivity(Intent.createChooser(intent, "Share Image to Another App"))
        }

    }

    private fun setIconHeart(img: String) {
        if(dao.isExistFavor(img)){
            img_icon_heart!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
            check = true
        }else{
            img_icon_heart!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_unselect_max))
        }

        img_icon_heart!!.setOnClickListener {
            if(check){
                img_icon_heart!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_unselect_max))
                deleteFavorite(img)
                Toast.makeText(this,"Đã xóa ảnh khỏi danh sách yêu thích ",Toast.LENGTH_SHORT).show()
                check = false
            }else{
                img_icon_heart!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
                insertDataToFavorite(img)
                Toast.makeText(this,"Đã thích ảnh",Toast.LENGTH_SHORT).show()
                check = true
            }
        }
    }
    private fun deleteFavorite(img: String) {
        dao.deleteItemWithUrl(img)
        img_icon_heart!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_unselect_max))

    }
    private fun insertDataToFavorite(img:String){
        if(inputCheck(img)&&!dao.isExistFavor(img)) {
            val myFavoritePicture = MyFavoritePicture(img)
            myFavoriteModel!!.addFavorite(myFavoritePicture)
            img_icon_heart!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heart_select_max))
        }
    }
    private fun inputCheck(pathPic: String): Boolean {
        return !(TextUtils.isEmpty(pathPic))
    }


    private fun setDowloadDialog(img: String) {
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
                                this@MyWallSliderActivity,
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
                    pd.setMessage("Dowloading : $per %")
                }

            })
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    pd.dismiss()
                    Toast.makeText(
                        this@MyWallSliderActivity,
                        "Dowloading Completed",
                        Toast.LENGTH_SHORT
                    )
                    setWalpaperDialog()
                }

                override fun onError(error: com.downloader.Error?) {
                    pd.dismiss()
                    Toast.makeText(this@MyWallSliderActivity, "Error", Toast.LENGTH_SHORT)
                }

                fun onError(error: Error?) {}
            })



    }

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

    private fun setBackgroundWallpaper() {
        var bitmapDrawale: BitmapDrawable = img_lay?.drawable as BitmapDrawable
        var bitmap: Bitmap = bitmapDrawale.bitmap
        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        wallpaperManager.setBitmap(bitmap)
        Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
    }
}