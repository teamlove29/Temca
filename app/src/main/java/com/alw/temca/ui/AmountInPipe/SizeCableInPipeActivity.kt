package com.alw.temca.ui.AmountInPipe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInPipe.SizeCableAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.SizeCableModel
import com.alw.temca.R
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_size_cable_in_rails.*
import java.util.ArrayList

class SizeCableInPipeActivity : AppCompatActivity(),onClickInAdapter {
    private val listSizeCable = ArrayList<SizeCableModel>()
    private val listSizeCable2 = ArrayList<SizeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_cable_in_pipe)

        val typeCableOfMain = intent.getStringExtra("typeCable").toString()


        if(typeCableOfMain != "IEC01" && typeCableOfMain != "XLPE 1/C" && typeCableOfMain != "XLPE 2/C" && typeCableOfMain != "XLPE 3/C" && typeCableOfMain != "XLPE 4/C" ){
            listSizeCable.add(SizeCableModel("1 mm2"))
        }
        listSizeCable.add(SizeCableModel("1.5 mm2"))
        listSizeCable.add(SizeCableModel("2.5 mm2"))
        listSizeCable.add(SizeCableModel("4 mm2"))
        listSizeCable.add(SizeCableModel("6 mm2"))
        listSizeCable.add(SizeCableModel("10 mm2"))
        listSizeCable.add(SizeCableModel("16 mm2"))
        listSizeCable.add(SizeCableModel("25 mm2"))
        listSizeCable.add(SizeCableModel("35 mm2"))
        listSizeCable.add(SizeCableModel("50 mm2"))
        listSizeCable.add(SizeCableModel("70 mm2"))
        listSizeCable.add(SizeCableModel("95 mm2"))
        listSizeCable.add(SizeCableModel("120 mm2"))
        listSizeCable.add(SizeCableModel("150 mm2"))
        listSizeCable.add(SizeCableModel("185 mm2"))
        listSizeCable.add(SizeCableModel("240 mm2"))
        listSizeCable.add(SizeCableModel("300 mm2"))
        listSizeCable.add(SizeCableModel("400 mm2"))
        listSizeCable.add(SizeCableModel("500 mm2"))
        if(typeCableOfMain == "XLPE 1/C"){
            listSizeCable.add(SizeCableModel("630 mm2"))
            listSizeCable.add(SizeCableModel("800 mm2"))
        }




        val amountRowInTable:Int = when(typeCableOfMain){
            "IEC01" -> 17 // 400
            "NYY 1/C" -> 18 // 500
            "NYY 2/C" -> 16 // 300
            "NYY 3/C" -> 16 // 300
            "NYY 4/C" -> 16 // 300
            "XLPE 1/C" -> 19 // 800
            "XLPE 2/C" -> 16 // 400
            "XLPE 3/C" -> 16 // 400
            "XLPE 4/C" -> 16 // 400
            "VCT 1/C" -> 8 // 35
            "VCT 2/C" -> 8 // 35
            "VCT 3/C" -> 8 // 35
            "VCT 4/C" -> 8 // 35
            "NYY 2/C - G" -> 16 // 300
            "NYY 3/C - G" -> 16 // 300
            "NYY 4/C - G" -> 16 // 300
            "VCT 2/C - G" -> 8 // 35
            "VCT 3/C - G" -> 8 // 35
            "VCT 4/C - G" -> 8 // 35
            else -> 0
        }

        for(i in 0..amountRowInTable){
            listSizeCable2.add(listSizeCable[i])
        }

        recyclerViewSizeCable.adapter = SizeCableAdapter(listSizeCable2,this)
        recyclerViewSizeCable.layoutManager = LinearLayoutManager(this)
        
    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataSizeCable",listSizeCable2[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()

    }
}