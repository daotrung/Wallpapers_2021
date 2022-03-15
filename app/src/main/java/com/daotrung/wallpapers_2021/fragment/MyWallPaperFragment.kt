package com.daotrung.wallpapers_2021.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.daotrung.wallpapers_2021.R
import com.daotrung.wallpapers_2021.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.newFixedThreadPoolContext


class MyWallPaperFragment : Fragment() {

    lateinit var viewPager2 : ViewPager2
    var tabTitle = arrayOf("Live","Wallpaper")
    lateinit var adapter : ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.fragment_my_wall_paper, container, false)

        viewPager2 = view.findViewById(R.id.view_pager2)
        val tabLayout = view.findViewById<TabLayout>(R.id.tablayout)

        viewPager2.adapter = ViewPagerAdapter(requireActivity(),lifecycle)

        TabLayoutMediator(tabLayout,viewPager2){
            tab,position -> tab.text = tabTitle[position]
        }.attach()

        return view
    }

}