package com.alw.temca.ui.ElectricalOnePhase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterOnePhase.InstallationOnePhaseAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.OnePhase.InstallationModel

import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_installation_in_one_phase.*


class InstallationInOnePhaseActivity : AppCompatActivity(),onClickInAdapter {
    private val installItem = ArrayList<InstallationModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installation_in_one_phase)

        val setResourceImage1 = resources.getIdentifier("setting_group1", "drawable", this.packageName)
        val setResourceImage2 = resources.getIdentifier("setting_group2", "drawable", this.packageName)
        val setResourceImage3 = resources.getIdentifier("setting_group3", "drawable", this.packageName)
        val setResourceImage4 = resources.getIdentifier("setting_group4", "drawable", this.packageName)
        val setResourceImage5 = resources.getIdentifier("setting_group5", "drawable", this.packageName)
        val setResourceImage6 = resources.getIdentifier("setting_group6", "drawable", this.packageName)
        val setResourceImage7 = resources.getIdentifier("setting_group7", "drawable", this.packageName)


        installItem.add(InstallationModel(resources.getDrawable(setResourceImage2),"กลุ่ม 2","เดินเกาะผนัง,เพดาน,ฝังคอนกรีต"))
        installItem.add(InstallationModel(resources.getDrawable(setResourceImage5),"กลุ่ม 5","เดินในท่อฝังดิน"))

        recyclerViewInstallation.adapter = InstallationOnePhaseAdapter(installItem,this)
        recyclerViewInstallation.layoutManager = LinearLayoutManager(this)

    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataInstall",installItem[postion].title)
        resultIntent.putExtra("dataInstallDes",installItem[postion].des)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}