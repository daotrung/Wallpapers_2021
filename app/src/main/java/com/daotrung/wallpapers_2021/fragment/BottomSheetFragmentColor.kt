package com.daotrung.wallpapers_2021.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.WallpaperListCategoriesMainSheetAdapter
import com.daotrung.wallpapers_2021.adapter.WallpaperListColorMainAdapter
import com.daotrung.wallpapers_2021.model.ColorMain
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragmentColor : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<ColorMain>
    private lateinit var imgId: Array<Int>
    private lateinit var titleColor: Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.bottom_sheet_color_fragment, container, false)
        recyclerView = view.findViewById(R.id.rv_list_color)

        imgId = arrayOf(
            R.drawable.blue,
            R.drawable.green,
            R.drawable.red,
            R.drawable.aqua,
            R.drawable.orange,
            R.drawable.yellow,
            R.drawable.purple,
            R.drawable.pink,
            R.drawable.teal,
            R.drawable.blond,
            R.drawable.maroon,
            R.drawable.gray,
            R.drawable.white,
            R.drawable.black
        )
        titleColor = arrayOf(
            "Blue",
            "Green",
            "Red",
            "Aqua",
            "Orange",
            "Yellow",
            "Purple",
            "Pink",
            "Teal",
            "Blond",
            "Maroon",
            "Gray",
            "White",
            "Black"
        )
        recyclerView.layoutManager = GridLayoutManager(context, 4)

        arrayList = arrayListOf<ColorMain>()

        getData()

        return view
    }


    private fun getData() {
        for (i in imgId.indices) {
            val colorMain = ColorMain(titleColor[i], imgId[i])
            arrayList.add(colorMain)

        }
        recyclerView.adapter = WallpaperListColorMainAdapter(arrayList)
    }

}