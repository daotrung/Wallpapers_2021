package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.LiveVideoActivity
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import java.io.Serializable

const val LIST_LIVE_VIDEO: String = "list_img_live"
const val POS_LIVE_VIDEO: String = "pos_img_live"

class LiveListAdapter(private val data: List<SlideLiveWapaper>) :
    RecyclerView.Adapter<LiveListAdapter.MyViewHolder>() {

    private var img: String? = null

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(slideLiveWallpaper: SlideLiveWapaper) {
            val imgView = view.findViewById<ImageView>(R.id.img_list_live_wapper)
            var imgIcon = view.findViewById<ImageView>(R.id.icon_wifi)
            if (slideLiveWallpaper.original.contains(".mp4") || slideLiveWallpaper.original.contains(".m4v")) {
                Glide.with(view.context).load(slideLiveWallpaper.thumbnail).centerCrop().into(imgView)
                img = slideLiveWallpaper.thumbnail
                imgView.setOnClickListener {

                    val intent = Intent(view.context, LiveVideoActivity::class.java)
                    intent.putExtra(LIST_LIVE_VIDEO, data as Serializable)
                    intent.putExtra(POS_LIVE_VIDEO, layoutPosition)
                    view.context.startActivity(intent)
                }
            } else {
                Glide.with(view.context).load(slideLiveWallpaper.thumbnail).centerCrop().into(imgView)
                img = slideLiveWallpaper.thumbnail
                imgView.setOnClickListener {

                    val intent = Intent(view.context, LiveVideoActivity::class.java)
                    intent.putExtra("list_img_live", data as Serializable)
                    intent.putExtra("pos_img_live", 0)
                    view.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.dong_list_live_wapper, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}