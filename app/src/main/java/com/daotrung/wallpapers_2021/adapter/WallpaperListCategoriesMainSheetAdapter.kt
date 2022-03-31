package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.WallpaperListActivity
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.model.CatMain
import com.daotrung.wallpapers_2021.model.MaterialWallpaperCateMain

class WallpaperListCategoriesMainSheetAdapter(private val data: MaterialWallpaperCateMain) :
    RecyclerView.Adapter<WallpaperListCategoriesMainSheetAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(materialWallpaperCateMain: CatMain) {

            val img_main = view.findViewById<ImageView>(R.id.img_categories_main_wallpaper)
            val txt_main = view.findViewById<TextView>(R.id.txt_title_categories_main_wallpaper)

            Glide.with(view.context).load(urlImg + materialWallpaperCateMain.category_image)
                .centerCrop().into(img_main)
            txt_main.text = materialWallpaperCateMain.category_name

            img_main.setOnClickListener {
                val intent = Intent(view.context, WallpaperListActivity::class.java)
                intent.putExtra("name_title_categories", materialWallpaperCateMain.category_name)
                intent.putExtra("id_categories", materialWallpaperCateMain.cid.toInt())
//                Log.e("id",materialWallpaperCateMain.cid)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.dong_categories_wallpaper_main, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data.MaterialWallpaper[position])
    }

    override fun getItemCount(): Int {
        return data.MaterialWallpaper.size
    }


}