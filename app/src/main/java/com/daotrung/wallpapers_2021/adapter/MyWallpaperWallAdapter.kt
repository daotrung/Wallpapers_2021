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
import com.daotrung.wallpapers_2021.MyWallSliderActivity
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.SliderWallpaperActivity
import com.daotrung.wallpapers_2021.model.MyWallpaperWall
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import com.daotrung.wallpapers_2021.room.MyPicturePaper
import com.daotrung.wallpapers_2021.room.MyWallPaper

class MyWallpaperWallAdapter() :
    RecyclerView.Adapter<MyWallpaperWallAdapter.MyViewHolder>() {

    private var myPicList = emptyList<MyPicturePaper>()
    inner class MyViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.dong_list_trending, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val currenItem = myPicList[position]
         Glide.with(holder.itemView.context).load(currenItem.myPicUrl).centerCrop()
             .into(holder.itemView.findViewById(R.id.img_list_trending))

        val imgView = holder.itemView.findViewById<ImageView>(R.id.img_list_trending)
        imgView.setOnClickListener {
            val intent = Intent(holder.itemView.context,MyWallSliderActivity::class.java)

            intent.putExtra("id_picture",position)
            Log.e("id_get",position.toString())

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return myPicList.size

    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(mywall:List<MyPicturePaper>){
        this.myPicList = mywall
        notifyDataSetChanged()
    }

}