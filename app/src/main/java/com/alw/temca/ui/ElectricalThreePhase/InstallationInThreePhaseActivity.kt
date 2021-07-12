package com.alw.temca.ui.ElectricalThreePhase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterThreePhase.InstallationThreePhaseAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.ThreePhase.InstallationModel

import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_installation_in_three_phase.*

class InstallationInThreePhaseActivity : AppCompatActivity(),onClickInAdapter {
    private val installItem = ArrayList<InstallationModel>()
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installation_in_three_phase)

        val setResourceImage1 = resources.getIdentifier("setting_group1", "drawable", this.packageName)
        val setResourceImage2 = resources.getIdentifier("setting_group2", "drawable", this.packageName)
        val setResourceImage3 = resources.getIdentifier("setting_group3", "drawable", this.packageName)
        val setResourceImage4 = resources.getIdentifier("setting_group4", "drawable", this.packageName)
        val setResourceImage5 = resources.getIdentifier("setting_group5", "drawable", this.packageName)
        val setResourceImage6 = resources.getIdentifier("setting_group6", "drawable", this.packageName)
        val setResourceImage7 = resources.getIdentifier("setting_group7", "drawable", this.packageName)


        //        installItem.add(InstallationModel(resources.getDrawable(setResourceImage1),"กลุ่ม 1","เดินสายภายในฝ้าเพดานที่เป็นฉนวนความร้อน"))
        installItem.add(InstallationModel(resources.getDrawable(setResourceImage2),"กลุ่ม 2","เดินเกาะผนัง,เพดาน,ฝังคอนกรีต"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage3),"กลุ่ม 3","เดินเกาะผนังไม่มีสิ่งปิดหุ้ม"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage4),"กลุ่ม 4","สายแกนเดี่ยวเดินบนฉนวนลูกถ้วย"))
        installItem.add(InstallationModel(resources.getDrawable(setResourceImage5),"กลุ่ม 5","เดินในท่อฝังดิน"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage6),"กลุ่ม 6","เดินฝังดินโดยตรง"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7),"กลุ่ม 7","วางบนรางเคเบิ้ล ไม่มีฝาปิดแบบระบายอากาศ"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7),"กลุ่ม 7","วางแบนรางเคเบิ้ล ไม่มีผาปิดแบบบันได"))


        recyclerViewInstallation.adapter = InstallationThreePhaseAdapter(installItem,this)
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