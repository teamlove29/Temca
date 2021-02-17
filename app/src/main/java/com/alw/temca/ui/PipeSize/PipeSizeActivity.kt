package com.alw.temca.ui.PipeSize

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alw.temca.R
import com.alw.temca.ui.WireSize.TypeCableActivity
import kotlinx.android.synthetic.main.activity_pipe_size.*
import kotlinx.android.synthetic.main.activity_wire_size.typeCableTextView


class PipeSizeActivity : AppCompatActivity() {
    final val TASK_NAME_REQUEST_CODE = 100
    final val TASK_LIST_PREF_KEY_SWITCH = "task_list_switch"
    final val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_PIPE = "task_list_type_cable_in_pipe"
    final val TASK_LIST_PREF_KEY_SIZE = "task_list_size"
    final val TASK_LIST_PREF_KEY_AMOUNT = "task_list_amount"
    final val PREF_NAME = "task_pipe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pipe_size)
        loadData()
        switchButton()
        tableBeforeCalculateInPipe.visibility = View.GONE

        editTextAmountCable.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tableBeforeCalculateInPipe.visibility = View.GONE
                btnCalInPipeSize.apply {
                }
            }
            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("amount",s.toString())
            }

        })

    }

    fun switchButton(){
        if (switchButtonPipeSize.isChecked == false){
            titlePipeSize.text = "หาขนาดท่อและราง"
            cardViewSizeConduit.visibility = View.GONE
            textViewShow7InPipe.visibility = View.GONE
            textViewShow8InPipe.visibility = View.GONE
            textViewShow3InPipe.visibility = View.VISIBLE
            textViewShow4InPipe.visibility = View.VISIBLE
            textViewShow5InPipe.visibility = View.VISIBLE
            textViewShow6InPipe.visibility = View.VISIBLE
            cardViewAmountCable.visibility = View.VISIBLE

        }else{
            titlePipeSize.text = "หาจำนวนสายในท่อ"
            cardViewSizeConduit.visibility = View.VISIBLE
            textViewShow7InPipe.visibility = View.VISIBLE
            textViewShow8InPipe.visibility = View.VISIBLE
            cardViewAmountCable.visibility = View.GONE
            textViewShow3InPipe.visibility = View.GONE
            textViewShow4InPipe.visibility = View.GONE
            textViewShow5InPipe.visibility = View.GONE
            textViewShow6InPipe.visibility = View.GONE
        }

        switchButtonPipeSize.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                tableBeforeCalculateInPipe.visibility = View.GONE
                titlePipeSize.text = "หาจำนวนสายในท่อ"
                cardViewSizeConduit.visibility = View.VISIBLE
                textViewShow7InPipe.visibility = View.VISIBLE
                textViewShow8InPipe.visibility = View.VISIBLE
                cardViewAmountCable.visibility = View.GONE
                textViewShow3InPipe.visibility = View.GONE
                textViewShow4InPipe.visibility = View.GONE
                textViewShow5InPipe.visibility = View.GONE
                textViewShow6InPipe.visibility = View.GONE

            }else{
                tableBeforeCalculateInPipe.visibility = View.GONE
                titlePipeSize.text = "หาขนาดท่อและราง"
                cardViewSizeConduit.visibility = View.GONE
                textViewShow7InPipe.visibility = View.GONE
                textViewShow8InPipe.visibility = View.GONE
                textViewShow3InPipe.visibility = View.VISIBLE
                textViewShow4InPipe.visibility = View.VISIBLE
                textViewShow5InPipe.visibility = View.VISIBLE
                textViewShow6InPipe.visibility = View.VISIBLE
                cardViewAmountCable.visibility = View.VISIBLE
            }
        }
    }

    fun calculatorOnClick(view: View) {
        if (editTextAmountCable.text.isEmpty()){
            editTextAmountCable.setText("2")
        }
        tableBeforeCalculateInPipe.visibility = View.VISIBLE
        editTextAmountCable.clearFocus()
        btnCalInPipeSize.apply {
            hideKeyboard()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){

//                val dataPhase = data!!.getIntExtra("dataPhase",0)
                val dataSizeCable = data!!.getStringExtra("dataSizeCable")
                val dataTypeCable = data!!.getStringExtra("dataTypeCable")
                val dataAmount = data!!.getStringExtra("dataAmount")

//                if (dataPhase != 0){
//                    phaseTextView.text = "$dataPhase เฟส"
//                    saveData("phase", dataPhase.toString())
//                }
                if (dataSizeCable != null) {
                    cableSizeTextView.text = dataSizeCable
                    saveData("sizeCable", dataSizeCable)
                }
                if (dataTypeCable != null) {
                    typeCableTextView.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)
                }
            }
        }
        tableBeforeCalculateInPipe.visibility = View.GONE
    }


    fun typeCableOnClickInPipeSize(view: View) {
            val intent = Intent(this, TypeCableActivity::class.java)
             intent.putExtra("Activity","PipeSize");
                startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun sizeCableOnClickInPipeSize(view: View) {
            val intent = Intent(this, SizeCableActivity::class.java)
             startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun sizeConduitOnClickInPipeSize(view: View) {
            val intent = Intent(this, SizeConduitActivity::class.java)
             startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }




    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
//            if (type == "phase"){
//                putString(TASK_LIST_PREF_KEY_PHASE, data)
//            }
            if (type == "sizeCable"){
                putString(TASK_LIST_PREF_KEY_SIZE, data)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_PIPE, data)
            }
            if (type == "amount"){
                putString(TASK_LIST_PREF_KEY_AMOUNT, data)
            }
            commit()
        }
    }

    fun loadData(){
    val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//    val dataOfPhase = sharedPref.getString(TASK_LIST_PREF_KEY_PHASE, "1")
    val dataOfSizeCable = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE, "2.5 มม2")
    val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_PIPE, "IEC01")
    val dataOfAmount = sharedPref.getString(TASK_LIST_PREF_KEY_AMOUNT, "2")
//
//
//    phaseTextView.text = "$dataOfPhase เฟส"
    cableSizeTextView.text = dataOfSizeCable
    typeCableTextView.text = dataOfTypeCable
    editTextAmountCable.setText(dataOfAmount)
    }


    override fun onRestart() {
        super.onRestart()
            val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
            sharedPref.apply()
    }


}

