package com.daotrung.wallpapers_2021

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.adapter.ListTrendingAdapter
import com.daotrung.wallpapers_2021.adapter.ListWallpaperCategoriesAdapter
import com.daotrung.wallpapers_2021.model.MaterialWallpaperCatList
import com.daotrung.wallpapers_2021.model.MaterialWapaper
import com.daotrung.wallpapers_2021.service.ApiInterface
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class WallpaperListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    private lateinit var myToolbar: Toolbar
    private lateinit var mTitle: TextView
    private var flag: Boolean = false
    private var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_trending)

        recyclerView = findViewById(R.id.rv_list_trending)
        val intent = intent

        // nhan dl tu cac man hinh chinh
        // tra ve mot list anh
        if (intent.getIntExtra("id_trend", 0) == 0 && intent.getStringExtra("title_trend")
                .equals("Featured")
        )
            getAllDataFeaturedTrending(recyclerView)
        if (intent.getIntExtra("id_trend", 0) == 1 && intent.getStringExtra("title_trend")
                .equals("Popular")
        )
            getAllDataPopularTrending(recyclerView)
        if (intent.getIntExtra("id_trend", 0) == 2 && intent.getStringExtra("title_trend")
                .equals("New")
        )
            getAllDataNewTrending(recyclerView)
        if (intent.getIntExtra(
                "id_categories",
                0
            ) != 0 && intent.getStringExtra("name_title_categories") != ""
        ) {
            id = intent.getIntExtra("id_categories", 0)
            getAllDataCategoriesListWallpaper(id, recyclerView)
        }
        if (intent.getIntExtra("id_color", 0) != 0 && intent.getStringExtra("name_color") != "") {
            id = intent.getIntExtra("id_color", 0)
            getAllSliderListColor(id, recyclerView)
        }
        manager = GridLayoutManager(this, 3)

        setToolbar()

    }

    private fun getAllDataCategoriesListWallpaper(mId: Int, rv: RecyclerView) {
        ApiInterface.Api2.retrofitService2.getAllDataCategoriesListWallpaper(mId)
            .enqueue(object : Callback<MaterialWallpaperCatList> {
                override fun onResponse(
                    call: Call<MaterialWallpaperCatList>,
                    response: Response<MaterialWallpaperCatList>
                ) {
                    if (response.isSuccessful) {
                        recyclerView = rv.apply {
                            myAdapter = ListWallpaperCategoriesAdapter(response.body()!!)
                            layoutManager = manager
                            adapter = myAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<MaterialWallpaperCatList>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun getAllDataNewTrending(rv: RecyclerView) {
        ApiInterface.Api2.retrofitService2.getAllDataNewTrending()
            .enqueue(object : Callback<MaterialWapaper> {

                override fun onResponse(
                    call: Call<MaterialWapaper>,
                    response: Response<MaterialWapaper>
                ) {
                    if (response.isSuccessful) {
//                    Log.e("lisst",Gson().toJson(response.body()))
                        recyclerView = rv.apply {
                            myAdapter = ListTrendingAdapter(response.body()!!)
                            layoutManager = manager
                            adapter = myAdapter
                        }

                    }
                }

                override fun onFailure(call: Call<MaterialWapaper>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun getAllDataPopularTrending(rv: RecyclerView) {
        ApiInterface.Api2.retrofitService2.getAllDataPopularTrending()
            .enqueue(object : Callback<MaterialWapaper> {
                override fun onResponse(
                    call: Call<MaterialWapaper>,
                    response: Response<MaterialWapaper>
                ) {
                    if (response.isSuccessful) {
                        recyclerView = rv.apply {
                            myAdapter = ListTrendingAdapter(response.body()!!)
                            layoutManager = manager
                            adapter = myAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<MaterialWapaper>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun getAllDataFeaturedTrending(rv: RecyclerView) {
        ApiInterface.Api2.retrofitService2.getAllDataFeaturedTrending()
            .enqueue(object : Callback<MaterialWapaper> {
                override fun onResponse(
                    call: Call<MaterialWapaper>,
                    response: Response<MaterialWapaper>
                ) {
                    if (response.isSuccessful) {
                        recyclerView = rv.apply {
                            myAdapter = ListTrendingAdapter(response.body()!!)
                            layoutManager = manager
                            adapter = myAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<MaterialWapaper>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    private fun getAllSliderListColor(mId: Int, rv: RecyclerView) {
        ApiInterface.Api2.retrofitService2.getAllSliderListColor(mId)
            .enqueue(object : Callback<MaterialWallpaperCatList> {
                override fun onResponse(
                    call: Call<MaterialWallpaperCatList>,
                    response: Response<MaterialWallpaperCatList>
                ) {
                    if (response.isSuccessful) {
                        recyclerView = rv.apply {
                            myAdapter = ListWallpaperCategoriesAdapter(response.body()!!)
                            layoutManager = manager
                            adapter = myAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<MaterialWallpaperCatList>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    @SuppressLint("ResourceAsColor")
    private fun setToolbar() {
        myToolbar = findViewById(R.id.toolbar_trending_list)
        mTitle = myToolbar.findViewById(R.id.txt_title_toolbar_trending)
        if (intent.getIntExtra("id_trend", 0) == 0 && intent.getStringExtra("title_trend")
                .equals("Featured")
        )
            mTitle.text = intent.getStringExtra("title_trend")
        setSupportActionBar(myToolbar)
        if (intent.getIntExtra("id_trend", 0) == 1 && intent.getStringExtra("title_trend")
                .equals("Popular")
        )
            mTitle.text = intent.getStringExtra("title_trend")
        setSupportActionBar(myToolbar)
        if (intent.getIntExtra("id_trend", 0) == 2 && intent.getStringExtra("title_trend")
                .equals("New")
        )
            mTitle.text = intent.getStringExtra("title_trend")
        setSupportActionBar(myToolbar)
        if (intent.getIntExtra(
                "id_categories",
                0
            ) != 0 && intent.getStringExtra("name_title_categories") != ""
        ) {
            mTitle.text = intent.getStringExtra("name_title_categories")
            setSupportActionBar(myToolbar)
        }
        if (intent.getIntExtra("id_color", 0) != 0 && intent.getStringExtra("name_color") != "") {
            mTitle.text = intent.getStringExtra("name_color")
            setSupportActionBar(myToolbar)
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_ios_forward)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myToolbar.setNavigationOnClickListener {
            finish()
        }
    }


}