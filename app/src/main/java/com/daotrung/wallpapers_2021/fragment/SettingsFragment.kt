package com.daotrung.wallpapers_2021.fragment

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.daotrung.wallpapers_2021.R
import android.content.Intent
import android.net.Uri
import com.daotrung.wallpapers_2021.BuildConfig
import com.daotrung.wallpapers_2021.PrivacyActivity
import com.daotrung.wallpapers_2021.SettingActivity
import java.lang.Exception


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val imgSetting: ImageView = view.findViewById(R.id.img_settings)
        val imgContact: ImageView = view.findViewById(R.id.img_contact)
        val imgShare: ImageView = view.findViewById(R.id.img_share)
        val imgWrite: ImageView = view.findViewById(R.id.img_write)


        // xu ly sự kiện button
        imgShare.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }
//        imgTerm.setOnClickListener {
//            val intent : Intent = Intent(context,PrivacyActivity::class.java)
//            context?.startActivity(intent)
//        }
//        imgPrivacy.setOnClickListener {
//            val intent : Intent = Intent(context,PrivacyActivity::class.java)
//            context?.startActivity(intent)
//        }

        imgWrite.setOnClickListener {
                goToUrl("https://www.facebook.com/SuntechLtd1/")
        }

        imgContact.setOnClickListener {
            goToUrl("https://suntechltd.net/contact/")
        }

        return view



    }

    private fun goToUrl(s: String) {
           val uri:Uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW,uri))
    }

}