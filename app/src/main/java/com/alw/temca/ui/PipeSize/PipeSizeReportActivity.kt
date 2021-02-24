package com.alw.temca.ui.PipeSize

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_pipe_size_report.*

class PipeSizeReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pipe_size_report)


        btnSendEmailInPipeSize.setOnClickListener(object : View.OnClickListener {
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
                    Toast.makeText(this@PipeSizeReportActivity, "Error to open email app", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

}