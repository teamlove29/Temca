package com.alw.temca.ui.WireSize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.typeCableAdapter
import com.alw.temca.Model.InstallationModel
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_type_cable.*

class TypeCableActivity : AppCompatActivity() , InstallationonClickAdapterListener {
    val typeCableItem = ArrayList<TypeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_cable)

        typeCableItem.add(TypeCableModel("CV"))
        typeCableItem.add(TypeCableModel("CVV - S"))
        typeCableItem.add(TypeCableModel("CVV"))
        typeCableItem.add(TypeCableModel("IEC01"))
        typeCableItem.add(TypeCableModel("IEC06"))
        typeCableItem.add(TypeCableModel("IEC10"))
        typeCableItem.add(TypeCableModel("IEC53"))
        typeCableItem.add(TypeCableModel("IEC53 - G"))
        typeCableItem.add(TypeCableModel("NYY - G"))
        typeCableItem.add(TypeCableModel("NYY"))
        typeCableItem.add(TypeCableModel("THW - A"))
        typeCableItem.add(TypeCableModel("VAF - G"))
        typeCableItem.add(TypeCableModel("VAF"))
        typeCableItem.add(TypeCableModel("VKF (IEC52)"))
        typeCableItem.add(TypeCableModel("VKF (IEC53)"))

        recyclerViewTypeCable.adapter = typeCableAdapter(typeCableItem,this)
        recyclerViewTypeCable.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(postion: Int) {
        val intent = Intent(this,WireSizeActivity::class.java)
        intent.putExtra("dataTypeCable",typeCableItem[postion].name)
        startActivity(intent)
    }
}