package com.alw.temca.ui.ElectricalThreePhase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_circuit_in_three_phase.*


class CircuitInThreePhaseActivity : AppCompatActivity() {
    companion object{
        private val TASK_LIST_PREF_KEY_CIRCUIT = "task_list_circuit"
        private val TASK_LIST_PREF_AMOUNT_ROW = "task_list_amount_row"
        private val PREF_NAME = "task_name"
        private val listAmp = arrayListOf("16","20","25","32","40","50","63","80","100","125","160","200","250","320","400","500","630","800","1000")
        private var RowAmountAmpMain:Int = 99
    }

    fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            if (type == "RowAmountAmp") putString(TASK_LIST_PREF_AMOUNT_ROW, data)
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_in_three_phase)


        var intent: Intent
        intent = getIntent()
        val nameA = intent.getStringExtra("data") // รับค่าจากการเลือก amp
        val RowAmountAmp = intent.getIntExtra("RowAmountAmp",99) //รับค่าจากหน้าหลัก

        if (RowAmountAmp != 99){
            RowAmountAmpMain = RowAmountAmp
            saveData("RowAmountAmp", RowAmountAmpMain.toString())
        }
        else{
//            loadData()
        }



        if (nameA != null){
            textViewCircuit.text = nameA // Circuit Breaker
            editTextOperating.setText(nameA.replace("A","")) // กระแสใช้งาน A
        }

        cardViewCircuitBreaker.setOnClickListener {
            intent = Intent(this, CircuitBreakerInThreePhaseActivity::class.java)
            intent.putExtra("RowAmountAmp", RowAmountAmpMain)
            startActivity(intent)
            finish()
        }

        editTextOperating.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty() && RowAmountAmpMain != 99) {
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
                } else {
//                    if(memoryGroup == 7) textViewCircuit.text = "400A"
//                    else textViewCircuit.text = "40A"
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun SubmitCircuitOnClick(view: View) {
        val Intent = Intent(this, ThreePhaseActivity::class.java)
        Intent.putExtra("dataCircuit",textViewCircuit.text)
        startActivity(Intent)
        finish()
    }

    fun AmpOnClick(view: View) {
        editTextOperating.setText("")
        editTextOperating.hint = " "
        editTextOperating.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextOperating, InputMethodManager.SHOW_IMPLICIT)
    }
}