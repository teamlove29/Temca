package com.alw.temca.ui.AmountInPipe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInPipe.SizeCableAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.SizeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_size_cable.*
import java.util.ArrayList

class SizeCableInPipeActivity : AppCompatActivity(),onClickInAdapter {
    private val listSizeCable = ArrayList<SizeCableModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_cable_in_pipe)


        listSizeCable.add(SizeCableModel("1 mm2"))
        listSizeCable.add(SizeCableModel("1.5 mm2"))
        listSizeCable.add(SizeCableModel("2.5 mm2"))
        listSizeCable.add(SizeCableModel("4 mm2"))
        listSizeCable.add(SizeCableModel("6 mm2"))
        listSizeCable.add(SizeCableModel("10 mm2"))
        listSizeCable.add(SizeCableModel("16 mm2"))
        listSizeCable.add(SizeCableModel("25 mm2"))
        listSizeCable.add(SizeCableModel("35 mm2"))
        listSizeCable.add(SizeCableModel("50 mm2"))
        listSizeCable.add(SizeCableModel("70 mm2"))
        listSizeCable.add(SizeCableModel("95 mm2"))
        listSizeCable.add(SizeCableModel("120 mm2"))
        listSizeCable.add(SizeCableModel("150 mm2"))
        listSizeCable.add(SizeCableModel("185 mm2"))
        listSizeCable.add(SizeCableModel("240 mm2"))
        listSizeCable.add(SizeCableModel("300 mm2"))
        listSizeCable.add(SizeCableModel("400 mm2"))
        listSizeCable.add(SizeCableModel("500 mm2"))

        recyclerViewSizeCable.adapter = SizeCableAdapter(listSizeCable,this)
        recyclerViewSizeCable.layoutManager = LinearLayoutManager(this)
        
    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataSizeCable",listSizeCable[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()

    }
}