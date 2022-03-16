package com.daotrung.wallpapers_2021

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackground
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.fragment.WallpaperFragment
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import com.downloader.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors
import java.util.jar.Manifest
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest

import java.lang.Error


class SliderLiveActivity : AppCompatActivity() {
    private var list : ArrayList<SlideLiveWapaper> = ArrayList()
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
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_slider_live)
        val intent = intent
        img_layout = findViewById(R.id.img_slider_live_last)
        img_close = findViewById(R.id.img_close_live)
        img_left_arrow = findViewById(R.id.img_arrow_left)
        img_right_arrow = findViewById(R.id.img_arrow_right)
        img_save_btn = findViewById(R.id.img_btn_save)
        img_share_btn = findViewById(R.id.img_share)
        list = intent.getSerializableExtra("list_img_live") as ArrayList<SlideLiveWapaper>
        id = intent.getIntExtra("pos_img_live",0)

        img = list.get(this.id).image

        PRDownloader.initialize(applicationContext)
        setImg(id)

        img_close.setOnClickListener {
            finish()
        }
        img_left_arrow.setOnClickListener {
             id--
            if(id<=0){
                id = list.size
                id--
                setImg(id)
            }
                setImg(id)
        }
        img_right_arrow.setOnClickListener {
            id++
            if(id>=list.size){
                id=0
                id++
                setImg(id)
            }
            setImg(id)
        }
        img_share_btn.setOnClickListener {
            var bitmapDrawale : BitmapDrawable = img_layout.drawable as BitmapDrawable
            var bitmap:Bitmap = bitmapDrawale.bitmap
            var bitmapPath : String = MediaStore.Images.Media.insertImage(contentResolver,bitmap,"Some title",null)
            var bitmapUri : Uri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_STREAM,bitmapUri)
            startActivity(Intent.createChooser(intent,"Share Image"))
        }
        img_save_btn.setOnClickListener {
//              setDilog()
        }
    }

//    private fun setDilog() {
//         val builder = AlertDialog.Builder(this)
//         builder.setTitle("Set up Wallpaper")
//         builder.setMessage("Do you want set up wallpaper this image now ?")
//         builder.setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
////             checkPermissions()
////             setBackgroundWallpaper()
//
//         }
//        builder.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->
//            dowloadImage()
//             finish()
//        }
//        builder.show()
//
//    }

//    private
//    fun setBackgroundWallpaper() {
//        var bitmapDrawale : BitmapDrawable = img_layout.drawable as BitmapDrawable
//        var bitmap:Bitmap = bitmapDrawale.bitmap
//        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
//        wallpaperManager.setBitmap(bitmap)
//        Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun checkPermissions() {
//        Dexter.withContext(this)
//            .withPermissions(
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                android.Manifest.permission.SET_WALLPAPER
//            ).withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                /* ... */ if(report.areAllPermissionsGranted()){
//                    dowloadImage()
//                    }else {
//                        Toast.makeText(this@SliderLiveActivity,"Please allow all permission",Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: List<PermissionRequest?>?,
//                    token: PermissionToken?
//                ) { /* ... */
//                }
//            }).check()
//    }
//
//    private fun dowloadImage() {
//
//        var pd = ProgressDialog(this)
//        pd.setMessage("Dowloading....")
//        pd.setCancelable(false)
//        pd.show()
//        var file:File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        PRDownloader.download(img, file.path, URLUtil.guessFileName(img,null,null))
//            .build()
//            .setOnStartOrResumeListener { }
//            .setOnPauseListener { }
//            .setOnCancelListener(object : OnCancelListener {
//                override fun onCancel() {}
//
//            })
//            .setOnProgressListener (object : OnProgressListener{
//                override fun onProgress(progress: Progress?) {
//                    var per = progress!!.currentBytes*100 / progress.totalBytes
//                    pd.setMessage("Dowloading : $per %")
//                }
//
//            })
//            .start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//                    pd.dismiss()
//                    Toast.makeText(this@SliderLiveActivity,"Dowloading Completed",Toast.LENGTH_SHORT)
//                }
//                override fun onError(error: com.downloader.Error?) {
//                    pd.dismiss()
//                    Toast.makeText(this@SliderLiveActivity,"Error",Toast.LENGTH_SHORT)
//                }
//
//                fun onError(error: Error?) {}
//            })
//    }


    private fun setImg(id: Int) {

        img = list.get(this.id).image
        Glide.with(this).load(img).into(img_layout)
    }





}