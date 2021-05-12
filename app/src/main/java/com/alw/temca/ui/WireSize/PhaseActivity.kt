package com.alw.temca.ui.WireSize

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.PhaseAdapter
import com.alw.temca.Adapter.onClickAdapterListener
import com.alw.temca.Model.PhaseModel
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_phase.*

class PhaseActivity : AppCompatActivity(), onClickAdapterListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phase)

        val intent = intent
        val comeForm = intent.getStringExtra("Activity")
        val phaseItem = ArrayList<PhaseModel>()

        if(comeForm == "Moter"){
            textViewTitlePhase.text = "ขนาดสายมอเตอร์"
            phaseItem.add(PhaseModel(1,"เฟส 2w (230 V)"))
            phaseItem.add(PhaseModel(3,"เฟส 3w (400 V)"))
        }else{
            phaseItem.add(PhaseModel(1,"เฟส 2สาย (230 V)"))
            phaseItem.add(PhaseModel(3,"เฟส 4สาย (400 V)"))
        }

        if(comeForm == "Group7"){
            phaseItem.clear()
            phaseItem.add(PhaseModel(3,"เฟส 4สาย (400 V)"))
        }



        recyclerViewPhase.adapter = PhaseAdapter(phaseItem,this)
        recyclerViewPhase.layoutManager = LinearLayoutManager(this)
        recyclerViewPhase.hasFixedSize()
    }


    override fun onClick(postion: Int, value: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataPhase",value)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}