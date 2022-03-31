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
import com.daotrung.wallpapers_2021.model.ColorGet
import com.daotrung.wallpapers_2021.model.ColorMain
import com.daotrung.wallpapers_2021.model.MaterialWallpaperColorMain
import com.daotrung.wallpapers_2021.service.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WallpaperListColorMainAdapter(private val data: List<ColorMain>) :
    RecyclerView.Adapter<WallpaperListColorMainAdapter.MyViewHolder>() {
    private var size: Int = 0
    var arrayList: ArrayList<ColorGet> = ArrayList()

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(materialWallpaperColorMain: ColorMain) {
            var imgView = view.findViewById<ImageView>(R.id.color_main)
            var txtColor = view.findViewById<TextView>(R.id.txt_name_color)
            imgView.setImageResource(materialWallpaperColorMain.category_image)
            txtColor.text = materialWallpaperColorMain.category_name

            // goi api de lay dl list color
            ApiInterface.Api2.retrofitService2.getColorModel().enqueue(object :
                Callback<MaterialWallpaperColorMain> {
                override fun onResponse(
                    call: Call<MaterialWallpaperColorMain>,
                    response: Response<MaterialWallpaperColorMain>
                ) {

                    if (response.isSuccessful) {
                        size = response.body()?.MaterialWallpaper?.size!!
                        for (index in 0 until size) {
                            arrayList.add(
                                ColorGet(
                                    response.body()!!.MaterialWallpaper[index].cid.toInt(),
                                    response.body()!!.MaterialWallpaper[index].category_name
                                )
                            )
                        }
                    }
                    // truyen id qua click moi imgview den man hinh hien thi
                    imgView.setOnClickListener {
                        val intent = Intent(view.context, WallpaperListActivity::class.java)
                        intent.putExtra("name_color", arrayList[layoutPosition].name)
                        intent.putExtra("id_color", arrayList[layoutPosition].id)
                        view.context.startActivity(intent)
                    }

                }

                override fun onFailure(call: Call<MaterialWallpaperColorMain>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dong_fragment_color, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


}