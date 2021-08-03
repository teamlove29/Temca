package com.alw.temca.ui.ElectricalOnePhase

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
import com.alw.temca.ui.SponsorActivity

import jxl.Workbook
import kotlinx.android.synthetic.main.activity_one_phase.*
import java.io.IOException


class OnePhaseActivity : AppCompatActivity() {
    
    companion object{
        const val TASK_NAME_REQUEST_CODE = 100
        const val TASK_LIST_PREF_KEY_PHASE_IN_ONE_PHASE = "task_list_phase_one_phase"
        const val TASK_LIST_PREF_KEY_INSTALLATION_ONE_PHASE = "task_list_installation_one_phase"
        const val TASK_LIST_PREF_KEY_INSTALLATION_DES_ONE_PHASE = "task_list_installation_des_one_phase"
        const val TASK_LIST_PREF_KEY_TYPE_CABLE_ONE_PHASE = "task_list_type_cable_one_phase"
        const val TASK_LIST_PREF_KEY_CIRCUIT_ONE_PHASE = "task_list_circuit_one_phase"
        const val TASK_LIST_PREF_KEY_DISTANCE_ONE_PHASE = "task_list_distance_one_phase"
        const val PREF_NAME = "task_one_phase"
        private val railSizeList = ArrayList<RailSizeModel>()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun saveData(type: String, value: String){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_ONE_PHASE, value)
            }
            if (type == "installationDes"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_DES_ONE_PHASE, value)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_ONE_PHASE, value)
            }
            if (type == "circuit"){
                putString(TASK_LIST_PREF_KEY_CIRCUIT_ONE_PHASE, value)
            }
            if (type == "distance"){
                putString(TASK_LIST_PREF_KEY_DISTANCE_ONE_PHASE, value)
            }
            commit()
        }
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfInstallation = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_ONE_PHASE, "กลุ่ม 2")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT_ONE_PHASE, "40A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_ONE_PHASE, "10")

        val dataOfTypeCable = if(dataOfInstallation!!.slice(0..6) == "กลุ่ม 5" ){
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_ONE_PHASE, "NYY 1/C")
        }else{
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_ONE_PHASE, "IEC01")
        }
        installationTextView.text = dataOfInstallation!!.slice(0..6)
        typeCableTextView.text = dataOfTypeCable
        circuitTextView.text = dataOfCircuit
        editTextDistance.setText(dataOfDistance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_phase)

        tableBeforeCalculate.visibility = View.GONE

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
                        val temp:String
                        var pressureDropIndexTable:Int
                        if(resultSizeConduitOfInch == "-") temp = "${resultSizeConduitOfmm}mm"
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

                        if(typeCableTextView.text == "NYY - G"
                                || typeCableTextView.text == "VCT - G"
                                || typeCableTextView.text == "VCT 2/C - G"
                                || typeCableTextView.text == "NYY 2/C - G"
                                || typeCableTextView.text == "XLPE 2/C, G") {
                                textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
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
        val intent = Intent(this, ReportInOnePhaseActivity::class.java)
//        val bundle = Bundle()
        val textInstallation = FindDetailInstallation(installationTextView.text.toString())


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

    fun CircuitOnClick(view: View) {

        val intent = Intent(this, CircuitInOnePhaseActivity::class.java)
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

     fun installationOnClick(view: View) {
        val intent = Intent(this, InstallationInOnePhaseActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
     fun typeCableOnClick(view: View) {
        val intent = Intent(this, TypeCableInOnePhaseActivity::class.java)
        if(installationTextView.text == "กลุ่ม 2"){
            intent.putExtra("Group", "Group2")
        }else if(installationTextView.text == "กลุ่ม 5"){
            intent.putExtra("Group", "Group5")
        }
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }

    private fun installationOfTable(installation: String):String{
        return when(installation){
            "กลุ่ม 2" -> "WireGroup_Group2.xls"
            "กลุ่ม 5" -> "WireGroup_Group5.xls"
            else -> ""
        }
    }
    private fun circuitCheckPhaseAndCableType(phase: String, cableType: String):Int{
        textViewReferenceVoltage.text = "(แรงดันอ้างอิง 230V)"
        return  when(cableType){
                "IEC01" -> 0
                "NYY 1/C" -> 1
                "VCT 1/C" -> 2
                "XLPE 1/C" -> 3
                "XLPE 2/C, G" -> 4
                "NYY - G" -> 5
                "VCT - G" -> 6
                "NYY 2/C - G" -> 7
                "VCT 2/C - G" -> 8
                else -> 0
            }

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
                        typeCableTextView.text = "IEC01"
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