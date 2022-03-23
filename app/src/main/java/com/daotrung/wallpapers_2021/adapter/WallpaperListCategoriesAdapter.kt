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

const val urlIma ="https://hdwalls.wallzapps.com/upload/"
class ListWallpaperCategoriesAdapter(private val data:MaterialWallpaperCatList) :
      RecyclerView.Adapter<ListWallpaperCategoriesAdapter.MyViewHolder>(){
    inner class MyViewHolder(val view:View):RecyclerView.ViewHolder(view) {
          fun bind(materialWallpaperCatList: CatList){
              val imageView = view.findViewById<ImageView>(R.id.img_list_trending)
              val imgIcon = view.findViewById<ImageView>(R.id.icon_heart)
              Glide.with(view.context).load(urlIma+materialWallpaperCatList.images).centerCrop().into(imageView)

              imageView.setOnClickListener {
                  val intent = Intent(view.context,SliderWallpaperActivity::class.java)
                  intent.putExtra("list_img_categories",data as Serializable)
                  intent.putExtra("pos_img_categories",layoutPosition+1)
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

    override fun getItemCount(): Int{
        return data.MaterialWallpaper.size
    }
}