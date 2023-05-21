package com.example.habitmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanagerkt.R
import com.vansuita.materialabout.builder.AboutBuilder

class AboutUsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return AboutBuilder.with(HabitManagerApplication.applicationContext())
            .setName(R.string.name)
            .setSubTitle(R.string.activity)
            .setBrief(R.string.brief)
            .setAppIcon(R.mipmap.ic_launcher)
            .setAppName(R.string.app_name)
            .addGitHubLink(R.string.gitHub)
            .addFiveStarsAction()
            .setVersionNameAsAppSubTitle()
            .addShareAction(R.string.app_name)
            .addLinkedInLink(R.string.linkedIn)
            .setWrapScrollView(true)
            .setLinksAnimated(true)
            .setShowAsCard(true)
            .build()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.getSupportActionBar()!!.show()
    }
}