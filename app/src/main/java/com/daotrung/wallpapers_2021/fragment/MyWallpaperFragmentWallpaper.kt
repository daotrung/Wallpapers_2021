package com.daotrung.wallpapers_2021.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.KEY_DB
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.MyWallpaperWallAdapter
import com.daotrung.wallpapers_2021.room.MyFavoriteModel


class MyWallpaperFragmentWallpaper : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myFavoriteModel: MyFavoriteModel

    private lateinit var mRefreshReceiver: BroadcastReceiver
    private lateinit var filter: IntentFilter
    private var posChange: Int = 0

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

        myFavoriteModel = ViewModelProvider(this)[MyFavoriteModel::class.java]
        myFavoriteModel.allPicFavorite.observe(viewLifecycleOwner, Observer { mywall ->
            myWallpaperWallAdapter.setData(mywall)
            setBroadCast(myWallpaperWallAdapter)
        })
        return v

    }

    private fun setBroadCast(myWallpaperWallAdapter: MyWallpaperWallAdapter) {
        filter = IntentFilter()
        filter.addAction("localBroadCastDB")
        mRefreshReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    if (intent.action.equals("localBroadCastDB")) {
                        posChange = intent.getIntExtra(KEY_DB, -1)
                        Log.e("posDB__", posChange.toString())
                        (myWallpaperWallAdapter as MyWallpaperWallAdapter).updatePos(posChange)
                    }
                }
            }
        }

        view?.let {
            LocalBroadcastManager.getInstance(it.context).registerReceiver(mRefreshReceiver, filter)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        view?.let {
            LocalBroadcastManager.getInstance(it.context).unregisterReceiver(mRefreshReceiver)
        }
    }
}