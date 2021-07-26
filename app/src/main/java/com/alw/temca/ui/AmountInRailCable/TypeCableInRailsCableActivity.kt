package com.alw.temca.ui.AmountInRailCable

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInRailsCable.TypeCableInRailsCableAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_type_cable_in_rails_cable.*

class TypeCableInRailsCableActivity : AppCompatActivity(),onClickInAdapter {
    private val typeCableItem = ArrayList<TypeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_cable_in_rails_cable)


        typeCableItem.add(TypeCableModel("NYY 1/C"))
        typeCableItem.add(TypeCableModel("NYY 4/C"))
        typeCableItem.add(TypeCableModel("XLPE 1/C"))
        typeCableItem.add(TypeCableModel("XLPE 4/C"))

        recyclerViewTypeCable.adapter = TypeCableInRailsCableAdapter(typeCableItem,this)
        recyclerViewTypeCable.layoutManager = LinearLayoutManager(this)

    }
    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataTypeCable",typeCableItem[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}