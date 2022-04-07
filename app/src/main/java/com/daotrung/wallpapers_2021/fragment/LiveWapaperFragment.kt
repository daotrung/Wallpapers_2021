package com.daotrung.wallpapers_2021.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.LiveMainAdapter
import com.daotrung.wallpapers_2021.model.WallPaper
import com.daotrung.wallpapers_2021.service.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LiveWapaperFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    private lateinit var myToobar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_live, container, false)

        recyclerView = view.findViewById(R.id.rv_live_wapaper)
        myToobar = view.findViewById(R.id.my_toolbar_live_wapaper)
//        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        myToobar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(myToobar)
        manager = GridLayoutManager(context, 2)

        getAllData(recyclerView)
        return view

    }

    private fun getAllData(rv: RecyclerView) {
        ApiInterface.Api.retrofitService.getALLData().enqueue(object : Callback<List<WallPaper>> {
            override fun onResponse(
                call: Call<List<WallPaper>>,
                response: Response<List<WallPaper>>
            ) {
                if (response.isSuccessful) {
//                   Log.e("data:",Gson().toJson(response.body()))
                    recyclerView = rv.apply {
                        myAdapter = LiveMainAdapter(response.body()!!)
                        layoutManager = manager
                        adapter = myAdapter
                    }
                }
            }


            override fun onFailure(call: Call<List<WallPaper>>, t: Throwable) {
                t.printStackTrace()
            }

        }
        )
    }


}