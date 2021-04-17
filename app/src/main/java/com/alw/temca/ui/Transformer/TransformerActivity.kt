package com.alw.temca.ui.Transformer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.TransformerSizeAdapterResult
import com.alw.temca.Function.FindCableSizeInTable
import com.alw.temca.Model.InstallationModelInTransformer
import com.alw.temca.Model.TransformerSizeModalResult
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.WireSize.TypeCableActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_moter.*
import kotlinx.android.synthetic.main.activity_transformer.*
import kotlinx.android.synthetic.main.activity_transformer.btnCalInPipeSize
import kotlinx.android.synthetic.main.activity_transformer.textViewElectricCurrenResult
import kotlinx.android.synthetic.main.activity_transformer.wayBackActivity1
import kotlinx.android.synthetic.main.activity_wire_size.*

class TransformerActivity : AppCompatActivity() {
    val TASK_NAME_REQUEST_CODE = 100
    val PREF_NAME = "task_transformer"
    val TASK_LIST_PREF_KEY_SIZE_TRANSFORMER = "task_list_size_transformer"
    val TASK_LIST_PREF_KEY_INSTALLATION_GROUP = "task_list_installation_group"
    val TASK_LIST_PREF_KEY_INSTALLATION = "task_list_installation"
    val TASK_LIST_PREF_KEY_TYPE_CABLE = "task_list_type_cable"
    val TASK_LIST_PREF_KEY_DISTANCE_TRANSFORMER = "task_list_distance"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transformer)
        tableBeforeCalculateInTransformer.visibility =  View.GONE
        loadData()

        editTextDistanceInTransformer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()){
                    editTextDistanceInTransformer.hint = "20"
                }else{
                    editTextDistanceInTransformer.hint = ""
                }
                tableBeforeCalculateInTransformer.visibility = View.GONE
                btnCalInPipeSize.visibility = View.VISIBLE
                wayBackActivity1.visibility = View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("distance",s.toString())
            }

        })


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

    fun setDistanceOnClick(view: View) {
        editTextDistanceInTransformer.setText("")
        editTextDistanceInTransformer.hint = "20"
        editTextDistanceInTransformer.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistanceInTransformer, InputMethodManager.SHOW_IMPLICIT)
        wayBackActivity1.visibility = View.VISIBLE
    }
    fun TransformerReportOnClick(view: View) {}

    fun calculatorTransformerOnClick(view: View) {

        if(editTextDistanceInTransformer.text.isEmpty() ){
//            val editTextToString = Integer.parseInt(editTextDistanceInTransformer.text.toString())
//            if(editTextToString <= 0){
//                editTextDistanceInTransformer.setText("20")
//            }
            editTextDistanceInTransformer.setText("20")
        }

        editTextDistanceInTransformer.clearFocus()
        btnCalInPipeSize.apply {
            hideKeyboard()
        }
        wayBackActivity1.visibility = View.GONE
        btnCalInPipeSize.visibility = View.GONE
        tableBeforeCalculateInTransformer.visibility = View.VISIBLE

        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfGroupInstall = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_GROUP,"กลุ่ม 7")


        val transformerGroupInTable = when(dataOfGroupInstall){
            "กลุ่ม 7" -> "transformer_group7.xls"
            else -> return
        }
        val transformerCableTypeInTable = when(TextViewCableType.text){
            "NYY" -> {
                if(TextViewGroupInstallation.text == "เดินเคเบิ้ลแบบระบายอากาศ") 0
                else 1
            }
            else -> return
        }

        val transformerSizeExcel = applicationContext.assets.open(transformerGroupInTable)
        val wb = Workbook.getWorkbook(transformerSizeExcel)
        val sheet = wb.getSheet(transformerCableTypeInTable)
        val dataListOfTable = ArrayList<TransformerSizeModalResult>()

        for(i in 1..27){ // row check transformer
            val findSizeTransformer = sheet.getCell(0, i).contents.toInt()
            val TransformerSizeOfTextView = Integer.parseInt(TextViewTransformerSize.text.toString().replace("kVA","").trim())
            if (TransformerSizeOfTextView <= findSizeTransformer){
                val getDataElectricCurrentInTable = sheet.getCell(1, i).contents // resultElectricCurrent
                textViewElectricCurrenResult.text = getDataElectricCurrentInTable
                for (j in i..i+2){ // row Data
                val getDataSizeCableInTable = sheet.getCell(2, j).contents // resultSizeCable
                val getDataSizeConduitInTable = sheet.getCell(3, j).contents // resultSizeConduit
                    if(getDataSizeCableInTable != "0"){
                        dataListOfTable.add(TransformerSizeModalResult(
                                getDataElectricCurrentInTable,
                                getDataSizeCableInTable,
                                getDataSizeConduitInTable,
                                "",
                                ""))
                    }
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
                val dataInstallation = data!!.getParcelableExtra<InstallationModelInTransformer>("dataInstall")
//                val dataTypeCable = data!!.getStringExtra("dataTypeCable")
//                val dataCircuit = data!!.getStringExtra("dataCircuit")
                if (dataTransformerSize != null){
                    TextViewTransformerSize.text = dataTransformerSize
                    saveData("transformerSize", dataTransformerSize)
                }
                if (dataInstallation != null) {
//                    val dataInstallationSlice = dataInstallation
                    TextViewGroupInstallation.text = dataInstallation.des
                    saveData("group", dataInstallation.title)
                    saveData("installation", dataInstallation.des)
                }
////                if (dataTypeCable != null) {
////                    typeCableTextView.text = dataTypeCable
////                    saveData("typeCable", dataTypeCable)
////                }
//                }
            }
            wayBackActivity1.visibility = View.VISIBLE
        }

        tableBeforeCalculateInTransformer.visibility = View.GONE
        btnCalInPipeSize.visibility = View.VISIBLE

    }


    fun saveData(type: String, value: String){
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

        }

    }
    fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfSizeTransformer = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE_TRANSFORMER,"500 kVA")
        val dataOfGroupInstall = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION,"เดินเคเบิลแบบระบายอากาศ")
        val dataOfCableType = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE,"NYY")
        val dataOfDistanceInTransformer = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_TRANSFORMER,"20")

        TextViewTransformerSize.text = dataOfSizeTransformer
        TextViewGroupInstallation.text = dataOfGroupInstall
        TextViewCableType.text = dataOfCableType
        editTextDistanceInTransformer.setText(dataOfDistanceInTransformer)
    }

    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
    fun backOnClick(view: View) {finish()}

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}



