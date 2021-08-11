package com.alw.temca.ui.AmountInRails

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.alw.temca.Model.AmountInRails.ResultRailsToReportModel
import com.alw.temca.R
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_amount_in_rails.*
import java.io.IOException


class AmountInRailsActivity : AppCompatActivity() {
    companion object {
        const val TASK_NAME_REQUEST_CODE = 100
        const val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_AMOUNT_RAILS = "task_list_type_cable_in_amount_rails"
        const val TASK_LIST_PREF_KEY_SIZE_IN_AMOUNT_RAILS = "task_list_size_in_amount_rails"
        const val TASK_LIST_PREF_KEY_AMOUNT_IN_AMOUNT_RAILS = "task_list_amount_in_amount_rails"
        const val PREF_NAME = "task_amount_in_rails"
        lateinit var maxamount:String
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_AMOUNT_RAILS, "IEC01")
        val dataOfSizeCable = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE_IN_AMOUNT_RAILS, "2.5 mm2")
        val dataOfRails = sharedPref.getString(TASK_LIST_PREF_KEY_AMOUNT_IN_AMOUNT_RAILS, "mm.")

        typeCableTextView.text = dataOfTypeCable
        cableSizeTextView.text = Html.fromHtml(dataOfSizeCable!!.replace("mm2","mm<sup><small>2</small></sup>"))
        SizeConduitTextView.text = dataOfRails
    }

    private fun saveData(type: String, value: String) {
        val sharedPref =
            getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {

            if (type == "typeCable") {
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_AMOUNT_RAILS, value)
            }
            if (type == "sizeCable") {
                putString(TASK_LIST_PREF_KEY_SIZE_IN_AMOUNT_RAILS, value)
            }
            if (type == "sizeConduit") {
                putString(TASK_LIST_PREF_KEY_AMOUNT_IN_AMOUNT_RAILS, value)
            }
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount_in_rails)

        switchButton()

        tableBeforeCalculateInPipe.visibility = View.GONE

        if(SizeConduitTextView.text == "mm.") {
            btnCalInPipeSize.isClickable = false
            btnCalInPipeSize.backgroundTintList = this.resources.getColorStateList(R.color.placeHolderBG)
        }
        else {
            btnCalInPipeSize.isClickable = true
            btnCalInPipeSize.backgroundTintList = this.resources.getColorStateList(R.color.btnBlue)
        }


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
                btnCalInPipeSize.apply {
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("amount", s.toString())
            }

        })

    }

    private fun switchButton() {
        if (switchButtonPipeSize.isChecked){
            titlePipeSize.text = "หาขนาดราง"
            cardViewSizeConduit.visibility = View.GONE
            cardViewAmountCable.visibility = View.VISIBLE

        }else{
            titlePipeSize.text = "หาจำนวนสายไฟสูงสุด"
            cardViewSizeConduit.visibility = View.VISIBLE
            cardViewAmountCable.visibility = View.GONE
        }

        switchButtonPipeSize.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                titlePipeSize.text = "หาขนาดราง"
                cardViewSizeConduit.visibility = View.GONE
                cardViewAmountCable.visibility = View.VISIBLE
                btnCalInPipeSize.isClickable = true
                btnCalInPipeSize.backgroundTintList = this.resources.getColorStateList(R.color.btnBlue)
                SizeConduitTextView.text = "mm."
                saveData("sizeConduit", "mm.")
            }else{
                titlePipeSize.text = "หาขนาดสายไฟสูงสุด"
                cardViewSizeConduit.visibility = View.VISIBLE
                cardViewAmountCable.visibility = View.GONE
                SizeConduitTextView.text = "mm."
                saveData("sizeConduit", "mm.")
                btnCalInPipeSize.isClickable = false
                btnCalInPipeSize.backgroundTintList = this.resources.getColorStateList(R.color.placeHolderBG)
            }
            wayBackActivity1.visibility = View.VISIBLE
            wayBackActivity2.visibility = View.GONE
            btnCalInPipeSize.visibility = View.VISIBLE
            tableBeforeCalculateInPipe.visibility = View.GONE
        }
    }

    fun calculatorOnClick(view: View) {

        btnCalInPipeSize.apply {
            hideKeyboard()
        }

        if (switchButtonPipeSize.isChecked){
            textViewMaxCable.visibility = View.GONE
            textViewResultMaxCable.visibility = View.GONE
            textViewRailsSize.visibility = View.VISIBLE
            textViewRailsSizeResult.visibility = View.VISIBLE
        }else{
            textViewMaxCable.visibility = View.VISIBLE
            textViewResultMaxCable.visibility = View.VISIBLE
            textViewRailsSize.visibility = View.GONE
            textViewRailsSizeResult.visibility = View.GONE
            textViewRailsSizeResult22.visibility = View.GONE
            textViewRailsSizeResult1.visibility = View.GONE
        }


        if (editTextAmountCable.text.isEmpty()){
            editTextAmountCable.setText("20")
        }

        if(editTextAmountCable.length() > 0 ){
            if(editTextAmountCable.text.toString() == "0" ) editTextAmountCable.setText("20")
            else if(editTextAmountCable.text.toString() == "00") editTextAmountCable.setText("20")
            else if(editTextAmountCable.text.toString() == "000") editTextAmountCable.setText("20")
            else if(editTextAmountCable.text.toString() == "0000") editTextAmountCable.setText("20")
            else if(editTextAmountCable.text.toString().slice(0..0) == "0"){
                for(i in 0..3){
                    if(editTextAmountCable.text.toString().slice(0..0) != "0") break
                    else editTextAmountCable.setText(editTextAmountCable.text.toString().slice(1..editTextAmountCable.length() - 1))
                }
            }
        }


        val sizeCable = arrayListOf("1 mm2", "1.5 mm2", "2.5 mm2", "4 mm2", "6 mm2", "10 mm2", "16 mm2", "25 mm2", "35 mm2", "50 mm2", "70 mm2", "95 mm2", "120 mm2", "150 mm2", "185 mm2", "240 mm2", "300 mm2", "400 mm2", "500 mm2", "630 mm2", "800 mm2")
        val sizeConduit = arrayListOf("50x75 mm.", "50x100 mm.", "75x100 mm.", "100x100 mm.", "100x150 mm.", "100x200 mm.", "100x250 mm.", "100x300 mm.", "150x300 mm.")
        tableBeforeCalculateInPipe.visibility = View.VISIBLE
        wayBackActivity1.visibility = View.GONE
        wayBackActivity2.visibility = View.VISIBLE
        btnCalInPipeSize.visibility = View.GONE

        val findSheetInTableCableSize = when(typeCableTextView.text){
            "IEC01" -> 0
            "NYY 1/C" -> 1
            "NYY 2/C" -> 2
            "NYY 3/C" -> 3
            "NYY 4/C" -> 4
            "XLPE 1/C" -> 5
            "XLPE 2/C" -> 6
            "XLPE 3/C" -> 7
            "XLPE 4/C" -> 8
            "VCT 1/C" -> 9
            "VCT 2/C" -> 10
            "VCT 3/C" -> 11
            "VCT 4/C" -> 12
            "NYY 2/C - G" -> 13
            "NYY 3/C - G" -> 14
            "NYY 4/C - G" -> 15
            "VCT 2/C - G" -> 16
            "VCT 3/C - G" -> 17
            "VCT 4/C - G" -> 18
            else -> return
        }


        try {
            val typeCable = applicationContext.assets.open("type_cable_pipe.xls")
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(findSheetInTableCableSize)
            val typeCabletitle = sheet.getCell(0, 0).contents
            if (typeCableTextView.text == typeCabletitle){
                sizeCable.forEachIndexed { indexSize, size  -> // หาขนาดสาย mm2
                    if (cableSizeTextView.text.toString().replace("mm2","mm2") == size){
                        sizeConduit.forEachIndexed{ indexConduit, conduit ->
                            if(SizeConduitTextView.text == conduit ){
                                    // ขนาด สายไฟสุงสุด 14
                                    textViewResultMaxCable.text = "${sheet.getCell(indexConduit + 14, indexSize + 1).contents} เส้น"
                                    if (sheet.getCell(indexConduit + 14, indexSize + 1).contents.toString() == "0"){
                                        textViewResultMaxCable.text = "- เส้น"
                                    }
                                textViewRailsSize2.visibility = View.GONE
                                textViewRailsSizeResult2.visibility = View.GONE

                            }else{
                                if(switchButtonPipeSize.isChecked){
                                    for (i in 14..22){
                                        if(editTextAmountCable.text.toString().toInt() <= sheet.getCell(i, indexSize + 1).contents.toInt()
                                            && sheet.getCell(i, indexSize + 1).contents.toInt() != 0) {
                                            textViewRailsSizeResult.text = "${sheet.getCell(i, 0).contents} mm."
                                            textViewRailsSizeResult1.text = "( ${sheet.getCell(i, indexSize + 1).contents} เส้น )"
                                            textViewRailsSize2.visibility = View.GONE
                                            textViewRailsSizeResult2.visibility = View.GONE
                                            textViewRailsSizeResult22.visibility = View.GONE
                                            textViewRailsSizeResult1.visibility = View.VISIBLE
                                            break
                                        }else{
                                                textViewRailsSizeResult.text = "- เส้น"
                                             if(sheet.getCell(i, indexSize + 1).contents.toInt() != 0){
                                                 textViewRailsSizeResult2.text = "${sheet.getCell(i, 0).contents} mm."
                                                 textViewRailsSizeResult22.text = "( ${sheet.getCell(i, indexSize + 1).contents} เส้น )"
                                                 maxamount = sheet.getCell(i, indexSize + 1).contents
                                                 textViewRailsSize2.visibility = View.VISIBLE
                                                 textViewRailsSizeResult2.visibility = View.VISIBLE
                                                 textViewRailsSizeResult22.visibility = View.VISIBLE
                                                 textViewRailsSizeResult1.visibility = View.GONE
                                             }
                                        }
                                    }
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

    fun pipeSizeReportOnClick(view: View) {
        val intent = Intent(this, ReportInRailsActivity::class.java)
        val bundle = Bundle()

         /* ResultToReportModel ประกอบไปด้วย
        1 ชนิดสายไฟ
        2 ขนาดสายไฟ
        3 ขนาดราง
        4 จำนวนสายไฟ
         */

        if(!switchButtonPipeSize.isChecked){
            val typeCable = typeCableTextView.text.toString()
            val sizeCable = cableSizeTextView.text.toString()
            val sizeRails = SizeConduitTextView.text.toString()
            val amountCable = textViewResultMaxCable.text.toString()
            bundle.putParcelable("resultInRails",
                    ResultRailsToReportModel(
                            typeCable,
                            sizeCable,
                            sizeRails,
                            amountCable,
                            "","",
                            false))
        }else{
            val typeCable = typeCableTextView.text.toString()
            val sizeCable = cableSizeTextView.text.toString()
            val rails = if( textViewRailsSizeResult.text.toString() != "- เส้น"){
                textViewRailsSizeResult.text.toString() + " " + textViewRailsSizeResult1.text.toString()
            }else{
                textViewRailsSizeResult2.text.toString() + " " + textViewRailsSizeResult22.text.toString()
            }
            val amount = if( textViewRailsSizeResult.text.toString() != "- เส้น"){
                editTextAmountCable.text.toString()
            }else{
                maxamount
            }
            bundle.putParcelable("resultInRails",
                    ResultRailsToReportModel(
                            typeCable,
                            sizeCable,
                            "",
                            "",
                            rails,
                            amount,
                            true))
        }



        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    fun setAmountOnClick(view: View) {
        editTextAmountCable.setText("")
        editTextAmountCable.hint = " "
        editTextAmountCable.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextAmountCable, InputMethodManager.SHOW_IMPLICIT)
    }

    // เลือกชนิดสายไฟ
    fun onclickChooseTypeCable(view: View) {
        val intent = Intent(this, TypeCableInRailsActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    // เลือกขนาดสายไฟ
    fun onClickChooseSizeCable(view: View) {
        val intent = Intent(this, SizeCableInRailsActivity::class.java)
        intent.putExtra("typeCable",typeCableTextView.text)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    // เลือกขนาดราง
    fun onClickChooseSizeConduit(view: View) {
        val intent = Intent(this, SizeConduitInRailsActivity::class.java)
        intent.putExtra("typeCable",typeCableTextView.text)
        intent.putExtra("sizeCable",cableSizeTextView.text.toString().replace("mm2","mm2"))
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }

    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }

    fun backOnClick(view: View) {
        val sharedPref =
            getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .clear()
        sharedPref.apply()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPref =
            getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .clear()
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
                val dataSizeConduit = data!!.getStringExtra("dataSizeConduit")

                if (dataTypeCable != null) {
                    typeCableTextView.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)

                    cableSizeTextView.text =  Html.fromHtml("2.5mm<sup><small>2</small></sup>")
                    SizeConduitTextView.text = "mm."
                    saveData("sizeCable", "2.5 mm2")
                    saveData("sizeConduit", "mm.")
                }
                if (dataSizeCable != null) {

                    cableSizeTextView.text = Html.fromHtml(dataSizeCable.replace("mm2","mm<sup><small>2</small></sup>"))
                    SizeConduitTextView.text = "mm."
                    saveData("sizeCable", dataSizeCable)
                    saveData("sizeConduit", "mm.")
                }
                if (dataSizeConduit != null) {
                    SizeConduitTextView.text = dataSizeConduit
                    saveData("sizeConduit", dataSizeConduit)
                }
            }
        }
        wayBackActivity1.visibility = View.VISIBLE
        wayBackActivity2.visibility = View.GONE
        btnCalInPipeSize.visibility = View.VISIBLE
        tableBeforeCalculateInPipe.visibility = View.GONE

        if(!switchButtonPipeSize.isChecked){
            if(SizeConduitTextView.text == "mm.") {
                btnCalInPipeSize.isClickable = false
                btnCalInPipeSize.backgroundTintList = this.resources.getColorStateList(R.color.placeHolderBG)
            }
            else {
                btnCalInPipeSize.isClickable = true
                btnCalInPipeSize.backgroundTintList = this.resources.getColorStateList(R.color.btnBlue)
            }
        }
    }
}