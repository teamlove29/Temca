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

        installItem.add(InstallationModel("กลุ่ม 1 - เดินสายภายในฝ้าเพดานที่เป็นฉนวนความร้อน"))
        installItem.add(InstallationModel("กลุ่ม 2 - เดินเกาะผนัง,เพดาน,ฝังคอนกรีต"))
        installItem.add(InstallationModel("กลุ่ม 3 - เดินเกาะผนังไม่มีสิ่งปิดหุ้ม"))
        installItem.add(InstallationModel("กลุ่ม 4 - สายแกนเดี่ยวเดินบนฉนวนลูกถ้วย"))
        installItem.add(InstallationModel("กลุ่ม 5 - เดินในท่อฝังดิน"))
        installItem.add(InstallationModel("กลุ่ม 6 - เดินฝังดินโดยตรง"))
        installItem.add(InstallationModel("กลุ่ม 7 - เดินในรางเคเบิล"))


        recyclerViewInstallation.adapter = InstallationAdapter(installItem,this)
        recyclerViewInstallation.layoutManager = LinearLayoutManager(this)

    }

    override fun onClick(postion: Int) {
//        val intent = Intent(this,WireSizeActivity::class.java)
//        intent.putExtra("dataInstall", installItem[postion].name)
//        startActivity(intent)
        val resultIntent = Intent()
        resultIntent.putExtra("dataInstall",installItem[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}