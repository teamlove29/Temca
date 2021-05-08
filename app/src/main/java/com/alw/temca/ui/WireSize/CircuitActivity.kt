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
    private val TASK_LIST_PREF_AMOUNT_ROW_GROUP_7 = "task_list_amount_row_group_7"
    private val PREF_NAME = "task_name"
    private val listAmp = arrayListOf<String>("16","20","25","32","40","50","63","80","100","125","160","200","250","320","400","500","630","800","1000")
    private val listAmpGroup7 = arrayListOf<String>("400","500","630","800","1000","1250","1600","2000")
    private var RowAmountAmpMain:Int = 99
    private var RowAmountAmpGroup7:Int = 7
    private var memoryGroup:Int = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit)

        var intent: Intent
        intent = getIntent()
        val nameA = intent.getStringExtra("data") // รับค่าจากการเลือก amp
        val RowAmountAmp = intent.getIntExtra("RowAmountAmp",99) //รับค่าจากหน้าหลัก

        if (RowAmountAmp != 99){
            if(RowAmountAmp == 77){ // ถ้าส่งข้อมูลมาเป็น 77 ให้ save RowAmountAmpMain เป็น 7 และ memoryGroup = 7 ถ้าไม่ก็จะปกติ
                RowAmountAmpMain = RowAmountAmpGroup7
                memoryGroup = 7
                textViewCircuit.text = "400A" // Circuit Breaker
                editTextOperating.setText("400") // กระแสใช้งาน A
            }else{
                RowAmountAmpMain = RowAmountAmp
            }
            saveData("RowAmountAmp",RowAmountAmpMain.toString())
            saveData("memoryGroup",memoryGroup.toString())
        }
        else{
            loadData()
        }

        if(RowAmountAmpMain != 99) {
            if(RowAmountAmpMain == 7){
                cardViewCircuitBreaker.setOnClickListener {
                    intent = Intent(this, CircuitBreakerActivity::class.java)
                    intent.putExtra("RowAmountAmp",77)
                    startActivity(intent)
                    finish()
                }
            }else{
                cardViewCircuitBreaker.setOnClickListener {
                    intent = Intent(this, CircuitBreakerActivity::class.java)
                    intent.putExtra("RowAmountAmp",RowAmountAmpMain)
                    startActivity(intent)
                    finish()
                }
            }
        }

        if (nameA != null){
            textViewCircuit.text = nameA // Circuit Breaker
            editTextOperating.setText(nameA.replace("A","")) // กระแสใช้งาน A
        }

        editTextOperating.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.isNotEmpty() && RowAmountAmpMain!= 99) {
                    val sToInt = Integer.parseInt(s.toString())
                    if(memoryGroup == 7){
                        for (i in 0..RowAmountAmpMain){
                        if(sToInt <= listAmpGroup7[i].toInt()){
                            textViewCircuit.text = "${listAmpGroup7[i]}A"
                            editTextOperating.hint = " "
                            break
                        }else {
                            textViewCircuit.text = "${listAmpGroup7[RowAmountAmpGroup7]}A"
                            editTextOperating.hint = " "
                        }
                    }
                    }
                        else{
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
                    }

                } else {
                        if(memoryGroup == 7) textViewCircuit.text = "400A"
                            else textViewCircuit.text = "40A"
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
//
    fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT, "40A")
        val rowAmountOfCircuit = sharedPref.getString(TASK_LIST_PREF_AMOUNT_ROW, "99")
        val rowAmountOfCircuitGroup7 = sharedPref.getString(TASK_LIST_PREF_AMOUNT_ROW_GROUP_7, "99")

        RowAmountAmpMain = rowAmountOfCircuit!!.toInt()
        memoryGroup = rowAmountOfCircuitGroup7!!.toInt()
         textViewCircuit.text = dataOfCircuit
        editTextOperating.setText(dataOfCircuit!!.replace("A",""))
    }

    fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            if (type == "RowAmountAmp") putString(TASK_LIST_PREF_AMOUNT_ROW, data)
            if (type == "memoryGroup") putString(TASK_LIST_PREF_AMOUNT_ROW_GROUP_7, data)
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