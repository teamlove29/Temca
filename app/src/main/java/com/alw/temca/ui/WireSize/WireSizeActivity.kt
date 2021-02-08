package com.alw.temca.ui.WireSize

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.Model.PhaseModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_wire_size.*



class WireSizeActivity : AppCompatActivity() {
    final val TASK_LIST_PREF_KEY_PHASE = "task_list_phase"
    final val TASK_LIST_PREF_KEY_INSTALLATION = "task_list_installation"
    final val TASK_LIST_PREF_KEY_TYPE_CABLE = "task_list_type_cable"
    final val TASK_LIST_PREF_KEY_CIRCUIT = "task_list_circuit"
    final val TASK_LIST_PREF_KEY_DISTANCE = "task_list_distance"
    final val PREF_NAME = "task_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wire_size)

        loadData()

        val intent = intent
        val dataPhase = intent.getIntExtra("dataPhase",0)
        val dataInstallation = intent.getStringExtra("dataInstall")
        val dataTypeCable = intent.getStringExtra("dataTypeCable")
        val dataCircuit = intent.getStringExtra("dataCircuit")

        if (dataPhase != 0){
            phaseTextView.text = "$dataPhase เฟส"
            saveData("phase",dataPhase.toString())
        }

        if (dataInstallation != null) {
            installationTextView.text = dataInstallation.slice(0..6)
            saveData("installation",dataInstallation)
        }
        if (dataTypeCable != null) {
            typeCableTextView.text = dataTypeCable
            saveData("typeCable",dataTypeCable)
        }
        if (dataCircuit != null) {
            circuitTextView.text = dataCircuit
            saveData("circuit",dataCircuit)
        }
    }

    fun phaseOnClick(view:View){
        val intent = Intent(this,PhaseActivity::class.java)
        startActivity(intent)
    }

    fun installationOnClick(view: View) {
        val intent = Intent(this,InstallationActivity::class.java)
        startActivity(intent)

    }  fun typeCableOnClick(view: View) {
        val intent = Intent(this,TypeCableActivity::class.java)
        startActivity(intent)
    }
    fun CircuitOnClick(view: View) {
        val intent = Intent(this,CircuitActivity::class.java)
        startActivity(intent)
    }

    fun DestanceOnClick(view: View) {
        editTextDistance.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistance, InputMethodManager.SHOW_IMPLICIT)
    }

    fun saveData(type:String,value:String){
        val data = value
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
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
            commit()
        }
    }

    fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfPhase = sharedPref.getString(TASK_LIST_PREF_KEY_PHASE,"1 เฟส")
        val dataOfInstallation = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION,"กลุ่ม 1")
        val dataOfTypeCable = sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE,"IEC01")
        val dataOfCircuit = sharedPref.getString(TASK_LIST_PREF_KEY_CIRCUIT,"40A")
        val dataOfDistance = sharedPref.getString(TASK_LIST_PREF_KEY_DISTANCE,"0")

        phaseTextView.text = "$dataOfPhase เฟส"
        installationTextView.text = dataOfInstallation!!.slice(0..6)
        typeCableTextView.text = dataOfTypeCable
        circuitTextView.text = dataOfCircuit
//        editTextDistance.text
    }
}



