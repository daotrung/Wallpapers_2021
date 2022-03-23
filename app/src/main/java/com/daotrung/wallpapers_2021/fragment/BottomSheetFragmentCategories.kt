package com.daotrung.wallpapers_2021.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.ListCategoriesMainAdapter
import com.daotrung.wallpapers_2021.adapter.WallpaperListCategoriesMainSheetAdapter
import com.daotrung.wallpapers_2021.model.MaterialWallpaperCateMain
import com.daotrung.wallpapers_2021.service.ApiInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetFragmentCategories : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.bottom_sheet_categories_fragment, container, false)
        recyclerView = v.findViewById(R.id.rv_list_categories)

        manager = GridLayoutManager(context, 3)
        getAllDataCategoriesMainWallpaper(recyclerView)
        return v
    }

    private fun getAllDataCategoriesMainWallpaper(rv: RecyclerView) {
        ApiInterface.Api2.retrofitService2.getAllDataCategoriesMainWallpaper().enqueue(object :
            Callback<MaterialWallpaperCateMain> {
            override fun onResponse(
                call: Call<MaterialWallpaperCateMain>,
                response: Response<MaterialWallpaperCateMain>

            ) {
                if (response.isSuccessful) {

                    recyclerView = rv.apply {
//                        Log.e("list", Gson().toJson(response.body()))
                        myAdapter = WallpaperListCategoriesMainSheetAdapter(response.body()!!)

                        layoutManager = manager

                        adapter = myAdapter
                    }
                }
            }

            override fun onFailure(call: Call<MaterialWallpaperCateMain>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }


}