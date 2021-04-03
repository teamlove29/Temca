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
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_wire_size.*
import kotlinx.android.synthetic.main.activity_wire_size.typeCableTextView
import kotlinx.android.synthetic.main.activity_wire_size.wayBackActivity1
import java.io.IOException


class WireSizeActivity : AppCompatActivity() {
    final val TASK_NAME_REQUEST_CODE = 100
    final val TASK_LIST_PREF_KEY_PHASE = "task_list_phase"
    final val TASK_LIST_PREF_KEY_INSTALLATION = "task_list_installation"
    final val TASK_LIST_PREF_KEY_TYPE_CABLE = "task_list_type_cable"
    final val TASK_LIST_PREF_KEY_CIRCUIT = "task_list_circuit"
    final val TASK_LIST_PREF_KEY_DISTANCE = "task_list_distance"
    final val PREF_NAME = "task_name"

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
                    installationTextView.text = dataInstallation.slice(0..6)
                    saveData("installation", dataInstallation)
                }
                if (dataTypeCable != null) {
                    typeCableTextView.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)
                }
            }
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

    }  fun typeCableOnClick(view: View) {
        val intent = Intent(this, TypeCableActivity::class.java)
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
        editTextDistance.hint = " "
        editTextDistance.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistance, InputMethodManager.SHOW_IMPLICIT)
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
        editTextDistance.clearFocus()
        btnCal.apply {
            hideKeyboard()
        }

        lateinit var groupInstallation:String
        when(installationTextView.text){
//            "กลุ่ม 1" -> groupInstallation = "Group1"
            "กลุ่ม 2" -> groupInstallation = "Group2"
            else -> groupInstallation = "Group2"
        }

        val typeCableFile:Int
        typeCableFile = when(typeCableTextView.text){
            "IEC01(THW)" -> 0
            "NYY 1C" -> 0
            "NYY 2C" -> 1
            "NYY 3C" -> 1
            "NYY 4C" -> 1
            "IEC10 2C" -> 1
            "IEC10 3C" -> 1
            "XLPE 1C" -> 2
            "XLPE 2C" -> 3
            "XLPE 3C" -> 3
            "XLPE 4C" -> 3
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
            else -> return
        }

        val installationInTable = when(installationTextView.text){
            "กลุ่ม 2" -> "WireGroup_Group2.xls"
            "กลุ่ม 5" -> "WireGroup_Group5.xls"
            else -> return
        }

        try {
            val typeCable = applicationContext.assets.open(installationInTable)
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(typeCableFile)

            if(phaseTextView.text == "1 เฟส"){
                for(i in 2..20){
                    val findInOnePhase = sheet.getCell(1, i).contents
                    val circuitToInt = Integer.parseInt(circuitTextView.text.toString().replace("A",""))
                    if (circuitToInt <= findInOnePhase.toInt()){
                        val getCableSizeInTable = sheet.getCell(0, i).contents // result
                        for (k in 1..19){
                            val typeCable = applicationContext.assets.open("TypeCable_Table.xls")
                            val wb = Workbook.getWorkbook(typeCable)
                            val sheet = wb.getSheet(fineSizeCableInTable)
                            val checkCableSizeInTable = sheet.getCell(0, k).contents

                            if(checkCableSizeInTable == getCableSizeInTable){
                                for (g in 1..12){
                                    val checkCableSizeInTable2 = sheet.getCell(g, k).contents.toInt()
                                    if (2 < checkCableSizeInTable2){ // 2 is PhaseSize
                                        val getConduitSize2InTable = sheet.getCell(g, 0).contents // result
                                        for (h in 2..20){
                                            val pressureCable = applicationContext.assets.open("qwerty.xls") // pressure_drop_file
                                            val wbPressure  = Workbook.getWorkbook(pressureCable)
                                            val sheetPressure = wbPressure.getSheet(0)
                                            val fineCabletypeInTable = sheetPressure.getCell(0, h).contents
                                            val amountDeistance = Integer.parseInt(editTextDistance.text.toString())

                                            if (getCableSizeInTable == fineCabletypeInTable){
                                                val getreslutInTable = sheetPressure.getCell(1, h).contents.toFloat()
                                                val pullResult = fineCabletypeInTable.toFloat() * getreslutInTable * amountDeistance / 1000 // result
                                                val PercentPressure  = 100 * pullResult / 230 // result
                                                val resultSizeConduit = FindCableSizeInTable(getConduitSize2InTable.replace("\"",""))
                                                textViewShow2.text = Html.fromHtml("$getCableSizeInTable mm<sup><small><small>2</small></small></sup>")
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
                    }else{
                        textViewShow2.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                        textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                        textViewShow4.text = "- (สูงสุด - เส้น)"
                        textViewShow6.text = "- V"
                    }
                }
            }else{
                //  Fine three Phase
                for(j in 2..20){
                    val findInthreePhase = sheet.getCell(2, j).contents
                    val circuitToInt = Integer.parseInt(circuitTextView.text.toString().replace("A",""))

                    if (circuitToInt <= findInthreePhase.toInt()){
                        val getCableSizeInTable = sheet.getCell(0, j).contents // result
                        val typeCable = applicationContext.assets.open("TypeCable_Table.xls")
                        val wb = Workbook.getWorkbook(typeCable)
                        val sheet = wb.getSheet(fineSizeCableInTable)

                        for (k in 1..19){
                            val checkCableSizeInTable = sheet.getCell(0, k).contents
                            if(checkCableSizeInTable == getCableSizeInTable){
                                for (g in 1..12){
                                    val checkCableSizeInTable2 = sheet.getCell(g, k).contents.toInt()
                                    if (4 < checkCableSizeInTable2){ // 4 is PhaseSize
                                        val getCableSize2InTable = sheet.getCell(g, 0).contents // result
                                        for (h in 2..20){
                                            val pressureCable = applicationContext.assets.open("qwerty.xls") // pressure_drop_file
                                            val wbPressure  = Workbook.getWorkbook(pressureCable)
                                            val sheetPressure = wbPressure.getSheet(0)
                                            val fineCabletypeInTable = sheetPressure.getCell(0, h).contents
                                            val amountDeistance = Integer.parseInt(editTextDistance.text.toString())

                                            if (getCableSizeInTable == fineCabletypeInTable){
                                                val getreslutInTable = sheetPressure.getCell(2, h).contents.toFloat()
                                                val pullResult = fineCabletypeInTable.toFloat() * getreslutInTable * amountDeistance / 1000 // result
                                                val PercentPressure  = 100 * pullResult / 400 // result
                                                val resultSizeConduit = FindCableSizeInTable(getCableSize2InTable.replace("\"",""))
                                                textViewShow2.text = Html.fromHtml("$getCableSizeInTable mm<sup><small><small>2</small></small></sup>")
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
                    }else{
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
                putString(TASK_LIST_PREF_KEY_PHASE, data)
            }
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION, data)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE, data)
            }
            if (type == "circuit"){
                putString(TASK_LIST_PREF_KEY_CIRCUIT, data)

            }
            if (type == "distance"){
                putString(TASK_LIST_PREF_KEY_DISTANCE, data)
            }
            commit()
        }
    }

    fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfPhase = sharedPref.getString(TASK_LIST_PREF_KEY_PHASE, "1")
        val dataOfInstallation = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION, "กลุ่ม 2")
        val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE, "IEC01(THW)")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT, "40A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE, "10")

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
            val typeCable = applicationContext.assets.open("CircuitSize.xls")
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(0)
            val sToInt = Integer.parseInt(breakerReA)

            for (i in 0..17) {
                val sizeCirCuit = sheet.getCell(0, i).contents.toInt()
                if (sToInt <= sizeCirCuit) {
                        val sizeWireGround = sheet.getCell(2, i).contents
                        textViewResultWireGround.text = Html.fromHtml("${sizeWireGround} mm<sup><small><small>2</small></small></sup>")
                    break
                } else {
                     textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
                }
            }

        } else {
                    textViewResultWireGround.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
        }
    }



}



