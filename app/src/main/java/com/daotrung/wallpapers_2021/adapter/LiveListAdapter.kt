package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.SliderLiveActivity
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import java.io.Serializable

class LiveListAdapter (private val data: List<SlideLiveWapaper>):
    RecyclerView.Adapter<LiveListAdapter.MyViewHolder>() {

    private lateinit var intent: Intent
    private var img : String? = null
    inner class MyViewHolder(val view:View):RecyclerView.ViewHolder(view){
        fun bind(slideLiveWapaper: SlideLiveWapaper){
            var imgView = view.findViewById<ImageView>(R.id.img_list_live_wapper)
            var imgIcon = view.findViewById<ImageView>(R.id.icon_wifi)
            Glide.with(view.context).load(slideLiveWapaper.image).centerCrop().into(imgView)

            img = slideLiveWapaper.image
            imgView.setOnClickListener {
                val intent= Intent(view.context,SliderLiveActivity::class.java)
                intent.putExtra("list_img_live",data as Serializable)
                intent.putExtra("pos_img_live",layoutPosition)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dong_list_live_wapper,parent,false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}