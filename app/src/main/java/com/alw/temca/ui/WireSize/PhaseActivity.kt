package com.alw.temca.ui.WireSize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.PhaseAdapter
import com.alw.temca.Adapter.onClickAdapterListener
import com.alw.temca.Model.PhaseModel
import com.alw.temca.R
import com.alw.temca.ui.PipeSize.PipeSizeActivity
import kotlinx.android.synthetic.main.activity_phase.*

class PhaseActivity : AppCompatActivity(), onClickAdapterListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phase)

        val phaseItem = ArrayList<PhaseModel>()
        phaseItem.add(PhaseModel(1,"เฟส"))
        phaseItem.add(PhaseModel(2,"เฟส"))

        recyclerViewPhase.adapter = PhaseAdapter(phaseItem,this)
        recyclerViewPhase.layoutManager = LinearLayoutManager(this)
        recyclerViewPhase.hasFixedSize()
    }


    override fun onClick(postion: Int, value: Int) {
        println("dsaasdadasdsad $value")
        val intent = Intent(this,WireSizeActivity::class.java)
//        val bundle = Bundle()
//        bundle.putParcelable("restaurant", foodList[postion])
        intent.putExtra("Test",value)
        startActivity(intent)
    }

}