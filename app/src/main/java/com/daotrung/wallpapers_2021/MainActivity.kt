package com.daotrung.wallpapers_2021

import androidx.fragment.app.Fragment
import com.daotrung.wallpapers_2021.databinding.ActivityMainBinding
import com.daotrung.wallpapers_2021.fragment.LiveWapaperFragment
import com.daotrung.wallpapers_2021.fragment.MyWallPaperFragment
import com.daotrung.wallpapers_2021.fragment.SettingsFragment
import com.daotrung.wallpapers_2021.fragment.WallpaperFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val livewallpaperFragment = LiveWapaperFragment()
    private val mywallpaperFragment= MyWallPaperFragment()
    private val settingsFragment = SettingsFragment()
    private val wallpaperFragment = WallpaperFragment()



    override fun binding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        replaceFragment(livewallpaperFragment)

        binding.navView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.live_wapaper -> replaceFragment(livewallpaperFragment)
                R.id.my_waper -> replaceFragment(mywallpaperFragment)
                R.id.settings -> replaceFragment(settingsFragment)
                R.id.wallpaper -> replaceFragment(wallpaperFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_main,fragment)
            commit()
        }
    }
}