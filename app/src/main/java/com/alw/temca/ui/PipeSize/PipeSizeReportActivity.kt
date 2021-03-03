package com.alw.temca.ui.PipeSize

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alw.temca.Model.ReportReslutPipeSizeModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_pipe_size_report.*

class PipeSizeReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pipe_size_report)

        val resultPipe = intent.getParcelableExtra<ReportReslutPipeSizeModel>("resultPipeSize")
        val resultMax = intent.getParcelableExtra<ReportReslutPipeSizeModel>("resultMaxCable")

        if(resultPipe != null){
            CableSizeInPipeReport2.visibility = View.VISIBLE
            textViewConduitSizeInPipeReport2.visibility = View.VISIBLE
            CableSizeInPipeReport.visibility = View.GONE
            textViewReslutCableTypeInPipeReport.text = "${resultPipe!!.cabletype} x ${resultPipe.sizecable}"
            textViewReslutAmountCableInPipeReport.text = "${resultPipe.amount}"
            textViewResultPipeSizeInPipeReport.text = "${resultPipe.pipesize}"
            textViewResultConduitSizeInPipeReport.text = "${resultPipe.conduitsize}"
        }else{
            CableSizeInPipeReport2.visibility = View.GONE
            textViewConduitSizeInPipeReport2.visibility = View.GONE
            CableSizeInPipeReport.visibility = View.VISIBLE
            textViewReslutCableTypeInPipeReport.text = "${resultMax!!.cabletype} x ${resultMax.sizecable}"
            textViewResultConduitSizeInPipeReport.text = "${resultMax.conduitsizechoose}"
            textViewReslutCableSizeInPipeReport.text = "${resultMax!!.amountcablemax}"
        }


        btnSendEmailInPipeSize.setOnClickListener {

            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_STREAM, "1234")
            sendIntent.type = "text/pdf"
            startActivity(Intent.createChooser(sendIntent, "SHARE"))
        }

//        btnSendEmailInPipeSize.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View?) {
//                val mailto = "mailto:useremail@gmail.com" +
//                        "?cc=" +
//                        "&subject=" + Uri.encode("ผลการคำนวณ") +
//                        "&body=" + Uri.encode("ทดสอบการส่งอีเมล์")
//                val emailIntent = Intent(Intent.ACTION_SENDTO)
//                emailIntent.data = Uri.parse(mailto)
//                try {
//                    startActivity(emailIntent)
//                } catch (e: ActivityNotFoundException) {
//                    Toast.makeText(this@PipeSizeReportActivity, "Error to open email app", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

    }

}