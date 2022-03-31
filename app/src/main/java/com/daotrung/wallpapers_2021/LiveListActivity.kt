package com.daotrung.wallpapers_2021

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.adapter.LiveListAdapter
import com.daotrung.wallpapers_2021.model.SlideLiveWapaper
import com.daotrung.wallpapers_2021.service.ApiInterface
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class LiveListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    private lateinit var myToobar: Toolbar
    private lateinit var mTitle: TextView
    private var myId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_live_wapaper)
        recyclerView = findViewById(R.id.rv_list_live_wapper)
        manager = GridLayoutManager(this, 3)
        val intent = intent
        myId = intent.getIntExtra("idGet", 0)

        Log.e("id",myId.toString())
        getSlideDataById(recyclerView, myId)
        setToolbar()
    }

    @SuppressLint("ResourceAsColor")
    private fun setToolbar() {
        myToobar = findViewById(R.id.toolbar_live_list)
        mTitle = myToobar.findViewById(R.id.txt_title_toolbar_live)
        mTitle.text = intent.getStringExtra("title")
        setSupportActionBar(myToobar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_ios_forward)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myToobar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getSlideDataById(rv: RecyclerView, myId: Int) {
        ApiInterface.Api.retrofitService.getSlideDataById(myId)
            .enqueue(object : Callback<List<SlideLiveWapaper>> {
                override fun onResponse(
                    call: Call<List<SlideLiveWapaper>>,
                    response: Response<List<SlideLiveWapaper>>
                ) {
//                Log.e("---->","response= ${response.isSuccessful}")

                    if (response.isSuccessful) {
                        recyclerView = rv.apply {
                            myAdapter = LiveListAdapter(response.body()!!)
                            layoutManager = manager
                            adapter = myAdapter
                            Log.e("data",response.body().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<List<SlideLiveWapaper>>, t: Throwable) {
                    t.printStackTrace()

                }

            }
            )
    }
}