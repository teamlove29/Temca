package com.alw.temca.ui.WireSize

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationAdapter
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Model.InstallationModel
import com.alw.temca.Model.PhaseModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_installation.*


class InstallationActivity : AppCompatActivity(), InstallationonClickAdapterListener {
    val installItem = ArrayList<InstallationModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installation)



        val setResourceImage1 = getResources().getIdentifier("setting_group1", "drawable", this.packageName)
        val setResourceImage2 = getResources().getIdentifier("setting_group2", "drawable", this.packageName)
        val setResourceImage3 = getResources().getIdentifier("setting_group3", "drawable", this.packageName)
        val setResourceImage4 = getResources().getIdentifier("setting_group4", "drawable", this.packageName)
        val setResourceImage5 = getResources().getIdentifier("setting_group5", "drawable", this.packageName)
        val setResourceImage6 = getResources().getIdentifier("setting_group6", "drawable", this.packageName)
        val setResourceImage7 = getResources().getIdentifier("setting_group7", "drawable", this.packageName)

//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage1),"กลุ่ม 1","เดินสายภายในฝ้าเพดานที่เป็นฉนวนความร้อน"))
        installItem.add(InstallationModel(resources.getDrawable(setResourceImage2),"กลุ่ม 2","เดินเกาะผนัง,เพดาน,ฝังคอนกรีต"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage3),"กลุ่ม 3","เดินเกาะผนังไม่มีสิ่งปิดหุ้ม"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage4),"กลุ่ม 4","สายแกนเดี่ยวเดินบนฉนวนลูกถ้วย"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage5),"กลุ่ม 5","เดินในท่อฝังดิน"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage6),"กลุ่ม 6","เดินฝังดินโดยตรง"))
//        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7),"กลุ่ม 7","เดินในรางเคเบิล"))

        recyclerViewInstallation.adapter = InstallationAdapter(installItem,this)
        recyclerViewInstallation.layoutManager = LinearLayoutManager(this)

    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataInstall",installItem[postion].title)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }


}