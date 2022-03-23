package com.daotrung.wallpapers_2021

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
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
import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import android.webkit.URLUtil
import com.downloader.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException
import java.lang.Error


private var list : ArrayList<SlideLiveWapaper> = ArrayList()
private var id : Int = 0
private lateinit var pathVideo : String
@SuppressLint("StaticFieldLeak")
private lateinit var videoView: VideoView
@SuppressLint("StaticFieldLeak")
private lateinit var img_close : ImageView
@SuppressLint("StaticFieldLeak")
private lateinit var img_left_arrow : ImageView
@SuppressLint("StaticFieldLeak")
private lateinit var img_right_arrow : ImageView
@SuppressLint("StaticFieldLeak")
private lateinit var img_save_btn : ImageView
@SuppressLint("StaticFieldLeak")
private lateinit var img_share_btn : ImageView
@SuppressLint("StaticFieldLeak")
private lateinit var img_gif : ImageView

class LiveVideoActivity : AppCompatActivity(){

    // set Intent by MyWallpaperService
    companion object {
        @JvmStatic
        fun prepareLiveWallpaperIntent(showAllLiveWallpapers: Boolean): Intent {
            val liveWallpaperIntent = Intent()
            if (showAllLiveWallpapers || Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                liveWallpaperIntent.action = WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER
            } else {
                liveWallpaperIntent.action = WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                val p = MyWallpaperService::class.java.`package`.name
                val c = MyWallpaperService::class.java.canonicalName
                liveWallpaperIntent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, ComponentName(p, c))
            }
            return liveWallpaperIntent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
        list = intent.getSerializableExtra("list_img_live") as ArrayList<SlideLiveWapaper>
        id = intent.getIntExtra("pos_img_live",0)

        // lấy path url thong qua dl vua truyen ve
        pathVideo = list[id].original

        setVideo(pathVideo)
        img_close.setOnClickListener {
            finish()
        }
        img_left_arrow.setOnClickListener {
            if(id==0)
            {
                id = list.size-1
                setVideo(list[id].original)
                pathVideo = list[id].original
            }else
            {
                id--
                setVideo(list[id].original)
                pathVideo = list[id].original
            }

        }
        img_right_arrow.setOnClickListener {
            if(id== list.size-1)
            {
                id=0
                setVideo(list[id].original)
                pathVideo = list[id].original
            }else
            {
                id++
                setVideo(list[id].original)
                pathVideo = list[id].original
            }

        }
        img_save_btn.setOnClickListener {
//              setDiloagVideo(pathVideo)
//            Log.e("path", pathVideo)

            // clear cache wallpaper
            var wallpaperManager:WallpaperManager = WallpaperManager.getInstance(applicationContext)
            try {
                wallpaperManager.clear()
            }catch (e : IOException){
                e.printStackTrace()
            }
            //  truyen url from activity to wallpaper_service
            var serviceIntent : Intent = Intent(applicationContext,MyWallpaperService::class.java)
            serviceIntent.putExtra("url_pass", pathVideo)
            Log.e("url_pass:", pathVideo)
            this.startService(serviceIntent)

            // setTo Wallpaper
            startActivity(prepareLiveWallpaperIntent(false))
            finish()
        }
    }

    private fun setDiloagVideo(pathVideo: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Save Video")
        builder.setMessage("Do you want save this video ?")
        builder.setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.SET_WALLPAPER
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        /* ... */ if(report.areAllPermissionsGranted()){
                            dowloadVideo(pathVideo)
                        }else {
                            Toast.makeText(this@LiveVideoActivity,"Please allow all permission",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) { /* ... */
                    }
                }).check()


        }
        builder.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->
            finish()

        }
        builder.show()
    }

    // xu ly download Video
    private fun dowloadVideo(pathVideo: String) {
        var pd = ProgressDialog(this)
        pd.setMessage("Dowloading....")
        pd.setCancelable(false)
        pd.show()
        var file: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        PRDownloader.download(pathVideo, file.path, URLUtil.guessFileName(pathVideo, null, null))
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
                        this@LiveVideoActivity,
                        "Dowloading Completed",
                        Toast.LENGTH_SHORT
                    )
                }

                override fun onError(error: com.downloader.Error?) {
                    pd.dismiss()
                    Toast.makeText(this@LiveVideoActivity, "Error", Toast.LENGTH_SHORT)
                }

                fun onError(error: Error?) {}
            })

    }

//    private fun saveVideo(pathVideo:String) {
//        var manager : DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        val uri = Uri.parse(pathVideo)
//        val request = DownloadManager.Request(uri)
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI )
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
//        request.setAllowedOverRoaming(false)
//
//        val reference: Long = manager.enqueue(request)
//    }

    private fun setVideo(path:String) {
        val uri : Uri = Uri.parse(path)
        val fileName = File(uri.path).name

        Log.e("name",fileName)
        if(fileName.contains(".bin")) {
            Toast.makeText(this,"Không hỗ trợ dạng tệp này trong trình phát ",Toast.LENGTH_SHORT).show()
        }else {
            if (fileName.contains(".mp4") || (fileName.contains(".3gp")) || (fileName.contains(".m4v"))) {
                videoView.visibility = View.VISIBLE
                videoView.setVideoURI(uri)
                videoView.start()
                img_gif.visibility = View.INVISIBLE
                videoView.setOnPreparedListener {
                    it.apply { isLooping = true }
                }
            } else {
                videoView.visibility = View.INVISIBLE
                img_gif.visibility = View.VISIBLE
                Glide.with(this).load(uri).into(img_gif)
            }
        }


    }
}