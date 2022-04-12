package com.daotrung.wallpapers_2021

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.WallpaperManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import java.io.File
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.*
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.URLUtil
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.daotrung.wallpapers_2021.room.IDao
import com.daotrung.wallpapers_2021.room.MyWallPaper
import com.daotrung.wallpapers_2021.room.MyWallPaperDatabase
import com.daotrung.wallpapers_2021.room.MyWallpaperViewModel
import com.downloader.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException
import java.lang.Error
import android.content.Intent
import com.daotrung.wallpapers_2021.adapter.LIST_LIVE_VIDEO
import com.daotrung.wallpapers_2021.adapter.POS_LIVE_VIDEO


private var list: ArrayList<SlideLiveWapaper> = ArrayList()
private var id: Int = 0
private lateinit var pathVideo: String

@SuppressLint("StaticFieldLeak")
private lateinit var videoView: VideoView

@SuppressLint("StaticFieldLeak")
private lateinit var img_close: ImageView

@SuppressLint("StaticFieldLeak")
private lateinit var img_left_arrow: ImageView

@SuppressLint("StaticFieldLeak")
private lateinit var img_right_arrow: ImageView

@SuppressLint("StaticFieldLeak")
private lateinit var img_save_btn: ImageView

@SuppressLint("StaticFieldLeak")
private lateinit var img_share_btn: ImageView

@SuppressLint("StaticFieldLeak")
private lateinit var img_gif: ImageView

class LiveVideoActivity : AppCompatActivity() {


    private lateinit var mMyWallpaperViewModel : MyWallpaperViewModel
    private lateinit var database:MyWallPaperDatabase
    private lateinit var iDao: IDao


    // set Intent by MyWallpaperService
    companion object {
        @JvmStatic
        fun prepareLiveWallpaperIntent(showAllLiveWallpapers: Boolean): Intent {
            val liveWallpaperIntent = Intent()
            if (showAllLiveWallpapers ||
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
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
        super.onCreate(savedInstanceState)

        // khởi tạo database , dao , viewModel
        database = Room.databaseBuilder(
            this,
            MyWallPaperDatabase::class.java,
            "mywall_database"
        ).allowMainThreadQueries().build()

        iDao = database.getMyWallDao()

        mMyWallpaperViewModel = ViewModelProvider(this)[MyWallpaperViewModel::class.java]

        // setFullScreen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_live_video)

        // init
        videoView = findViewById(R.id.video_view)
        img_close = findViewById(R.id.img_close_live_video)
        img_left_arrow = findViewById(R.id.img_arrow_left_video)
        img_right_arrow = findViewById(R.id.img_arrow_right_video)
        img_save_btn = findViewById(R.id.img_btn_save_video)
        img_share_btn = findViewById(R.id.img_share_video)
        img_gif = findViewById(R.id.img_slider_live_gif)

        PRDownloader.initialize(applicationContext)

        // lấy dữ liệu từ liveListAdapter
        val intent = intent
            list = intent.getSerializableExtra(LIST_LIVE_VIDEO) as ArrayList<SlideLiveWapaper>
            id = intent.getIntExtra(POS_LIVE_VIDEO, 0)

            // lấy path url thong qua dl vua truyen ve
            pathVideo = list[id].original

            // insert data to mywallpaper
            insertDataToDatabase(pathVideo)

            setVideo(pathVideo)
            img_close.setOnClickListener {
                finish()
            }
            img_left_arrow.setOnClickListener {
                if (id == 0) {
                    id = list.size - 1
                    setVideo(list[id].original)
                    pathVideo = list[id].original
                    insertDataToDatabase(pathVideo)
                } else {
                    id--
                    setVideo(list[id].original)
                    pathVideo = list[id].original
                    insertDataToDatabase(pathVideo)
                }

            }
            img_right_arrow.setOnClickListener {
                if (id == list.size - 1) {
                    id = 0
                    setVideo(list[id].original)
                    pathVideo = list[id].original
                    insertDataToDatabase(pathVideo)
                } else {
                    id++
                    setVideo(list[id].original)
                    pathVideo = list[id].original
                    insertDataToDatabase(pathVideo)
                }

            }
            img_save_btn.setOnClickListener {
                // downloadVideo
                setDiloagVideo(pathVideo)


            }
            img_share_btn.setOnClickListener {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post")
                share.putExtra(Intent.EXTRA_TEXT, pathVideo)
                startActivity(Intent.createChooser(share, "Share link!"))
            }

    }

    // Xử lý add Room_database
    private fun insertDataToDatabase(pathVideo: String) {
          if(inputCheck(pathVideo)&& !iDao.isExistWall(pathVideo)){
                  val myWallpaper = MyWallPaper(pathVideo)
                  mMyWallpaperViewModel.addMyWallPaper(myWallpaper)
              }

    }
    private fun inputCheck(url:String):Boolean{

        return !(TextUtils.isEmpty(url))
    }

    // xu ly download Video
    private fun setDiloagVideo(pathVideo: String) {
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
                            dowloadVideo(pathVideo)
                        } else {
                            Toast.makeText(
                                this@LiveVideoActivity,
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
            setDialogWallPaper()
        }
        builder.show()
    }


    private fun dowloadVideo(pathVideo: String) {
        var pd = ProgressDialog(this)
        pd.setMessage("Downloading....")
        pd.setCancelable(false)
        pd.show()
        var file: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        PRDownloader.download(pathVideo, file.path, URLUtil.guessFileName(pathVideo, null, null))
            .build()
            .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnCancelListener { }
            .setOnProgressListener { progress ->
                var per = progress!!.currentBytes * 100 / progress.totalBytes
                pd.setMessage("Downloading : $per %")
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    pd.dismiss()
                    Toast.makeText(
                        this@LiveVideoActivity,
                        "Downloading Completed",
                        Toast.LENGTH_SHORT
                    ).show()
                    setDialogWallPaper()
                }

                override fun onError(error: com.downloader.Error?) {
                    pd.dismiss()
                    Toast.makeText(this@LiveVideoActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                fun onError(error: Error?) {}
            })

    }

    // set dialog wallpaper
    private fun setDialogWallPaper(){

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Wallpaper")
        builder.setMessage("Do you wan set this video to wallpaper ? ")
        builder.setPositiveButton("Yes"){_:DialogInterface,_:Int->
            // clear cache wallpaper
            var wallpaperManager: WallpaperManager =
                WallpaperManager.getInstance(applicationContext)
            try {
                wallpaperManager.clear()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //  truyen url from activity to wallpaper_service
            var serviceIntent: Intent =
                Intent(applicationContext, MyWallpaperService::class.java)
            serviceIntent.putExtra("url_pass", pathVideo)
            this.startService(serviceIntent)
            // setTo Wallpaper
            startActivity(prepareLiveWallpaperIntent(false))
            onBackPressed()
        }
        builder.setNegativeButton("No"){_:DialogInterface,_:Int->
            builder.setCancelable(true)
        }
        builder.show()


    }

    // display video to background
    private fun setVideo(path: String) {

        val progressBar : ProgressDialog
        val uri: Uri = Uri.parse(path)
        val fileName = File(uri.path).name

        if (fileName.contains(".bin")) {
            Toast.makeText(this, R.string.warning_type_file, Toast.LENGTH_SHORT).show()
        } else {
            if (fileName.contains(".mp4") || (fileName.contains(".3gp")) || (fileName.contains(".m4v"))) {
                videoView.visibility = View.VISIBLE
                videoView.setMediaController(MediaController(this))
                videoView.setVideoURI(uri)
                videoView.start()
                img_gif.visibility = View.INVISIBLE

                progressBar = ProgressDialog.show(this,"Please wait...","Loading data",true)
                videoView.setOnPreparedListener {
                    progressBar.dismiss()
                    it.apply {
                        isLooping = true }
                }
            } else {
                videoView.visibility = View.INVISIBLE
                img_gif.visibility = View.VISIBLE
                Glide.with(this).load(uri).into(img_gif)
            }
        }


    }


}