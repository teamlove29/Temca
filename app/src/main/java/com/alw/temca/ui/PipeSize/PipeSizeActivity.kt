package com.alw.temca.ui.PipeSize

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.Common.Common
import com.alw.temca.MainActivity
import com.alw.temca.Model.ReportReslutPipeSizeModel
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.WireSize.TypeCableActivity
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_pipe_size.*
import kotlinx.android.synthetic.main.activity_wire_size.typeCableTextView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class PipeSizeActivity : AppCompatActivity() {
    final val TASK_NAME_REQUEST_CODE = 100
    final val TASK_LIST_PREF_KEY_SWITCH = "task_list_switch"
    final val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_PIPE = "task_list_type_cable_in_pipe"
    final val TASK_LIST_PREF_KEY_SIZE = "task_list_size"
    final val TASK_LIST_PREF_KEY_AMOUNT = "task_list_amount"
    final val TASK_LIST_PREF_KEY_CONDUIT = "task_list_conduit"
    final val PREF_NAME = "task_pipe"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pipe_size)
        loadData()
        switchButton()
        wayBackActivity2.visibility = View.GONE
        tableBeforeCalculateInPipe.visibility = View.GONE
        calculator()



//        editTextAmountCable.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus){
//                editTextAmountCable.hint = " "
//            }
//        }

        editTextAmountCable.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()){
                    editTextAmountCable.hint = "20"
                }else{
                    editTextAmountCable.hint = ""
                }
                wayBackActivity1.visibility = View.VISIBLE
                wayBackActivity2.visibility = View.GONE
                tableBeforeCalculateInPipe.visibility = View.GONE
                btnCalInPipeSize.visibility = View.VISIBLE
                btnCalInPipeSize.apply {
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("amount", s.toString())
            }

        })
    }

    private fun calculator() {

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
            btnCalInPipeSize.visibility = View.VISIBLE
        }
    }

    fun calculatorOnClick(view: View) {

        val sizeCable = arrayListOf<String>("1 มม2", "1.5 มม2", "2.5 มม2", "4 มม2", "6 มม2", "10 มม2", "16 มม2", "25 มม2", "35 มม2", "50 มม2", "70 มม2", "95 มม2", "120 มม2", "150 มม2", "185 มม2", "240 มม2", "300 มม2", "400 มม2", "500 มม2")
        val sizeConduit = arrayListOf<String>("50x75 มม.", "50x100 มม.", "75x100 มม.", "100x100 มม.", "100x150 มม.", "100x200 มม.", "100x250 มม.", "100x300 มม.", "150x300 มม.")
        var typeCable = ""
        if (editTextAmountCable.text.isEmpty()){
            editTextAmountCable.setText("20")
        }

        tableBeforeCalculateInPipe.visibility = View.VISIBLE
        editTextAmountCable.clearFocus()
        btnCalInPipeSize.apply {
            hideKeyboard()
        }
        wayBackActivity1.visibility = View.GONE
        wayBackActivity2.visibility = View.VISIBLE
        btnCalInPipeSize.visibility = View.GONE

        val fineSheetInTableCableSize = when(typeCableTextView.text){
                    "IEC01" -> 0
                    "IEC10 2C" -> 1
                    "IEC10 3C" -> 2
                    "IEC10 4C" -> 3
                    "NYY 1C" -> 4
                    "NYY 2C" -> 5
                    "NYY 3C" -> 6
                    "NYY 4C" -> 7
                    "XLPE 1C" -> 8
                    "XLPE 2C" -> 9
                    "XLPE 3C" -> 10
                    "XLPE 4C" -> 11
                    else -> return
        }




        if(switchButtonPipeSize.isChecked == false){
            try {
                val typeCable = applicationContext.assets.open("TypeCable_Table.xls")
                val wb = Workbook.getWorkbook(typeCable)
                val sheet = wb.getSheet(fineSheetInTableCableSize)
                val typeCabletitle = sheet.getCell(0, 0).contents
                if (typeCableTextView.text == typeCabletitle){
                    sizeCable.forEachIndexed { index, size  ->
                        if (cableSizeTextView.text == size){
                            for (i in 1..12){
                                //  i is col result ขนาดท่อ
                                if (editTextAmountCable.text.toString().toInt() <= sheet.getCell(i, index + 1).contents.toInt()){
                                    textViewShow4InPipe.text = "${sheet.getCell(i, 0).contents} (${sheet.getCell(i, index + 1).contents} เส้น)"
                                    break
                                }else{
                               textViewShow4InPipe.text = "- เส้น"
//                                    if(editTextAmountCable.text.toString().toInt() >= sheet.getCell(i, index + 1).contents.toInt() && sheet.getCell(i, index + 1).contents.toInt() != 0){
//                                        textViewShow4InPipe.text = "${sheet.getCell(i, 0).contents} (${sheet.getCell(i, index + 1).contents} เส้น)"
//                                    }
                                }
                            }
                            for (j in 14..22){
                                //  j is col result ขนาดราง
                                if(editTextAmountCable.text.toString().toInt() <= sheet.getCell(j, index + 1).contents.toInt()){
                                    textViewShow6InPipe.text = "${sheet.getCell(j, 0).contents} (${sheet.getCell(j, index + 1).contents} เส้น)"
                                    break
                                }else{
                                textViewShow6InPipe.text = "- เส้น"
//                                    if(editTextAmountCable.text.toString().toInt() >= sheet.getCell(j, index + 1).contents.toInt() && sheet.getCell(j, index + 1).contents.toInt() != 0){
//                                        textViewShow6InPipe.text = "${sheet.getCell(j, 0).contents} (${sheet.getCell(j, index + 1).contents} เส้น)"
//                                    }
                                }
                            }
                        } }

                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error : $e", Toast.LENGTH_LONG).show()
            }
        }else{
            try {
                val typeCable = applicationContext.assets.open("TypeCable_Table.xls")
                val wb = Workbook.getWorkbook(typeCable)
                val sheet = wb.getSheet(fineSheetInTableCableSize)
                val typeCabletitle = sheet.getCell(0, 0).contents
                if (typeCableTextView.text == typeCabletitle){
                    sizeCable.forEachIndexed { indexSize, size  ->
                        if (cableSizeTextView.text == size){
                            sizeConduit.forEachIndexed{ indexConduit, conduit ->
                                if(SizeConduitTextView.text == conduit ){
                                    // ขนาด สายไฟสุงสุด
                                    textViewShow8InPipe.text = "${sheet.getCell(indexConduit + 14, indexSize + 1).contents} เส้น"
                                    if (sheet.getCell(indexConduit + 14, indexSize + 1).contents == "0"){
                                        textViewShow8InPipe.text = "- เส้น"
                                    }
                                }
                            }
                        } }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error : $e", Toast.LENGTH_LONG).show()
            }
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){

//                val dataPhase = data!!.getIntExtra("dataPhase",0)
                val dataSizeCable = data!!.getStringExtra("dataSizeCable")
                val dataTypeCable = data.getStringExtra("dataTypeCable")
                val dataConduit = data.getStringExtra("dataSizeConduit")


                if (dataSizeCable != null) {
                    cableSizeTextView.text = dataSizeCable
                    saveData("sizeCable", dataSizeCable)
                }
                if (dataTypeCable != null) {
                    typeCableTextView.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)
                }
                if (dataConduit != null){
                    SizeConduitTextView.text = dataConduit
                    saveData("conduit", dataConduit)
                }
            }
        }
        wayBackActivity1.visibility = View.VISIBLE
        wayBackActivity2.visibility = View.GONE
        btnCalInPipeSize.visibility = View.VISIBLE
        tableBeforeCalculateInPipe.visibility = View.GONE
    }


    fun typeCableOnClickInPipeSize(view: View) {
            val intent = Intent(this, TypeCableActivity::class.java)
             intent.putExtra("Activity", "PipeSize");
                startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun sizeCableOnClickInPipeSize(view: View) {
            val intent = Intent(this, SizeCableActivity::class.java)
             startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun sizeConduitOnClickInPipeSize(view: View) {
            val intent = Intent(this, SizeConduitActivity::class.java)
             startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }




    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {

            if (type == "sizeCable"){
                putString(TASK_LIST_PREF_KEY_SIZE, data)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_PIPE, data)
            }
            if (type == "amount"){
                putString(TASK_LIST_PREF_KEY_AMOUNT, data)
            }
            if (type == "conduit"){
                putString(TASK_LIST_PREF_KEY_CONDUIT, data)
            }
            commit()
        }
    }

    fun loadData(){
    val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val dataOfSizeCable = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE, "2.5 มม2")
    val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_PIPE, "IEC01")
    val dataOfAmount = sharedPref.getString(TASK_LIST_PREF_KEY_AMOUNT, null)
    val dataOfConduit = sharedPref.getString(TASK_LIST_PREF_KEY_CONDUIT, "50x75 มม.")

    SizeConduitTextView.text = dataOfConduit
    cableSizeTextView.text = dataOfSizeCable
    typeCableTextView.text = dataOfTypeCable
//    editTextAmountCable.setText(dataOfAmount)
    }


    override fun onRestart() {
        super.onRestart()
            val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
            sharedPref.apply()
    }

    fun setAmountOnClick(view: View) {
        editTextAmountCable.setText("")
        editTextAmountCable.hint = " "
        editTextAmountCable.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextAmountCable, InputMethodManager.SHOW_IMPLICIT)
    }

    fun pipeSizeReportOnClick(view: View) {
        val intent = Intent(this,PipeSizeReportActivity::class.java)
        val bundle = Bundle()

        if(switchButtonPipeSize.isChecked == true){
            bundle.putParcelable("resultMaxCable",ReportReslutPipeSizeModel(
                    typeCableTextView.text.toString(),cableSizeTextView.text.toString(),
                    "","","",SizeConduitTextView.text.toString(),textViewShow8InPipe.text.toString()))
            intent.putExtras(bundle)

        }else{
            bundle.putParcelable("resultPipeSize",ReportReslutPipeSizeModel(
                    typeCableTextView.text.toString(),cableSizeTextView.text.toString(),
                    editTextAmountCable.text.toString(), textViewShow4InPipe.text.toString(),
                    textViewShow6InPipe.text.toString(),"",""))
            intent.putExtras(bundle)
        }

        startActivity(intent)
        finish()
    }



    fun backOnClick(view: View) {
        finish()
    }

    fun sponsorOnClick(view: View) {
        val intent = Intent(this,SponsorActivity::class.java)
        startActivity(intent)
    }


}

