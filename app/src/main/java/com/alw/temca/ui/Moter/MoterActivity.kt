package com.alw.temca.ui.Moter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alw.temca.Model.InstallationModelInTransformer
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.Transformer.InstallationTransformerActivity
import com.alw.temca.ui.WireSize.PhaseActivity
import com.alw.temca.ui.WireSize.TypeCableActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_moter.*
import kotlinx.android.synthetic.main.activity_moter.btnCalInPipeSize
import kotlinx.android.synthetic.main.activity_moter.phaseTextView
import kotlinx.android.synthetic.main.activity_moter.wayBackActivity1
import kotlinx.android.synthetic.main.activity_wire_size.*


class MoterActivity : AppCompatActivity() {
    private val TASK_NAME_REQUEST_CODE = 100
    private val PREF_NAME = "task_moter"
    private val TASK_LIST_PREF_KEY_SIZE_UNIT = "task_list_unit"
    private val TASK_LIST_PREF_KEY_PHASE_IN_MOTER = "task_list_phase_in_moter"
    private val TASK_LIST_PREF_KEY_INSTALLATION_IN_MOTER = "task_list_installation_in_moter"
    private val TASK_LIST_PREF_KEY_STARTPANTERN_IN_MOTER = "task_list_startpantern_in_moter"
    private val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_MOTER = "task_list_type_cable_in_moter"
    private val TASK_LIST_PREF_KEY_DISTANCE_IN_MOTER = "task_list_distance_in_moter"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moter)
        tableBeforeCalculateInMoter.visibility = View.GONE
        loadData()

        editTextDistanceInMoter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()){
                    editTextDistanceInMoter.hint = " "
                }else{
                    editTextDistanceInMoter.hint = ""
                }
                tableBeforeCalculateInMoter.visibility = View.GONE
                btnCalInPipeSize.visibility = View.VISIBLE
                wayBackActivity1.visibility = View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("distance",s.toString())
            }
        })

        editTextAmountMoterSize.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ก่อนเปลี่ยนคือ ?
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()){
                    editTextAmountMoterSize.hint = " "
                }else{
                    editTextAmountMoterSize.hint = ""
                }

                tableBeforeCalculateInMoter.visibility = View.GONE
                btnCalInPipeSize.visibility = View.VISIBLE
                wayBackActivity1.visibility = View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {
                //หลังจากพิมพ์ผลลัพคือ ?
                saveData("distance",s.toString())
            }
        })

        if(phaseTextView.text == "1 เฟส") iconImageViewToPage.visibility = View.GONE
        else iconImageViewToPage.visibility = View.VISIBLE

    }


    fun moterSizeOnClick(view: View) {
        editTextAmountMoterSize.setText("")
        editTextAmountMoterSize.hint = " "
        editTextAmountMoterSize.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextAmountMoterSize, InputMethodManager.SHOW_IMPLICIT)
    }
    fun phaseOnClick(view: View) {
        val intent = Intent(this, PhaseActivity::class.java)
        intent.putExtra("Activity","Moter")
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun GroupInstallationOnClick(view: View) {
        val intent = Intent(this, InstallationTransformerActivity::class.java)
        intent.putExtra("Activity","Moter")
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun sizeCableTypeOnClick(view: View) {
        val intent = Intent(this, TypeCableActivity::class.java)
        intent.putExtra("Activity","Moter")
        if(TextViewStartPantern.text == "STAR DELTA")  intent.putExtra("Type","DELTA")


        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }

    fun startPantternOnClick(view: View) {
        if(phaseTextView.text != "1 เฟส"){
            val intent = Intent(this, StartPatternActivity::class.java)
            startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
        }
    }
    fun setDistanceOnClick(view: View) {
        editTextDistanceInMoter.setText("")
        editTextDistanceInMoter.hint = " "
        editTextDistanceInMoter.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistanceInMoter, InputMethodManager.SHOW_IMPLICIT)
        wayBackActivity1.visibility = View.VISIBLE
    }

    fun unitOnClick(view: View) {
        val intent = Intent(this, UnitMoterActivity::class.java)
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }
    fun calculatorMoterOnClick(view: View) {
        btnCalInPipeSize.visibility = View.GONE
        tableBeforeCalculateInMoter.visibility = View.VISIBLE
        editTextDistanceInMoter.clearFocus()
        btnCalInPipeSize.apply {
            hideKeyboard()
        }

        if(editTextAmountMoterSize.text.isEmpty()) editTextAmountMoterSize.setText("10")
        if(editTextDistanceInMoter.text.isEmpty()) editTextDistanceInMoter.setText("20")


        wayBackActivity1.visibility = View.GONE
        btnCalInPipeSize.visibility = View.GONE
        tableBeforeCalculateInMoter.visibility = View.VISIBLE

        var stepInFor:Int = 0
        var maxRowsheet:Int
        val indexSheetInMoter:Int
        if(phaseTextView.text == "1 เฟส") {
            maxRowsheet = 53
            stepInFor = 4
            indexSheetInMoter = 0
        } else {
            if(TextViewStartPantern.text == "STAR DELTA") {

                maxRowsheet = 46
                stepInFor = 3
                indexSheetInMoter = 2
            }else {
                maxRowsheet = 93
                stepInFor = 4
                indexSheetInMoter = 1
            }
        }


        val typeCable = applicationContext.assets.open("moter.xls")
        val wb = Workbook.getWorkbook(typeCable)
        val sheet = wb.getSheet(indexSheetInMoter)
        val columnIntable:Int = when(textUnit.text){
            "kW" -> 0
            "HP" -> 1
            "A" -> 10
            else -> 0
        }

        val pressureDropIndexTable:Int =  when(TextViewCableTypeInMoter.text){
            "IEC 01" -> 0
            "NYY 1C" -> 0
            "NYY 2C-G" -> 1
            "XLPE 1C" -> 2
            else -> 0
        }

        var voteInMoter:Int

        for(i in 2..maxRowsheet step stepInFor){
            val editTextToDouble:Double = java.lang.Double.parseDouble(editTextAmountMoterSize.text.toString())
            val checkAmpInTable = sheet.getCell(columnIntable, i).contents.toDouble()

            if(editTextToDouble <= checkAmpInTable){
                for(j in i..i+3){
                    val checkCableType = sheet.getCell(3, j).contents // เทียบค่าชนิดสายในตาราง
                    val cableTypeIntable = sheet.getCell(4, j).contents // ขนาดสายในตาราง
                    val powerRatingIntable = sheet.getCell(2, i).contents // พิกัดกระแสไฟในตาราง
                    val calbeGroundIntable = sheet.getCell(7, j).contents // ขนาดสายดินในตาราง
                    val conduitInTable = sheet.getCell(5, j).contents // ขนาดท่อร้อยสายในตาราง
                    val breakerInTable = sheet.getCell(6, i).contents // ขนาดเบรกเกอร์ในตาราง
                    val cableSizeWithOutX = sheet.getCell(9, j).contents // ขนาดเบรกเกอร์ในตาราง

                    if(TextViewCableTypeInMoter.text == checkCableType){
                        println("TRUE")
                        textViewElectricCurrenResult.text = powerRatingIntable
                        textViewSizeCableResult.text = Html.fromHtml("${cableTypeIntable.replace("mm2","mm<sup><small><small>2</small></small></sup>")}")
                        if(TextViewCableTypeInMoter.text != "NYY 2C-G") textViewGroundSizeResult.text = Html.fromHtml("${calbeGroundIntable.replace("mm2","mm<sup><small><small>2</small></small></sup>")}")
                        else textViewGroundSizeResult.text = Html.fromHtml("- mm<sup><small><small>2</small></small></sup>")
//                        textViewConduitSizeResult.text = conduitInTable
                        textViewBreakerResult.text = breakerInTable

                        val temp:String = conduitInTable.replace(")"," inch )")
                        val s = SpannableString(temp.trim())
                        if (temp.indexOf('/') != -1) {
                            val len = temp.length
                            s.setSpan(SuperscriptSpan(), len -10, len -9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                            s.setSpan( applicationContext,len -10, len -9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวเศษ
                            s.setSpan(applicationContext, len - 9, len - 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัว /
                            s.setSpan(SubscriptSpan(), len - 8, len - 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
//                            s.setSpan(applicationContext, len - 7, len-6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // ตัวส่วน
                        }


                        if(phaseTextView.text == "1 เฟส") {
                            textViewCableSize.text = "แรงดัน(230V)"
                            voteInMoter = 230
                        }
                        else {
                            textViewCableSize.text = "แรงดัน(400V)"
                            voteInMoter = 400
                        }

                        textViewConduitSizeResult.text = s

                        for (h in 2..20) {
                            val pressureCable = applicationContext.assets.open("pressure_drop.xls") // pressure_drop_file
                            val wbPressure = Workbook.getWorkbook(pressureCable)
                            val sheetPressure = wbPressure.getSheet(pressureDropIndexTable)
                            val fineCableTypeInTable = sheetPressure.getCell(0, h).contents
                            val amountDeistance = Integer.parseInt(editTextDistanceInMoter.text.toString())
                            if (cableSizeWithOutX == fineCableTypeInTable) { // แก้ cableSize ตัดคำออก
                                val getreslutInTable = sheetPressure.getCell(1, h).contents.toDouble()
                                val pullResult = getreslutInTable * Integer.parseInt(breakerInTable.toString().replace(" A", "")) * amountDeistance / 1000 // result
                                val PercentPressure  = 100 * pullResult / voteInMoter // result
                                textViewPressureResult.text = "${"%.2f V".format(pullResult)} (${"%.2f".format(PercentPressure)}%)"
                                break
                            }
                        }



                        break
                    }
                }
                break
            }else{
                textViewCableSize.text = "แรงดัน(-V)"
                textViewElectricCurrenResult.text = "-"
                textViewSizeCableResult.text = "-"
                textViewGroundSizeResult.text = "-"
                textViewConduitSizeResult.text = "-"
                textViewBreakerResult.text = "-"
                textViewPressureResult.text = "-"
            }

        }



    }

    fun moterReportOnClick(view: View) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){

                val dataUnitMoter = data!!.getStringExtra("dataUnitMoter")
                val dataPhase = data.getIntExtra("dataPhase",0)
                val dataInstallation = data.getParcelableExtra<InstallationModelInTransformer>("dataInstall")
                val dataStartPantern = data!!.getStringExtra("dataStartPantern")
                val dataTypeCable = data!!.getStringExtra("dataTypeCable")

                if (dataUnitMoter != null){
                    textUnit.text = dataUnitMoter
                    saveData("unitMoter", dataUnitMoter)
                }

                if (dataPhase != 0) {
                    phaseTextView.text = "$dataPhase เฟส"
                    saveData("phase", "$dataPhase เฟส")
                }

//                if (dataInstallation != null) {
////                    val dataInstallationSlice = dataInstallation
//                    TextViewGroupInstallations.text = dataInstallation.des
//                    saveData("group", dataInstallation.title)
//                    saveData("installation", dataInstallation.des)
//                }

                if (dataStartPantern != null){
                    TextViewStartPantern.text = dataStartPantern
                    saveData("dataStartPantern", dataStartPantern)
                }

                if (dataTypeCable != null){
                    TextViewCableTypeInMoter.text = dataTypeCable
                    saveData("dataTypeCable", dataTypeCable)
                }

            }
            wayBackActivity1.visibility = View.VISIBLE
            btnCalInPipeSize.visibility = View.VISIBLE
            tableBeforeCalculateInMoter.visibility = View.GONE


            if(phaseTextView.text == "1 เฟส") {
                iconImageViewToPage.visibility = View.GONE
                TextViewStartPantern.text = "DOL"
                saveData("dataStartPantern", "DOL")
            }
            else {
                if(TextViewStartPantern.text == "STAR DELTA") {
                    println("dasdasd ${TextViewCableTypeInMoter.text}")
                    if(TextViewCableTypeInMoter.text == "NYY 2C-G"){
                        TextViewCableTypeInMoter.text = "NYY 1C"
                        saveData("dataTypeCable", "NYY 1C")
                    }
                }
                iconImageViewToPage.visibility = View.VISIBLE
            }



        }
    }

    fun saveData(type: String, value: String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            if (type == "unitMoter"){
                putString(TASK_LIST_PREF_KEY_SIZE_UNIT, data)
            }
            if (type == "phase"){
                putString(TASK_LIST_PREF_KEY_PHASE_IN_MOTER, data)
            }
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_MOTER, data)
            }
            if (type == "dataStartPantern"){
                putString(TASK_LIST_PREF_KEY_STARTPANTERN_IN_MOTER, data)
            }
            if (type == "dataTypeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_MOTER, data)
            }
        }
    }
    fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfUnitMoter = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE_UNIT,"HP")
        val dataOfPhase = sharedPref.getString(TASK_LIST_PREF_KEY_PHASE_IN_MOTER,"1 เฟส")
        val dataOfInstall = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_MOTER,"เดินเคเบิลแบบระบายอากาศ")
        val dataOfStartPantern = sharedPref.getString(TASK_LIST_PREF_KEY_STARTPANTERN_IN_MOTER,"DOL")
        val dataOfCableSize = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_MOTER,"NYY 1C")
        val dataOfDistanceInTransformer = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_IN_MOTER,"20")

        textUnit.text = dataOfUnitMoter
        phaseTextView.text = dataOfPhase
//        TextViewGroupInstallations.text = dataOfInstall
        TextViewStartPantern.text = dataOfStartPantern
        TextViewCableTypeInMoter.text = dataOfCableSize
        editTextDistanceInMoter.setText(dataOfDistanceInTransformer)
    }

    fun backOnClick(view: View) {finish()}
    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}