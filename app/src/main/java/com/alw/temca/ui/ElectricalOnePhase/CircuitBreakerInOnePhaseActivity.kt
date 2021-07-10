package com.alw.temca.ui.ElectricalOnePhase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterOnePhase.CircuitOnePhaseAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.CircuitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_circuit_breaker_in_one_phase.*

class CircuitBreakerInOnePhaseActivity : AppCompatActivity(),onClickInAdapter {

    companion object{
        private val listAmp = arrayListOf("16","20","25","32","40","50","63","80","100","125","160","200","250","320","400","500","630","800","1000")
        private val CircuitItem = ArrayList<CircuitModel>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_breaker_in_one_phase)

        CircuitItem.clear()
        intent = intent
        val rowAmountAmp = intent.getIntExtra("RowAmountAmp",99)

        if(rowAmountAmp != 99){
            for (i in 0..rowAmountAmp){ CircuitItem.add(CircuitModel("${listAmp[i]}A")) }
        }

        recyclerViewCircuit.adapter = CircuitOnePhaseAdapter(CircuitItem,this)
        recyclerViewCircuit.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(postion: Int) {
        val intent = Intent(this, CircuitInOnePhaseActivity::class.java)
        intent.putExtra("data",CircuitItem[postion].name)
        startActivity(intent)
        overridePendingTransition(R.anim.nothing,R.anim.nothing)
        finish()
    }
}