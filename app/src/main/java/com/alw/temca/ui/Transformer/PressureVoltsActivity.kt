package com.alw.temca.ui.Transformer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.PressureAdapter
import com.alw.temca.Model.PressureModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_pressure_volts.*

class PressureVoltsActivity : AppCompatActivity(),InstallationonClickAdapterListener {
    private val pressureItemList = ArrayList<PressureModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pressure_volts)

        pressureItemList.add(PressureModel("230/400 V"))
        pressureItemList.add(PressureModel("240/416 V"))

        recyclerViewPressure.adapter = PressureAdapter(pressureItemList,this)
        recyclerViewPressure.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataOfPressureVolts",pressureItemList[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}