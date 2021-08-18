package com.alw.temca.ui.Transformer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.TransformerSizeAdapterResult
import com.alw.temca.Model.DataToTransformerReportModel
import com.alw.temca.Model.InstallationModelInTransformer
import com.alw.temca.Model.TransformerSizeModalResult
import com.alw.temca.R
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.WireSize.TypeCableActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_transformer.*
import kotlinx.android.synthetic.main.activity_transformer.btnCalInPipeSize
import kotlinx.android.synthetic.main.activity_transformer.textViewElectricCurrenResult
import kotlinx.android.synthetic.main.activity_transformer.wayBackActivity1

class TransformerActivity : AppCompatActivity() {
    private val dataListOfTable = ArrayList<TransformerSizeModalResult>()
    private val TASK_NAME_REQUEST_CODE = 100
    private val PREF_NAME = "task_transformer"
    private val TASK_LIST_PREF_KEY_PRESSURE = "task_list_pressure_volts"
    private val TASK_LIST_PREF_KEY_SIZE_TRANSFORMER = "task_list_size_transformer"
    private val TASK_LIST_PREF_KEY_INSTALLATION_GROUP = "task_list_installation_group_transformer"
    private val TASK_LIST_PREF_KEY_INSTALLATION = "task_list_installation_transformer"
    private val TASK_LIST_PREF_KEY_TYPE_CABLE = "task_list_type_cable_transformer"
    private val TASK_LIST_PREF_KEY_DISTANCE_TRANSFORMER = "task_list_distance_transformer"
    private var pressureDropIndexTable:Int = 0
    private var checkPressureVolts:Int = 400
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transformer)
        tableBeforeCalculateInTransformer.visibility =  View.GONE
        loadData()
        
//        editTextDistanceInTransformer.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                //ก่อนเปลี่ยนคือ ?
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s!!.isEmpty()){
//                    editTextDistanceInTransformer.hint = "20"
//                }else{
//                    editTextDistanceInTransformer.hint = ""
//                }
//                tableBeforeCalculateInTransformer.visibility = View.GONE
//                btnCalInPipeSize.visibility = View.VISIBLE
//                wayBackActivity1.visibility = View.VISIBLE
//            }
//            override fun afterTextChanged(s: Editable?) {
//                //หลังจากพิมพ์ผลลัพคือ ?
//                saveData("distance",s.toString())
//            }
//        })
    }
    fun choosePressureOnClick(view: View) {
        val intent = Intent(this, PressureVoltsActivity::class.java)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun transformerSizeOnClick(view: View) {
        val intent = Intent(this,TransformerSizeActivity::class.java)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun GroupInstallationOnClick(view: View) {
        val intent = Intent(this,InstallationTransformerActivity::class.java)
//        intent.putExtra("Activity","Transformer")
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun sizeCableTypeOnClick(view: View) {
        val intent = Intent(this,TypeCableActivity::class.java)
        intent.putExtra("Activity","Transformer")
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }

//    fun setDistanceOnClick(view: View) {
//        editTextDistanceInTransformer.setText("")
//        editTextDistanceInTransformer.hint = " "
//        editTextDistanceInTransformer.requestFocus()
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(editTextDistanceInTransformer, InputMethodManager.SHOW_IMPLICIT)
//        wayBackActivity1.visibility = View.VISIBLE
//    }

    fun TransformerReportOnClick(view: View) {
    val dataOfResultTransformer = ArrayList<DataToTransformerReportModel>()
    val intent = Intent(this,TransformerReportActivity::class.java)
    val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val dataOfGroup = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_GROUP,"กลุ่ม 7")

    for (i in 0..dataListOfTable.size - 1){
        dataOfResultTransformer.add(DataToTransformerReportModel(
            TextViewPressure.text.toString(),
            "$dataOfGroup ${TextViewGroupInstallation.text}",
            TextViewCableType.text.toString(),
            TextViewTransformerSize.text.toString(),
            dataListOfTable[i].electricCurrent,
            dataListOfTable[i].cableSize,
            dataListOfTable[i].conduitSize,
        ))
    }

    intent.putParcelableArrayListExtra("DataFromTransformerActivity",dataOfResultTransformer)
    startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    finish()
    }

    fun calculatorTransformerOnClick(view: View) {
        dataListOfTable.clear()
//        if(editTextDistanceInTransformer.text.isEmpty()) editTextDistanceInTransformer.setText("20")

//        editTextDistanceInTransformer.clearFocus()
        btnCalInPipeSize.apply { hideKeyboard() }
        wayBackActivity1.visibility = View.GONE
        btnCalInPipeSize.visibility = View.GONE
        tableBeforeCalculateInTransformer.visibility = View.VISIBLE

        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfGroupInstall = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_GROUP,"กลุ่ม 7")
        val transformerGroupInTable:String
          when(dataOfGroupInstall){
            "กลุ่ม 7" -> {
                if (TextViewPressure.text == "230/400 V")  {
                    transformerGroupInTable ="transformer_group7_400.xls"
                    checkPressureVolts = 400
                }
                else {
                    transformerGroupInTable ="transformer_group7_416.xls"
                    checkPressureVolts = 416
                }
            }
            else -> return
        }
        val transformerCableTypeInTable = when(TextViewCableType.text){
            "NYY 1/C" -> {
                pressureDropIndexTable = 0
                if(TextViewGroupInstallation.text == "รางเคเบิ้ลแบบระบายอากาศ") 0
                else 1
            }
            "XLPE 1/C" -> {
                pressureDropIndexTable = 2
                if(TextViewGroupInstallation.text == "รางเคเบิ้ลแบบระบายอากาศ") 2
                else 3
            }
            else -> return
        }

        val transformerSizeExcel = applicationContext.assets.open(transformerGroupInTable)
        val wb = Workbook.getWorkbook(transformerSizeExcel)
        val sheet = wb.getSheet(transformerCableTypeInTable)


        for(i in 3..20 step 2){ // row check transformer

            println(TextViewGroupInstallation.text)
            val findSizeTransformer = sheet.getCell(0, i).contents.toInt()
            val TransformerSizeOfTextView = Integer.parseInt(TextViewTransformerSize.text.toString().replace("kVA","").trim())
            if (TransformerSizeOfTextView <= findSizeTransformer){
                val getDataElectricCurrentInTable = sheet.getCell(1, i).contents // resultElectricCurrent
                textViewElectricCurrenResult.text = getDataElectricCurrentInTable
                for (j in i..i+1){ // row Data

                val getDataSizeCableInTable = sheet.getCell(2, j).contents // resultSizeCable
                val getDataSizeConduitInTable = sheet.getCell(3, j).contents // resultSizeConduit
                val getCableTypeInTable = sheet.getCell(5, j).contents // get TableType
                val getAmpInTable = sheet.getCell(6, i).contents.toInt() // get branker circuit

//                    for (h in 2..20) {
//                        val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
//                        val wbPressure = Workbook.getWorkbook(pressureCable)
//                        val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
//                        val fineCableTypeInTable = sheetPressure.getCell(0, h).contents
//                        val amountDeistance = Integer.parseInt(editTextDistanceInTransformer.text.toString())
//
//                        if (getCableTypeInTable == fineCableTypeInTable) { //
//                            val getreslutInTable = sheetPressure.getCell(2, h).contents.toDouble() // 1 เฟส
//                            val pullResult = getreslutInTable * getAmpInTable * amountDeistance / 1000 // result
//                            val percentPressure  = 100 * pullResult / checkPressureVolts // result
//                            println("checkPressureVolts $checkPressureVolts")
//                           val sumPullAndPercent =  "${"%.2f V".format(pullResult)} (${"%.2f".format(percentPressure)}%)"
//                        }
//                    }

                    dataListOfTable.add(TransformerSizeModalResult(
                            getDataElectricCurrentInTable,
                            getDataSizeCableInTable,
                            getDataSizeConduitInTable,
                            "",
                            ""))
                }
                recyclerViewResultTransformer.adapter = TransformerSizeAdapterResult(dataListOfTable)
                recyclerViewResultTransformer.layoutManager = LinearLayoutManager(this)
                break
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                val dataTransformerSize = data!!.getStringExtra("dataTransformerSize")
//                val dataInstallation = data.getParcelableExtra<InstallationModelInTransformer>("dataInstall")
                val dataInstallationGroup = data.getStringExtra("dataInstall")
                val dataInstallationDes = data.getStringExtra("dataInstallDes")
                val dataTypeCable = data.getStringExtra("dataTypeCable")
                val dataOfPressureVolts = data.getStringExtra("dataOfPressureVolts")

                if (dataTransformerSize != null){
                    TextViewTransformerSize.text = dataTransformerSize
                    saveData("transformerSize", dataTransformerSize)
                }
                if (dataInstallationGroup != null && dataInstallationDes != null) {
                    TextViewGroupInstallation.text = dataInstallationDes
                    saveData("group", dataInstallationGroup.toString())
                    saveData("installation", dataInstallationDes.toString())
                }
                if (dataTypeCable != null) {
                    TextViewCableType.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)
                }
                if (dataOfPressureVolts != null) {
                    TextViewPressure.text = dataOfPressureVolts
                    saveData("PressureVolts",dataOfPressureVolts)
                }
            }
            wayBackActivity1.visibility = View.VISIBLE
        }
        tableBeforeCalculateInTransformer.visibility = View.GONE
        btnCalInPipeSize.visibility = View.VISIBLE
    }

    private  fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            if (type == "transformerSize"){
                putString(TASK_LIST_PREF_KEY_SIZE_TRANSFORMER, data)
            }
            if (type == "group"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_GROUP, data)
            }
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION, data)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE, data)
            }
            if (type == "PressureVolts"){
                putString(TASK_LIST_PREF_KEY_PRESSURE, data)
            }
        }
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfPressureVolt = sharedPref.getString(TASK_LIST_PREF_KEY_PRESSURE,"230/400 V")
        val dataOfSizeTransformer = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE_TRANSFORMER,"500 kVA")
        val dataOfGroupInstall = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION,"รางเคเบิ้ลแบบระบายอากาศ")
        val dataOfCableType = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE,"NYY 1/C")
//        val dataOfDistanceInTransformer = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_TRANSFORMER,"20")

        TextViewPressure.text = dataOfPressureVolt
        TextViewTransformerSize.text = dataOfSizeTransformer
        TextViewGroupInstallation.text = dataOfGroupInstall
        TextViewCableType.text = dataOfCableType
//        editTextDistanceInTransformer.setText(dataOfDistanceInTransformer)
    }

    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
    fun backOnClick(view: View) {finish()}

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}



