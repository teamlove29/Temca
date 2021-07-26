package com.alw.temca.ui.AmountInPipe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInPipe.TypeCableAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_type_cable.*

class TypeCableInPipeActivity : AppCompatActivity() , onClickInAdapter {
    private val typeCableItem = ArrayList<TypeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_cable_in_pipe)

        typeCableItem.add(TypeCableModel("IEC01"))
        typeCableItem.add(TypeCableModel("NYY 1/C"))
        typeCableItem.add(TypeCableModel("NYY 2/C"))
        typeCableItem.add(TypeCableModel("NYY 3/C"))
        typeCableItem.add(TypeCableModel("NYY 4/C"))
        typeCableItem.add(TypeCableModel("XLPE 1/C"))
        typeCableItem.add(TypeCableModel("XLPE 2/C"))
        typeCableItem.add(TypeCableModel("XLPE 3/C"))
        typeCableItem.add(TypeCableModel("XLPE 4/C"))
        typeCableItem.add(TypeCableModel("VCT 1/C"))
        typeCableItem.add(TypeCableModel("VCT 2/C"))
        typeCableItem.add(TypeCableModel("VCT 3/C"))
        typeCableItem.add(TypeCableModel("VCT 4/C"))
        typeCableItem.add(TypeCableModel("NYY 2/C - G"))
        typeCableItem.add(TypeCableModel("NYY 3/C - G"))
        typeCableItem.add(TypeCableModel("NYY 4/C - G"))
        typeCableItem.add(TypeCableModel("VCT 2/C - G"))
        typeCableItem.add(TypeCableModel("VCT 3/C - G"))
        typeCableItem.add(TypeCableModel("VCT 4/C - G"))

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