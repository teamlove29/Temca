package com.alw.temca.ui.Moter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.MoterAdapter
import com.alw.temca.Model.MoterModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_unit_moter.*

class UnitMoterActivity : AppCompatActivity() , InstallationonClickAdapterListener {
    private val moterItem = ArrayList<MoterModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_moter)

        moterItem.add(MoterModel("HP"))
        moterItem.add(MoterModel("kW"))
        moterItem.add(MoterModel("A"))

        recyclerViewUnitMoter.adapter = MoterAdapter(moterItem,this)
        recyclerViewUnitMoter.layoutManager = LinearLayoutManager(this)

    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataUnitMoter",moterItem[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}