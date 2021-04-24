package com.alw.temca.ui.WireSize

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.CircuitAdapter
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Model.CircuitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_circuit_breaker.*

class CircuitBreakerActivity : AppCompatActivity(), InstallationonClickAdapterListener {
    private val listAmp = arrayListOf<String>("16","20","25","32","40","50","63","80","100","125","160","200","250","320","400","500","630","800","1000")
    private val CircuitItem = ArrayList<CircuitModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_breaker)

        intent = getIntent()
        val RowAmountAmp = intent.getIntExtra("RowAmountAmp",99)
        println("RowAmountAmp2 $RowAmountAmp")
        if(RowAmountAmp != 99){
            for (i in 0..RowAmountAmp){
                CircuitItem.add(CircuitModel("${listAmp[i]}A"))
            }
        }
        recyclerViewCircuit.adapter = CircuitAdapter(CircuitItem,this)
        recyclerViewCircuit.layoutManager = LinearLayoutManager(this)

//        CircuitItem.add(CircuitModel("16A"))
//        CircuitItem.add(CircuitModel("20A"))
//        CircuitItem.add(CircuitModel("25A"))
//        CircuitItem.add(CircuitModel("32A"))
//        CircuitItem.add(CircuitModel("40A"))
//        CircuitItem.add(CircuitModel("50A"))
//        CircuitItem.add(CircuitModel("63A"))
//        CircuitItem.add(CircuitModel("80A"))
//        CircuitItem.add(CircuitModel("100A"))
//        CircuitItem.add(CircuitModel("125A"))
//        CircuitItem.add(CircuitModel("160A"))
//        CircuitItem.add(CircuitModel("200A"))
//        CircuitItem.add(CircuitModel("250A"))
//        CircuitItem.add(CircuitModel("320A"))
//        CircuitItem.add(CircuitModel("400A"))
//        CircuitItem.add(CircuitModel("500A"))
//        CircuitItem.add(CircuitModel("630A"))
//        CircuitItem.add(CircuitModel("800A"))
//        CircuitItem.add(CircuitModel("1000A"))
    }

    override fun onClick(postion: Int) {
        val Intent = Intent(this,CircuitActivity::class.java)
        Intent.putExtra("data",CircuitItem[postion].name)
        startActivity(Intent)
        overridePendingTransition(R.anim.nothing,R.anim.nothing)
        finish()
    }
}