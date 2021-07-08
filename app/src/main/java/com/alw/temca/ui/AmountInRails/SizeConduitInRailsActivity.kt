package com.alw.temca.ui.AmountInRails

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInRails.SizeConduitAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.SizeConduitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_size_conduit_in_rails.*


class SizeConduitInRailsActivity : AppCompatActivity(),onClickInAdapter {
    val listConduit = ArrayList<SizeConduitModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_conduit_in_rails)

        listConduit.add(SizeConduitModel("50x75 mm."))
        listConduit.add(SizeConduitModel("50x100 mm."))
        listConduit.add(SizeConduitModel("75x100 mm."))
        listConduit.add(SizeConduitModel("100x100 mm."))
        listConduit.add(SizeConduitModel("100x150 mm."))
        listConduit.add(SizeConduitModel("100x200 mm."))
        listConduit.add(SizeConduitModel("100x250 mm."))
        listConduit.add(SizeConduitModel("100x300 mm."))
        listConduit.add(SizeConduitModel("150x300 mm."))

        recyclerViewSizeConduit.adapter = SizeConduitAdapter(listConduit,this)
        recyclerViewSizeConduit.layoutManager = LinearLayoutManager(this)

    }
    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataSizeConduit",listConduit[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}