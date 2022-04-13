package com.daotrung.wallpapers_2021.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.daotrung.wallpapers_2021.fragment.MyWallpaperFragmentLive
import com.daotrung.wallpapers_2021.fragment.MyWallpaperFragmentWallpaper

class ViewPagerAdapter(fragmentActivity: FragmentActivity, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyWallpaperFragmentLive()
            1 -> MyWallpaperFragmentWallpaper()
            else -> MyWallpaperFragmentLive()
        }
    }
}