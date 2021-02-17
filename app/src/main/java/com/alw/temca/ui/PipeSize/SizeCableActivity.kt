package com.alw.temca.ui.PipeSize

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.SizeCableAdapter
import com.alw.temca.Model.SizeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_size_cable.*
import java.util.ArrayList

class SizeCableActivity : AppCompatActivity() , InstallationonClickAdapterListener {
    val listSizeCable = ArrayList<SizeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_cable)


        listSizeCable.add(SizeCableModel("1 มม2"))
        listSizeCable.add(SizeCableModel("1.5 มม2"))
        listSizeCable.add(SizeCableModel("2.5 มม2"))
        listSizeCable.add(SizeCableModel("4 มม2"))
        listSizeCable.add(SizeCableModel("6 มม2"))
        listSizeCable.add(SizeCableModel("10 มม2"))
        listSizeCable.add(SizeCableModel("16 มม2"))
        listSizeCable.add(SizeCableModel("25 มม2"))
        listSizeCable.add(SizeCableModel("35 มม2"))
        listSizeCable.add(SizeCableModel("50 มม2"))
        listSizeCable.add(SizeCableModel("70 มม2"))
        listSizeCable.add(SizeCableModel("95 มม2"))
        listSizeCable.add(SizeCableModel("120 มม2"))
        listSizeCable.add(SizeCableModel("150 มม2"))
        listSizeCable.add(SizeCableModel("185 มม2"))
        listSizeCable.add(SizeCableModel("240 มม2"))
        listSizeCable.add(SizeCableModel("300 มม2"))
        listSizeCable.add(SizeCableModel("400 มม2"))
        listSizeCable.add(SizeCableModel("500 มม2"))

        recyclerViewSizeCable.adapter = SizeCableAdapter(listSizeCable,this)
        recyclerViewSizeCable.layoutManager = LinearLayoutManager(this)
    }



    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataSizeCable",listSizeCable[postion].name);
        setResult(Activity.RESULT_OK, resultIntent)
        finish()

    }
}