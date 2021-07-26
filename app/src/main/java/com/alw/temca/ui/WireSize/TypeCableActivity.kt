package com.alw.temca.ui.WireSize

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.TypeCableAdapter
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_type_cable.*

class TypeCableActivity : AppCompatActivity() , InstallationonClickAdapterListener {
    private val typeCableItem = ArrayList<TypeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_cable)

        val checkActivity = intent.getStringExtra("Activity")
        val TypeActivityMoter = intent.getStringExtra("Type")
        val phase = intent.getStringExtra("Phase")

        if (checkActivity.equals("PipeSize")){
            headerTypeCable.text = "หาขนาดท่อและราง"
            typeCableItem.add(TypeCableModel("IEC01"))
            typeCableItem.add(TypeCableModel("IEC10 2C"))
            typeCableItem.add(TypeCableModel("IEC10 3C"))
            typeCableItem.add(TypeCableModel("IEC10 4C"))
            typeCableItem.add(TypeCableModel("NYY 1C"))
            typeCableItem.add(TypeCableModel("NYY 2C"))
            typeCableItem.add(TypeCableModel("NYY 3C"))
            typeCableItem.add(TypeCableModel("NYY 4C"))
            typeCableItem.add(TypeCableModel("XLPE 1C"))
            typeCableItem.add(TypeCableModel("XLPE 2C"))
            typeCableItem.add(TypeCableModel("XLPE 3C"))
            typeCableItem.add(TypeCableModel("XLPE 4C"))
            typeCableItem.add(TypeCableModel("VCT 1C"))
            typeCableItem.add(TypeCableModel("VCT 2C"))
            typeCableItem.add(TypeCableModel("VCT 3C"))
            typeCableItem.add(TypeCableModel("VCT 4C"))
        }

        else if(checkActivity.equals("Moter")){

            headerTypeCable.text = "ขนาดวงจรมอเตอร์"
            if(TypeActivityMoter != "DELTA"){
                if(phase == "3"){
                typeCableItem.add(TypeCableModel("IEC 01"))
                typeCableItem.add(TypeCableModel("NYY 1C"))
                typeCableItem.add(TypeCableModel("NYY 3C-G"))
                typeCableItem.add(TypeCableModel("XLPE 1C"))
                }else{
                    typeCableItem.add(TypeCableModel("IEC 01"))
                    typeCableItem.add(TypeCableModel("NYY 1C"))
                    typeCableItem.add(TypeCableModel("NYY 2C-G"))
                    typeCableItem.add(TypeCableModel("XLPE 1C"))
                }


            }else{
                typeCableItem.add(TypeCableModel("IEC 01"))
                typeCableItem.add(TypeCableModel("NYY 1C"))
                typeCableItem.add(TypeCableModel("XLPE 1C"))
            }




        }

        else if (checkActivity.equals("Transformer")){
            headerTypeCable.text = "หาขนาดสายหม้อแปลง"
            typeCableItem.add(TypeCableModel("NYY"))
            typeCableItem.add(TypeCableModel("XLPE"))
        }else{
            if(checkActivity!="Group5"){
                typeCableItem.add(TypeCableModel("IEC01"))
            }
            typeCableItem.add(TypeCableModel("NYY"))
            typeCableItem.add(TypeCableModel("XLPE"))
            typeCableItem.add(TypeCableModel("VCT"))
            typeCableItem.add(TypeCableModel("NYY-G"))
            typeCableItem.add(TypeCableModel("VCT-G"))
        }
        if(checkActivity == "Group7"){
            typeCableItem.clear()
            typeCableItem.add(TypeCableModel("NYY 1C"))
            typeCableItem.add(TypeCableModel("NYY 4C"))
            typeCableItem.add(TypeCableModel("XLPE 1C"))
            typeCableItem.add(TypeCableModel("XLPE 4C"))

        }


        recyclerViewTypeCable.adapter = TypeCableAdapter(typeCableItem,this)
        recyclerViewTypeCable.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataTypeCable",typeCableItem[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}