package com.alw.temca.ui.WireSize

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_circuit.*


class CircuitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit)

        var intent: Intent

        cardViewCircuitBreaker.setOnClickListener {
            intent = Intent(this, CircuitBreakerActivity::class.java)
            startActivity(intent)
            finish()
        }


        if (savedInstanceState == null) {
            val extras = getIntent().extras
            if (extras != null)
                textViewCircuit.text = extras.getString("data")
            else
                textViewCircuit.text = "40A"

        }


        editTextOperating.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })

    }
}