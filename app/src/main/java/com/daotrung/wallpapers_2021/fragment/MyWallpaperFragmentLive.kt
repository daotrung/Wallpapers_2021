package com.daotrung.wallpapers_2021.fragment

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
import com.daotrung.wallpapers_2021.adapter.MyWallpaperLiveAdapter
import com.daotrung.wallpapers_2021.room.MyWallpaperViewModel


private lateinit var recyclerView: RecyclerView
private lateinit var manager: RecyclerView.LayoutManager


class MyWallpaperFragmentLive : Fragment() {
    private lateinit var myWallpaperViewModel: MyWallpaperViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_wallpaper_live, container, false)

        val myWallpaperLiveAdapter = MyWallpaperLiveAdapter()
        recyclerView = view.findViewById(R.id.rv_list_my_wallpaper_live)
        manager = GridLayoutManager(view.context, 3)
        recyclerView.adapter = myWallpaperLiveAdapter
        recyclerView.layoutManager = manager

        myWallpaperViewModel = ViewModelProvider(this)[MyWallpaperViewModel::class.java]
        myWallpaperViewModel.allWallPaper.observe(viewLifecycleOwner, Observer { mywall ->
            myWallpaperLiveAdapter.setData(mywall)

        })

        return view
    }


}