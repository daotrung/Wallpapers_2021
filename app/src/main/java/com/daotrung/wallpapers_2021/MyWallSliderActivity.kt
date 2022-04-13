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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.adapter.ID_MY_WALL_LIST
import com.daotrung.wallpapers_2021.room.*
import com.downloader.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.lang.Error
import android.graphics.drawable.Drawable
import android.app.DownloadManager
import android.content.Context
import androidx.core.content.PackageManagerCompat

import androidx.core.content.PackageManagerCompat.LOG_TAG
import java.lang.Exception
import java.security.AccessController.getContext


private var id: Int = 0

private lateinit var myList: List<MyFavoritePicture>
const val KEY_DB: String = "POS_DB"

class MyWallSliderActivity : AppCompatActivity() {
    private var img: String? = ""
    private var img_lay: ImageView? = null
    private lateinit var database: MyWallPaperDatabase
    private lateinit var dao: IDao
    private var myFavoriteModel: MyFavoriteModel? = null
    private var img_icon_heart: ImageView? = null
    private var check: Boolean = false
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

        img_lay = findViewById(R.id.img_slider_wallpaper_last)
        val img_close: ImageView = findViewById(R.id.img_close_live_wallpaper)
        val img_left_arrow: ImageView = findViewById(R.id.img_arrow_left_wallpaper)
        val img_right_arrow: ImageView = findViewById(R.id.img_arrow_right_wallpaper)
        val img_share_btn: ImageView = findViewById(R.id.img_share_wallpaper)
        val img_save_btn: ImageView = findViewById(R.id.img_btn_save_wallpaper)
        img_icon_heart = findViewById(R.id.img_icon_heart_big)

        img_close.setOnClickListener {
            finish()
        }

        // lấy list từ room database
        myFavoriteModel!!.allPicFavorite.observe(this, Observer {

                mypic ->
            myList = mypic
            val intent = intent
            id = intent.getIntExtra(ID_MY_WALL_LIST, -1)
            setIconHeart(myList[id].myUrlHeart, id)
            Glide.with(this).load(myList[id].myUrlHeart).into(img_lay!!)
            img = myList[id].myUrlHeart
        })
        img_left_arrow.setOnClickListener {
            if (id == 0) {
                id = myList.size - 1
                img = myList[id].myUrlHeart
                Glide.with(this).load(img).into(img_lay!!)
                setIconHeart(img!!, id)
            } else {
                id--
                img = myList[id].myUrlHeart
                Glide.with(this).load(img).into(img_lay!!)
                setIconHeart(img!!, id)
            }
        }
        img_right_arrow.setOnClickListener {
            if (id == myList.size - 1) {
                id = 0
                img = myList[id].myUrlHeart
                Glide.with(this).load(img).into(img_lay!!)
                setIconHeart(img!!, id)

            } else {
                id++
                img = myList[id].myUrlHeart
                Glide.with(this).load(img).into(img_lay!!)
                setIconHeart(img!!, id)
            }
        }

        img_save_btn.setOnClickListener {
            setDownloadDialog(img!!)
        }

        img_share_btn.setOnClickListener {
            shareImg()
        }

    }

    private fun setDownloadDialog(img: String) {
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
                            downloadImage(img)
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
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
            setWallpaperDialog(img)
        }
        builder.show()
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
                            Toast.makeText(
                                this@MyWallSliderActivity,
                                "Share img",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Toast.makeText(
                            this@MyWallSliderActivity,
                            "Image download started.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val bitmapDrawable: BitmapDrawable = img_lay?.drawable as BitmapDrawable
                        val bitmap: Bitmap = bitmapDrawable.bitmap
                        val bitmapPath: String =
                            MediaStore.Images.Media.insertImage(
                                contentResolver,
                                bitmap,
                                "Some title",
                                null
                            )
                        val bitmapUri: Uri = Uri.parse(bitmapPath)
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                        startActivity(Intent.createChooser(intent, "Share Image to Another App"))

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

    private fun setIconHeart(img: String, pos: Int) {
        if (dao.isExistFavor(img)) {
            img_icon_heart!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.heart_select_max
                )
            )
            check = true
        } else {
            img_icon_heart!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.heart_unselect_max
                )
            )
        }

        img_icon_heart!!.setOnClickListener {
            if (check) {
                img_icon_heart!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.heart_unselect_max
                    )
                )
                deleteFavorite(img)
                sendLocalBroadcastForMyDB(pos)
                check = false
                finish()

            } else {
                img_icon_heart!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.heart_select_max
                    )
                )
                insertDataToFavorite(img)
                check = true
            }
        }
    }

    private fun deleteFavorite(img: String) {
        dao.deleteItemWithUrl(img)
        img_icon_heart!!.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.heart_unselect_max
            )
        )

    }

    private fun insertDataToFavorite(img: String) {
        if (inputCheck(img) && !dao.isExistFavor(img)) {
            val myFavoritePicture = MyFavoritePicture(img)
            myFavoriteModel!!.addFavorite(myFavoritePicture)
            img_icon_heart!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.heart_select_max
                )
            )
        }
    }

    private fun inputCheck(pathPic: String): Boolean {
        return !(TextUtils.isEmpty(pathPic))
    }


    private fun sendLocalBroadcastForMyDB(pos: Int) {
        val intent = Intent("localBroadCastDB")
        intent.putExtra(KEY_DB, pos)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        Log.e("posDB_", pos.toString())

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
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            val intent = Intent(this, SetWallpaperActivity::class.java)
            intent.putExtra("mw_2", img)
            Log.e("mw_2", img.toString())
            startActivity(intent)
        }

        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
            builder.setOnDismissListener {
                builder.setCancelable(true)
            }
        }
        builder.show()
    }
}