package com.daotrung.wallpapers_2021

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import java.io.File
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.util.jar.Manifest

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
    private lateinit var bitmap: Bitmap
    private var imgUri:Uri? = null

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
              val drwable: Drawable = img_layout.drawable
        }
        img_save_btn.setOnClickListener {
            val dowloadTask:DowloadTask = DowloadTask(this)
            dowloadTask.execute(list.get(this.id).image)
        }


    }


    private fun setImg(id: Int) {
        img = list.get(this.id).image
        Glide.with(this).load(img).into(img_layout)
    }

    inner class DowloadTask(context: Context) : AsyncTask<String, Void, Bitmap>() {

        var progressDialog:ProgressDialog? = null
        var context: Context? = null
        init {
            this.context = context
            progressDialog = ProgressDialog(context)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog!!.setMessage("Dowloading")
            progressDialog!!.show()

        }

        override fun doInBackground(vararg params: String?): Bitmap {
            val stringURL= params[0]
            var bitmap:Bitmap?= null
            try {
                var url=URL(stringURL)
                val inputStream:InputStream = url.openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return bitmap!!
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            img_layout!!.setImageBitmap(result)
            if(progressDialog!!.isShowing){
                progressDialog?.dismiss()
            }

        }
    }
    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "images/*"
        galleryActivityResultLauncher.launch(intent)

    }
    private var galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {
            result ->
            if(result.resultCode == Activity.RESULT_OK){
               val intent = result.data
                imgUri = intent!!.data
                img_layout.setImageURI(imgUri)


            }
            else{

            }
        }
    )


}