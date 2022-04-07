package com.daotrung.wallpapers_2021

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.adapter.*
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

    private lateinit var mRefreshReceiver : BroadcastReceiver
    private lateinit var filter: IntentFilter
    private var posChange : Int = 0
    private var flag: Boolean = false
    private var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_trending)

        recyclerView = findViewById(R.id.rv_list_trending)
        val intent = intent

        // nhan dl tu cac man hinh chinh
        // tra ve mot list anh

        if (intent.getIntExtra(ID_TREND, 0) == 0 && intent.getStringExtra(TITLE_TREND)
                .equals("Featured")
        )
            getAllDataFeaturedTrending(recyclerView)
        if (intent.getIntExtra(ID_TREND, 0) == 1 && intent.getStringExtra(TITLE_TREND)
                .equals("Popular")
        )
            getAllDataPopularTrending(recyclerView)
        if (intent.getIntExtra(ID_TREND, 0) == 2 && intent.getStringExtra(TITLE_TREND)
                .equals("New")
        )
            getAllDataNewTrending(recyclerView)

        if (intent.getIntExtra(
                ID_CATEGORIES,
                0
            ) != 0 && intent.getStringExtra(NAME_TITLE_CATEGORIES) != ""
        ) {
            id = intent.getIntExtra(ID_CATEGORIES, 0)
            getAllDataCategoriesListWallpaper(id, recyclerView)
        }
        if (intent.getIntExtra(ID_COLOR, 0) != 0 && intent.getStringExtra(NAME_COLOR) != "") {
            id = intent.getIntExtra(ID_COLOR, 0)
            getAllSliderListColor(id, recyclerView)
        }
        manager = GridLayoutManager(this, 3)

        setToolbar()

    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshReceiver)
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

                            filter = IntentFilter()
                            filter.addAction("localBroadCastForData")
                            mRefreshReceiver = object :BroadcastReceiver(){
                                override fun onReceive(context: Context?, intent: Intent?) {
                                    if (intent != null) {
                                        if(intent.action.equals("localBroadCastForData")){
                                            posChange = intent.getIntExtra("pos_change",-1)
                                            (myAdapter as ListWallpaperCategoriesAdapter).updateDataItem(posChange)
                                        }
                                    }
                                }


                            }
                            LocalBroadcastManager.getInstance(this@WallpaperListActivity).registerReceiver(mRefreshReceiver, filter)


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
                            filter = IntentFilter()
                            filter.addAction("localBroadCastForData")
                            mRefreshReceiver = object :BroadcastReceiver(){
                                override fun onReceive(context: Context?, intent: Intent?) {
                                    if (intent != null) {
                                        if(intent.action.equals("localBroadCastForData")){
                                            posChange = intent.getIntExtra("pos_change",-1)
                                            (myAdapter as ListTrendingAdapter).updateDataItem(posChange)
                                        }
                                    }
                                }


                            }
                            LocalBroadcastManager.getInstance(this@WallpaperListActivity).registerReceiver(mRefreshReceiver, filter)



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

                            filter = IntentFilter()
                            filter.addAction("localBroadCastForData")
                            mRefreshReceiver = object :BroadcastReceiver(){
                                override fun onReceive(context: Context?, intent: Intent?) {
                                    if (intent != null) {
                                        if(intent.action.equals("localBroadCastForData")){
                                            posChange = intent.getIntExtra("pos_change",-1)
                                            (myAdapter as ListTrendingAdapter).updateDataItem(posChange)
                                        }
                                    }
                                }


                            }
                            LocalBroadcastManager.getInstance(this@WallpaperListActivity).registerReceiver(mRefreshReceiver, filter)





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

                            filter = IntentFilter()
                            filter.addAction("localBroadCastForData")
                            mRefreshReceiver = object :BroadcastReceiver(){
                                override fun onReceive(context: Context?, intent: Intent?) {
                                    if (intent != null) {
                                        if(intent.action.equals("localBroadCastForData")){
                                            posChange = intent.getIntExtra("pos_change",-1)
                                            (myAdapter as ListTrendingAdapter).updateDataItem(posChange)
                                        }
                                    }
                                }


                            }
                            LocalBroadcastManager.getInstance(this@WallpaperListActivity).registerReceiver(mRefreshReceiver, filter)




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

                            filter = IntentFilter()
                            filter.addAction("localBroadCastForData")
                            mRefreshReceiver = object :BroadcastReceiver(){
                                override fun onReceive(context: Context?, intent: Intent?) {
                                    if (intent != null) {
                                        if(intent.action.equals("localBroadCastForData")){
                                            posChange = intent.getIntExtra("pos_change",-1)
                                            (myAdapter as ListWallpaperCategoriesAdapter).updateDataItem(posChange)
                                        }
                                    }
                                }


                            }
                            LocalBroadcastManager.getInstance(this@WallpaperListActivity).registerReceiver(mRefreshReceiver, filter)




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
        if (intent.getIntExtra(ID_TREND, 0) == 0 && intent.getStringExtra(TITLE_TREND)
                .equals("Featured")
        )
            mTitle.text = intent.getStringExtra(TITLE_TREND)
        setSupportActionBar(myToolbar)
        if (intent.getIntExtra(ID_TREND, 0) == 1 && intent.getStringExtra(TITLE_TREND)
                .equals("Popular")
        )
            mTitle.text = intent.getStringExtra(TITLE_TREND)
        setSupportActionBar(myToolbar)
        if (intent.getIntExtra(ID_TREND, 0) == 2 && intent.getStringExtra(TITLE_TREND)
                .equals("New")
        )
            mTitle.text = intent.getStringExtra(TITLE_TREND)
        setSupportActionBar(myToolbar)
        if (intent.getIntExtra(
                ID_CATEGORIES,
                0
            ) != 0 && intent.getStringExtra(NAME_TITLE_CATEGORIES) != ""
        ) {
            mTitle.text = intent.getStringExtra(NAME_TITLE_CATEGORIES)
            setSupportActionBar(myToolbar)
        }
        if (intent.getIntExtra(ID_COLOR, 0) != 0 && intent.getStringExtra(NAME_COLOR) != "") {
            mTitle.text = intent.getStringExtra(NAME_COLOR)
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