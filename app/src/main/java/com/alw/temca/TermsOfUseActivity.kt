package com.alw.temca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TermsOfUseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)
    }

    fun backOnClick(view: View) {
        finish()
    }
}