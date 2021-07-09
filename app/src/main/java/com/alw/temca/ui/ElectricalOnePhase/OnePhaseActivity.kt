package com.alw.temca.ui.ElectricalOnePhase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alw.temca.R

class OnePhaseActivity : AppCompatActivity() {
    
    companion object{
        const val TASK_NAME_REQUEST_CODE = 100
        const val TASK_LIST_PREF_KEY_PHASE_IN_ONE_PHASE = "task_list_phase_one_phase"
        const val TASK_LIST_PREF_KEY_INSTALLATION_ONE_PHASE = "task_list_installation_one_phase"
        const val TASK_LIST_PREF_KEY_INSTALLATION_ONE_PHASE_DES = "task_list_installation_one_phase"
        const val TASK_LIST_PREF_KEY_TYPE_CABLE_ONE_PHASE = "task_list_type_cable_one_phase"
        const val TASK_LIST_PREF_KEY_CIRCUIT_ONE_PHASE = "task_list_circuit_one_phase"
        const val TASK_LIST_PREF_KEY_DISTANCE_ONE_PHASE = "task_list_distance_one_phase"
        const val PREF_NAME = "task_one_phase"
//        val railSizeList = ArrayList<RailSizeModel>()
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_phase)
    }
}