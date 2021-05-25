package com.alw.temca.ui.Transformer


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationAdapter
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Model.InstallationModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_installation_transformer.*


class InstallationTransformerActivity : AppCompatActivity(), InstallationonClickAdapterListener {
    private val installItem = ArrayList<InstallationModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installation_transformer)

        val intent = intent
        val comeForm = intent.getStringExtra("Activity")

        if(comeForm == "Moter"){
            textViewTitleInstallTransformer.text = "ขนาดสายหม้อแปลง"
        }else{

        }

//        installItem.add(InstallationModelInTransformer("กลุ่ม 7", "ไม่มีฝาปิดแบบระบายอากาศ"))
//        installItem.add(InstallationModelInTransformer("กลุ่ม 7", "ไม่มีฝาปิดแบบบันได"))

        val setResourceImage1 = getResources().getIdentifier("setting_group1", "drawable", this.packageName)
        val setResourceImage2 = getResources().getIdentifier("setting_group2", "drawable", this.packageName)
        val setResourceImage3 = getResources().getIdentifier("setting_group3", "drawable", this.packageName)
        val setResourceImage4 = getResources().getIdentifier("setting_group4", "drawable", this.packageName)
        val setResourceImage5 = getResources().getIdentifier("setting_group5", "drawable", this.packageName)
        val setResourceImage6 = getResources().getIdentifier("setting_group6", "drawable", this.packageName)
        val setResourceImage7 = getResources().getIdentifier("setting_group7", "drawable", this.packageName)

        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7),"กลุ่ม 7","วางบนรางเคเบิ้ล ไม่มีฝาปิดแบบระบายอากาศ"))
        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7),"กลุ่ม 7","วางบนรางเคเบิ้ล ไม่มีฝาปิดแบบบันได"))

        recyclerViewInstallationTransformer.adapter = InstallationAdapter(installItem, this)
        recyclerViewInstallationTransformer.layoutManager = LinearLayoutManager(this)
    }


    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataInstall",installItem[postion].title)
        resultIntent.putExtra("dataInstallDes",installItem[postion].des.replace("วางบนรางเคเบิ้ล",""))
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}