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
import com.daotrung.wallpapers_2021.model.MyWallpaperWall
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper

class MyWallpaperWallAdapter(private val data: MyWallpaperWall) :
    RecyclerView.Adapter<MyWallpaperWallAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(myWallpaperWall: SlideLiveWapaper) {
            val imgView = view.findViewById<ImageView>(R.id.img_list_trending)
            val imgIcon = view.findViewById<ImageView>(R.id.icon_heart)
            Glide.with(view.context).load(myWallpaperWall.image).centerCrop().into(imgView)

            imgView.setOnClickListener {
                val intent = Intent(view.context, SliderWallpaperActivity::class.java)
                intent.putExtra("pos_my_wallpaper", layoutPosition + 1)
                intent.putExtra("list_img_my_wallpaper", data as MyWallpaperWall)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.dong_list_trending, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data.wallpapers[position])
    }

    override fun getItemCount(): Int {
        return data.wallpapers.size
    }

}