package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.LiveListActivity
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.model.WallPaper

class LiveMainAdapter(private val data: List<WallPaper>) :
    RecyclerView.Adapter<LiveMainAdapter.MyViewHolder>() {

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(wallpaper: WallPaper) {
            val title = view.findViewById<TextView>(R.id.txt_title_wapaper)
            val imageView = view.findViewById<ImageView>(R.id.img_live_wapaper)

            title.text = wallpaper.title
            Glide.with(view.context).load(wallpaper.image).centerCrop().into(imageView)

            imageView.setOnClickListener {
                val intent = Intent(view.context, LiveListActivity::class.java)
                intent.putExtra("idGet", wallpaper.id)
                intent.putExtra("title", wallpaper.title)
                view.context.startActivity(intent)
            }

        }
//        init {
//            view.setOnClickListener {
//                val position:Int = adapterPosition
//                val intent =  Intent(view.context,ActivityListLiveWapaper::class.java)
//                intent.putExtra("idGet",idGetList)
//                intent.putExtra("title",tile_m)
//                view.context.startActivity(intent)
//            }
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.dong_categories_live_wapaper, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}