package com.daotrung.wallpapers_2021

import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daotrung.wallpapers_2021.adapter.ID_OFF_MY_LIVE
import com.daotrung.wallpapers_2021.room.MyWallPaper
import com.daotrung.wallpapers_2021.room.MyWallpaperViewModel
import com.downloader.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.IOException

private var id: Int = 0
private lateinit var pathVideo: String
private lateinit var myList: List<MyWallPaper>

class MyLiveSliderActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var img_close: ImageView
    private lateinit var img_left_arrow: ImageView
    private lateinit var img_right_arrow: ImageView
    private lateinit var img_save_btn: ImageView
    private var img_share_btn: ImageView? = null
    private lateinit var myWallpaperViewModel: MyWallpaperViewModel


    // set Intent by MyWallpaperService
    companion object {
        @JvmStatic
        fun prepareLiveWallpaperIntent(showAllLiveWallpapers: Boolean): Intent {
            val liveWallpaperIntent = Intent()
            if (showAllLiveWallpapers ||
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
            ) {
                liveWallpaperIntent.action = WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER
            } else {
                liveWallpaperIntent.action = WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                val p = MyWallpaperService::class.java.`package`.name
                val c = MyWallpaperService::class.java.canonicalName
                liveWallpaperIntent.putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    ComponentName(p, c)
                )
            }
            return liveWallpaperIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // set full screen video
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_live_slider)

        videoView = findViewById(R.id.video_view_my_live)
        img_close = findViewById(R.id.img_close_live_video_my_live)
        img_left_arrow = findViewById(R.id.img_arrow_left_video_my_live)
        img_right_arrow = findViewById(R.id.img_arrow_right_video_my_live)
        img_save_btn = findViewById(R.id.img_btn_save_video_my_live)
        img_share_btn = findViewById(R.id.img_share_video_my_live)
        myWallpaperViewModel = ViewModelProvider(this)[MyWallpaperViewModel::class.java]

        PRDownloader.initialize(applicationContext)

        myList = ArrayList()
        myWallpaperViewModel.allWallPaper.observe(this, Observer { mywall ->
            myList = mywall

            val intent = intent
            id = intent.getIntExtra(ID_OFF_MY_LIVE, -1)
            pathVideo = myList[id].myUrl
            setVideo(pathVideo)

        })

        img_close.setOnClickListener {
            finish()
        }
        img_left_arrow.setOnClickListener {
            if (id == 0) {
                id = myList.size - 1
                setVideo(myList[id].myUrl)
                pathVideo = myList[id].myUrl
            } else {
                id--
                setVideo(myList[id].myUrl)
                pathVideo = myList[id].myUrl
            }
        }
        img_right_arrow.setOnClickListener {
            if (id == myList.size - 1) {
                id = 0
                setVideo(myList[id].myUrl)
                pathVideo = myList[id].myUrl
            } else {
                id++
                setVideo(myList[id].myUrl)
                pathVideo = myList[id].myUrl
            }
        }
        img_save_btn.setOnClickListener {

            setDialogVideo(pathVideo)

        }
        img_share_btn!!.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post")
            share.putExtra(Intent.EXTRA_TEXT, pathVideo)
            startActivity(Intent.createChooser(share, "Share link!"))
        }

    }

    private fun setVideo(path: String) {
        val progressDialog: ProgressDialog

        val uri: Uri = Uri.parse(path)
        val fileName = File(uri.path).name

        Log.e("name", fileName)
        if (fileName.contains(".bin")) {
            Toast.makeText(this, R.string.warning_type_file, Toast.LENGTH_SHORT).show()
        } else {
            if (fileName.contains(".mp4") || (fileName.contains(".3gp")) || (fileName.contains(".m4v"))) {
                videoView.visibility = View.VISIBLE
                videoView.setMediaController(MediaController(this))
                videoView.setVideoURI(uri)
                videoView.start()

                progressDialog = ProgressDialog.show(this, "Please wait...", "Loading data", true)
                videoView.setOnPreparedListener {
                    progressDialog.dismiss()
                    it.apply { isLooping = true }
                }
            }
        }

    }

    // xu ly download Video
    private fun setDialogVideo(pathVideo: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Save Video")
        builder.setMessage("Do you want save this video ?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.SET_WALLPAPER
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        /* ... */ if (report.areAllPermissionsGranted()) {
                            downloadVideo(pathVideo)
                        } else {
                            Toast.makeText(
                                this@MyLiveSliderActivity,
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
            val wallpaperManager: WallpaperManager =
                WallpaperManager.getInstance(applicationContext)
            try {
                // clear cache wallpaper
                wallpaperManager.clear()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //  truyen url from activity to wallpaper_service
            val serviceIntent: Intent = Intent(applicationContext, MyWallpaperService::class.java)
            serviceIntent.putExtra("url_pass", pathVideo)
            this.startService(serviceIntent)
            // setTo Wallpaper
            startActivity(prepareLiveWallpaperIntent(false))
            setVideo(pathVideo)

        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
            setDialogWallpaper()
        }
        builder.show()

    }

    private fun downloadVideo(pathVideo: String) {
        val pd = ProgressDialog(this)
        pd.setMessage("Dowloading....")
        pd.setCancelable(false)
        pd.show()
        val file: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        PRDownloader.download(pathVideo, file.path, URLUtil.guessFileName(pathVideo, null, null))
            .build()
            .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnCancelListener { }
            .setOnProgressListener { progress ->
                val per = progress!!.currentBytes * 100 / progress.totalBytes
                pd.setMessage("Downloading : $per %")
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    pd.dismiss()
                    Toast.makeText(
                        this@MyLiveSliderActivity,
                        "Downloading Completed",
                        Toast.LENGTH_SHORT
                    ).show()
                    setDialogWallpaper()
                }

                override fun onError(error: com.downloader.Error?) {
                    pd.dismiss()
                    Toast.makeText(this@MyLiveSliderActivity, "Error", Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun setDialogWallpaper() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Wallpaper")
        builder.setMessage("Do you wan set this video to wallpaper ? ")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            // clear cache wallpaper
            val wallpaperManager: WallpaperManager =
                WallpaperManager.getInstance(applicationContext)
            try {
                wallpaperManager.clear()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //  truyen url from activity to wallpaper_service
            val serviceIntent: Intent =
                Intent(applicationContext, MyWallpaperService::class.java)
            serviceIntent.putExtra("url_pass", pathVideo)
            this.startService(serviceIntent)
            // setTo Wallpaper
            startActivity(LiveVideoActivity.prepareLiveWallpaperIntent(false))
            onBackPressed()
        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
            builder.setCancelable(true)
        }
        builder.show()
    }

}