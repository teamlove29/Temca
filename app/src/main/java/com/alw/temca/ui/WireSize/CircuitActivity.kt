package com.alw.temca.ui.WireSize

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.R
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_circuit.*
import kotlinx.android.synthetic.main.activity_circuit.cardViewCircuitBreaker
import kotlinx.android.synthetic.main.activity_wire_size.*


class CircuitActivity : AppCompatActivity() {
    final val TASK_LIST_PREF_KEY_CIRCUIT = "task_list_circuit"
    final val PREF_NAME = "task_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit)

        loadData()
        var intent: Intent

        cardViewCircuitBreaker.setOnClickListener {
            intent = Intent(this, CircuitBreakerActivity::class.java)
            startActivity(intent)
            finish()
        }

        intent = getIntent()
        val nameA = intent.extras?.getString("data")
        if (nameA != null){
            textViewCircuit.text = nameA
            editTextOperating.setText(nameA.replace("A",""))
        }



        editTextOperating.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    val typeCable = applicationContext.assets.open("CircuitSize.xls")
                    val wb = Workbook.getWorkbook(typeCable)
                    val sheet = wb.getSheet(0)
                    val sToInt = Integer.parseInt(s.toString())

                    for (i in 0..17) {
                        val sizeCirCuit = sheet.getCell(0, i).contents.toInt()
                        if (sToInt <= sizeCirCuit) {
//                            val sizeWireGround = sheet.getCell(2, i).contents
                            textViewCircuit.text = "${sizeCirCuit}A"
//                            textViewGroundWire.text = Html.fromHtml("${sizeWireGround} มม<sup><small><small>2</small></small></sup>")
                            break
                        } else {
                            textViewCircuit.text = "1000A"
//                            textViewGroundWire.text = Html.fromHtml("70 มม<sup><small><small>2</small></small></sup>")
                        }
                    }

                } else {
                    textViewCircuit.text = "40A"
//                    textViewGroundWire.text = Html.fromHtml("4 มม<sup><small><small>2</small></small></sup>")
                }
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

//    fun saveData(type: String, value: String){
//        val data = value
//        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
//        with(sharedPref.edit()) {
//            if (type == "circuit"){
//                putString(TASK_LIST_PREF_KEY_CIRCUIT, data)
//            }
//            commit()
//        }
//    }

    fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT, "40A")
        textViewCircuit.text = dataOfCircuit
        editTextOperating.setText(dataOfCircuit!!.replace("A",""))
    }


}