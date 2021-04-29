package com.alw.temca.ui.WireSize


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable

import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.R

import kotlinx.android.synthetic.main.activity_circuit.*
import kotlinx.android.synthetic.main.activity_circuit.cardViewCircuitBreaker
import kotlinx.android.synthetic.main.activity_wire_size.*


class CircuitActivity : AppCompatActivity() {
    private val TASK_LIST_PREF_KEY_CIRCUIT = "task_list_circuit"
    private val TASK_LIST_PREF_AMOUNT_ROW = "task_list_amount_row"
    private val PREF_NAME = "task_name"
    private var RowAmountAmpMain:Int = 99
    private val listAmp = arrayListOf<String>("16","20","25","32","40","50","63","80","100","125","160","200","250","320","400","500","630","800","1000")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit)

        var intent: Intent
        intent = getIntent()
        val nameA = intent.getStringExtra("data")
        val RowAmountAmp = intent.getIntExtra("RowAmountAmp",99)


        if (RowAmountAmp != 99){
            RowAmountAmpMain = RowAmountAmp
            saveData("RowAmountAmp",RowAmountAmp.toString())
        }else{
            loadData()
        }

        if(RowAmountAmpMain != 99) {
            cardViewCircuitBreaker.setOnClickListener {
                intent = Intent(this, CircuitBreakerActivity::class.java)
                intent.putExtra("RowAmountAmp",RowAmountAmpMain)
                startActivity(intent)
                finish()
            }
        }

        if (nameA != null){
            textViewCircuit.text = nameA
            editTextOperating.setText(nameA.replace("A",""))
        }

        editTextOperating.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty() && RowAmountAmpMain!= 99) {
                    val sToInt = Integer.parseInt(s.toString())
                    for (i in 0..RowAmountAmpMain){
                        if(sToInt <= listAmp[i].toInt()){
                            textViewCircuit.text = "${listAmp[i]}A"
                            editTextOperating.hint = " "
                            break
                        }else {
                            textViewCircuit.text = "${listAmp[RowAmountAmpMain]}A"
                            editTextOperating.hint = ""
                        }
                    }
                } else textViewCircuit.text = "40A"
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun SubmitCircuitOnClick(view: View) {
//        saveData("circuit", textViewCircuit.text.toString())
        val Intent = Intent(this,WireSizeActivity::class.java)
        Intent.putExtra("dataCircuit",textViewCircuit.text)
        startActivity(Intent)
        finish()
    }

    fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT, "40A")
        val rowAmountOfCircuit = sharedPref.getString(TASK_LIST_PREF_AMOUNT_ROW, "99")
        RowAmountAmpMain = rowAmountOfCircuit!!.toInt()
        textViewCircuit.text = dataOfCircuit
        editTextOperating.setText(dataOfCircuit!!.replace("A",""))
    }

    fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            if (type == "RowAmountAmp") putString(TASK_LIST_PREF_AMOUNT_ROW, data)
            commit()
        }
    }

    fun AmpOnClick(view: View) {
        editTextOperating.setText("")
        editTextOperating.hint = " "
        editTextOperating.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextOperating, InputMethodManager.SHOW_IMPLICIT)

    }
}