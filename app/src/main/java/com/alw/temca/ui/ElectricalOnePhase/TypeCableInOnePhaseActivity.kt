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

        if(checkGroup != "Group5"){
            typeCableItem.add(TypeCableModel("IEC01"))
        }
        typeCableItem.add(TypeCableModel("NYY"))
        typeCableItem.add(TypeCableModel("XLPE"))
        typeCableItem.add(TypeCableModel("VCT"))
        typeCableItem.add(TypeCableModel("NYY-G"))
        typeCableItem.add(TypeCableModel("VCT-G"))

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