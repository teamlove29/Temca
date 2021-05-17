package com.alw.temca.ui.Moter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.SizeMoterAdapter
import com.alw.temca.Model.DataToSizeMoterModel
import com.alw.temca.Model.SizeMoterModel
import com.alw.temca.R
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_moter.*
import kotlinx.android.synthetic.main.activity_size_moter.*

class SizeMoterActivity : AppCompatActivity(), InstallationonClickAdapterListener {
    var sizeMoterList = ArrayList<SizeMoterModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_moter)
        intent = getIntent()

        val unitOfMoterActivity = intent.getParcelableArrayListExtra<DataToSizeMoterModel>("Unit")
        val phase = unitOfMoterActivity!![0].phase
        val pantern = unitOfMoterActivity[0].panternStart
        val unit = unitOfMoterActivity[0].unit

        val stepInFor:Int
        val maxRowsheet:Int
        val SizeMoterIndex:Int
            when(phase){
            "1 เฟส" -> {
                SizeMoterIndex = 0
                maxRowsheet = 53
                stepInFor = 4
            }
            else -> {
                if (pantern == "DOL") {
                    SizeMoterIndex = 1
                    maxRowsheet = 93
                    stepInFor = 4
                } else {
                    SizeMoterIndex = 2
                    maxRowsheet = 46
                    stepInFor = 3
                }
            }
        }
        val columnInTable:Int = when(unit){
            "kW" -> 0
            "HP" -> 1
            "A" -> 10
            else -> 0
        }


        val sizeMoterTable = applicationContext.assets.open("moter.xls") // pressure_drop_file
        val wbPressure = Workbook.getWorkbook(sizeMoterTable)
        val sheetPressure = wbPressure.getSheet(SizeMoterIndex)

        for(i in 2..maxRowsheet step stepInFor){
            val findAmountTypeInTable = sheetPressure.getCell(columnInTable, i).contents
            sizeMoterList.add(SizeMoterModel(findAmountTypeInTable,unit))
        }
        recyclerViewSizeMoter.adapter = SizeMoterAdapter(sizeMoterList,this)
        recyclerViewSizeMoter.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataSizeMoter",sizeMoterList[postion].amount)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}