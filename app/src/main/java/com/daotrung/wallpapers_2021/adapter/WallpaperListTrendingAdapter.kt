package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.SliderWallpaperActivity
import com.daotrung.wallpapers_2021.model.MaterialWapaper
import com.daotrung.wallpapers_2021.model.Trending
import com.daotrung.wallpapers_2021.room.IDao
import com.daotrung.wallpapers_2021.room.MyWallPaperDatabase
import java.io.Serializable
import kotlin.math.log

const val LIST_IMG_TREND : String = "list_img_trend"
const val POST_IMG_TREND : String = "pos_img_trend"
class ListTrendingAdapter(private val data: MaterialWapaper) :
    RecyclerView.Adapter<ListTrendingAdapter.MyViewHolder>() {

    private lateinit var database: MyWallPaperDatabase
    private lateinit var dao: IDao

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(materialWapaper: Trending) {

            // khởi tạo database , dao , viewModel
            database = Room.databaseBuilder(
                itemView.context,
                MyWallPaperDatabase::class.java,
                "mywall_database"
            ).allowMainThreadQueries().build()

            dao = database.getMyWallDao()

            val imgView = view.findViewById<ImageView>(R.id.img_list_trending)
            val imgIcon = view.findViewById<ImageView>(R.id.icon_heart)


            Glide.with(view.context).load(get_url_img_thumb + materialWapaper.image).centerCrop()
                .into(imgView)

            imgView.setOnClickListener {
                val intent = Intent(view.context, SliderWallpaperActivity::class.java)

                intent.putExtra(LIST_IMG_TREND, data as Serializable)
                intent.putExtra(POST_IMG_TREND, layoutPosition + 1)
//                Log.e("id=", layoutPosition.toString())
                view.context.startActivity(intent)
            }

            if(dao.isExistFavor(get_url_img_thumb+materialWapaper.image)){
                imgIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.heart_select_max))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.dong_list_trending, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data.MaterialWallpaper[position])
    }

    override fun getItemCount(): Int {
        return data.MaterialWallpaper.size
    }
    fun updateDataItem(pos:Int){

        notifyItemChanged(pos)
    }
}