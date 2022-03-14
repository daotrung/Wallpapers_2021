package com.daotrung.wallpapers_2021.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.MyWallpaperWallAdapter
import com.daotrung.wallpapers_2021.model.MyWallpaperWall
import com.daotrung.wallpapers_2021.service.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyWallpaperFragmentWallpaper : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_my_wallpaper_wallpaper, container, false)

        recyclerView = v.findViewById(R.id.rv_list_my_wallpaper_wall)
        manager = GridLayoutManager(v.context,3)

        getAllListMyWallpaper(recyclerView)
        return v
    }

    private fun getAllListMyWallpaper(rv: RecyclerView?) {
          ApiInterface.Api.retrofitService.getAllListMyWallpaper().enqueue(object : Callback<MyWallpaperWall>{
              override fun onResponse(
                  call: Call<MyWallpaperWall>,
                  response: Response<MyWallpaperWall>
              ) {
                  if(response.isSuccessful){
                      recyclerView = rv?.apply {
                          myAdapter = MyWallpaperWallAdapter(response.body()!!)
                          layoutManager = manager
                          adapter = myAdapter
                      }!!
                  }
              }

              override fun onFailure(call: Call<MyWallpaperWall>, t: Throwable) {
                  t.printStackTrace()
              }

          })
    }

}