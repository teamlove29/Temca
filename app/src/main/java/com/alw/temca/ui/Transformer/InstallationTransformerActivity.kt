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
            textViewTitleInstallTransformer.text = "ขนาดวงจรหม้อแปลง"
        }else{

        }

//        installItem.add(InstallationModelInTransformer("กลุ่ม 7", "ไม่มีฝาปิดแบบระบายอากาศ"))
//        installItem.add(InstallationModelInTransformer("กลุ่ม 7", "ไม่มีฝาปิดแบบบันได"))

        val setResourceImage1 = resources.getIdentifier("setting_group1", "drawable", this.packageName)
        val setResourceImage2 = resources.getIdentifier("setting_group2", "drawable", this.packageName)
        val setResourceImage3 = resources.getIdentifier("setting_group3", "drawable", this.packageName)
        val setResourceImage4 = resources.getIdentifier("setting_group4", "drawable", this.packageName)
        val setResourceImage5 = resources.getIdentifier("setting_group5", "drawable", this.packageName)
        val setResourceImage6 = resources.getIdentifier("setting_group6", "drawable", this.packageName)

        val setResourceImage7_air = resources.getIdentifier("ic_group7_air", "drawable", this.packageName)
        val setResourceImage7_stairs = resources.getIdentifier("ic_group7_stairs", "drawable", this.packageName)

        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7_air),"กลุ่ม 7","วางบนรางเคเบิล ไม่มีฝาปิดแบบระบายอากาศ"))
        installItem.add(InstallationModel(resources.getDrawable(setResourceImage7_stairs),"กลุ่ม 7","วางบนรางเคเบิล ไม่มีฝาปิดแบบบันได"))

        recyclerViewInstallationTransformer.adapter = InstallationAdapter(installItem, this)
        recyclerViewInstallationTransformer.layoutManager = LinearLayoutManager(this)
    }


    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        val installDes:String = if(installItem[postion].des == "วางบนรางเคเบิล ไม่มีฝาปิดแบบระบายอากาศ"){
            "รางเคเบิลแบบระบายอากาศ"
        }else{
            "รางเคเบิลแบบบันได"
        }
        resultIntent.putExtra("dataInstall",installItem[postion].title)
        resultIntent.putExtra("dataInstallDes",installDes)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}