package com.daotrung.wallpapers_2021.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.WallpaperListActivity
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.model.TrenMain

const val TITLE_TREND: String = "title_trend"
const val ID_TREND: String = "id_trend"

class WallpaperListTrendingMainAdapter(private val data: List<TrenMain>) :
    RecyclerView.Adapter<WallpaperListTrendingMainAdapter.MyViewHolder>() {


    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(trendMain: TrenMain) {
            val imgView = view.findViewById<ImageView>(R.id.img_trending)
            val txt = view.findViewById<TextView>(R.id.txt_trending)
            txt.text = trendMain.name

            imgView.setImageResource(trendMain.img)

            imgView.setOnClickListener {
                val intent = Intent(view.context, WallpaperListActivity::class.java)
                intent.putExtra(TITLE_TREND, trendMain.name)
                intent.putExtra(ID_TREND, layoutPosition)

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

