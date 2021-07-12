package com.alw.temca.ui.CurrentRating

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alw.temca.R
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.WireSize.PhaseActivity
import kotlinx.android.synthetic.main.activity_current_rating.*
import kotlinx.android.synthetic.main.activity_current_rating.typeCableTextView

class CurrentRatingActivity : AppCompatActivity() {
    companion object{
        const val TASK_NAME_REQUEST_CODE = 100
        const val TASK_LIST_PREF_KEY_PHASE_IN_CURRENT_RATING = "task_list_phase_current_rating"
        const val TASK_LIST_PREF_KEY_INSTALLATION_CURRENT_RATING = "task_list_installation_current_rating"
        const val TASK_LIST_PREF_KEY_INSTALLATION_DES_CURRENT_RATING = "task_list_installation_des_current_rating"
        const val TASK_LIST_PREF_KEY_TYPE_CABLE_CURRENT_RATING = "task_list_type_cable_current_rating"
        const val TASK_LIST_PREF_KEY_SIZE_CABLE_CURRENT_RATING = "task_list_size_cable_current_rating"

        const val PREF_NAME = "task_current_rating"
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun saveData(type: String, value: String){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            if (type == "phase"){
                putString(TASK_LIST_PREF_KEY_PHASE_IN_CURRENT_RATING, value)
            }
            if (type == "installation"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_CURRENT_RATING, value)
            }
            if (type == "installationDes"){
                putString(TASK_LIST_PREF_KEY_INSTALLATION_DES_CURRENT_RATING, value)
            }
            if (type == "typeCable"){
                putString(TASK_LIST_PREF_KEY_TYPE_CABLE_CURRENT_RATING, value)
            }
            if (type == "sizeCable"){
                putString(TASK_LIST_PREF_KEY_SIZE_CABLE_CURRENT_RATING, value)
            }
            commit()
        }
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dataOfPhase = sharedPref.getString(TASK_LIST_PREF_KEY_PHASE_IN_CURRENT_RATING, "1 เฟส")
        val dataOfInstallation = sharedPref.getString(TASK_LIST_PREF_KEY_INSTALLATION_CURRENT_RATING, "กลุ่ม 2")
        val dataOfTypeCable = if(dataOfInstallation!!.slice(0..6) == "กลุ่ม 5" ){
            sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_CURRENT_RATING, "NYY") }
            else{ sharedPref.getString(TASK_LIST_PREF_KEY_TYPE_CABLE_CURRENT_RATING, "IEC01") }
        val dataOfSizeCable = sharedPref.getString(TASK_LIST_PREF_KEY_SIZE_CABLE_CURRENT_RATING, "2.5 mm2")

        PhaseTextView.text = dataOfPhase
        InstallationTextView.text = dataOfInstallation
        typeCableTextView.text = dataOfTypeCable
        cableSizeTextView.text =  dataOfSizeCable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_rating)
    }

    fun calculatorOnClick(view: View) {}
    fun reportOnClickInCurrentRating(view: View) {}

    fun onclickChoosePhase(view: View) {
        val intent = Intent(this, PhaseActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun onClickChooseInstallation(view: View) {}
    fun onclickChooseTypeCable(view: View) {}
    fun onClickChooseSizeCable(view: View) {}
    fun sponsorOnClick(view: View) {
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }
    fun backOnClick(view: View) {
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
        sharedPref.apply()
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear()
        sharedPref.apply()
        finish()
    }

}