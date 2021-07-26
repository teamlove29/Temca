package com.alw.temca.ui.AmountInRailCable

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInRailsCable.InstallationInRailsCableAdapter

import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.InstallationModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_installation_in_rails_cable.*


class InstallationInRailsCableActivity : AppCompatActivity(),onClickInAdapter {
    private val installItem = ArrayList<InstallationModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installation_in_rails_cable)


        val setResourceImage7_air = resources.getIdentifier("ic_group7_air", "drawable", this.packageName)
        val setResourceImage7_stairs = resources.getIdentifier("ic_group7_stairs", "drawable", this.packageName)

        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7_air),"กลุ่ม 7","วางบนรางเคเบิ้ล ไม่มีฝาปิดแบบระบายอากาศ"))
        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7_stairs),"กลุ่ม 7","วางแบนรางเคเบิ้ล ไม่มีผาปิดแบบบันได"))

        recyclerViewInstallation.adapter = InstallationInRailsCableAdapter(installItem,this)
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