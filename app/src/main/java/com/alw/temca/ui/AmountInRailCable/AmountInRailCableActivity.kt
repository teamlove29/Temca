package com.alw.temca.ui.AmountInRailCable

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.WireSizeAdapter
import com.alw.temca.Function.FindDetailInstallation
import com.alw.temca.Model.AmountInRailsCable.ReportResultCurrentRatting
import com.alw.temca.Model.RailSizeModel
import com.alw.temca.R
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
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
        lateinit var groupInstall:String
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
        val dataOfGroupInstall = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_RAILS_CABLE_DES,"รางเคเบิลแบบระบายอากาศ")
        val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_RAILS_CABLE, "NYY 1/C")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT_IN_RAILS_CABLE, "400A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_IN_RAILS_CABLE, "10")

//        installationTextView.text = dataOfInstallation!!.slice(0..6)
        groupInstall = dataOfInstallation.toString()
        installationTextView.text = "$dataOfGroupInstall"
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
            val circuitCheckGroup:String = installationOfTable(groupInstall)
            val circuitCheckCableType:Int = circuitCheckPhaseAndCableType(phaseTextView.text.toString(), typeCableTextView.text.toString())
            val typeCable = applicationContext.assets.open(circuitCheckGroup)
            val wb = Workbook.getWorkbook(typeCable)
            val sheet = wb.getSheet(circuitCheckCableType)

            // if group 7
            if(groupInstall == "กลุ่ม 7"){
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
                                val divisor = sheet.getCell(7, j).contents.toInt()



                                var pressureDropIndexTable:Int = when(typeCableTextView.text){
                                    "NYY 1/C" -> 0
                                    "NYY 4/C" -> 1
                                    "XLPE 1/C" -> 2
                                    "XLPE 4/C" -> 3
                                    else -> 0
                                }


                                for (h in 2..20) {

                                    val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
                                    val wbPressure = Workbook.getWorkbook(pressureCable)
                                    val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
                                    val fineCableTypeInTable = sheetPressure.getCell(0, h).contents
                                    val amountDeistance = Integer.parseInt(editTextDistance.text.toString())

                                    if (getSizeCableIntable == fineCableTypeInTable) { // แก้ cableSize ตัดคำออก
                                        val getreslutInTable = sheetPressure.getCell(3, h).contents.toDouble()
                                        val pullResult = getreslutInTable * Integer.parseInt(circuitTextView.text.toString().replace("A", "")) * amountDeistance / 1000 / divisor.toDouble() // result
                                        val PercentPressure  = 100 * pullResult / 400 / divisor.toDouble() // result
                                        var resultRefPressure:String

                                        var pullResulttoString = "${"%.2f V".format(pullResult)}"
                                        var percentPressuretoString = "${"%.2f".format(PercentPressure)}"

                                        if(pullResult >= 1000){
                                            pullResulttoString = "${pullResulttoString.slice(0..0)},${pullResulttoString.slice(1 until pullResulttoString.length)}"
                                            if(PercentPressure >= 1000){
                                                percentPressuretoString = "( ${percentPressuretoString.slice(0..0)},${percentPressuretoString.slice(1 until percentPressuretoString.length)}% )"
                                            }else{
                                                percentPressuretoString = "( ${percentPressuretoString}% )"
                                            }
                                            resultRefPressure = "$pullResulttoString $percentPressuretoString"
                                        }else{
                                            resultRefPressure = "${"%.2f V".format(pullResult)} ( ${"%.2f".format(PercentPressure)}% )"
                                        }

                                        railSizeList.add(RailSizeModel(
                                            getCableSizeInTable
                                            , getGroudSizeInTable
                                            , getRailSizeInTable
                                            , "400V"
                                            , resultRefPressure,
                                                divisor.toString()))

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
    fun ReportOnClick(view: View) {
        val dataToReport = ArrayList<ReportResultCurrentRatting>()
        val intent = Intent(this, ReportInCurrentRattingActivity::class.java)
//        val bundle = Bundle()

//        val textInstallation = FindDetailInstallation(installationTextView.text.toString())
        val textInstallation = if(installationTextView.text == "รางเคเบิลแบบบันได"){
            "กลุ่ม 7 วางบนรางเคเบิลไม่มีฝาปิด แบบบันได "
        }else{
            "กลุ่ม 7 วางบนรางเคเบิลไม่มีฝาปิด แบบระบายอากาศ"
        }


        val groundSize1 = "${railSizeList[0].groundSize} mm2"
        val groundSize2 = "${railSizeList[1].groundSize} mm2"

//        val groundSize1 = if(railSizeList[0].divisor == "1"){
//            "${railSizeList[0].groundSize} mm2"
//        }else{
//            "${railSizeList[0].divisor} ( ${railSizeList[0].groundSize} mm2 )"
//        }

//        val groundSize2 = if(railSizeList[1].divisor == "1"){
//            "${railSizeList[1].groundSize} mm2"
//        }else{
//            "${railSizeList[1].divisor} ( ${railSizeList[1].groundSize} mm2 )"
//        }

        dataToReport.add(ReportResultCurrentRatting(
                phaseTextView.text.toString(), // phase
                textInstallation,  // groupinstallation
                typeCableTextView.text.toString(), // typcable
                circuitTextView.text.toString(), // CB
                editTextDistance.text.toString(), // amountDis
                railSizeList[0].wireSize, // text2 is cablesize
                groundSize1, // wiresizegroud
                "${railSizeList[0].railSize} mm.", // text4 is conduitsize
                railSizeList[0].resultPressure) // result presure
        )
        dataToReport.add(ReportResultCurrentRatting(
                phaseTextView.text.toString(), // phase
                textInstallation,  // groupinstallation
                typeCableTextView.text.toString(), // typcable
                circuitTextView.text.toString(), // CB
                editTextDistance.text.toString(), // amountDis
                railSizeList[1].wireSize, // text2 is cablesize
                groundSize2, // wiresizegroud
                "${railSizeList[1].railSize} mm.", // text4 is conduitsize
                railSizeList[1].resultPressure) // result presure
        )
        intent.putParcelableArrayListExtra("dataResult",dataToReport)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
        finish()
    }


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

        return if(installationTextView.text == "รางเคเบิลแบบระบายอากาศ"){
                    when(cableType){
                        "NYY 1/C" -> 0
                        "NYY 4/C" -> 2
                        "XLPE 1/C" -> 4
                        "XLPE 4/C" -> 6
                        else -> 0
                    }
                }else{
                    when(cableType){
                        "NYY 1/C" -> 1
                        "NYY 4/C" -> 3
                        "XLPE 1/C" -> 5
                        "XLPE 4/C" -> 7
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
                    installationTextView.text = "$dataInstallationDes"
                    saveData("installation", dataInstallationSlice)
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