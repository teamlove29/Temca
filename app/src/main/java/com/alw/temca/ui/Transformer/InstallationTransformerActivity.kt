package com.alw.temca.ui.Transformer


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationInTransformerAdapter
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Model.InstallationModelInTransformer
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_installation_transformer.*


class InstallationTransformerActivity : AppCompatActivity(), InstallationonClickAdapterListener {
    private val installItem = ArrayList<InstallationModelInTransformer>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installation_transformer)

        val intent = intent
        val comeForm = intent.getStringExtra("Activity")

        if(comeForm == "Moter"){
            textViewTitleInstallTransformer.text = "ขนาดสายหม้อแปลง"
        }else{

        }

        installItem.add(InstallationModelInTransformer("กลุ่ม 7", "เดินเคเบิ้ลแบบระบายอากาศ"))
        installItem.add(InstallationModelInTransformer("กลุ่ม 7", "เดินคเบิ้ลแบบขั้นบันได"))

        recyclerViewInstallationTransformer.adapter = InstallationInTransformerAdapter(installItem, this)
        recyclerViewInstallationTransformer.layoutManager = LinearLayoutManager(this)


    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        val bundle = Bundle()
        bundle.putParcelable("dataInstall",installItem[postion])
        resultIntent.putExtras(bundle)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}