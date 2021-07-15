package com.alw.temca.ui.AmountInRailCable

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.WireSizeAdapter
import com.alw.temca.Model.RailSizeModel
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_amount_in_rail_cable.*
import java.io.IOException

class AmountInRailCableActivity : AppCompatActivity() {
    companion object{
        private val TASK_NAME_REQUEST_CODE = 100
        private val TASK_LIST_PREF_KEY_INSTALLATION_IN_RAILS_CABLE = "task_list_installation_in_rails_cable"
        private val TASK_LIST_PREF_KEY_INSTALLATION_IN_RAILS_CABLE_DES = "task_list_installation_in_rails_cable_des"
        private val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_RAILS_CABLE = "task_list_type_cable_in_rails_cable"
        private val TASK_LIST_PREF_KEY_CIRCUIT_IN_RAILS_CABLE = "task_list_circuit_in_rails_cable"
        private val TASK_LIST_PREF_KEY_DISTANCE_IN_RAILS_CABLE = "task_list_distance_in_rails_cable"
        private val PREF_NAME = "task_rails_cable"
        private val railSizeList = ArrayList<RailSizeModel>()
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
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_RAILS_CABLE, data)
            }
            if (type == "installationDes"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_RAILS_CABLE_DES, data)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_RAILS_CABLE, data)
            }
            if (type == "circuit"){
                putString(TASK_LIST_PREF_KEY_CIRCUIT_IN_RAILS_CABLE, data)
            }
            if (type == "distance"){
                putString(TASK_LIST_PREF_KEY_DISTANCE_IN_RAILS_CABLE, data)
            }
            commit()
        }
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfInstallation = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_RAILS_CABLE, "กลุ่ม 7")
        val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_RAILS_CABLE, "NYY 1C")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT_IN_RAILS_CABLE, "400A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_IN_RAILS_CABLE, "10")

        installationTextView.text = dataOfInstallation!!.slice(0..6)
        typeCableTextView.text = dataOfTypeCable
        circuitTextView.text = dataOfCircuit
        editTextDistance.setText(dataOfDistance)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount_in_rail_cable)

        var intent: Intent = intent
        val dataCircuit = intent.getStringExtra("dataCircuit")
        if(dataCircuit != null){
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
        railSizeList.clear()

        if (editTextDistance.text.isEmpty()){
            editTextDistance.setText("0")
        }
        if (circuitTextView.text.isEmpty()){
            circuitTextView.text = "400A"
        }

        if(editTextDistance.length() > 0 ){
            if(editTextDistance.text.toString() == "0" ) editTextDistance.setText("0")
            else if(editTextDistance.text.toString() == "00") editTextDistance.setText("0")
            else if(editTextDistance.text.toString() == "000") editTextDistance.setText("0")
            else if(editTextDistance.text.toString() == "0000") editTextDistance.setText("0")
            else if(editTextDistance.text.toString().slice(0..0) == "0"){
                for(i in 0..3){
                    if(editTextDistance.text.toString().slice(0..0) != "0") break
                    else editTextDistance.setText(editTextDistance.text.toString().slice(1..editTextDistance.length()-1))
                }
            }
        }


        btnCal.visibility = View.GONE
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
                                            resultRefPressure = "${"%.2f V".format(pullResult)} (${"%.2f".format(PercentPressure)}%)"
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

            }
        }catch (e: IOException){ println("Error : $e") }

    }
    fun ReportOnClick(view: View) {}


    fun installationOnClick(view: View) {
        val intent = Intent(this, InstallationInRailsCableActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun typeCableOnClick(view: View) {
        val intent = Intent(this, TypeCableInRailsCableActivity::class.java)
            intent.putExtra("Activity", "Group7")

        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun CircuitOnClick(view: View) {

        val intent = Intent(this, CircuitInRailsCableActivity::class.java)
        intent.putExtra("RowAmountAmp", 77)
        startActivity(intent)
        finish()


    }
    fun DestanceOnClick(view: View) {
        editTextDistance.setText("")
        editTextDistance.hint = " "
        editTextDistance.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistance, InputMethodManager.SHOW_IMPLICIT)
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

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun installationOfTable(installation: String):String{
        return when(installation){
            "กลุ่ม 7" -> "WireGroup_Group7.xls"
            else -> ""
        }
    }

    private fun circuitCheckPhaseAndCableType(phase: String, cableType: String):Int{
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfInstallationDes = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_RAILS_CABLE_DES, null)

        return if(dataOfInstallationDes == "วางบนรางเคเบิ้ล ไม่มีฝาปิดแบบระบายอากาศ"){
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
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                val dataInstallation = data!!.getStringExtra("dataInstall")
                val dataInstallationDes = data.getStringExtra("dataInstallDes").toString()
                val dataTypeCable = data.getStringExtra("dataTypeCable")
                val dataCircuit = data.getStringExtra("dataCircuit")


                if (dataInstallation != null) {
                    val dataInstallationSlice = dataInstallation.slice(0..6)
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
        tableBeforeCalculateGroup7.visibility = View.GONE
        btnCal.visibility = View.VISIBLE


    }

}