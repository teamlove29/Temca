package com.alw.temca.ui.PipeSize

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.SizeConduitAdapter
import com.alw.temca.Model.SizeConduitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_size_conduit.*

class SizeConduitActivity : AppCompatActivity(), InstallationonClickAdapterListener {
    val listConduit = ArrayList<SizeConduitModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_conduit)


        listConduit.add(SizeConduitModel("50x75 มม."))
        listConduit.add(SizeConduitModel("50x100 มม."))
        listConduit.add(SizeConduitModel("75x100 มม."))
        listConduit.add(SizeConduitModel("100x100 มม."))
        listConduit.add(SizeConduitModel("100x150 มม."))
        listConduit.add(SizeConduitModel("100x200 มม."))
        listConduit.add(SizeConduitModel("100x250 มม."))
        listConduit.add(SizeConduitModel("100x300 มม."))
        listConduit.add(SizeConduitModel("150x300 มม."))

        recyclerViewSizeConduit.adapter = SizeConduitAdapter(listConduit,this)
        recyclerViewSizeConduit.layoutManager = LinearLayoutManager(this)

    }
    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataSizeConduit",listConduit[postion].name);
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}