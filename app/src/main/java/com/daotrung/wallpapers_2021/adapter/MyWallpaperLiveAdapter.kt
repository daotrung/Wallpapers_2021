package com.daotrung.wallpapers_2021.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.LiveVideoActivity
import com.daotrung.wallpapers_2021.MyLiveSliderActivity
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.room.MyWallPaper
import java.io.Serializable

class MyWallpaperLiveAdapter : RecyclerView.Adapter<MyWallpaperLiveAdapter.MyViewHolder>(){
    private var myWallpaperList = emptyList<MyWallPaper>()
    class MyViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dong_list_live_wapper,parent,false))

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = myWallpaperList[position]
        Glide.with(holder.itemView.context).load(currentItem.myUrl).centerCrop()
            .into(holder.itemView.findViewById(R.id.img_list_live_wapper))
        val imgView = holder.itemView.findViewById<ImageView>(R.id.img_list_live_wapper)
        imgView.setOnClickListener {
            val intent = Intent(holder.itemView.context,MyLiveSliderActivity::class.java)
            intent.putExtra("myUrl",currentItem.myUrl)
            intent.putExtra("ID",position)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  myWallpaperList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(mywall:List<MyWallPaper>){
        this.myWallpaperList = mywall
        notifyDataSetChanged()
    }

}