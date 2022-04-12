package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.daotrung.wallpapers_2021.MyWallSliderActivity
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.room.IDao
import com.daotrung.wallpapers_2021.room.MyFavoritePicture
import com.daotrung.wallpapers_2021.room.MyWallPaperDatabase

const val ID_MY_WALL_LIST : String = "id_picture"
class MyWallpaperWallAdapter() :
    RecyclerView.Adapter<MyWallpaperWallAdapter.MyViewHolder>() {

    private var myPicList = mutableListOf<MyFavoritePicture>()
    private lateinit var dao : IDao
    private lateinit var database: MyWallPaperDatabase
    inner class MyViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.dong_list_trending, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

         val currenItem = myPicList[position]
         Glide.with(holder.itemView.context).load(currenItem.myUrlHeart).centerCrop()
             .into(holder.itemView.findViewById(R.id.img_list_trending))

        val imgView = holder.itemView.findViewById<ImageView>(R.id.img_list_trending)
        val imgIcoN = holder.itemView.findViewById<ImageView>(R.id.icon_heart)
        imgView.setOnClickListener {
            val intent = Intent(holder.itemView.context,MyWallSliderActivity::class.java)

            intent.putExtra(ID_MY_WALL_LIST,position)

            holder.itemView.context.startActivity(intent)
        }
        imgIcoN.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context,R.drawable.heart_select_max))

        database = Room.databaseBuilder(
            holder.itemView.context,
            MyWallPaperDatabase::class.java,
            "mywall_database"
        ).allowMainThreadQueries().build()

        dao = database.getMyWallDao()
    }

    override fun getItemCount(): Int {
        return myPicList.size

    }
    fun setData(mywall: List<MyFavoritePicture>){

        this.myPicList = mywall as MutableList<MyFavoritePicture>
        notifyDataSetChanged()


    }
    fun updatePos(pos : Int){
        myPicList.remove(myPicList[pos])
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos,myPicList.size)

    }



}