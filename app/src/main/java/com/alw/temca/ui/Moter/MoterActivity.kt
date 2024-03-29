package com.alw.temca.ui.Moter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.alw.temca.Model.DataToMoterReportModel
import com.alw.temca.Model.DataToSizeMoterModel
import com.alw.temca.Model.InstallationModelInTransformer
import com.alw.temca.R
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.Transformer.InstallationTransformerActivity
import com.alw.temca.ui.WireSize.PhaseActivity
import com.alw.temca.ui.WireSize.TypeCableActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_moter.*
import kotlinx.android.synthetic.main.activity_moter.btnCalInPipeSize
import kotlinx.android.synthetic.main.activity_moter.phaseTextView
import kotlinx.android.synthetic.main.activity_moter.wayBackActivity1
import kotlinx.android.synthetic.main.activity_wire_size.*


class MoterActivity : AppCompatActivity() {
    private val TASK_NAME_REQUEST_CODE = 100
    private val PREF_NAME = "task_moter"
    private val TASK_LIST_PREF_KEY_SIZE_MOTER = "task_list_size_moter"
    private val TASK_LIST_PREF_KEY_SIZE_UNIT = "task_list_unit"
    private val TASK_LIST_PREF_KEY_PHASE_IN_MOTER = "task_list_phase_in_moter"
    private val TASK_LIST_PREF_KEY_INSTALLATION_IN_MOTER = "task_list_installation_in_moter"
    private val TASK_LIST_PREF_KEY_STARTPANTERN_IN_MOTER = "task_list_startpantern_in_moter"
    private val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_MOTER = "task_list_type_cable_in_moter"
    private val TASK_LIST_PREF_KEY_DISTANCE_IN_MOTER = "task_list_distance_in_moter"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moter)
        tableBeforeCalculateInMoter.visibility = View.GONE
        loadData()

        editTextDistanceInMoter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()){
                    editTextDistanceInMoter.hint = " "
                }else{
                    editTextDistanceInMoter.hint = ""
                }
                tableBeforeCalculateInMoter.visibility = View.GONE
                btnCalInPipeSize.visibility = View.VISIBLE
                wayBackActivity1.visibility = View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("distance",s.toString())
            }
        })

//        editTextAmountMoterSize.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                //ก่อนเปลี่ยนคือ ?
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s!!.isEmpty()){
//                    editTextAmountMoterSize.hint = " "
//                }else{
//                    editTextAmountMoterSize.hint = ""
//                }
//
//                tableBeforeCalculateInMoter.visibility = View.GONE
//                btnCalInPipeSize.visibility = View.VISIBLE
//                wayBackActivity1.visibility = View.VISIBLE
//            }
//            override fun afterTextChanged(s: Editable?) {
//                //หลังจากพิมพ์ผลลัพคือ ?
//                saveData("distance",s.toString())
//            }
//        })

        if(phaseTextView.text == "1 เฟส 230V") iconImageViewToPage.visibility = View.GONE
        else iconImageViewToPage.visibility = View.VISIBLE

    }


    fun OnClickAmuontSizeMoter(view: View) {
        val intent = Intent(this, SizeMoterActivity::class.java)
        val dataToSizemoter = ArrayList<DataToSizeMoterModel>()
        dataToSizemoter.add(DataToSizeMoterModel(phaseTextView.text.toString(),TextViewStartPantern.text.toString(),textUnit.text.toString()))
        intent.putParcelableArrayListExtra("Unit",dataToSizemoter)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }

//    fun moterSizeOnClick(view: View) {
//        editTextAmountMoterSize.setText("")
//        editTextAmountMoterSize.hint = " "
//        editTextAmountMoterSize.requestFocus()
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(editTextAmountMoterSize, InputMethodManager.SHOW_IMPLICIT)
//    }

    fun phaseOnClick(view: View) {
        val intent = Intent(this, PhaseActivity::class.java)
        intent.putExtra("Activity","Moter")
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun GroupInstallationOnClick(view: View) {
        val intent = Intent(this, InstallationTransformerActivity::class.java).also {
            it.putExtra("Activity","Moter")
            startActivityForResult(it,TASK_NAME_REQUEST_CODE)
        }
    }
    fun sizeCableTypeOnClick(view: View) {
        val intent = Intent(this, TypeCableActivity::class.java)
        intent.putExtra("Activity","Moter")
        if(phaseTextView.text == "3 เฟส 400V"){
            intent.putExtra("Phase","3")
        }

        if(TextViewStartPantern.text == "STAR DELTA")  intent.putExtra("Type","DELTA")
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }

    fun startPantternOnClick(view: View) {
        if(phaseTextView.text != "1 เฟส 230V"){
            val intent = Intent(this, StartPatternActivity::class.java)
            startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
        }
    }
    fun setDistanceOnClick(view: View) {
        editTextDistanceInMoter.setText("")
        editTextDistanceInMoter.hint = " "
        editTextDistanceInMoter.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistanceInMoter, InputMethodManager.SHOW_IMPLICIT)
        wayBackActivity1.visibility = View.VISIBLE
    }

    fun unitOnClick(view: View) {
        val intent = Intent(this, UnitMoterActivity::class.java)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }


    fun moterReportOnClick(view: View) {
        val dataToReport = ArrayList<DataToMoterReportModel>()
        val intent = Intent(this, MoterReportActivity::class.java)
        dataToReport.add(DataToMoterReportModel(
            TextAmountMoterSize.text.toString(),
            textUnit.text.toString(),
            phaseTextView.text.toString(),
            TextViewStartPantern.text.toString(),
            TextViewCableTypeInMoter.text.toString(),
            editTextDistanceInMoter.text.toString(),
            textViewElectricCurrenResult.text.toString(),
            textViewCableSize.text.toString(),
            textViewSizeCableResult.text.toString(),
            textViewGroundSizeResult.text.toString(),
            textViewConduitSizeResult.text.toString(),
            textViewBreakerResult.text.toString(),
            textViewPressureResult.text.toString()
        ))
        intent.putParcelableArrayListExtra("DataFromMoter",dataToReport)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
        finish()
    }

    fun calculatorMoterOnClick(view: View) {

//        if(editTextDistanceInMoter. == 0){
//
//        }


        if(editTextDistanceInMoter.text.toString() == "0"){
            textViewPressure.visibility = View.GONE
            textViewPressureResult.visibility = View.GONE
        }else{
            textViewPressure.visibility = View.VISIBLE
            textViewPressureResult.visibility = View.VISIBLE
        }

//        if(TextAmountMoterSize.text.isEmpty()) TextAmountMoterSize.setText("10")
        if(editTextDistanceInMoter.text.isEmpty()) editTextDistanceInMoter.setText("0")

        if(editTextDistanceInMoter.length() > 0 ){
            if(editTextDistanceInMoter.text.toString() == "0" ) editTextDistanceInMoter.setText("0")
            else if(editTextDistanceInMoter.text.toString() == "00") editTextDistanceInMoter.setText("0")
            else if(editTextDistanceInMoter.text.toString() == "000") editTextDistanceInMoter.setText("0")
            else if(editTextDistanceInMoter.text.toString() == "0000") editTextDistanceInMoter.setText("0")
            else if(editTextDistanceInMoter.text.toString().slice(0..0) == "0"){
                for(i in 0..3){
                    if(editTextDistanceInMoter.text.toString().slice(0..0) != "0") break
                    else editTextDistanceInMoter.setText(editTextDistanceInMoter.text.toString().slice(1..editTextDistanceInMoter.length()-1))
                }
            }
        }


//        if(editTextAmountMoterSize.length() > 0 ){
//            if(editTextAmountMoterSize.text.toString() == "0." ) editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == "00.") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == "000.") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == ".0") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == ".00") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == ".000") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == "0") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == "00") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == "000") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString() == "0000") editTextAmountMoterSize.setText("10")
//            else if(editTextAmountMoterSize.text.toString().slice(0..1) == "00" || editTextAmountMoterSize.text.toString().slice(0..0) == ".") {
//                   for(i in 0..3){
//                       println(i)
//                    if(editTextAmountMoterSize.text.toString().slice(0..1) == "0." &&  editTextAmountMoterSize.text.toString().slice(0..0) != ".") break
//                    else editTextAmountMoterSize.setText(editTextAmountMoterSize.text.toString().slice(1..editTextAmountMoterSize.length()-1))
//                }
//
//                if(editTextDistanceInMoter.text.toString().slice(0..0) == "0"){
//                    for(i in 0..3){
//                        if(editTextDistanceInMoter.text.toString().slice(0..0) != "0") break
//                        else editTextDistanceInMoter.setText(editTextDistanceInMoter.text.toString().slice(1..editTextDistanceInMoter.length()-1))
//                    }
//                }
//
//            }
//        }

        btnCalInPipeSize.visibility = View.GONE
        tableBeforeCalculateInMoter.visibility = View.VISIBLE
        wayBackActivity1.visibility = View.GONE
        editTextDistanceInMoter.clearFocus()
        btnCalInPipeSize.apply {
            hideKeyboard()
        }



        var stepInFor:Int = 0
        val maxRowsheet:Int
        val indexSheetInMoter:Int
        if(phaseTextView.text == "1 เฟส 230V") {
            maxRowsheet = 53
            stepInFor = 4
            indexSheetInMoter = 0
        } else {
            if(TextViewStartPantern.text == "STAR DELTA") {
                maxRowsheet = 46
                stepInFor = 3
                indexSheetInMoter = 2
            }else {
                maxRowsheet = 93
                stepInFor = 4
                indexSheetInMoter = 1
            }
        }


        val typeCable = applicationContext.assets.open("moter.xls")
        val wb = Workbook.getWorkbook(typeCable)
        val sheet = wb.getSheet(indexSheetInMoter)
        val columnIntable:Int = when(textUnit.text){
            "kW" -> 0
            "HP" -> 1
            "A" -> 10
            else -> 0
        }

        val pressureDropIndexTable:Int =  when(TextViewCableTypeInMoter.text){
            "IEC 01" -> 0
            "NYY 1/C" -> 0
            "NYY 2/C - G" -> 1
            "NYY 3/C - G" -> 1
            "XLPE 1/C" -> 2
            else -> 0
        }

        val phaseIndex:Int =  when(phaseTextView.text){
            "1 เฟส 230V" -> 1
            else -> 2
        }

        var voteInMoter:Int

        for(i in 2..maxRowsheet step stepInFor){
            val editTextToDouble:Double = java.lang.Double.parseDouble(TextAmountMoterSize.text.toString())
            val checkAmpInTable = sheet.getCell(columnIntable, i).contents.toDouble()

            if(editTextToDouble <= checkAmpInTable){
                for(j in i..i+3){
                    val checkCableType = sheet.getCell(3, j).contents // เทียบค่าชนิดสายในตาราง
                    val cableTypeIntable = sheet.getCell(4, j).contents // ขนาดสายในตาราง
                    val powerRatingIntable = sheet.getCell(2, i).contents // พิกัดกระแสไฟในตาราง
                    val calbeGroundIntable = sheet.getCell(7, j).contents // ขนาดสายดินในตาราง
                    val conduitInTable = sheet.getCell(5, j).contents // ขนาดท่อร้อยสายในตาราง
                    val breakerInTable = sheet.getCell(6, i).contents // ขนาดเบรกเกอร์ในตาราง
                    val circuitInTable = sheet.getCell(10, i).contents // พิกัดกระแสไฟ
                    val cableSizeWithOutX = sheet.getCell(9, j).contents // ขนาดเบรกเกอร์ในตาราง

                    if(TextViewCableTypeInMoter.text == checkCableType){
                        textViewElectricCurrenResult.text = powerRatingIntable
                        textViewSizeCableResult.text = Html.fromHtml("${cableTypeIntable.replace("mm2","mm<sup><small><small>2</small></small></sup>")}")

                        if(TextViewCableTypeInMoter.text != "NYY 2/C-G"
                           && TextViewCableTypeInMoter.text != "NYY 3/C-G"){
                            textViewGroundSizeResult.text = Html.fromHtml("${calbeGroundIntable.replace("mm2","mm<sup><small><small>2</small></small></sup>")}")
                        }
                        else {
                            textViewGroundSizeResult.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                        }
//                        textViewConduitSizeResult.text = conduitInTable
                        textViewBreakerResult.text = breakerInTable

                        val temp:String = conduitInTable.replace(")","\" )")
                        val s = SpannableString(temp.trim())
                        if (temp.indexOf('/') != -1) {
                            val len = temp.length
                            s.setSpan(SuperscriptSpan(), len - 6, len - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                            s.setSpan(applicationContext,len - 6, len - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                            s.setSpan(applicationContext, len - 5, len - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัว /
                            s.setSpan(SubscriptSpan(), len - 4, len - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
                            s.setSpan(applicationContext, len - 4, len - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
                        }


                        if(phaseTextView.text == "1 เฟส 230V") {
                            textViewCableSize.text = "แรงดัน(230V)"
                            voteInMoter = 230
                        }
                        else {
                            textViewCableSize.text = "แรงดัน(400V)"
                            voteInMoter = 400
                        }

                        textViewConduitSizeResult.text = s

                        for (h in 2..20) {
                            val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
                            val wbPressure = Workbook.getWorkbook(pressureCable)
                            val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
                            val fineCableTypeInTable = sheetPressure.getCell(0, h).contents
                            val amountDeistance = Integer.parseInt(editTextDistanceInMoter.text.toString())
                            if (cableSizeWithOutX == fineCableTypeInTable) { // แก้ cableSize ตัดคำออก
                                val getreslutInTable = sheetPressure.getCell(phaseIndex, h).contents.toDouble()
                                val pullResult = if(TextViewStartPantern.text == "STAR DELTA"){  // result
                                        (getreslutInTable * (circuitInTable.toDouble()* 0.58 )) * amountDeistance / 1000
                                }else{
                                    (getreslutInTable * circuitInTable.toDouble()) * amountDeistance / 1000
                                }
                                val PercentPressure  = 100 * pullResult / voteInMoter // result

                                var pullResulttoString = "${"%.2f V".format(pullResult)}"
                                var percentPressuretoString = "${"%.2f".format(PercentPressure)}"
                                if(pullResult >= 1000){
                                    pullResulttoString = "${pullResulttoString.slice(0..0)},${pullResulttoString.slice(1 until pullResulttoString.length)}"
                                    if(PercentPressure >= 1000){
                                        percentPressuretoString = "( ${percentPressuretoString.slice(0..0)},${percentPressuretoString.slice(1 until percentPressuretoString.length)}% )"
                                    }else{
                                        percentPressuretoString = "( ${percentPressuretoString}% )"
                                    }
                                    textViewPressureResult.text = "$pullResulttoString $percentPressuretoString"
                                }else{
                                    textViewPressureResult.text = "${"%.2f V".format(pullResult)} ( ${"%.2f".format(PercentPressure)}% )"
                                }
                                break
                            }
                        }
                        break
                    }
                }
                break
            }else{
                textViewCableSize.text = "แรงดัน(-V)"
                textViewElectricCurrenResult.text = "-"
                textViewSizeCableResult.text = "-"
                textViewGroundSizeResult.text = "- G"
                textViewConduitSizeResult.text = "-"
                textViewBreakerResult.text = "-"
                textViewPressureResult.text = "-"
                if(i == maxRowsheet-3)Toast.makeText(this,"ไม่สามารถคำนวนได้หรือใส่รูปแบบผิด",Toast.LENGTH_SHORT).show()
            }

        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){

                val dataSizeMoter = data!!.getStringExtra("dataSizeMoter")
                val dataUnitMoter = data!!.getStringExtra("dataUnitMoter")
                val dataPhase = data.getIntExtra("dataPhase",0)
                val dataInstallation = data.getParcelableExtra<InstallationModelInTransformer>("dataInstall")
                val dataStartPantern = data!!.getStringExtra("dataStartPantern")
                val dataTypeCable = data!!.getStringExtra("dataTypeCable")

                if (dataSizeMoter != null){
                    TextAmountMoterSize.text = dataSizeMoter
                    saveData("dataSizeMoter", dataSizeMoter)
                }

                if (dataUnitMoter != null){
                    if(phaseTextView.text == "1 เฟส 230V"){
                        if(dataUnitMoter == "A"){
                            TextAmountMoterSize.text = "3.9"
                            saveData("dataSizeMoter", "3.9")
                        }else if(dataUnitMoter == "kW"){
                            TextAmountMoterSize.text = "0.37"
                            saveData("dataSizeMoter", "0.37")
                        }
                        else{
                            TextAmountMoterSize.text = "0.5"
                            saveData("dataSizeMoter", "0.5")
                        }
                    }else{
                        if(dataUnitMoter == "HP"){
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "10"
                                saveData("dataSizeMoter", "10")
                            }else{
                                TextAmountMoterSize.text = "0.5"
                                saveData("dataSizeMoter", "0.5")
                            }

                        }else if(dataUnitMoter == "kW"){
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "7.5"
                                saveData("dataSizeMoter", "7.5")
                            }else{
                                TextAmountMoterSize.text = "0.37"
                                saveData("dataSizeMoter", "0.37")
                            }
                        }
                        else{
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "15"
                                saveData("dataSizeMoter", "15")
                            }else{
                                TextAmountMoterSize.text = "1"
                                saveData("dataSizeMoter", "1")
                            }

                        }
                    }

                    textUnit.text = dataUnitMoter
                    saveData("unitMoter", dataUnitMoter)
                }

                if (dataPhase != 0) {
                    if(dataPhase == 1){
                        phaseTextView.text = "$dataPhase เฟส 230V"
                        saveData("phase", "$dataPhase เฟส")
                    }else{
                        phaseTextView.text = "$dataPhase เฟส 400V"
                        saveData("phase", "$dataPhase เฟส")
                    }
                    TextViewCableTypeInMoter.text = "NYY 1/C"
                    saveData("dataTypeCable", "NYY 1/C")

                    if(phaseTextView.text == "1 เฟส 230V"){
                        if(textUnit.text == "A"){
                            TextAmountMoterSize.text = "3.9"
                            saveData("dataSizeMoter", "3.9")
                        }else if(textUnit.text == "kW"){
                            TextAmountMoterSize.text = "0.37"
                            saveData("dataSizeMoter", "0.37")
                        }
                        else{
                            TextAmountMoterSize.text = "0.5"
                            saveData("dataSizeMoter", "0.5")
                        }
                    }else{
                        if(textUnit.text == "HP"){
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "10"
                                saveData("dataSizeMoter", "10")
                            }else{
                                TextAmountMoterSize.text = "0.5"
                                saveData("dataSizeMoter", "0.5")
                            }

                        }else if(textUnit.text == "kW"){
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "7.5"
                                saveData("dataSizeMoter", "7.5")
                            }else{
                                TextAmountMoterSize.text = "0.37"
                                saveData("dataSizeMoter", "0.37")
                            }
                        }
                        else{
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "15"
                                saveData("dataSizeMoter", "15")
                            }else{
                                TextAmountMoterSize.text = "1.5"
                                saveData("dataSizeMoter", "1.5")
                            }

                        }
                    }

                }

                if (dataStartPantern != null){
                    TextViewStartPantern.text = dataStartPantern
                    saveData("dataStartPantern", dataStartPantern)

                    TextViewCableTypeInMoter.text = "NYY 1/C"
                    saveData("dataTypeCable", "NYY 1/C")

                    if(phaseTextView.text == "1 เฟส 230V"){
                        if(textUnit.text == "A"){
                            TextAmountMoterSize.text = "3.9"
                            saveData("dataSizeMoter", "3.9")
                        }else if(textUnit.text == "kW"){
                            TextAmountMoterSize.text = "0.37"
                            saveData("dataSizeMoter", "0.37")
                        }
                        else{
                            TextAmountMoterSize.text = "0.5"
                            saveData("dataSizeMoter", "0.5")
                        }
                    }else{
                        if(textUnit.text == "HP"){
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "10"
                                saveData("dataSizeMoter", "10")
                            }else{
                                TextAmountMoterSize.text = "0.5"
                                saveData("dataSizeMoter", "0.5")
                            }

                        }else if(textUnit.text == "kW"){
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "7.5"
                                saveData("dataSizeMoter", "7.5")
                            }else{
                                TextAmountMoterSize.text = "0.37"
                                saveData("dataSizeMoter", "0.37")
                            }
                        }
                        else{
                            if(TextViewStartPantern.text == "STAR DELTA"){
                                TextAmountMoterSize.text = "15"
                                saveData("dataSizeMoter", "15")
                            }else{
                                TextAmountMoterSize.text = "1.5"
                                saveData("dataSizeMoter", "1.5")
                            }

                        }
                    }

                }

                if (dataTypeCable != null){
                    TextViewCableTypeInMoter.text = dataTypeCable
                    saveData("dataTypeCable", dataTypeCable)
                }

            }
            wayBackActivity1.visibility = View.VISIBLE
            btnCalInPipeSize.visibility = View.VISIBLE
            tableBeforeCalculateInMoter.visibility = View.GONE


            if(phaseTextView.text == "1 เฟส 230V") {
                iconImageViewToPage.visibility = View.GONE
                TextViewStartPantern.text = "DOL"
                saveData("dataStartPantern", "DOL")
            }
            else {
                if(TextViewStartPantern.text == "STAR DELTA") {
                    if(TextViewCableTypeInMoter.text == "NYY 2/C - G"){
                        TextViewCableTypeInMoter.text = "NYY 1/C"
                        saveData("dataTypeCable", "NYY 1/C")
                    }
                }
                iconImageViewToPage.visibility = View.VISIBLE
            }

        }
    }

    fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            if (type == "dataSizeMoter"){
                putString(TASK_LIST_PREF_KEY_SIZE_MOTER, data)
            }
            if (type == "unitMoter"){
                putString(TASK_LIST_PREF_KEY_SIZE_UNIT, data)
            }
            if (type == "phase"){
                putString(TASK_LIST_PREF_KEY_PHASE_IN_MOTER, data)
            }
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_MOTER, data)
            }
            if (type == "dataStartPantern"){
                putString(TASK_LIST_PREF_KEY_STARTPANTERN_IN_MOTER, data)
            }
            if (type == "dataTypeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_MOTER, data)
            }
        }
    }

    fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataSizeOfMoter = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE_MOTER,"0.37")
        val dataOfUnitMoter = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE_UNIT,"kW")
        val dataOfPhase = sharedPref.getString(TASK_LIST_PREF_KEY_PHASE_IN_MOTER,"1 เฟส 230V")
//        val dataOfInstall = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_MOTER,"เดินเคเบิลแบบระบายอากาศ")
        val dataOfStartPantern = sharedPref.getString(TASK_LIST_PREF_KEY_STARTPANTERN_IN_MOTER,"DOL")
        val dataOfCableSize = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_MOTER,"NYY 1/C")
        val dataOfDistanceInTransformer = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_IN_MOTER,"20")

        TextAmountMoterSize.text = dataSizeOfMoter
        textUnit.text = dataOfUnitMoter
        phaseTextView.text = dataOfPhase
//        TextViewGroupInstallations.text = dataOfInstall
        TextViewStartPantern.text = dataOfStartPantern
        TextViewCableTypeInMoter.text = dataOfCableSize
        editTextDistanceInMoter.setText(dataOfDistanceInTransformer)
    }

    fun backOnClick(view: View) {
        val sharedPref =
                getSharedPreferences(OnePhaseActivity.PREF_NAME, Context.MODE_PRIVATE).edit()
                        .clear()
        sharedPref.apply()
        finish()}
    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPref =
                getSharedPreferences(OnePhaseActivity.PREF_NAME, Context.MODE_PRIVATE).edit()
                        .clear()
        sharedPref.apply()
        finish()
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }




}