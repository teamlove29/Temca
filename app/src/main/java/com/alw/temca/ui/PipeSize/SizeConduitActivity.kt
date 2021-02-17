package com.alw.temca.ui.PipeSize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.SizeConduitAdapter
import com.alw.temca.Adapter.onClickAdapterListener
import com.alw.temca.Model.SizeConduitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_size_conduit.*

class SizeConduitActivity : AppCompatActivity(), InstallationonClickAdapterListener {
    val listConduit = ArrayList<SizeConduitModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_conduit)


        listConduit.add(SizeConduitModel("100x100 มม."))
        listConduit.add(SizeConduitModel("100x150 มม."))

        recyclerViewSizeConduit.adapter = SizeConduitAdapter(listConduit,this)
        recyclerViewSizeConduit.layoutManager = LinearLayoutManager(this)

    }
    override fun onClick(postion: Int) {

    }
}