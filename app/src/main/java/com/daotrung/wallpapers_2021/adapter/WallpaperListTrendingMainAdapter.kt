package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.ActivityListWallpaper
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.model.TrenMain

class WallpaperListTrendingMainAdapter(private val data: List<TrenMain>) :
    RecyclerView.Adapter<WallpaperListTrendingMainAdapter.MyViewHolder>() {

    private lateinit var textView: String

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(trenMain: TrenMain) {
            var imgView = view.findViewById<ImageView>(R.id.img_trending)
            var txt = view.findViewById<TextView>(R.id.txt_trending)
            txt.text = trenMain.name

            imgView.setImageResource(trenMain.img)

            imgView.setOnClickListener {
                val intent = Intent(view.context, ActivityListWallpaper::class.java)
//                  Log.e("ggg",trenMain.name)
                intent.putExtra("title_trend", trenMain.name)
                intent.putExtra("id_trend", layoutPosition)
//                  Log.e("id = ",layoutPosition.toString())
                view.context.startActivity(intent)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dong_trending, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

