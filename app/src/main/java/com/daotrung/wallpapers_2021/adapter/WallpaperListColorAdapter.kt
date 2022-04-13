package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
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
import com.daotrung.wallpapers_2021.model.CatList
import com.daotrung.wallpapers_2021.model.MaterialWallpaperCatList
import com.daotrung.wallpapers_2021.room.IDao
import com.daotrung.wallpapers_2021.room.MyWallPaperDatabase
import java.io.Serializable

const val LIST_IMG_COLOR: String = "list_img_color"
const val POS_IMG_COLOR: String = "pos_img_color"

class ListWallpaperColorAdapter(private val data: MaterialWallpaperCatList) :
    RecyclerView.Adapter<ListWallpaperColorAdapter.MyViewHolder>() {

    private lateinit var database: MyWallPaperDatabase
    private lateinit var dao: IDao

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(materialWallpaperCatList: CatList) {

            // khởi tạo database , dao , viewModel
            database = Room.databaseBuilder(
                itemView.context,
                MyWallPaperDatabase::class.java,
                "mywall_database"
            ).allowMainThreadQueries().build()

            dao = database.getMyWallDao()

            val imgView = view.findViewById<ImageView>(R.id.img_list_trending)
            val imgIcon = view.findViewById<ImageView>(R.id.icon_heart)
            Glide.with(view.context).load(get_url_img_thumb + materialWallpaperCatList.images)
                .centerCrop()
                .into(imgView)

            imgView.setOnClickListener {
                val intent = Intent(view.context, SliderWallpaperActivity::class.java)
                intent.putExtra(LIST_IMG_COLOR, data as Serializable)
                intent.putExtra(POS_IMG_COLOR, layoutPosition + 1)
                view.context.startActivity(intent)
            }

            if (dao.isExistFavor(get_url_img_thumb + materialWallpaperCatList.images)) {
                imgIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.heart_select_max
                    )
                )
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

}