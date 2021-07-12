package com.alw.temca.ui.AmountInRails

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInPipe.TypeCableAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_type_cable_in_rails.*


class TypeCableInRailsActivity : AppCompatActivity(), onClickInAdapter {
    private val typeCableItem = ArrayList<TypeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_cable_in_rails)

        typeCableItem.add(TypeCableModel("IEC01"))
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
//        typeCableItem.add(TypeCableModel("NYY 2C - G"))
//        typeCableItem.add(TypeCableModel("NYY 3C - G"))
//        typeCableItem.add(TypeCableModel("NYY 4C - G"))
//        typeCableItem.add(TypeCableModel("VCT 2C - G"))
//        typeCableItem.add(TypeCableModel("VCT 3C - G"))
//        typeCableItem.add(TypeCableModel("VCT 4C - G"))

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