package com.daotrung.wallpapers_2021.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.MyWallpaperWallAdapter
import com.daotrung.wallpapers_2021.model.MyWallpaperWall
import com.daotrung.wallpapers_2021.room.MyFavoriteModel
import com.daotrung.wallpapers_2021.room.MyPicViewModel
import com.daotrung.wallpapers_2021.service.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyWallpaperFragmentWallpaper : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myFavoriteModel: MyFavoriteModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_my_wallpaper_wallpaper, container, false)

        val myWallpaperWallAdapter = MyWallpaperWallAdapter()


        recyclerView = v.findViewById(R.id.rv_list_my_wallpaper_wall)
        manager = GridLayoutManager(v.context, 3)

        recyclerView.adapter = myWallpaperWallAdapter
        recyclerView.layoutManager = manager

        myFavoriteModel = ViewModelProvider(this).get(MyFavoriteModel::class.java)
        myFavoriteModel.allPicFavorite.observe(viewLifecycleOwner, Observer {
            mywall->
            myWallpaperWallAdapter.setData(mywall)
        })
        return v
    }




}