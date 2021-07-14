package com.alw.temca.ui.CurrentRating

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterCurrentRating.PhaseCurrentAdapter
import com.alw.temca.Interfaces.IOnClickAdapterListener
import com.alw.temca.Model.PhaseModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_phase_in_current.*


class PhaseInCurrentActivity : AppCompatActivity() , IOnClickAdapterListener {
    private val phaseItem = ArrayList<PhaseModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phase_in_current)

        phaseItem.add(PhaseModel(1,"เฟส 2สาย (230 V)"))
        phaseItem.add(PhaseModel(3,"เฟส 4สาย (400 V)"))

        recyclerViewPhase.adapter = PhaseCurrentAdapter(phaseItem,this)
        recyclerViewPhase.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(postion: Int, value: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataPhase",value)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }


}