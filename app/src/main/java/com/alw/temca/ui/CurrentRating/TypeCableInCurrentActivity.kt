package com.alw.temca.ui.CurrentRating

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterCurrentRating.TypeCableCurrentAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_type_cable_in_current.*


class TypeCableInCurrentActivity : AppCompatActivity(), onClickInAdapter {
    private val typeCableItem = ArrayList<TypeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_cable_in_current)

        val phaseOfMain = intent.getStringExtra("Phase").toString()
        val groupOfMain = intent.getStringExtra("Group").toString()

        println(phaseOfMain)
        println(groupOfMain)

        if(phaseOfMain == "1 เฟส"){
            if(groupOfMain == "กลุ่ม 2"){
                typeCableItem.add(TypeCableModel("IEC01"))
                typeCableItem.add(TypeCableModel("IEC10 2C"))
                typeCableItem.add(TypeCableModel("NYY 1C"))
                typeCableItem.add(TypeCableModel("NYY 2C"))
                typeCableItem.add(TypeCableModel("VCT 1C"))
                typeCableItem.add(TypeCableModel("VCT 2C"))
                typeCableItem.add(TypeCableModel("XLPE 1C"))
                typeCableItem.add(TypeCableModel("XLPE 2C"))
            }else if(groupOfMain == "กลุ่ม 5"){
                typeCableItem.add(TypeCableModel("NYY 1C"))
                typeCableItem.add(TypeCableModel("NYY 2C"))
                typeCableItem.add(TypeCableModel("XLPE 1C"))
                typeCableItem.add(TypeCableModel("XLPE 2C"))
            }

        }else{
            if(groupOfMain == "กลุ่ม 2"){
                typeCableItem.add(TypeCableModel("IEC01"))
                typeCableItem.add(TypeCableModel("IEC10 3C"))
                typeCableItem.add(TypeCableModel("IEC10 4C"))
                typeCableItem.add(TypeCableModel("NYY 1C"))
                typeCableItem.add(TypeCableModel("NYY 3C"))
                typeCableItem.add(TypeCableModel("NYY 4C"))
                typeCableItem.add(TypeCableModel("VCT 1C"))
                typeCableItem.add(TypeCableModel("VCT 3C"))
                typeCableItem.add(TypeCableModel("VCT 4C"))
                typeCableItem.add(TypeCableModel("XLPE 1C"))
                typeCableItem.add(TypeCableModel("XLPE 2C"))
                typeCableItem.add(TypeCableModel("XLPE 3C"))
                typeCableItem.add(TypeCableModel("XLPE 4C"))
            }else if(groupOfMain == "กลุ่ม 5"){
                typeCableItem.add(TypeCableModel("NYY 1C"))
                typeCableItem.add(TypeCableModel("NYY 2C"))
                typeCableItem.add(TypeCableModel("NYY 3C"))
                typeCableItem.add(TypeCableModel("NYY 4C"))
                typeCableItem.add(TypeCableModel("XLPE 1C"))
                typeCableItem.add(TypeCableModel("XLPE 2C"))
                typeCableItem.add(TypeCableModel("XLPE 3C"))
                typeCableItem.add(TypeCableModel("XLPE 4C"))
            }
        }

        recyclerViewTypeCable.adapter = TypeCableCurrentAdapter(typeCableItem,this)
        recyclerViewTypeCable.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataTypeCable",typeCableItem[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}