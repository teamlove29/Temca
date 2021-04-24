package com.alw.temca.ui.Moter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alw.temca.Model.InstallationModelInTransformer
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.Transformer.InstallationTransformerActivity
import com.alw.temca.ui.WireSize.PhaseActivity
import com.alw.temca.ui.WireSize.TypeCableActivity
import kotlinx.android.synthetic.main.activity_moter.*
import kotlinx.android.synthetic.main.activity_moter.btnCalInPipeSize
import kotlinx.android.synthetic.main.activity_moter.phaseTextView
import kotlinx.android.synthetic.main.activity_moter.wayBackActivity1


class MoterActivity : AppCompatActivity() {
    val TASK_NAME_REQUEST_CODE = 100
    val PREF_NAME = "task_moter"
    val TASK_LIST_PREF_KEY_SIZE_UNIT = "task_list_unit"
    val TASK_LIST_PREF_KEY_PHASE_IN_MOTER = "task_list_phase_in_moter"
    val TASK_LIST_PREF_KEY_INSTALLATION_IN_MOTER = "task_list_installation_in_moter"
    val TASK_LIST_PREF_KEY_TYPE_CABLE_IN_MOTER = "task_list_type_cable_in_moter"
    val TASK_LIST_PREF_KEY_DISTANCE_IN_MOTER = "task_list_distance_in_moter"
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
                    editTextDistanceInMoter.hint = "20"
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
                    editTextAmountMoterSize.hint = "45"
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
    }

    fun moterSizeOnClick(view: View) {
        editTextAmountMoterSize.setText("")
        editTextAmountMoterSize.hint = "45"
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
        startActivityForResult(intent,TASK_NAME_REQUEST_CODE)
    }

    fun setDistanceOnClick(view: View) {
        editTextDistanceInMoter.setText("")
        editTextDistanceInMoter.hint = "20"
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

        if(editTextAmountMoterSize.text.isEmpty()) editTextAmountMoterSize.setText("45")
        if(editTextDistanceInMoter.text.isEmpty()) editTextDistanceInMoter.setText("20")


        wayBackActivity1.visibility = View.GONE
        btnCalInPipeSize.visibility = View.GONE
        tableBeforeCalculateInMoter.visibility = View.VISIBLE

    }

    fun moterReportOnClick(view: View) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK){

                val dataUnitMoter = data!!.getStringExtra("dataUnitMoter")
                val dataPhase = data.getIntExtra("dataPhase",0)
                val dataInstallation = data.getParcelableExtra<InstallationModelInTransformer>("dataInstall")
                val dataTypeCable = data!!.getStringExtra("dataTypeCable")
////                val dataCircuit = data!!.getStringExtra("dataCircuit")

                if (dataUnitMoter != null){
                    textUnit.text = dataUnitMoter
                    saveData("unitMoter", dataUnitMoter)
                }

                if (dataPhase != 0) {
                    phaseTextView.text = "$dataPhase เฟส"
                    saveData("phase", "$dataPhase เฟส")
                }

                if (dataInstallation != null) {
//                    val dataInstallationSlice = dataInstallation
                    TextViewGroupInstallations.text = dataInstallation.des
                    saveData("group", dataInstallation.title)
                    saveData("installation", dataInstallation.des)
                }
                if (dataTypeCable != null){
                    TextViewCableTypeInMoter.text = dataTypeCable
                    saveData("dataTypeCable", dataTypeCable)
                }

            }
            wayBackActivity1.visibility = View.VISIBLE
            btnCalInPipeSize.visibility = View.VISIBLE
            tableBeforeCalculateInMoter.visibility = View.GONE
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
        val dataOfCableSize = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_IN_MOTER,"NYY")
        val dataOfDistanceInTransformer = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE_IN_MOTER,"20")

        textUnit.text = dataOfUnitMoter
        phaseTextView.text = dataOfPhase
        TextViewGroupInstallations.text = dataOfInstall
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