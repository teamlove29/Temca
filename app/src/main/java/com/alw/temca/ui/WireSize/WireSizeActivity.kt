package com.alw.temca.ui.WireSize

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.Model.PhaseModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_wire_size.*



class WireSizeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wire_size)


        val intent = intent
        val restaurantData = intent.getIntExtra("Test",0)
        if (restaurantData != 0){
            phaseTextView.text = "$restaurantData เฟส"
        }


    }

    fun phaseOnClick(view:View){
        val intent = Intent(this,PhaseActivity::class.java)
        startActivity(intent)
    }

    fun DestanceOnClick(view: View) {
        editTextDistance.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextDistance, InputMethodManager.SHOW_IMPLICIT)
    }

}