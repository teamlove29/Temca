package com.alw.temca.ui.ElectricalThreePhase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alw.temca.Function.FindDetailInstallation
import com.alw.temca.Model.RailSizeModel
import com.alw.temca.Model.ReportResultWireSize
import com.alw.temca.R
import com.alw.temca.ui.AmountInRailCable.AmountInRailCableActivity
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
import com.alw.temca.ui.SoonActivity
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook

import kotlinx.android.synthetic.main.activity_three_phase.*
import kotlinx.android.synthetic.main.activity_three_phase.btnCal
import kotlinx.android.synthetic.main.activity_three_phase.circuitTextView
import kotlinx.android.synthetic.main.activity_three_phase.editTextDistance
import kotlinx.android.synthetic.main.activity_three_phase.installationTextView
import kotlinx.android.synthetic.main.activity_three_phase.phaseTextView
import kotlinx.android.synthetic.main.activity_three_phase.tableBeforeCalculate
import kotlinx.android.synthetic.main.activity_three_phase.textViewReferenceVoltage
import kotlinx.android.synthetic.main.activity_three_phase.textViewResultWireGround
import kotlinx.android.synthetic.main.activity_three_phase.textViewShow2
import kotlinx.android.synthetic.main.activity_three_phase.textViewShow4
import kotlinx.android.synthetic.main.activity_three_phase.textViewShow5
import kotlinx.android.synthetic.main.activity_three_phase.textViewShow6
import kotlinx.android.synthetic.main.activity_three_phase.typeCableTextView
import kotlinx.android.synthetic.main.activity_three_phase.wayBackActivity1
import java.io.IOException

class ThreePhaseActivity : AppCompatActivity() {

    companion object{
        private const val TASK_NAME_REQUEST_CODE = 100
        private const val TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE= "task_list_installation_in_three_phase"
        private const val TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE_DES = "task_list_installation_in_three_phase_des"
        private const val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_THREE_PHASE = "task_list_type_cable_in_three_phase"
        private const val TASK_LIST_PREF_KEY_CIRCUIT_IN_THREE_PHASE= "task_list_circuit_in_three_phase"
        private const val TASK_LIST_PREF_KEY_DISTANCE_IN_THREE_PHASE = "task_list_distance_in_three_phase"
        private const val PREF_NAME = "task_three_phase"
        private val railSizeList = ArrayList<RailSizeModel>()
        var pressureDropIndexTable:Int = 0
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE, data)
            }
            if (type == "installationDes"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE_DES, data)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_THREE_PHASE, data)
            }
            if (type == "circuit"){
                putString(TASK_LIST_PREF_KEY_CIRCUIT_IN_THREE_PHASE, data)
            }
            if (type == "distance"){
                putString(TASK_LIST_PREF_KEY_DISTANCE_IN_THREE_PHASE, data)
            }
            commit()
        }
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfInstallation = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE, "กลุ่ม 2")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT_IN_THREE_PHASE, "40A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_IN_THREE_PHASE, "10")

        val dataOfTypeCable = if(dataOfInstallation!!.slice(0..6) == "กลุ่ม 5" ){
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_THREE_PHASE, "NYY 1/C")
        }else{
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_THREE_PHASE, "IEC 01")
        }
        installationTextView.text = dataOfInstallation!!.slice(0..6)
        typeCableTextView.text = dataOfTypeCable
        circuitTextView.text = dataOfCircuit
        editTextDistance.setText(dataOfDistance)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_phase)

        tableBeforeCalculate.visibility = View.GONE
        tableBeforeCalculateGroup7.visibility = View.GONE

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

    fun calculatorOnClick(view: View) {

        if (editTextDistance.text.isEmpty()){
            editTextDistance.setText("0")
            textViewShow5.visibility = View.GONE
            textViewReferenceVoltage.visibility = View.GONE
            textViewShow6.visibility = View.GONE
        }else{
            textViewShow5.visibility = View.VISIBLE
            textViewReferenceVoltage.visibility = View.VISIBLE
            textViewShow6.visibility = View.VISIBLE
        }


        if (circuitTextView.text.isEmpty()){
            circuitTextView.text = "40A"
        }

        if(editTextDistance.length() > 0 ){
            if(editTextDistance.text.toString() == "0" ) {
                editTextDistance.setText("0")
                textViewShow5.visibility = View.GONE
                textViewReferenceVoltage.visibility = View.GONE
                textViewShow6.visibility = View.GONE
            }
            else if(editTextDistance.text.toString() == "00") editTextDistance.setText("0")
            else if(editTextDistance.text.toString() == "000") editTextDistance.setText("0")
            else if(editTextDistance.text.toString() == "0000") editTextDistance.setText("0")
            else if(editTextDistance.text.toString().slice(0..0) == "0"){
                for(i in 0..3){
                    if(editTextDistance.text.toString().slice(0..0) != "0") break
                    else editTextDistance.setText(editTextDistance.text.toString().slice(1 until editTextDistance.length()))
                }
            }else{
                textViewShow5.visibility = View.VISIBLE
                textViewReferenceVoltage.visibility = View.VISIBLE
                textViewShow6.visibility = View.VISIBLE
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
                    val divisor = sheet.getCell(7, i).contents.toInt()



                    val temp:String
                    if(resultSizeConduitOfInch == "-") temp = "${resultSizeConduitOfmm}mm."
                    else temp = "$resultSizeConduitOfmm mm. ( $resultSizeConduitOfInch\" )"

                    val s = SpannableString(temp.trim())
                    if (temp.indexOf('/') != -1) {
                        val len = temp.length
                        s.setSpan(SuperscriptSpan(), len - 6, len - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                        s.setSpan(applicationContext,len - 6, len - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                        s.setSpan(applicationContext, len - 5, len - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัว /
                        s.setSpan(SubscriptSpan(), len - 4, len - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
                        s.setSpan(applicationContext, len - 4, len - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
                    }

                    if(cableSize.length > 10) textViewShow2.text = Html.fromHtml("${cableSize.replace("mm","mm<sup><small><small>2</small></small></sup>")}")
                    else textViewShow2.text = Html.fromHtml("$cableSize mm<sup><small><small>2</small></small></sup>")

                    if(typeCableTextView.text == "NYY 4/C - G"
                        || typeCableTextView.text == "VCT 4/C - G"
                        || typeCableTextView.text == "NYY 2/C - G"
                        || typeCableTextView.text == "VCT 2/C - G") {
                        textViewResultWireGround.text =  Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                    }else{
                        if(circuitTextView.text == "500A"
                                ||circuitTextView.text == "500A"
                                ||circuitTextView.text == "630A"
                                ||circuitTextView.text == "800A"
                                ||circuitTextView.text == "1000A"){
                            textViewResultWireGround.text = Html.fromHtml("$sizeWireGround mm<sup><small><small>2</small></small></sup> )")
                        }else{
                            textViewResultWireGround.text = Html.fromHtml("$sizeWireGround mm<sup><small><small>2</small></small></sup>")
                        }
                    }


                    if(typeCableTextView.text == "IEC 01"
                            ||typeCableTextView.text == "NYY 1/C"
                            ||typeCableTextView.text == "VCT 1/C"){
                        pressureDropIndexTable = 0
                    }else if(typeCableTextView.text == "NYY 2/C - G"
                            ||typeCableTextView.text == "VCT 2/C - G"
                            ||typeCableTextView.text == "NYY 4/C - G"
                            ||typeCableTextView.text == "VCT 4/C - G"){
                        pressureDropIndexTable = 1
                    }else if(typeCableTextView.text == "XLPE 1/C"){
                        pressureDropIndexTable = 2
                    }else if(typeCableTextView.text == "XLPE 2/C, G"
                            ||typeCableTextView.text == "XLPE 4/C, G"){
                        pressureDropIndexTable = 3
                    }


                    textViewShow4.text = s

                    for (h in 2..20) {
                        val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
                        val wbPressure = Workbook.getWorkbook(pressureCable)
                        val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
                        val fineCableTypeInTable = sheetPressure.getCell(0, h).contents
                        val amountDeistance = Integer.parseInt(editTextDistance.text.toString())

                        if (cableSizeWithOutX == fineCableTypeInTable) { // แก้ cableSize ตัดคำออก
                            val getreslutInTable = sheetPressure.getCell(2, h).contents.toDouble()

                            val pullResult = getreslutInTable * Integer.parseInt(circuitTextView.text.toString().replace("A", "")) * amountDeistance / 1000 / divisor.toDouble() // result
                            val PercentPressure  = 100 * pullResult / 230 / divisor.toDouble() // result

                            var pullResulttoString = "${"%.2f V".format(pullResult)}"
                            var percentPressuretoString = "${"%.2f".format(PercentPressure)}"
                            if(pullResult >= 1000){
                                pullResulttoString = "${pullResulttoString.slice(0..0)},${pullResulttoString.slice(1 until pullResulttoString.length)}"
                                if(PercentPressure >= 1000){
                                    percentPressuretoString = "( ${percentPressuretoString.slice(0..0)},${percentPressuretoString.slice(1 until percentPressuretoString.length)}% )"
                                }else{
                                    percentPressuretoString = "( ${percentPressuretoString}% )"
                                }
                                textViewShow6.text = "$pullResulttoString $percentPressuretoString"
                            }else{
                                textViewShow6.text = "${"%.2f V".format(pullResult)} ( ${"%.2f".format(PercentPressure)}% )"
                            }
                        }
                    }
                    break
                }
            }



        }catch (e: IOException){ println("Error : $e") }


    }

    fun ReportOnClick(view: View) {
        val dataToReport = ArrayList<ReportResultWireSize>()
        val intent = Intent(this, ReportInThreePhaseActivity::class.java)
//        val bundle = Bundle()
        val textInstallation = FindDetailInstallation(installationTextView.text.toString())


//        val groundSize = if(AmountInRailCableActivity.railSizeList[0].divisor == "1"){
//            "${AmountInRailCableActivity.railSizeList[0].groundSize} mm2"
//        }else{
//            "${AmountInRailCableActivity.railSizeList[0].divisor} ( ${AmountInRailCableActivity.railSizeList[0].groundSize} mm2 )"
//        }



        dataToReport.add(
            ReportResultWireSize(
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

        intent.putParcelableArrayListExtra("DataFromWireSize",dataToReport)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
        finish()
    }

    fun installationOnClick(view: View) {
        val intent = Intent(this, InstallationInThreePhaseActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun typeCableOnClick(view: View) {
        val intent = Intent(this, TypeCableInThreePhaseActivity::class.java)
        if(installationTextView.text == "กลุ่ม 2"){
            intent.putExtra("Group", "Group2")
        }else if(installationTextView.text == "กลุ่ม 5"){
            intent.putExtra("Group", "Group5")
        }
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }

    fun CircuitOnClick(view: View) {

        val intent = Intent(this, CircuitInThreePhaseActivity::class.java)
        val circuitCheckGroup:String = installationOfTable(installationTextView.text.toString())
        val circuitCheckCableType:Int = circuitCheckPhaseAndCableType(phaseTextView.text.toString(), typeCableTextView.text.toString())
        val typeCable = applicationContext.assets.open(circuitCheckGroup)
        val wb = Workbook.getWorkbook(typeCable)

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
    }

    fun DestanceOnClick(view: View) {
        editTextDistance.setText("")
        editTextDistance.hint = " "
        editTextDistance.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistance, InputMethodManager.SHOW_IMPLICIT)
        wayBackActivity1.visibility = View.VISIBLE
    }

    fun backOnClick(view: View) {
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
        sharedPref.apply()
        finish()
    }
    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
        sharedPref.apply()
        finish()
    }
    private fun installationOfTable(installation: String):String{
        return when(installation){
            "กลุ่ม 2" -> "WireGroup_Group2.xls"
            "กลุ่ม 5" -> "WireGroup_Group5.xls"
            else -> ""
        }
    }
    private fun circuitCheckPhaseAndCableType(phase: String, cableType: String):Int{
        textViewReferenceVoltage.text = "(แรงดันอ้างอิง 400V)"
        return when(cableType){  // 3 เฟส
                    "IEC 01" -> 9
                    "NYY 1/C" -> 10
                    "VCT 1/C" -> 11
                    "XLPE 1/C" -> 12
                    "XLPE 4/C, G" -> 13
                    "NYY 4/C - G" -> 14
                    "VCT 4/C - G" -> 15
                    else -> 0
                }
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                val dataInstallation = data!!.getStringExtra("dataInstall")
                val dataInstallationDes = data.getStringExtra("dataInstallDes").toString()
                val dataTypeCable = data.getStringExtra("dataTypeCable")

                if (dataInstallation != null) {
                    val dataInstallationSlice = dataInstallation.slice(0..6)
                    if(dataInstallationSlice == "กลุ่ม 5"){
                        typeCableTextView.text = "NYY 1/C"
                        saveData("typeCable", typeCableTextView.text.toString())
                    }else{
                        typeCableTextView.text = "IEC 01"
                        saveData("typeCable", typeCableTextView.text.toString())
                    }

                    installationTextView.text = dataInstallationSlice
                    saveData("installation", dataInstallation)
                    saveData("installationDes", dataInstallationDes)
                }
                if (dataTypeCable != null) {
                    typeCableTextView.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)
                }
                circuitTextView.text = "40A"
                saveData("circuit", "40A")

            }
            wayBackActivity1.visibility = View.VISIBLE
        }
        tableBeforeCalculate.visibility = View.GONE
        btnCal.visibility = View.VISIBLE


    }

}