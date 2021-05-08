package com.alw.temca.ui.WireSize

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
    private val listAmpGroup7 = arrayListOf<String>("400","500","630","800","1000","1250","1600","2000")
    private val CircuitItem = ArrayList<CircuitModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_breaker)

        intent = getIntent()
        val RowAmountAmp = intent.getIntExtra("RowAmountAmp",99)


        if(RowAmountAmp != 99){
      if(RowAmountAmp == 77){
              for (i in 0..7){ CircuitItem.add(CircuitModel("${listAmpGroup7[i]}A")) }
          }else{
              for (i in 0..RowAmountAmp){ CircuitItem.add(CircuitModel("${listAmp[i]}A")) }
          }
        }

        recyclerViewCircuit.adapter = CircuitAdapter(CircuitItem,this)
        recyclerViewCircuit.layoutManager = LinearLayoutManager(this)

    }

    override fun onClick(postion: Int) {
        val Intent = Intent(this,CircuitActivity::class.java)
        Intent.putExtra("data",CircuitItem[postion].name)
        startActivity(Intent)
        overridePendingTransition(R.anim.nothing,R.anim.nothing)
        finish()
    }
}