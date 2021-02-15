package com.alw.temca.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_soon.*

class SoonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soon)
        textHeader.text = intent.getStringExtra("txt").toString()
    }
}