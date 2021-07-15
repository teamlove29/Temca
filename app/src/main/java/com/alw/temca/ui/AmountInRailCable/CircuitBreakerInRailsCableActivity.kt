package com.alw.temca.ui.AmountInRailCable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInRailsCable.CircuitInRailsCableAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.CircuitModel
import com.alw.temca.R
import com.alw.temca.ui.WireSize.CircuitActivity
import kotlinx.android.synthetic.main.activity_circuit_breaker_in_rails_cable.*

class CircuitBreakerInRailsCableActivity : AppCompatActivity(), onClickInAdapter {
    private val listAmpGroup7 =
        arrayListOf<String>("400", "500", "630", "800", "1000", "1250", "1600", "2000")
    private val CircuitItem = ArrayList<CircuitModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_breaker_in_rails_cable)


        intent = getIntent()
        val RowAmountAmp = intent.getIntExtra("RowAmountAmp", 99)


        if (RowAmountAmp != 99 && RowAmountAmp == 77) {
            for (i in 0..7) {
                CircuitItem.add(CircuitModel("${listAmpGroup7[i]}A"))
            }

        recyclerViewCircuit.adapter = CircuitInRailsCableAdapter(CircuitItem,this)
        recyclerViewCircuit.layoutManager = LinearLayoutManager(this)

        }
    }

    override fun onClick(postion: Int) {
        val Intent = Intent(this, CircuitInRailsCableActivity::class.java)
        Intent.putExtra("data", CircuitItem[postion].name)
        startActivity(Intent)
        overridePendingTransition(R.anim.nothing, R.anim.nothing)
        finish()
    }
}