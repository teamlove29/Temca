package com.alw.temca.ui.Moter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.StartPanternAdapter
import com.alw.temca.Model.startPanternModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_start_pattern.*

class StartPatternActivity : AppCompatActivity(), InstallationonClickAdapterListener {

    val startPanternList = ArrayList<startPanternModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_pattern)

        startPanternList.add(startPanternModel("DOL"))
        startPanternList.add(startPanternModel("STAR DELTA"))

        recyclerViewStartPantern.adapter = StartPanternAdapter(startPanternList,this)
        recyclerViewStartPantern.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataStartPantern",startPanternList[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}