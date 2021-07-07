package com.alw.temca.ui.AmountInPipe

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_amount_in_pipe.*
import kotlinx.android.synthetic.main.activity_amount_in_pipe.btnCalInPipeSize
import kotlinx.android.synthetic.main.activity_amount_in_pipe.cableSizeTextView
import kotlinx.android.synthetic.main.activity_amount_in_pipe.editTextAmountCable
import kotlinx.android.synthetic.main.activity_amount_in_pipe.tableBeforeCalculateInPipe
import kotlinx.android.synthetic.main.activity_amount_in_pipe.typeCableTextView
import kotlinx.android.synthetic.main.activity_amount_in_pipe.wayBackActivity1
import kotlinx.android.synthetic.main.activity_amount_in_pipe.wayBackActivity2
import kotlinx.android.synthetic.main.activity_pipe_size.*
import kotlinx.android.synthetic.main.activity_wire_size.*
import java.io.IOException


class AmountInPipeActivity : AppCompatActivity() {

    companion object{
         const val TASK_NAME_REQUEST_CODE = 100
         const val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_AMOUNT_PIPE = "task_list_type_cable_in_amount_pipe"
         const val TASK_LIST_PREF_KEY_SIZE_IN_AMOUNT_PIPE = "task_list_size_in_amount_pipe"
         const val TASK_LIST_PREF_KEY_AMOUNT_IN_AMOUNT_PIPE = "task_list_amount_in_amount_pipe"
         const val PREF_NAME = "task_pipe"
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_AMOUNT_PIPE, "IEC01")
        val dataOfSizeCable = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE_IN_AMOUNT_PIPE, "2.5 mm2")
        val dataOfAmount = sharedPref.getString(TASK_LIST_PREF_KEY_AMOUNT_IN_AMOUNT_PIPE, "20")

        typeCableTextView.text = dataOfTypeCable
        cableSizeTextView.text = dataOfSizeCable
        editTextAmountCable.setText(dataOfAmount)

    }

    private fun saveData(type: String, value: String){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {

            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_AMOUNT_PIPE, value)
            }
            if (type == "sizeCable"){
                putString(TASK_LIST_PREF_KEY_SIZE_IN_AMOUNT_PIPE, value)
            }
            if (type == "amount"){
                putString(TASK_LIST_PREF_KEY_AMOUNT_IN_AMOUNT_PIPE, value)
            }
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount_in_pipe)

        wayBackActivity2.visibility = View.GONE
        tableBeforeCalculateInPipe.visibility = View.GONE

        editTextAmountCable.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //ก่อนเปลี่ยนคือ ?
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isEmpty()) {
                editTextAmountCable.hint = "20"
            } else {
                editTextAmountCable.hint = ""
            }
            wayBackActivity1.visibility = View.VISIBLE
            wayBackActivity2.visibility = View.GONE
            tableBeforeCalculateInPipe.visibility = View.GONE
            btnCalInPipeSize.visibility = View.VISIBLE
        }
        override fun afterTextChanged(s: Editable?) {
            //หลังจากพิมพ์ผลลัพคือ ?
            saveData("amount", s.toString())
        } })
    }

    // เลือกชนิดสายไฟ
    fun onclickChooseTypeCable(view: View) {
        val intent = Intent(this, TypeCableInPipeActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }

    // เลือกขนาดสายไฟ
    fun onClickChooseSizeCable(view: View) {
        val intent = Intent(this, SizeCableInPipeActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }

    // จำนวนเส้น
    fun setAmountOnClick(view: View) {
        editTextAmountCable.setText("")
        editTextAmountCable.hint = " "
        editTextAmountCable.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextAmountCable, InputMethodManager.SHOW_IMPLICIT)
    }

    // คำนวน
    fun calculatorOnClick(view: View) {

        // ถ้าจำนวนเส้นเป็นค่าว่าง = 20
        if (editTextAmountCable.text.isEmpty()){
            editTextAmountCable.setText("20")
        }
        // ลบ 0 ถ้าใส่ 0 นำหน้า
        if(editTextAmountCable.length() > 0 ){
            if(editTextAmountCable.text.toString() == "0" ) editTextAmountCable.setText("20")
            else if(editTextAmountCable.text.toString() == "00") editTextAmountCable.setText("20")
            else if(editTextAmountCable.text.toString() == "000") editTextAmountCable.setText("20")
            else if(editTextAmountCable.text.toString() == "0000") editTextAmountCable.setText("20")
            else if(editTextAmountCable.text.toString().slice(0..0) == "0"){
                for(i in 0..3){
                    if(editTextAmountCable.text.toString().slice(0..0) != "0") break
                    else editTextAmountCable.setText(editTextAmountCable.text.toString().slice(1 until editTextAmountCable.length()))
                }
            }
        }
        tableBeforeCalculateInPipe.visibility = View.VISIBLE
        wayBackActivity1.visibility = View.GONE
        wayBackActivity2.visibility = View.VISIBLE
        btnCalInPipeSize.visibility = View.GONE
        editTextAmountCable.clearFocus()
        btnCalInPipeSize.apply { hideKeyboard() }


        val findSheetInTableCableSize = when(typeCableTextView.text){
            "IEC01" -> 0
            "NYY 1C" -> 1
            "NYY 2C" -> 2
            "NYY 3C" -> 3
            "NYY 4C" -> 4
            "XLPE 1C" -> 5
            "XLPE 2C" -> 6
            "XLPE 3C" -> 7
            "XLPE 4C" -> 8
            "VCT 1C" -> 9
            "VCT 2C" -> 10
            "VCT 3C" -> 11
            "VCT 4C" -> 12
            "NYY 2C - G" -> 13
            "NYY 3C - G" -> 14
            "NYY 4C - G" -> 15
            "VCT 2C - G" -> 16
            "VCT 3C - G" -> 17
            "VCT 4C - G" -> 18
            else -> return
        }
        val sizeCable = arrayListOf("1 mm2", "1.5 mm2", "2.5 mm2", "4 mm2", "6 mm2", "10 mm2", "16 mm2", "25 mm2", "35 mm2", "50 mm2", "70 mm2", "95 mm2", "120 mm2", "150 mm2", "185 mm2", "240 mm2", "300 mm2", "400 mm2", "500 mm2", "630 mm2", "800 mm2")
        println("dasdada ${cableSizeTextView.text}")
        try {
            val typeCable = applicationContext.assets.open("type_cable_pipe.xls")
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(findSheetInTableCableSize)
            val typeCabletitle = sheet.getCell(0, 0).contents

            if (typeCableTextView.text == typeCabletitle){
                sizeCable.forEachIndexed { index, sizeCable  -> // หาขนาดสาย mm2
                    if (cableSizeTextView.text == sizeCable){
                        for (i in 1..12){ // แนวนอนขนาดสาย
                            //  i is col result ขนาดท่อ
                            if (editTextAmountCable.text.toString().toInt() <= sheet.getCell(i, index + 1).contents.toInt()){
                                textViewResultSizePipe.text = "${sheet.getCell(i, 0).contents} (${sheet.getCell(i, index + 1).contents} เส้น)"
                                break
                            }else textViewResultSizePipe.text = "- เส้น"


                        }
                        for (j in 14..22){ // แนวนอนขนาดท่อ
                            //  j is col result ขนาดราง
                            if(editTextAmountCable.text.toString().toInt() <= sheet.getCell(j, index + 1).contents.toInt()){
                                textViewResultRailSize.text = "${sheet.getCell(j, 0).contents} (${sheet.getCell(j, index + 1).contents} เส้น)"
                                break
                            }else textViewResultRailSize.text = "- เส้น"
//

                        }
                    } }

            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error : $e", Toast.LENGTH_LONG).show()
        }
    }

    // สร้างรายงาน
    fun pipeSizeReportOnClick(view: View) {}


    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
    fun backOnClick(view: View) {
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
        sharedPref.apply()
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
        sharedPref.apply()
        finish()
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK) {

                val dataTypeCable = data!!.getStringExtra("dataTypeCable")
                val dataSizeCable = data!!.getStringExtra("dataSizeCable")

                if (dataTypeCable != null) {
                    typeCableTextView.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)
                }
                if (dataSizeCable != null) {
                    cableSizeTextView.text = dataSizeCable
                    saveData("sizeCable", dataSizeCable)
                }
            }
        }
        wayBackActivity1.visibility = View.VISIBLE
        wayBackActivity2.visibility = View.GONE
        btnCalInPipeSize.visibility = View.VISIBLE
        tableBeforeCalculateInPipe.visibility = View.GONE
    }

}