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
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.SliderWallpaperActivity
import com.daotrung.wallpapers_2021.model.CatList
import com.daotrung.wallpapers_2021.model.MaterialWallpaperCatList
import com.daotrung.wallpapers_2021.room.IDao
import com.daotrung.wallpapers_2021.room.MyWallPaperDatabase
import java.io.Serializable

const val get_url_img_thumb = "http://hdwalls.wallzapps.com/upload/"

const val LIST_IMG_CATEGORIES: String = "list_img_categories"
const val POST_IMG_CATEGORIES: String = "pos_img_categories"

class ListWallpaperCategoriesAdapter(private val data: MaterialWallpaperCatList) :
    RecyclerView.Adapter<ListWallpaperCategoriesAdapter.MyViewHolder>() {

    private lateinit var database: MyWallPaperDatabase
    private lateinit var dao: IDao
    private lateinit var imgIcon: ImageView

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(materialWallpaperCatList: CatList) {


            // khởi tạo database , dao , viewModel
            database = Room.databaseBuilder(
                itemView.context,
                MyWallPaperDatabase::class.java,
                "mywall_database"
            ).allowMainThreadQueries().build()

            dao = database.getMyWallDao()


            val imageView = view.findViewById<ImageView>(R.id.img_list_trending)
            imgIcon = view.findViewById(R.id.icon_heart)
            Glide.with(view.context).load(get_url_img_thumb + materialWallpaperCatList.images)
                .centerCrop()
                .into(imageView)

            imageView.setOnClickListener {
                val intent = Intent(view.context, SliderWallpaperActivity::class.java)
                intent.putExtra(LIST_IMG_CATEGORIES, data as Serializable)
                intent.putExtra(POST_IMG_CATEGORIES, layoutPosition + 1)
                view.context.startActivity(intent)
            }
            if (dao.isExistFavor(get_url_img_thumb + materialWallpaperCatList.images)) {
                imgIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.heart_select_max
                    )
                )

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.dong_list_trending, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data.MaterialWallpaper[position])

    }

    override fun getItemCount(): Int {
        return data.MaterialWallpaper.size
    }

    fun updateDataItem(pos: Int) {
        notifyItemChanged(pos)

    }


}