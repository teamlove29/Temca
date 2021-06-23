package com.alw.temca.ui.WireSize

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.WireSizeAdapter
import com.alw.temca.Function.FindDetailInstallation
import com.alw.temca.Model.RailSizeModel
import com.alw.temca.Model.ReportResultWireSize
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_moter.*
import kotlinx.android.synthetic.main.activity_wire_size.*
import kotlinx.android.synthetic.main.activity_wire_size.phaseTextView
import kotlinx.android.synthetic.main.activity_wire_size.wayBackActivity1
import java.io.IOException


class WireSizeActivity : AppCompatActivity() {
    private val TASK_NAME_REQUEST_CODE = 100
    private val TASK_LIST_PREF_KEY_PHASE_IN_WIRESIZE = "task_list_phase_in_wiresize"
    private val TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE = "task_list_installation_in_wiresize"
    private val TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE_DES = "task_list_installation_in_wiresize_des"
    private val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE = "task_list_type_cable_in_wiresize"
    private val TASK_LIST_PREF_KEY_CIRCUIT_IN_WIRESIZE = "task_list_circuit_in_wiresize"
    private val TASK_LIST_PREF_KEY_DISTANCE_IN_WIRESIZE = "task_list_distance_in_wiresize"
    private val PREF_NAME = "task_wire"
    private val railSizeList = ArrayList<RailSizeModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wire_size)
        tableBeforeCalculate.visibility = View.GONE
        tableBeforeCalculateGroup7.visibility = View.GONE


        val codeStart = intent.extras?.getBoolean("code")
        if(codeStart == true){
            val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
            sharedPref.apply()
        }

        loadData()
        intent = intent
        val dataCircuit = intent.getStringExtra("dataCircuit")
        if (dataCircuit != null){
            circuitTextView.text = dataCircuit
            saveData("circuit", dataCircuit)
        }
        editTextDistance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()) {
                    editTextDistance.hint = " "
                } else {
                    editTextDistance.hint = ""
                }
//                sponsorImageView.visibility = View.VISIBLE
                tableBeforeCalculate.visibility = View.GONE
                tableBeforeCalculateGroup7.visibility = View.GONE
                btnCal.visibility = View.VISIBLE
                wayBackActivity1.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("distance", s.toString())
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                val dataPhase = data!!.getIntExtra("dataPhase", 0)
                val dataInstallation = data.getStringExtra("dataInstall")
                val dataInstallationDes = data.getStringExtra("dataInstallDes").toString()
                val dataTypeCable = data.getStringExtra("dataTypeCable")
//                val dataCircuit = data!!.getStringExtra("dataCircuit")

                if (dataPhase != 0){
                    phaseTextView.text = "$dataPhase เฟส"
                    saveData("phase", dataPhase.toString())
                }
                if (dataInstallation != null) {
                    val dataInstallationSlice = dataInstallation.slice(0..6)
                    if(dataInstallationSlice == "กลุ่ม 5"){
                            if (typeCableTextView.text != "NYY" && typeCableTextView.text != "XLPE" && typeCableTextView.text != "VCT" && typeCableTextView.text != "NYY-G" && typeCableTextView.text != "VCT-G"){
                                typeCableTextView.text = "NYY"
                            }
                        circuitTextView.text = "40A"
                    }else if(dataInstallationSlice == "กลุ่ม 7"){
                        if(typeCableTextView.text != "NYY 1C" && typeCableTextView.text != "NYY 4C" && typeCableTextView.text != "XLPE 1C" && typeCableTextView.text != "XLPE 4C"){
                            typeCableTextView.text = "NYY 1C"
                        }
                        if(phaseTextView.text != "3 เฟส"){
                            phaseTextView.text = "3 เฟส"
                        }
                        circuitTextView.text = "400A"
                    }else circuitTextView.text = "40A"

                    installationTextView.text = dataInstallationSlice
                    saveData("installation", dataInstallation)
                    saveData("installationDes", dataInstallationDes)
                }
                if (dataTypeCable != null) {
                    typeCableTextView.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)
                }
            }
            wayBackActivity1.visibility = View.VISIBLE
        }
//        sponsorImageView.visibility = View.VISIBLE
        tableBeforeCalculate.visibility = View.GONE
        tableBeforeCalculateGroup7.visibility = View.GONE
        btnCal.visibility = View.VISIBLE
//        circuitTextView.text = "40A"
    }

    fun phaseOnClick(view: View){
        val intent = Intent(this, PhaseActivity::class.java)
        if(installationTextView.text == "กลุ่ม 7"){
            intent.putExtra("Activity", "Group7")
        }
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun installationOnClick(view: View) {
        val intent = Intent(this, InstallationActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun typeCableOnClick(view: View) {
        val intent = Intent(this, TypeCableActivity::class.java)
        if(installationTextView.text == "กลุ่ม 5"){
            intent.putExtra("Activity", "Group5")
        }
        else if(installationTextView.text == "กลุ่ม 7"){
            intent.putExtra("Activity", "Group7")
        }
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun CircuitOnClick(view: View) {

        val intent = Intent(this, CircuitActivity::class.java)
        val circuitCheckGroup:String = installationOfTable(installationTextView.text.toString())
        val circuitCheckCableType:Int = circuitCheckPhaseAndCableType(phaseTextView.text.toString(), typeCableTextView.text.toString())
        val typeCable = applicationContext.assets.open(circuitCheckGroup)
        val wb = Workbook.getWorkbook(typeCable)

        if(circuitCheckGroup != "WireGroup_Group7.xls"){
            val sheet = wb.getSheet(circuitCheckCableType)
            for(i in 2..20){
                val checkAmountAmp = sheet.getCell(0, i).contents.toInt()

                if(checkAmountAmp == 0 || checkAmountAmp == 1000){
                    val rowAmount = when(checkAmountAmp){
                        1000 -> i - 2
                        else -> i - 3
                    }
                    intent.putExtra("RowAmountAmp", rowAmount)

                    startActivity(intent)
                    finish()
                    break
                }
            }
        }else{
                    intent.putExtra("RowAmountAmp", 77)
                    startActivity(intent)
                    finish()
        }
    }

    fun ReportOnClick(view: View) {
        val dataToReport = ArrayList<ReportResultWireSize>()
        val intent = Intent(this, ReportActivity::class.java)
//        val bundle = Bundle()
        val textInstallation = FindDetailInstallation(installationTextView.text.toString())

        if(installationTextView.text != "กลุ่ม 7"){
            dataToReport.add(ReportResultWireSize(
                    phaseTextView.text.toString(), // phase
                    textInstallation,  // groupinstallation
                    typeCableTextView.text.toString(), // typcable
                    circuitTextView.text.toString(), // CB
                    editTextDistance.text.toString(), // amountDis
                    textViewShow2.text.toString().replace("mm2","mm"), // text2 is cablesize
                    textViewResultWireGround.text.toString(), // wiresizegroud
                    textViewShow4.text.toString(), // text4 is conduitsize
                    textViewShow6.text.toString()) // result presure
            )
        }else{
            dataToReport.add(ReportResultWireSize(
                    phaseTextView.text.toString(), // phase
                    textInstallation,  // groupinstallation
                    typeCableTextView.text.toString(), // typcable
                    circuitTextView.text.toString(), // CB
                    editTextDistance.text.toString(), // amountDis
                    railSizeList[0].wireSize, // text2 is cablesize
                    "${railSizeList[0].groundSize}mm2", // wiresizegroud
                    "${railSizeList[0].railSize}mm", // text4 is conduitsize
                    railSizeList[0].resultPressure) // result presure
            )
            dataToReport.add(ReportResultWireSize(
                    phaseTextView.text.toString(), // phase
                    textInstallation,  // groupinstallation
                    typeCableTextView.text.toString(), // typcable
                    circuitTextView.text.toString(), // CB
                    editTextDistance.text.toString(), // amountDis
                    railSizeList[1].wireSize, // text2 is cablesize
                    "${railSizeList[1].groundSize}mm2", // wiresizegroud
                    "${railSizeList[1].railSize}mm", // text4 is conduitsize
                    railSizeList[1].resultPressure) // result presure
            )
        }
//        bundle.putParcelable("reportWireSize", ReportResultWireSize(
//                phaseTextView.text.toString(),
//                textInstallation,
//                typeCableTextView.text.toString(),
//                circuitTextView.text.toString(),
//                editTextDistance.text.toString(),
//                textViewShow2.text.toString(), // text2 is cablesize
//                textViewResultWireGround.text.toString(),
//                textViewShow4.text.toString(), // text4 is conduitsize
//                textViewShow6.text.toString()))
//        intent.putExtras(bundle)

        intent.putParcelableArrayListExtra("DataFromWireSize",dataToReport)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
        finish()
    }

    fun DestanceOnClick(view: View) {
        editTextDistance.setText("")
        editTextDistance.hint = " "
        editTextDistance.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistance, InputMethodManager.SHOW_IMPLICIT)
        wayBackActivity1.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    fun calculatorOnClick(view: View) {
        railSizeList.clear()

        if (editTextDistance.text.isEmpty()){
            editTextDistance.setText("10")
        }
        if (circuitTextView.text.isEmpty()){
            circuitTextView.text = "40A"
        }

        if(editTextDistance.length() > 0 ){
            if(editTextDistance.text.toString() == "0" ) editTextDistance.setText("20")
            else if(editTextDistance.text.toString() == "00") editTextDistance.setText("20")
            else if(editTextDistance.text.toString() == "000") editTextDistance.setText("20")
            else if(editTextDistance.text.toString() == "0000") editTextDistance.setText("20")
            else if(editTextDistance.text.toString().slice(0..0) == "0"){
                for(i in 0..3){
                    if(editTextDistance.text.toString().slice(0..0) != "0") break
                    else editTextDistance.setText(editTextDistance.text.toString().slice(1..editTextDistance.length()-1))
                }
            }
        }


        btnCal.visibility = View.GONE
        tableBeforeCalculate.visibility = View.VISIBLE
        wayBackActivity1.visibility = View.GONE
        editTextDistance.clearFocus()
        btnCal.apply {
            hideKeyboard()
        }

        try {
            val circuitCheckGroup:String = installationOfTable(installationTextView.text.toString())
            val circuitCheckCableType:Int = circuitCheckPhaseAndCableType(phaseTextView.text.toString(), typeCableTextView.text.toString())
            val typeCable = applicationContext.assets.open(circuitCheckGroup)
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(circuitCheckCableType)

            // if group 7
            if(installationTextView.text == "กลุ่ม 7"){
                tableBeforeCalculateGroup7.visibility = View.VISIBLE
                tableBeforeCalculate.visibility = View.GONE

                for(i in 2..17){
                    val checkAmp = sheet.getCell(0, i).contents
                    if(checkAmp != ""){
                        if(circuitTextView.text.toString().replace("A","") == checkAmp){
                            for(j in i..i+1){
                                val getCableSizeInTable = sheet.getCell(1, j).contents
                                val getGroudSizeInTable = sheet.getCell(2, j).contents
                                val getRailSizeInTable = sheet.getCell(3, j).contents
                                val getSizeCableIntable = sheet.getCell(6, j).contents

                                var pressureDropIndexTable:Int

                                pressureDropIndexTable =  when(typeCableTextView.text){
                                    "NYY 1C" -> 0
                                    "NYY 4C" -> 1
                                    "XLPE 1C" -> 2
                                    "XLPE 4C" -> 3
                                    else -> 0
                                }

                                for (h in 2..20) {
                                    val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
                                    val wbPressure = Workbook.getWorkbook(pressureCable)
                                    val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
                                    val fineCableTypeInTable = sheetPressure.getCell(0, h).contents
                                    val amountDeistance = Integer.parseInt(editTextDistance.text.toString())

                                    if (getSizeCableIntable == fineCableTypeInTable) { // แก้ cableSize ตัดคำออก
                                        val getreslutInTable = sheetPressure.getCell(2, h).contents.toDouble()
                                        val pullResult = getreslutInTable * Integer.parseInt(circuitTextView.text.toString().replace("A", "")) * amountDeistance / 1000 // result
                                        val PercentPressure  = 100 * pullResult / 400 // result
                                        var resultRefPressure:String

                                        var pullResulttoString = "${"%.2f V".format(pullResult)}"
                                        var percentPressuretoString = "${"%.2f V".format(PercentPressure)}"

                                        if(pullResult >= 1000){
                                            pullResulttoString = pullResulttoString.replace("${pullResulttoString.slice(0..0)}","${pullResulttoString.slice(0..0)},")
                                            if(PercentPressure >= 1000){
                                                percentPressuretoString = percentPressuretoString.replace("${percentPressuretoString.slice(0..0)}","${percentPressuretoString.slice(0..0)},")
                                            }
                                            resultRefPressure = "$pullResulttoString $percentPressuretoString"
                                        }else{
                                            resultRefPressure = "${"%.2f V".format(pullResult)} (${"%.2f".format(PercentPressure)}%) "
                                        }

                                        railSizeList.add(RailSizeModel(
                                                getCableSizeInTable
                                                , getGroudSizeInTable
                                                , getRailSizeInTable
                                                , "400V"
                                                , resultRefPressure))
                                    }
                                }
                            }
                            recycerViewWireSize.adapter = WireSizeAdapter(railSizeList)
                            recycerViewWireSize.layoutManager = LinearLayoutManager(this)
                            break
                        }
                    }else{
                        println("Nooooo")
                    }
                }

            }else{
                tableBeforeCalculateGroup7.visibility = View.GONE
                tableBeforeCalculate.visibility = View.VISIBLE
                for(i in 2..20){
                    val checkAmp = sheet.getCell(0, i).contents.toInt()
                    val circuitTextViewToInt = Integer.parseInt(circuitTextView.text.toString().replace("A", ""))
                    if(circuitTextViewToInt <= checkAmp){

                        val cableSize = sheet.getCell(1, i).contents
                        val cableSizeWithOutX = sheet.getCell(6, i).contents
                        val sizeWireGround = sheet.getCell(2, i).contents
                        val resultSizeConduitOfmm = sheet.getCell(3, i).contents
                        val resultSizeConduitOfInch = sheet.getCell(4, i).contents
                        val temp:String
                        var pressureDropIndexTable:Int
                        if(resultSizeConduitOfInch == "-") temp = "${resultSizeConduitOfmm}mm"
                        else temp = "${resultSizeConduitOfmm} mm ( ${resultSizeConduitOfInch} inch )"

                        val s = SpannableString(temp.trim())
                        if (temp.indexOf('/') != -1) {
                            val len = temp.length
                            s.setSpan(SuperscriptSpan(), len - 10, len - 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                            s.setSpan( applicationContext,len - 10, len - 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                            s.setSpan(applicationContext, len - 9, len - 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัว /
                            s.setSpan(SubscriptSpan(), len - 8, len - 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
                            s.setSpan(applicationContext, len - 8, len-7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
                        }

                        if(cableSize.length > 10) textViewShow2.text = Html.fromHtml("${cableSize.replace("mm","mm<sup><small><small>2</small></small></sup>")}")
                        else textViewShow2.text = Html.fromHtml("$cableSize mm<sup><small><small>2</small></small></sup>")

                        if(typeCableTextView.text == "NYY-G" || typeCableTextView.text == "VCT-G"){
                            textViewResultWireGround.text = "-"
                            pressureDropIndexTable = 1
                        }else{
                            textViewResultWireGround.text = Html.fromHtml("$sizeWireGround mm<sup><small><small>2</small></small></sup>")
                            if(typeCableTextView.text == "XLPE") pressureDropIndexTable = 2
                            else pressureDropIndexTable = 0
                        }
                        textViewShow4.text = s

                        for (h in 2..20) {
                            val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
                            val wbPressure = Workbook.getWorkbook(pressureCable)
                            val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
                            val fineCableTypeInTable = sheetPressure.getCell(0, h).contents
                            val amountDeistance = Integer.parseInt(editTextDistance.text.toString())

                            if (cableSizeWithOutX == fineCableTypeInTable) { // แก้ cableSize ตัดคำออก
                                val getreslutInTable = sheetPressure.getCell(1, h).contents.toDouble()
                                val pullResult = getreslutInTable * Integer.parseInt(circuitTextView.text.toString().replace("A", "")) * amountDeistance / 1000 // result
                                val PercentPressure  = 100 * pullResult / 230 // result


                                var pullResulttoString = "${"%.2f V".format(pullResult)}"
                                var percentPressuretoString = "${"%.2f V".format(PercentPressure)}"
                                if(pullResult >= 1000){
                                    pullResulttoString = pullResulttoString.replace("${pullResulttoString.slice(0..0)}","${pullResulttoString.slice(0..0)},")
                                    if(PercentPressure >= 1000){
                                        percentPressuretoString = percentPressuretoString.replace("${percentPressuretoString.slice(0..0)}","${percentPressuretoString.slice(0..0)},")
                                    }
                                    textViewShow6.text = "$pullResulttoString $percentPressuretoString"
                                }else{
                                    textViewShow6.text = "${"%.2f V".format(pullResult)} (${"%.2f".format(PercentPressure)}%) "
                                }


                            }
                        }
                        break
                    }
                }
            }


        }catch (e: IOException){ println("Error : $e") }
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            if (type == "phase"){
                putString(TASK_LIST_PREF_KEY_PHASE_IN_WIRESIZE, data)
            }
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE, data)
            }
            if (type == "installationDes"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE_DES, data)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE, data)
            }
            if (type == "circuit"){
                putString(TASK_LIST_PREF_KEY_CIRCUIT_IN_WIRESIZE, data)
            }
            if (type == "distance"){
                putString(TASK_LIST_PREF_KEY_DISTANCE_IN_WIRESIZE, data)
            }
            commit()
        }
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfPhase = sharedPref.getString(TASK_LIST_PREF_KEY_PHASE_IN_WIRESIZE, "1")
        val dataOfInstallation = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE, "กลุ่ม 2")
//        val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE, "IEC01(THW)")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT_IN_WIRESIZE, "40A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_IN_WIRESIZE, "10")

        val dataOfTypeCable = if(dataOfInstallation!!.slice(0..6) == "กลุ่ม 5" ){
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE, "NYY")
        }else{
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE, "IEC01")
        }
        phaseTextView.text = "$dataOfPhase เฟส"
        installationTextView.text = dataOfInstallation!!.slice(0..6)
        typeCableTextView.text = dataOfTypeCable
        circuitTextView.text = dataOfCircuit
        editTextDistance.setText(dataOfDistance)

    }
    fun backOnClick(view: View) {
        finish()
    }

    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }

    private fun installationOfTable(installation: String):String{
        return when(installation){
            "กลุ่ม 2" -> "WireGroup_Group2.xls"
            "กลุ่ม 5" -> "WireGroup_Group5.xls"
            "กลุ่ม 7" -> "WireGroup_Group7.xls"
            else -> ""
        }
    }

    private fun circuitCheckPhaseAndCableType(phase: String, cableType: String):Int{
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfInstallationDes = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE_DES, null)

        return if(phase == "1 เฟส"){
            textViewReferenceVoltage.text = "(แรงดันอ้างอิง 230V)"
            when(cableType){
                "IEC01" -> 0
                "NYY" -> 1
                "VCT" -> 2
                "XLPE" -> 3
                "NYY-G" -> 4
                "VCT-G" -> 5
                else -> 0
            }
        }else{ // 3 เฟส
            if(installationTextView.text == "กลุ่ม 7"){
                textViewReferenceVoltage.text = "(แรงดันอ้างอิง 400V)"

                if(dataOfInstallationDes == "วางบนรางเคเบิ้ล ไม่มีฝาปิดแบบระบายอากาศ"){
                    when(cableType){
                        "NYY 1C" -> 0
                        "NYY 4C" -> 2
                        "XLPE 1C" -> 4
                        "XLPE 4C" -> 6
                        else -> 0
                    }
                }else{
                    when(cableType){
                        "NYY 1C" -> 1
                        "NYY 4C" -> 3
                        "XLPE 1C" -> 5
                        "XLPE 4C" -> 7
                        else -> 0
                    }
                }
            }else{
                textViewReferenceVoltage.text = "(แรงดันอ้างอิง 400V)"
                when(cableType){
                    "IEC01" -> 6
                    "NYY" -> 7
                    "VCT" -> 8
                    "XLPE" -> 9
                    "NYY-G" -> 10
                    "VCT-G" -> 11
                    else -> 0
                }
            }

        }
    }

}



