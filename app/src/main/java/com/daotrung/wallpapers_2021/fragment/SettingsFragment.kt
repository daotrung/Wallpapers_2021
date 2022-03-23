package com.daotrung.wallpapers_2021.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.daotrung.wallpapers_2021.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val imgSetting: ImageView = view.findViewById(R.id.img_settings)
        val imgPrivacy: ImageView = view.findViewById(R.id.img_privacy)
        val imgTerm: ImageView = view.findViewById(R.id.img_term)
        val imgContact: ImageView = view.findViewById(R.id.img_contact)
        val imgShare: ImageView = view.findViewById(R.id.img_share)
        val imgWrite: ImageView = view.findViewById(R.id.img_write)
        return view
    }

}