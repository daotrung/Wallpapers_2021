package com.daotrung.wallpapers_2021.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.WallpaperListTrendingMainAdapter
import com.daotrung.wallpapers_2021.model.TrenMain

class WallpaperTrendingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<TrenMain>
    lateinit var imgId : Array<Int>
    lateinit var heading : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trending,container,false)
        recyclerView = view.findViewById(R.id.rv_trending)

        imgId = arrayOf(
            R.drawable.btn_trend_1,R.drawable.btn_trend_2,R.drawable.btn_trend_3)
        heading = arrayOf(
            "Featured","Popular","New")

        recyclerView.layoutManager = GridLayoutManager(context,3)

        recyclerView.setHasFixedSize(true)

        arrayList = arrayListOf<TrenMain>()

        getData()

        return view

    }

    private fun getData() {
        for(i in imgId.indices){
            val trends = TrenMain(heading[i],imgId[i])
            arrayList.add(trends)
        }
        recyclerView.adapter = WallpaperListTrendingMainAdapter(arrayList)

    }



}