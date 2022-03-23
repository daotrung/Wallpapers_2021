package com.daotrung.wallpapers_2021.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.LiveListAdapter
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import com.daotrung.wallpapers_2021.service.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyWallpaperFragmentLive : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_wallpaper_live, container, false)

        recyclerView = view.findViewById(R.id.rv_list_my_wallpaper_live)
        manager = GridLayoutManager(view.context, 3)
        getAllListMyLive(recyclerView)

        return view
    }

    private fun getAllListMyLive(rv: RecyclerView) {
        ApiInterface.Api.retrofitService.getAllListMyLive()
            .enqueue(object : Callback<List<SlideLiveWapaper>> {
                override fun onResponse(
                    call: Call<List<SlideLiveWapaper>>,
                    response: Response<List<SlideLiveWapaper>>
                ) {
                    if (response.isSuccessful) {
                        recyclerView = rv.apply {
                            myAdapter = LiveListAdapter(response.body()!!)
                            layoutManager = manager
                            adapter = myAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<List<SlideLiveWapaper>>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }


}