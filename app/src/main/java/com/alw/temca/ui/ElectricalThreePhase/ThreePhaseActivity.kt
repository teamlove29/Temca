package com.alw.temca.ui.ElectricalThreePhase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.alw.temca.Model.RailSizeModel
import com.alw.temca.R
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
import com.alw.temca.ui.SponsorActivity
import kotlinx.android.synthetic.main.activity_three_phase.*
import kotlinx.android.synthetic.main.activity_three_phase.btnCal
import kotlinx.android.synthetic.main.activity_three_phase.circuitTextView
import kotlinx.android.synthetic.main.activity_three_phase.editTextDistance
import kotlinx.android.synthetic.main.activity_three_phase.installationTextView
import kotlinx.android.synthetic.main.activity_three_phase.phaseTextView
import kotlinx.android.synthetic.main.activity_three_phase.tableBeforeCalculate
import kotlinx.android.synthetic.main.activity_three_phase.tableBeforeCalculateGroup7
import kotlinx.android.synthetic.main.activity_three_phase.typeCableTextView
import kotlinx.android.synthetic.main.activity_three_phase.wayBackActivity1

class ThreePhaseActivity : AppCompatActivity() {

    companion object{
        private const val TASK_NAME_REQUEST_CODE = 100
        private const val TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE= "task_list_installation_in_three_phase"
        private const val TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE_DES = "task_list_installation_in_three_phase_des"
        private const val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_THREE_PHASE = "task_list_type_cable_in_three_phase"
        private const val TASK_LIST_PREF_KEY_CIRCUIT_IN_THREE_PHASE= "task_list_circuit_in_three_phase"
        private const val TASK_LIST_PREF_KEY_DISTANCE_IN_THREE_PHASE = "task_list_distance_in_three_phase"
        private const val PREF_NAME = "task_three_phase"
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
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE, data)
            }
            if (type == "installationDes"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE_DES, data)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_THREE_PHASE, data)
            }
            if (type == "circuit"){
                putString(TASK_LIST_PREF_KEY_CIRCUIT_IN_THREE_PHASE, data)
            }
            if (type == "distance"){
                putString(TASK_LIST_PREF_KEY_DISTANCE_IN_THREE_PHASE, data)
            }
            commit()
        }
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfInstallation = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE, "กลุ่ม 2")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT_IN_THREE_PHASE, "40A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_IN_THREE_PHASE, "10")

        val dataOfTypeCable = if(dataOfInstallation!!.slice(0..6) == "กลุ่ม 5" ){
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_THREE_PHASE, "NYY")
        }else{
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_THREE_PHASE, "IEC01")
        }
        installationTextView.text = dataOfInstallation!!.slice(0..6)
        typeCableTextView.text = dataOfTypeCable
        circuitTextView.text = dataOfCircuit
        editTextDistance.setText(dataOfDistance)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_phase)

        tableBeforeCalculate.visibility = View.GONE
        tableBeforeCalculateGroup7.visibility = View.GONE

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
                    editTextDistance.hint = " "
                } else {
                    editTextDistance.hint = ""
                }
//                sponsorImageView.visibility = View.VISIBLE
                tableBeforeCalculate.visibility = View.GONE
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

    fun calculatorOnClick(view: View) {}
    fun ReportOnClick(view: View) {}

    fun installationOnClick(view: View) {}
    fun typeCableOnClick(view: View) {}
    fun CircuitOnClick(view: View) {}
    fun DestanceOnClick(view: View) {}

    fun backOnClick(view: View) {
        finish()
    }
    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPref =
            getSharedPreferences(OnePhaseActivity.PREF_NAME, Context.MODE_PRIVATE).edit()
                .clear()
        sharedPref.apply()
        finish()
    }
    private fun installationOfTable(installation: String):String{
        return when(installation){
            "กลุ่ม 2" -> "WireGroup_Group2.xls"
            "กลุ่ม 5" -> "WireGroup_Group5.xls"
            "กลุ่ม 7" -> "WireGroup_Group7.xls"
            else -> ""
        }
    }
    private fun circuitCheckPhaseAndCableType(phase: String, cableType: String):Int{
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfInstallationDes = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_IN_THREE_PHASE_DES, null)

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
            if(installationTextView.text == "กลุ่ม 7"){
                textViewReferenceVoltage.text = "(แรงดันอ้างอิง 400V)"

                if(dataOfInstallationDes == "วางบนรางเคเบิ้ล ไม่มีฝาปิดแบบระบายอากาศ"){
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
            }else{
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                val dataPhase = data!!.getIntExtra("dataPhase", 0)
                val dataInstallation = data.getStringExtra("dataInstall")
                val dataInstallationDes = data.getStringExtra("dataInstallDes").toString()
                val dataTypeCable = data.getStringExtra("dataTypeCable")
//                val dataCircuit = data!!.getStringExtra("dataCircuit")

                if (dataPhase != 0){
                    phaseTextView.text = "$dataPhase เฟส"
                    saveData("phase", dataPhase.toString())
                }
                if (dataInstallation != null) {
                    val dataInstallationSlice = dataInstallation.slice(0..6)
                    if(dataInstallationSlice == "กลุ่ม 5"){
                        if (typeCableTextView.text != "NYY" && typeCableTextView.text != "XLPE" && typeCableTextView.text != "VCT" && typeCableTextView.text != "NYY-G" && typeCableTextView.text != "VCT-G"){
                            typeCableTextView.text = "NYY"
                        }
                        circuitTextView.text = "40A"
                    }else if(dataInstallationSlice == "กลุ่ม 7"){
                        if(typeCableTextView.text != "NYY 1C" && typeCableTextView.text != "NYY 4C" && typeCableTextView.text != "XLPE 1C" && typeCableTextView.text != "XLPE 4C"){
                            typeCableTextView.text = "NYY 1C"
                        }
                        if(phaseTextView.text != "3 เฟส"){
                            phaseTextView.text = "3 เฟส"
                        }
                        circuitTextView.text = "400A"
                    }else circuitTextView.text = "40A"

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
//        sponsorImageView.visibility = View.VISIBLE
        tableBeforeCalculate.visibility = View.GONE
        tableBeforeCalculateGroup7.visibility = View.GONE
        btnCal.visibility = View.VISIBLE
//        circuitTextView.text = "40A"
    }



}