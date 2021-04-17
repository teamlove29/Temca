package com.alw.temca.ui.WireSize

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.Function.FindCableSizeInTable
import com.alw.temca.Function.FindDetailInstallation
import com.alw.temca.Model.ReportResultWireSize
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_moter.*
import kotlinx.android.synthetic.main.activity_wire_size.*
import kotlinx.android.synthetic.main.activity_wire_size.phaseTextView
import kotlinx.android.synthetic.main.activity_wire_size.typeCableTextView
import kotlinx.android.synthetic.main.activity_wire_size.wayBackActivity1
import java.io.IOException


class WireSizeActivity : AppCompatActivity() {
    final val TASK_NAME_REQUEST_CODE = 100
    final val TASK_LIST_PREF_KEY_PHASE_IN_WIRESIZE = "task_list_phase_in_wiresize"
    final val TASK_LIST_PREF_KEY_INSTALLATION_IN_WIRESIZE = "task_list_installation_in_wiresize"
    final val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE = "task_list_type_cable_in_wiresize"
    final val TASK_LIST_PREF_KEY_CIRCUIT_IN_WIRESIZE = "task_list_circuit_in_wiresize"
    final val TASK_LIST_PREF_KEY_DISTANCE_IN_WIRESIZE = "task_list_distance_in_wiresize"
    final val PREF_NAME = "task_wire"

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
        intent = getIntent()
        val dataCircuit = intent.extras?.getString("dataCircuit")
        if (dataCircuit != null){
            circuitTextView.text = dataCircuit
            saveData("circuit", dataCircuit)
        }
        editTextDistance.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()){
                    editTextDistance.hint = "2000"
                }else{
                    editTextDistance.hint = ""
                }
//                sponsorImageView.visibility = View.VISIBLE
                tableBeforeCalculate.visibility = View.GONE
                btnCal.visibility = View.VISIBLE
                wayBackActivity1.visibility = View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("distance",s.toString())
            }

        })





//        editTextAmountCable.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                //ก่อนเปลี่ยนคือ ?
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s!!.isEmpty()){
//                    editTextAmountCable.hint = "20"
//                }else{
//                    editTextAmountCable.hint = ""
//                }
//                wayBackActivity1.visibility = View.VISIBLE
//                wayBackActivity2.visibility = View.GONE
//                tableBeforeCalculateInPipe.visibility = View.GONE
//                btnCalInPipeSize.visibility = View.VISIBLE
//                btnCalInPipeSize.apply {
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                //หลังจากพิมพ์ผลลัพคือ ?
//                saveData("amount", s.toString())
//            }
//
//        })



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                val dataPhase = data!!.getIntExtra("dataPhase",0)
                val dataInstallation = data!!.getStringExtra("dataInstall")
                val dataTypeCable = data!!.getStringExtra("dataTypeCable")
//                val dataCircuit = data!!.getStringExtra("dataCircuit")

                if (dataPhase != 0){
                    phaseTextView.text = "$dataPhase เฟส"
                    saveData("phase", dataPhase.toString())
                }
                if (dataInstallation != null) {
                    val dataInstallationSlice = dataInstallation.slice(0..6)
                    val listRemoveGroup5 = arrayListOf<String>("IEC01(THW)","IEC10 2C","IEC10 3C","IEC10 4C")
                    if(dataInstallationSlice == "กลุ่ม 5"){
                        listRemoveGroup5.forEach {
                            if (it == typeCableTextView.text){
                                typeCableTextView.text = "NYY 1C"
                            }
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
    }

    fun phaseOnClick(view: View){
        val intent = Intent(this, PhaseActivity::class.java)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun installationOnClick(view: View) {
        val intent = Intent(this, InstallationActivity::class.java)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun typeCableOnClick(view: View) {
        val intent = Intent(this, TypeCableActivity::class.java)
        if(installationTextView.text == "กลุ่ม 5"){
            intent.putExtra("Activity", "Group5")
        }
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun CircuitOnClick(view: View) {
        val intent = Intent(this, CircuitActivity::class.java)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
        finish()
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
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
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

//        lateinit var groupInstallation:String
//        when(installationTextView.text){
////            "กลุ่ม 1" -> groupInstallation = "Group1"
//            "กลุ่ม 2" -> groupInstallation = "Group2"
//            else -> groupInstallation = "Group2"
//        }

        val typeCableFile:Int
        typeCableFile = when(typeCableTextView.text){
            "IEC01(THW)" -> 0
            "IEC10 2C" -> 1
            "IEC10 3C" -> 1
            "IEC10 4C" -> 1
            "NYY 1C" -> 0
            "NYY 2C" -> 1
            "NYY 3C" -> 1
            "NYY 4C" -> 1
            "XLPE 1C" -> 2
            "XLPE 2C" -> 3
            "XLPE 3C" -> 3
            "XLPE 4C" -> 3
            "VCT 1C" -> 0
            "VCT 2C" -> 1
            "VCT 3C" -> 1
            "VCT 4C" -> 1
            else -> return
        }

        val fineSizeCableInTable:Int
        fineSizeCableInTable = when(typeCableTextView.text){
            "IEC01(THW)" -> 0
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
            "VCT 1C" -> 12
            "VCT 2C" -> 13
            "VCT 3C" -> 14
            "VCT 4C" -> 15
            else -> return
        }

        val installationInTable = when(installationTextView.text){
            "กลุ่ม 2" -> "WireGroup_Group2.xls"
            "กลุ่ม 5" -> "WireGroup_Group5.xls"
            else -> return
        }

        val pressureDropIndexTable = when(typeCableTextView.text){
            "IEC01(THW)" -> 0
            "IEC10 2C" -> 1
            "IEC10 3C" -> 1
            "IEC10 4C" -> 1
            "NYY 1C" -> 0
            "NYY 2C" -> 1
            "NYY 3C" -> 1
            "NYY 4C" -> 1
            "XLPE 1C" -> 2
            "XLPE 2C" -> 3
            "XLPE 3C" -> 3
            "XLPE 4C" -> 3
            "VCT 1C" -> 0
            "VCT 2C" -> 1
            "VCT 3C" -> 1
            "VCT 4C" -> 1
            else -> return
        }


        try {
            val typeCable = applicationContext.assets.open(installationInTable)
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(typeCableFile)
            if(phaseTextView.text == "1 เฟส"){
                for(i in 2..26){ //WireGroup_Group
                    val findInOnePhase = sheet.getCell(1, i).contents
                    val circuitToInt = Integer.parseInt(circuitTextView.text.toString().replace("A",""))
                    if (circuitToInt <= findInOnePhase.toInt()){
                        val getCableSizeInTable = sheet.getCell(0, i).contents // result
                        for (k in 1..21){ // row สาย
                            val typeCable = applicationContext.assets.open("TypeCable_Table.xls")
                            val wb = Workbook.getWorkbook(typeCable)
                            val sheet = wb.getSheet(fineSizeCableInTable)
                            val checkCableSizeInTable = sheet.getCell(0, k).contents

                            if(checkCableSizeInTable == getCableSizeInTable){
                                for (g in 1..12){
                                    val checkCableSizeInTable2 = sheet.getCell(g, k).contents.toInt()

                                    val twoSet = if(findInOnePhase.toInt() >= 630 && typeCableTextView.text != "XLPE 1C"){ 4 }else{ 2 } // กรณีที่เป็นสองชุด ให้ x 2

                                    if (twoSet < checkCableSizeInTable2){ // 2 is PhaseSize ถ้าน้อยกว่า 2 จะไม่แสดงค่า
                                        val getConduitSize2InTable = sheet.getCell(g, 0).contents // result
                                        for (h in 2..20){
                                            val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
                                            val wbPressure  = Workbook.getWorkbook(pressureCable)
                                            val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
                                            val fineCabletypeInTable = sheetPressure.getCell(0, h).contents
                                            val amountDeistance = Integer.parseInt(editTextDistance.text.toString())

                                            if (getCableSizeInTable == fineCabletypeInTable){
                                                val getreslutInTable = sheetPressure.getCell(1, h).contents.toFloat()
//                                                val pullResult = fineCabletypeInTable.toFloat() * getreslutInTable * amountDeistance / 1000 // result
                                                val pullResult =
                                                            getreslutInTable *
                                                            Integer.parseInt(circuitTextView.text.toString().replace("A","")) *
                                                            amountDeistance / 1000 // result

                                                val PercentPressure  = 100 * pullResult / 230 // result
                                                val resultSizeConduit = FindCableSizeInTable(getConduitSize2InTable.replace("\"",""))

                                                if(findInOnePhase.toInt() >= 630 && typeCableTextView.text != "XLPE 1C"){
                                                    textViewShow2.text = Html.fromHtml("2ชุด - $getCableSizeInTable mm<sup><small><small>2</small></small></sup>")
                                                }else{
                                                    if (findInOnePhase.toInt() >= 800 )
                                                        textViewShow2.text = Html.fromHtml("2ชุด - $getCableSizeInTable mm<sup><small><small>2</small></small></sup>")
                                                    else
                                                        textViewShow2.text = Html.fromHtml("$getCableSizeInTable mm<sup><small><small>2</small></small></sup>")
                                                }
                                                textViewShow4.text = "$getConduitSize2InTable $resultSizeConduit mm (สูงสุด $checkCableSizeInTable2 เส้น)"
                                                textViewShow6.text = "${"%.2f V".format(pullResult)} (${"%.2f".format(PercentPressure)}%) "
                                                textViewReferenceVoltage.text = "(แรงดันอ้างอิง 230V)"
                                                fineWireGround(circuitTextView.text.toString())
                                                break
                                            }
                                        }
                                        break
                                    }else{
                                        textViewShow2.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                                        textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                                        textViewShow4.text = "- (สูงสุด - เส้น)"
                                        textViewShow6.text = "- V"
                                    }
                                }

                            }
                        }
                        break
                    }else{ //กรณีที่ไม่มีค่า || มากกว่าที่ค่ามีในตาราง
                        textViewShow2.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                        textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                        textViewShow4.text = "- (สูงสุด - เส้น)"
                        textViewShow6.text = "- V"
                    }
                }
            }else{
                //  Fine three Phase
                for(j in 2..26){ //WireGroup_Group
                    val findInthreePhase = sheet.getCell(2, j).contents
                    val circuitToInt = Integer.parseInt(circuitTextView.text.toString().replace("A",""))

                    if (circuitToInt <= findInthreePhase.toInt()){
                        val getCableSizeInTable = sheet.getCell(0, j).contents // result
                        val typeCable = applicationContext.assets.open("TypeCable_Table.xls")
                        val wb = Workbook.getWorkbook(typeCable)
                        val sheet = wb.getSheet(fineSizeCableInTable)

                        for (k in 1..21){ // row สาย
                            val checkCableSizeInTable = sheet.getCell(0, k).contents
                            if(checkCableSizeInTable == getCableSizeInTable){
                                for (g in 1..12){
                                    val checkCableSizeInTable2 = sheet.getCell(g, k).contents.toInt()

                                    val twoSet = if(findInthreePhase.toInt() >= 630 && typeCableTextView.text != "XLPE 1C"){ 8 }else{ 4 } // กรณีที่เป็นสองชุด ให้ x 2

                                    if (twoSet < checkCableSizeInTable2){ // 4 is PhaseSize ถ้าน้อยกว่า 4 จะไม่แสดงค่า
                                        val getCableSize2InTable = sheet.getCell(g, 0).contents // result
                                        for (h in 2..20){
                                            val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
                                            val wbPressure  = Workbook.getWorkbook(pressureCable)
                                            val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
                                            val fineCabletypeInTable = sheetPressure.getCell(0, h).contents
                                            val amountDeistance = Integer.parseInt(editTextDistance.text.toString())

                                            if (getCableSizeInTable == fineCabletypeInTable){
                                                val getreslutInTable = sheetPressure.getCell(2, h).contents.toFloat()
//                                                val pullResult = fineCabletypeInTable.toFloat() * getreslutInTable * amountDeistance / 1000 // result
                                                val pullResult =
                                                    getreslutInTable *
                                                            Integer.parseInt(circuitTextView.text.toString().replace("A","")) *
                                                            amountDeistance / 1000 // result
                                                val PercentPressure  = 100 * pullResult / 400 // result

                                                val resultSizeConduit = FindCableSizeInTable(getCableSize2InTable.replace("\"",""))

                                                if(findInthreePhase.toInt() >= 630 && typeCableTextView.text != "XLPE 1C"){
                                                    textViewShow2.text = Html.fromHtml("2ชุด - $getCableSizeInTable mm<sup><small><small>2</small></small></sup>")
                                                }else{
                                                    if (findInthreePhase.toInt() >= 800 )
                                                        textViewShow2.text = Html.fromHtml("2ชุด - $getCableSizeInTable mm<sup><small><small>2</small></small></sup>")
                                                    else
                                                        textViewShow2.text = Html.fromHtml("$getCableSizeInTable mm<sup><small><small>2</small></small></sup>")
                                                }
                                                textViewShow4.text = "$getCableSize2InTable $resultSizeConduit mm (สูงสุด $checkCableSizeInTable2 เส้น)"
                                                textViewShow6.text = "${"%.2f V".format(pullResult)} (${"%.2f".format(PercentPressure)}%) "
                                                textViewReferenceVoltage.text = "(แรงดันอ้างอิง 400V)"
                                                fineWireGround(circuitTextView.text.toString())
                                                break
                                            }
                                        }
                                        break
                                    }else{
                                        textViewShow2.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                                        textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                                        textViewShow4.text = "- (สูงสุด - เส้น)"
                                        textViewShow6.text = "- V"
                                    }
                                }
                            }
                        }
                        break
                    }else{ //กรณีที่ไม่มีค่า || มากกว่าที่ค่ามีในตาราง
                        textViewShow2.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                        textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                        textViewShow4.text = "- (สูงสุด - เส้น)"
                        textViewShow6.text = "- V"
                    }
                }
            }


        }catch (e:IOException){
            println("Error : $e")
        }
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
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE, "NYY 1C")
        }else{
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_WIRESIZE, "IEC01(THW)")
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

    fun fineWireGround(breaker:String){
        val breakerReA =  breaker.replace("A","")
        if (breakerReA.isNotEmpty()) {
            val typeCable = applicationContext.assets.open("CircuitSize.xls") // สายดิน
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(0)
            val sToInt = Integer.parseInt(breakerReA)

            for (i in 0..18) {
                val sizeCirCuit = sheet.getCell(0, i).contents.toInt()
                if (sToInt <= sizeCirCuit) {
                        val sizeWireGround = sheet.getCell(2, i).contents
                        textViewResultWireGround.text = Html.fromHtml("$sizeWireGround mm<sup><small><small>2</small></small></sup>")
                    break
                } else {
                     textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                }
            }
        } else { textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>") }
    }



}



