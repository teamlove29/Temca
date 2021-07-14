package com.alw.temca.ui.CurrentRating

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alw.temca.Model.CurrentRating.ReportResultCurrent
import com.alw.temca.R
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
import com.alw.temca.ui.SponsorActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_current_rating.*
import kotlinx.android.synthetic.main.activity_current_rating.typeCableTextView
import kotlinx.android.synthetic.main.activity_current_rating.wayBackActivity1
import kotlinx.android.synthetic.main.activity_current_rating.wayBackActivity2
import java.io.IOException


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




    fun calculatorOnClick(view: View) {

        tableBeforeCalculateInPipe.visibility = View.VISIBLE
        wayBackActivity1.visibility = View.GONE
        wayBackActivity2.visibility = View.VISIBLE
        btnCalInPipeSize.visibility = View.GONE



        val getXLS =  when(InstallationTextView.text){
            "กลุ่ม 2" -> "currentRating_group2.xls"
            "กลุ่ม 5" -> "currentRating_group5.xls"
            else -> ""
        }

        val findSheetInTableCableSize = when(PhaseTextView.text){
            "1 เฟส" -> 0
            "3 เฟส" -> 1
            else -> return
        }

        var colInTable:Int = 0
        if(InstallationTextView.text == "กลุ่ม 2"){
            colInTable = when(typeCableTextView.text){
                "IEC01" -> 1
                "IEC10 2C" -> 2
                "IEC10 3C" -> 2
                "IEC10 4C" -> 2
                "NYY 1C" -> 1
                "NYY 2C" -> 2
                "NYY 3C" -> 2
                "NYY 4C" -> 2
                "XLPE 1C" -> 3
                "XLPE 2C" -> 4
                "XLPE 3C" -> 4
                "XLPE 4C" -> 4
                "VCT 1C" -> 1
                "VCT 2C" -> 2
                "VCT 3C" -> 2
                "VCT 4C" -> 2
                else -> return
            }
        }else if(InstallationTextView.text == "กลุ่ม 5"){
            colInTable = when(typeCableTextView.text){
                "NYY 1C" -> 1
                "NYY 2C" -> 1
                "NYY 3C" -> 1
                "NYY 4C" -> 1
                "XLPE 1C" -> 2
                "XLPE 2C" -> 2
                "XLPE 3C" -> 2
                "XLPE 4C" -> 2
                else -> return
            }
        }







        try {
            val XLS = applicationContext.assets.open(getXLS)
            val wb = Workbook.getWorkbook(XLS)
            val sheet = wb.getSheet(findSheetInTableCableSize)            // index
            val sizeCable = arrayListOf("1 mm2", "1.5 mm2", "2.5 mm2", "4 mm2", "6 mm2", "10 mm2", "16 mm2", "25 mm2", "35 mm2", "50 mm2", "70 mm2", "95 mm2", "120 mm2", "150 mm2", "185 mm2", "240 mm2", "300 mm2")
                sizeCable.forEachIndexed { index, sizeCable  -> // หาขนาดสาย mm2
                    if (cableSizeTextView.text == sizeCable){
                        val dataOfTale = sheet.getCell(colInTable, index).contents
                                textViewResultMaxCurren.text = "${dataOfTale}A"

                    }
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error : $e", Toast.LENGTH_LONG).show()
        }
}
    fun reportOnClickInCurrentRating(view: View) {

        // TODO wait edit "ReportResultCurrent" below here
        val dataToReport = ArrayList<ReportResultCurrent>()
        val intent = Intent(this, ReportInCurrentActivity::class.java)

//        dataToReport.add(
//            ReportResultWireSize(
//                phaseTextView.text.toString(), // phase
//                textInstallation,  // groupinstallation
//                typeCableTextView.text.toString(), // typcable
//                circuitTextView.text.toString(), // CB
//                editTextDistance.text.toString(), // amountDis
//                textViewShow2.text.toString().replace("mm2","mm"), // text2 is cablesize
//                textViewResultWireGround.text.toString(), // wiresizegroud
//                textViewShow4.text.toString(), // text4 is conduitsize
//                textViewShow6.text.toString()) // result presure
//        )

        intent.putParcelableArrayListExtra("DataFromCurrentRating",dataToReport)
        startActivityForResult(intent, OnePhaseActivity.TASK_NAME_REQUEST_CODE)
        finish()


    }

    fun onclickChoosePhase(view: View) {
        val intent = Intent(this, PhaseInCurrentActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun onClickChooseInstallation(view: View) {
        val intent = Intent(this, InstallationInCurrentActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun onclickChooseTypeCable(view: View) {
        val intent = Intent(this, TypeCableInCurrentActivity::class.java)
        intent.putExtra("Phase",PhaseTextView.text)
        intent.putExtra("Group",InstallationTextView.text)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
    fun onClickChooseSizeCable(view: View) {
        val intent = Intent(this, SizeCableInCurrentActivity::class.java)
        startActivityForResult(intent, TASK_NAME_REQUEST_CODE)
    }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TASK_NAME_REQUEST_CODE){
            if(resultCode == RESULT_OK) {

                val dataPhase = data!!.getIntExtra("dataPhase",0)
                val dataInstallation = data!!.getStringExtra("dataInstall")
                val dataInstallationDes = data.getStringExtra("dataInstallDes").toString()
                val dataTypeCable = data!!.getStringExtra("dataTypeCable")
                val dataSizeCable = data!!.getStringExtra("dataSizeCable")

                if(dataPhase != null && dataPhase != 0){
                    if(InstallationTextView.text == "กลุ่ม 5"){
                        typeCableTextView.text = "NYY 1C"
                        saveData("typeCable", typeCableTextView.text.toString())
                    }else{
                        typeCableTextView.text = "IEC01"
                        saveData("typeCable", typeCableTextView.text.toString())
                    }
                    PhaseTextView.text = "$dataPhase เฟส"
                    saveData("phase", "$dataPhase เฟส")
                }
                if (dataInstallation != null) {
                    val dataInstallationSlice = dataInstallation!!.slice(0..6)
                    if(dataInstallationSlice == "กลุ่ม 5"){
                        typeCableTextView.text = "NYY 1C"
                        saveData("typeCable", typeCableTextView.text.toString())
                    }else{
                        typeCableTextView.text = "IEC01"
                        saveData("typeCable", typeCableTextView.text.toString())
                    }

                    InstallationTextView.text = dataInstallationSlice
                    saveData("installation", dataInstallation)
                    saveData("installationDes", dataInstallationDes)
                }
                if (dataTypeCable != null) {
                    typeCableTextView.text = dataTypeCable
                    saveData("typeCable", dataTypeCable)
                }
                if (dataSizeCable != null) {
                    cableSizeTextView.text = dataSizeCable
                    saveData("sizeCable", dataSizeCable)
                }

            }
        }
        wayBackActivity1.visibility = View.VISIBLE
        wayBackActivity2.visibility = View.GONE
        btnCalInPipeSize.visibility = View.VISIBLE
        tableBeforeCalculateInPipe.visibility = View.GONE
    }

}

