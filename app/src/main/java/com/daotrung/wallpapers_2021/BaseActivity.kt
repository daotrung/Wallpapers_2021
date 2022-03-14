package com.daotrung.wallpapers_2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    var TAG = this::class.simpleName

    lateinit var binding: VB

    abstract fun binding(): VB
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = binding()
        setContentView(binding.root)
        initView()
    }

}