package com.alw.temca.ui.WireSize

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.Function.FindDetailInstallation
import com.alw.temca.Model.ReportResultWireSize
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_wire_size.*
import java.io.IOException


class WireSizeActivity : AppCompatActivity() {
    private val TASK_NAME_REQUEST_CODE = 100
    private val TASK_LIST_PREF_KEY_PHASE_IN_WIRESIZE = "task_list_phase_in_wiresize"
    private val TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE = "task_list_installation_in_wiresize"
    private val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE = "task_list_type_cable_in_wiresize"
    private val TASK_LIST_PREF_KEY_CIRCUIT_IN_WIRESIZE = "task_list_circuit_in_wiresize"
    private val TASK_LIST_PREF_KEY_DISTANCE_IN_WIRESIZE = "task_list_distance_in_wiresize"
    private val PREF_NAME = "task_wire"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wire_size)
        tableBeforeCalculate.visibility = View.GONE

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
                    editTextDistance.hint = "2000"
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                val dataPhase = data!!.getIntExtra("dataPhase", 0)
                val dataInstallation = data!!.getStringExtra("dataInstall")
                val dataTypeCable = data!!.getStringExtra("dataTypeCable")
//                val dataCircuit = data!!.getStringExtra("dataCircuit")

                if (dataPhase != 0){
                    phaseTextView.text = "$dataPhase เฟส"
                    saveData("phase", dataPhase.toString())
                }
                if (dataInstallation != null) {
                    val dataInstallationSlice = dataInstallation.slice(0..6)
                    if(dataInstallationSlice == "กลุ่ม 5"){
                            if (typeCableTextView.text.equals("IEC01")){
                                typeCableTextView.text = "NYY"
                            }
                    }
                    installationTextView.text = dataInstallationSlice
                    saveData("installation", dataInstallation)
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
        btnCal.visibility = View.VISIBLE
        circuitTextView.text = "40A"
    }

    fun phaseOnClick(view: View){
        val intent = Intent(this, PhaseActivity::class.java)
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
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun CircuitOnClick(view: View) {

        val intent = Intent(this, CircuitActivity::class.java)

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
                    else -> i-3
                }
                intent.putExtra("RowAmountAmp", rowAmount)
                startActivity(intent)
                finish()
                break
            }
        }
    }

    fun ReportOnClick(view: View) {
        val intent = Intent(this, ReportActivity::class.java)
        val bundle = Bundle()
        val textInstallation = FindDetailInstallation(installationTextView.text.toString())
        bundle.putParcelable("reportWireSize", ReportResultWireSize(
                phaseTextView.text.toString(),
                textInstallation,
                typeCableTextView.text.toString(),
                circuitTextView.text.toString(),
                editTextDistance.text.toString(),
                textViewShow2.text.toString(), // text2 is cablesize
                textViewResultWireGround.text.toString(),
                textViewShow4.text.toString(), // text4 is conduitsize
                textViewShow6.text.toString()))
        intent.putExtras(bundle)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
        finish()
    }


    fun DestanceOnClick(view: View) {
        editTextDistance.setText("")
        editTextDistance.hint = "20"
        editTextDistance.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistance, InputMethodManager.SHOW_IMPLICIT)
        wayBackActivity1.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    fun calculatorOnClick(view: View) {
        if (editTextDistance.text.isEmpty()){
            editTextDistance.setText("10")
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
                        s.setSpan(TextAppearanceSpan(null, 0, 40, null, null), len - 10, len - 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                        s.setSpan(TextAppearanceSpan(null, 0, 40, null, null), len - 9, len - 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัว /
                        s.setSpan(SubscriptSpan(), len - 8, len - 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
                        s.setSpan(TextAppearanceSpan(null, 0, 40, null, null), len - 8, len-7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
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
                            textViewShow6.text = "${"%.2f V".format(pullResult)} (${"%.2f".format(PercentPressure)}%) "
                        }
                    }
                    break
                }
            }
        }catch (e: IOException){ println("Error : $e") }
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            if (type == "phase"){
                putString(TASK_LIST_PREF_KEY_PHASE_IN_WIRESIZE, data)
            }
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE, data)
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

    fun loadData(){
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
            else -> ""
        }
    }

    private fun circuitCheckPhaseAndCableType(phase: String, cableType: String):Int{
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



