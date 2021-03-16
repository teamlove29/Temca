package com.alw.temca.ui.WireSize

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener.clear
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.alw.temca.MainActivity
import com.alw.temca.Model.ReportReslutPipeSizeModel
import com.alw.temca.Model.ReportResultWireSize
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_circuit.*
import kotlinx.android.synthetic.main.activity_pipe_size.*
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
        loadData()

        editTextDistance.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                sponsorImageView.visibility = View.VISIBLE
                tableBeforeCalculate.visibility = View.GONE
                btnCal.visibility = View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("distance",s.toString())
            }

        })


        intent = getIntent()
        val dataCircuit = intent.extras?.getString("dataCircuit")
        if (dataCircuit != null){
             circuitTextView.text = dataCircuit
             saveData("circuit", dataCircuit)
        }


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
        bundle.putParcelable("reportWireSize", ReportResultWireSize(
                textViewShow2.text.toString(), // text2 is cablesize
                textViewShow4.text.toString(), // text4 is conduitsize
                ""))
        intent.putExtras(bundle)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
        finish()
    }

    fun DestanceOnClick(view: View) {
        editTextDistance.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistance, InputMethodManager.SHOW_IMPLICIT)
    }

    fun calculatorOnClick(view: View) {
        if (editTextDistance.text.isEmpty()){
            editTextDistance.setText("100")
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

        val typeCableFile:String
        typeCableFile = when(typeCableTextView.text){
            "IEC01" -> "WireGroup_IEC01_${groupInstallation}.xls"
            else -> "WireGroup_IEC01_Group2.xls"
//            "NYY 1C" -> typeCableFile = "NYY1C.xls"
//            "NYY 2C" -> typeCableFile = "NYY2C.xls"
//            "NYY 3C" -> typeCableFile = "NYY3C.xls"
//            "NYY 4C" -> typeCableFile = "NYY4C.xls"
//            "IEC10 2C" -> typeCableFile = "IEC102C.xls"
//            "IEC10 3C" -> typeCableFile = "IEC103C.xls"
//            "XLPE 1C" -> typeCableFile = "XLPE1C.xls"
//            "XLPE 2C" -> typeCableFile = "XLPE2C.xls"
//            "XLPE 3C" -> typeCableFile = "XLPE3C.xls"
//            "XLPE 4C" -> typeCableFile = "XLPE4C.xls"
        }

        lateinit var fineSizeCableInTable:String
        when(typeCableTextView.text){
            "IEC01" -> fineSizeCableInTable = "IEC01.xls"
            "NYY 1C" -> fineSizeCableInTable = "NYY1C.xls"
            "NYY 2C" -> fineSizeCableInTable = "NYY2C.xls"
            "NYY 3C" -> fineSizeCableInTable = "NYY3C.xls"
            "NYY 4C" -> fineSizeCableInTable = "NYY4C.xls"
            "IEC10 2C" -> fineSizeCableInTable = "IEC102C.xls"
            "IEC10 3C" -> fineSizeCableInTable = "IEC103C.xls"
            "XLPE 1C" -> fineSizeCableInTable = "XLPE1C.xls"
            "XLPE 2C" -> fineSizeCableInTable = "XLPE2C.xls"
            "XLPE 3C" -> fineSizeCableInTable = "XLPE3C.xls"
            "XLPE 4C" -> fineSizeCableInTable = "XLPE4C.xls"
        }


        try {
            val typeCable = applicationContext.assets.open(typeCableFile)
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(0)

            if(phaseTextView.text == "1 เฟส"){
                for(i in 2..20){
                    val findInOnePhase = sheet.getCell(1, i).contents
                    val circuitToInt = Integer.parseInt(circuitTextView.text.toString().replace("A",""))

                    if (circuitToInt <= findInOnePhase.toInt()){
                        val getCableSizeInTable = sheet.getCell(0, i).contents

                        val typeCable = applicationContext.assets.open(fineSizeCableInTable)
                        val wb = Workbook.getWorkbook(typeCable)
                        val sheet = wb.getSheet(0)

                        for (k in 1..19){
                            val checkCableSizeInTable = sheet.getCell(0, k).contents
                            if(checkCableSizeInTable == getCableSizeInTable){
                                for (g in 1..12){
                                    val checkCableSizeInTable2 = sheet.getCell(g, k).contents.toInt()
                                    if (2 <= checkCableSizeInTable2){ // 4 is PhaseSize
                                        val getCableSize2InTable = sheet.getCell(g, 0).contents
                                        textViewShow2.text = "${getCableSizeInTable} มม."
                                        textViewShow4.text = "${getCableSize2InTable} (สูงสุด ${checkCableSizeInTable2} เส้น)"
                                        break
                                    }
                                }
                            }
                        }
                        break
                    }
                }
            }else{
                //  Fine three Phase
                for(j in 2..20){
                    val findInthreePhase = sheet.getCell(2, j).contents
                    val circuitToInt = Integer.parseInt(circuitTextView.text.toString().replace("A",""))

                    if (circuitToInt <= findInthreePhase.toInt()){
                        val getCableSizeInTable = sheet.getCell(0, j).contents


                        val typeCable = applicationContext.assets.open(fineSizeCableInTable)
                        val wb = Workbook.getWorkbook(typeCable)
                        val sheet = wb.getSheet(0)

                        for (k in 1..19){
                            val checkCableSizeInTable = sheet.getCell(0, k).contents
                            if(checkCableSizeInTable == getCableSizeInTable){
                                for (g in 1..12){
                                    val checkCableSizeInTable2 = sheet.getCell(g, k).contents.toInt()
                                    if (4 <= checkCableSizeInTable2){ // 4 is PhaseSize
                                        val getCableSize2InTable = sheet.getCell(g, 0).contents
                                        textViewShow2.text = "${getCableSizeInTable} มม."
                                        textViewShow4.text = "${getCableSize2InTable} (สูงสุด ${checkCableSizeInTable2} เส้น)"
                                        break
                                    }
                                }
                            }
                        }
                        break
                    }
                }
            }


        }catch (e:IOException){}
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
        val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE, "IEC01")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT, "40A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE, "100")




         phaseTextView.text = "$dataOfPhase เฟส"
        installationTextView.text = dataOfInstallation!!.slice(0..6)
        typeCableTextView.text = dataOfTypeCable
        circuitTextView.text = dataOfCircuit
        editTextDistance.hint = dataOfDistance
    }


    override fun onRestart() {
        super.onRestart()
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
        sharedPref.apply()
    }

    fun backOnClick(view: View) {
        finish()
    }
    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
}



