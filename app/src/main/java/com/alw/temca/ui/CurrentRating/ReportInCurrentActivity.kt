package com.alw.temca.ui.CurrentRating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alw.temca.Model.CurrentRating.ReportResultCurrent
import com.alw.temca.R

class ReportInCurrentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_in_current)

        val DataFromCurrentRating = intent.getParcelableArrayListExtra<ReportResultCurrent>("DataFromCurrentRating")!!

    }
}