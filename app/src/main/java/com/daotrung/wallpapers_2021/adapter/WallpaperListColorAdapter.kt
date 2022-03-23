package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.SliderWallpaperActivity
import com.daotrung.wallpapers_2021.model.CatList
import com.daotrung.wallpapers_2021.model.MaterialWallpaperCatList
import java.io.Serializable

const val urlIm ="https://hdwalls.wallzapps.com/upload/"
class ListWallpaperColorAdapter (private val data:MaterialWallpaperCatList):
      RecyclerView.Adapter<ListWallpaperColorAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        fun bind(materialWallpaperCatList: CatList){
            val imgView = view.findViewById<ImageView>(R.id.img_list_trending)
            val imgIcon = view.findViewById<ImageView>(R.id.icon_heart)
            Glide.with(view.context).load(urlIm+materialWallpaperCatList.images).centerCrop().into(imgView)

            imgView.setOnClickListener {
                val intent = Intent(view.context,SliderWallpaperActivity::class.java)
                intent.putExtra("list_img_color",data as Serializable)
                intent.putExtra("pos_img_color",layoutPosition+1)
                view.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dong_list_trending,parent,false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data.MaterialWallpaper[position])
    }

    override fun getItemCount(): Int {
        return data.MaterialWallpaper.size
    }

}