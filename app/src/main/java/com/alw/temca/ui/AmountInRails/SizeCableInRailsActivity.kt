package com.alw.temca.ui.AmountInRails

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInRails.SizeCableAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.SizeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_size_cable.*
import java.util.ArrayList

class SizeCableInRailsActivity : AppCompatActivity(), onClickInAdapter {
    private val listSizeCable = ArrayList<SizeCableModel>()
    private val listSizeCable2 = ArrayList<SizeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_cable_in_rails)

        listSizeCable.add(SizeCableModel("1 mm2")) // 0
        listSizeCable.add(SizeCableModel("1.5 mm2")) // 1
        listSizeCable.add(SizeCableModel("2.5 mm2")) // 2
        listSizeCable.add(SizeCableModel("4 mm2")) // 3
        listSizeCable.add(SizeCableModel("6 mm2")) // 4
        listSizeCable.add(SizeCableModel("10 mm2")) // 5
        listSizeCable.add(SizeCableModel("16 mm2")) // 6
        listSizeCable.add(SizeCableModel("25 mm2")) // 7
        listSizeCable.add(SizeCableModel("35 mm2")) // 8
        listSizeCable.add(SizeCableModel("50 mm2")) // 9
        listSizeCable.add(SizeCableModel("70 mm2")) // 10
        listSizeCable.add(SizeCableModel("95 mm2")) // 11
        listSizeCable.add(SizeCableModel("120 mm2")) // 12
        listSizeCable.add(SizeCableModel("150 mm2")) // 13
        listSizeCable.add(SizeCableModel("185 mm2")) // 14
        listSizeCable.add(SizeCableModel("240 mm2")) // 15
        listSizeCable.add(SizeCableModel("300 mm2")) // 16
        listSizeCable.add(SizeCableModel("400 mm2")) // 17
        listSizeCable.add(SizeCableModel("500 mm2")) // 18

        val typeCableOfMain = intent.getStringExtra("typeCable").toString()

        val amountRowInTable:Int = when(typeCableOfMain){
            "IEC 01" -> 16 // 300
            "NYY 1/C" -> 14 // 185
            "NYY 2/C" -> 8 // 35
            "NYY 3/C" -> 8 // 35
            "NYY 4/C" -> 8 // 35
            "XLPE 1/C" -> 16 // 300
            "XLPE 2/C" -> 17 // 400
            "XLPE 3/C" -> 17 // 400
            "XLPE 4/C" -> 16 // 300
            "VCT 1/C" -> 8 // 35
            "VCT 2/C" -> 8 // 35
            "VCT 3/C" -> 8 // 35
            "VCT 4/C" -> 8 // 35
            else -> 99
        }


        var startRow:Int = if(typeCableOfMain == "IEC 01")
        { 2 } else 0

        if(typeCableOfMain == "XLPE 1/C" || typeCableOfMain == "XLPE 2/C" || typeCableOfMain == "XLPE 3/C" || typeCableOfMain == "XLPE 4/C")
        { startRow = 1 } else if(typeCableOfMain == "IEC 01"){
            startRow = 2
        }else{
            0
        }


        if (amountRowInTable == 99 ){
            Toast.makeText(this,"Warning : ไม่มีค่าสำหรับชนิดสายนี้", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            for(i in startRow..amountRowInTable){
                listSizeCable2.add(listSizeCable[i])
            }
        }

//        var startRow:Int = if(typeCableOfMain == "IEC 01"
//            || typeCableOfMain == "XLPE 1C"
//            || typeCableOfMain == "XLPE 2C"
//            || typeCableOfMain == "XLPE 3C"
//            || typeCableOfMain =="XLPE 4C")
//            { 1 } else 0






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