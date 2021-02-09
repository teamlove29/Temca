package com.alw.temca.ui.WireSize

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        btnSendEmail.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val mailto = "mailto:useremail@gmail.com" +
                        "?cc=" +
                        "&subject=" + Uri.encode("ผลการคำนวณ") +
                        "&body=" + Uri.encode("ทดสอบการส่งอีเมล์")
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse(mailto)
                try {
                    startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@ReportActivity, "Error to open email app", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}