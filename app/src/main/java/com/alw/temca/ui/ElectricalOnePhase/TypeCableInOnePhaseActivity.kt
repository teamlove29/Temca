package com.alw.temca.ui.ElectricalOnePhase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterOnePhase.TypeCableOnePhase
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_type_cable_in_on_phase.*


class TypeCableInOnePhaseActivity : AppCompatActivity() ,onClickInAdapter{
    private val typeCableItem = ArrayList<TypeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_cable_in_on_phase)

        val checkGroup = intent.getStringExtra("Group")

        if(checkGroup == "Group2"){ // has 7
            typeCableItem.add(TypeCableModel("IEC01")) // 2
            typeCableItem.add(TypeCableModel("NYY 1C")) // 2, 5
            typeCableItem.add(TypeCableModel("XLPE 1C")) // 2, 5
            typeCableItem.add(TypeCableModel("XLPE 2C")) // 2, 5
            typeCableItem.add(TypeCableModel("VCT 1C")) // 2, 5
            typeCableItem.add(TypeCableModel("NYY 2C - G")) // 2
            typeCableItem.add(TypeCableModel("VCT 2C - G")) // 2

        }
        else if (checkGroup == "Group5"){ // has 6
            typeCableItem.add(TypeCableModel("NYY 1C")) // 2, 5
            typeCableItem.add(TypeCableModel("XLPE 1C")) // 2, 5
            typeCableItem.add(TypeCableModel("XLPE 2C")) // 2, 5
            typeCableItem.add(TypeCableModel("VCT 1C")) // 2, 5
            typeCableItem.add(TypeCableModel("NYY - G")) // 5
            typeCableItem.add(TypeCableModel("VCT - G")) // 5
        }


        recyclerViewTypeCable.adapter = TypeCableOnePhase(typeCableItem,this)
        recyclerViewTypeCable.layoutManager = LinearLayoutManager(this)

    }
    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataTypeCable",typeCableItem[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}