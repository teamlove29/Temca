package com.alw.temca.ui.PipeSize

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import com.alw.temca.Adapter.PdfDocumentAdapter
import com.alw.temca.Common.ApplicationSelectorReceiver
import com.alw.temca.Common.Common
import com.alw.temca.Model.ReportReslutPipeSizeModel
import com.alw.temca.R
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfDocument
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_pipe_size.*
import kotlinx.android.synthetic.main.activity_pipe_size_report.*
import kotlinx.android.synthetic.main.activity_pipe_size_report.textViewReferenceVoltageInReport2
import kotlinx.android.synthetic.main.activity_pipe_size_report.textViewResultConduitSize
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.activity_wire_size.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class PipeSizeReportActivity : AppCompatActivity() {

    val file_name:String = "_result_calculate.pdf"
    private var mailClientOpened = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pipe_size_report)

        val resultPipe = intent.getParcelableExtra<ReportReslutPipeSizeModel>("resultPipeSize") // have not railSize
        val resultMax = intent.getParcelableExtra<ReportReslutPipeSizeModel>("resultMaxCable") // have not amount

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    if (resultPipe != null) {
                        createPDFFile(
                                Common.getAppPath(this@PipeSizeReportActivity) + file_name,
                                resultPipe
                        )
                    } else {
                        createPDFFile(
                                Common.getAppPath(this@PipeSizeReportActivity) + file_name,
                                resultMax
                        )
                    }

                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                            this@PipeSizeReportActivity,
                            "Denied Permission",
                            Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }

            })
            .check()


        if(resultPipe != null){
            textViewMainBreakerInReport.visibility = View.VISIBLE // จำนวนเส้น
            textViewMainResultBreakerInReportData.visibility = View.VISIBLE // result จำนวนเส้น
            textViewMainCableTypeInReport.visibility = View.GONE // ขนาดราง
            textViewMainResultCableTypeInReport.visibility = View.GONE // result ขนาดราง
            textViewAmountCableInPipeReport.visibility = View.GONE // จำนวนสายไฟ
            textViewReslutAmountCableInPipeReport.visibility = View.GONE // result จำนวนสายไฟ
            textViewReslutMainCableTypeInPipeReport.text = resultPipe.cabletype
            textViewMainResultInstallationInReport.text = resultPipe.sizecable
            textViewMainResultBreakerInReportData.text = resultPipe.amount
            textViewResultConduitSize.text = resultPipe.pipesize
            textViewResultRailSizeInReportPipeSize.text = resultPipe.conduitsize
        }else{
            println(resultMax)
            textViewMainBreakerInReport.visibility = View.GONE // จำนวนเส้น
            textViewMainResultBreakerInReportData.visibility = View.GONE // result จำนวนเส้น
            textViewPipeSizeInPipeReport.visibility = View.GONE
            textViewReferenceVoltageInReport2.visibility = View.GONE
            textViewResultConduitSize.visibility = View.GONE
            textViewRailSizeInReportPipeSize.visibility = View.GONE
            textViewRailSizeInReportPipeSize2.visibility = View.GONE
            textViewResultRailSizeInReportPipeSize.visibility = View.GONE

            textViewMainCableTypeInReport.visibility = View.VISIBLE // ขนาดราง
            textViewMainResultCableTypeInReport.visibility = View.VISIBLE // result ขนาดราง
            textViewAmountCableInPipeReport.visibility = View.VISIBLE // จำนวนสายไฟ
            textViewReslutAmountCableInPipeReport.visibility = View.VISIBLE // result จำนวนสายไฟ
            textViewReslutMainCableTypeInPipeReport.text = resultMax!!.cabletype
            textViewMainResultInstallationInReport.text = resultMax.sizecable
            textViewMainResultCableTypeInReport.text = resultMax.conduitsizechoose
            textViewReslutAmountCableInPipeReport.text = resultMax.amountcablemax
        }

//        btnPrint.setOnClickListener {
//            printPDF()
//        }

        btnSendEmailInPipeSize.setOnClickListener {
            try {
                val fileWithinMyDir = File(
                        Environment.getExternalStorageDirectory().toString()
                                + File.separator
                                + applicationContext.resources.getString(R.string.app_name)
                                + file_name)

//                val uri = FileProvider.getUriForFile(this, this.packageName.toString() + ".fileprovider", fileWithinMyDir)
//                val sendIntent = Intent(Intent.ACTION_SEND)
//                val receiver = Intent(this, ApplicationSelectorReceiver::class.java)
//                val pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT)
//                sendIntent.type = "*/*"
//                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "รายงานผลการคำนวณขนาดท่อและราง")
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "รายงานผลการคำนวณขนาดท่อและรางตามไฟล์ที่แนบ...")
//                sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
//                startActivityForResult(Intent.createChooser(sendIntent, "SHARE", pendingIntent.intentSender), 800)
            } catch (e: Exception) {
                Toast.makeText(this, "Error FileProvider: $e", Toast.LENGTH_SHORT).show()
//                println("Error : $e")
            }
        }
    }

    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        try {
            val printAdapter = PdfDocumentAdapter(this,Common.getAppPath(this) + file_name)
            printManager.print("Document",printAdapter, PrintAttributes.Builder().build())
        }catch (e:Exception){
            Log.e("Error Report", e.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//                println("requestCode ${requestCode}")
//                println("mailClientOpened ${mailClientOpened}")
//                println("resultCode ${resultCode}")
//                println("resultCode2222 ${Activity.RESULT_OK}")

//        if(requestCode == 800){
////            val intent = Intent(this,MainActivity::class.java)
////            startActivityForResult(intent,100)
//        }
//        if (resultCode == Activity.RESULT_OK) {
//            Toast.makeText(this, "Successfully Sharing", Toast.LENGTH_SHORT).show()
//
//        }
    }


    override fun onResume() {
        super.onResume()
        mailClientOpened = false
    }

    override fun onStop() {
        super.onStop()
        mailClientOpened = true
    }

    fun createPDFFile(path: String, data: ReportReslutPipeSizeModel?) {
        if(File(path).exists()) File(path).delete()
        try {
            val document = Document()

            //Save
            PdfWriter.getInstance(document, FileOutputStream(path))

            //Open to Write
            document.open()

            //Setting
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("Dev")
            document.addCreator("Marutthep Rompho")
            document.setMargins(40.0f, 40.0F, 30.0F, 40.0F) // มีผลเฉพาะ Page 2 เท่านั้น
//            document.isMarginMirroring = true
            // Font setting
            val colorAccent = BaseColor(14, 65, 148, 255)
            val headingFontSize = 12.0f
            val valueFontSzie = 12.0f
            val SubvalueFontSzie = 10.0f

            // Custom font
            val fontName = BaseFont.createFont(
                    "assets/sarabun_regular.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.NOT_EMBEDDED, true
            )
            val fontNameBoldStyle = BaseFont.createFont(
                    "assets/sarabun_medium.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.NOT_EMBEDDED, true
            )

            // Add Title to document
            val titleStyleTitle = Font(fontNameBoldStyle, 12.0f, Font.NORMAL, BaseColor.BLACK)
            val subTitleStyle = Font(fontNameBoldStyle, 9.0f, Font.NORMAL, BaseColor.BLACK)
            val detailStyleTitle = Font(fontNameBoldStyle, 12.0f, Font.NORMAL, colorAccent)
            val titleStyle = Font(fontName, 18.0f, Font.NORMAL, BaseColor.BLACK)
            val headingStyle = Font(fontName, headingFontSize, Font.BOLD, BaseColor.BLACK)
            var valueStyle = Font(fontName, valueFontSzie, Font.NORMAL, colorAccent)
            var SubvalueStyle = Font(fontName, SubvalueFontSzie, Font.NORMAL, colorAccent)


            addNewItemWithLeftAndRight(document, "รายงานคำนวนขนาดท่อและราง", "", titleStyle, detailStyleTitle)
            addLineSeperator(document)
//            addNewItem(document, "รายงานผลการคำนวณขนาดท่อและราง", Element.ALIGN_CENTER, titleStyleTitle)
//            addLineSpace(document)

//            addNewItemWithLeftAndRight(document, "รายงานคำนวณขนาดสายไฟฟ้าและท่อไฟฟ้า", "", titleStyle, detailStyleTitle)
//            addLineSeperator(document)

            addNewItem(document, "ข้อมูลการใช้งาน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)

            addItemAndResult(document, "                ชนิดสายไฟ : ", data!!.cabletype, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดสาย :   ", data.sizecable, titleStyleTitle, valueStyle)
            addLineSpace(document)
            if(data.amountcablemax == ""){
                addItemAndResult(document, "                จำนวน :       ", data.amount, titleStyleTitle, valueStyle)
                addLineSpace(document)
            }else{
                addItemAndResult(document, "                ขนาดราง :   ", data.conduitsizechoose, titleStyleTitle, valueStyle)
                addLineSpace(document)
            }
            addNewItem(document, "ผลการคำนวน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)

            if(data.amountcablemax == ""){
                addItemAndResult(document, "                ขนาดท่อ                    ", data.pipesize, titleStyleTitle, valueStyle)
                addItemAndResult(document, "                   (จำนวนสูงสุด)", "", subTitleStyle, valueStyle)
                addLineSpace(document)
                addItemAndResult(document, "                ขนาดราง                    ", data.conduitsize, titleStyleTitle, valueStyle)
                addItemAndResult(document, "                   (จำนวนสูงสุด)", "", subTitleStyle, valueStyle)
                addLineSpace(document)
            }else{
                addItemAndResult(document, "                จำนวนสายไฟ                    ", data.amountcablemax, titleStyleTitle, valueStyle)
                addLineSpace(document)
            }


            //cabletype
//            addNewItemWithLeftAndRight(document, "ชนิดสายไฟ", "${data!!.cabletype} x ${data.sizecable}mm\u00B2", titleStyle, headingStyle)
//            addLineSpace(document)

//            if(data!!.amountcablemax == ""){
//                //amount
//                addNewItemWithLeftAndRight(document, "จำนวน", "${data.amount}", titleStyle, headingStyle)
//
//                //conduit Size
//                addNewItemWithLeftAndRight(document, "ขนาดท่อ", "${data.pipesize}", titleStyle, headingStyle)
//                addNewItemWithLeftAndRight(document, "(จำนวนสูงสุด)", "", SubvalueStyle, SubvalueStyle)
//
//                //Rail  Size
//                addNewItemWithLeftAndRight(document, "ขนาดราง", "${data.conduitsize}", titleStyle, headingStyle)
//                addNewItemWithLeftAndRight(document, "(จำนวนสูงสุด)", "", SubvalueStyle, SubvalueStyle)
//
//            }else{
//                //////////////  resultMax
//
//                //Rail  Size
//                addNewItemWithLeftAndRight(document, "ขนาดราง", "${data.conduitsizechoose}", titleStyle, headingStyle)
//                addLineSpace(document)
//
//
//                //Cable Size Max
//                addNewItemWithLeftAndRight(document, "ขนาดไฟสูงสุด", "${data.amountcablemax}", titleStyle, headingStyle)
//                addLineSpace(document)
//            }

//            addLineSeperator(document)
//            addNewItem(document,"Account Name:",Element.ALIGN_LEFT,headingStyle)
//            addNewItem(document,"Marutthep Rompho",Element.ALIGN_LEFT,valueStyle)

            //close
            document.close()
//            Toast.makeText(this, "Save PDF SUCCESS !", Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            println("Error : $e")
        }
    }

    @Throws(DocumentException::class)
    fun addItemAndResult(document: Document, textLeft: String, textRight: String, leftStyle: Font, rightStyle: Font){
        val glue =  Chunk(VerticalPositionMark())
        val p = Paragraph()

//        val s = SpannableString(textRight.trim())
//        p.add(Chunk(textLeft, leftStyle))
//        if (textRight.indexOf("mm2") > 1){
//            p.add(Chunk(textRight.replace("mm2", "mm²"), rightStyle))
//        }else{
//            if(textRight.indexOf('/') != -1) p.add(Chunk("$s", rightStyle))
//            else p.add(Chunk(textRight, rightStyle))
////
//
//        }
        p.add(Chunk(textLeft, leftStyle))
        p.add(Chunk(textRight, rightStyle))
        document.add(p)

    }


    @Throws(DocumentException::class)
    fun addNewItemWithLeftAndRight(
            document: Document,
            textLeft: String,
            textRight: String,
            leftStyle: Font,
            rightStyle: Font
    ) {
        //add Image
        val d = resources.getDrawable(R.drawable.logo_pdf_temca)
        val bitDw = d as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image: Image = Image.getInstance(stream.toByteArray())
        var chunkTextRight = if (textLeft == "รายงานคำนวนขนาดท่อและราง"){
            Chunk(image, 0F, -20F, true)
        }else{
            Chunk(textRight, rightStyle)
        }
        val chunkTextLeft = Chunk(textLeft, leftStyle)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)
    }


    @Throws(DocumentException::class)
    fun addLineSeperator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)

    }

    @Throws(DocumentException::class)
    fun addLineSpace(document: Document) {
        document.add(Paragraph(" "))
    }

    @Throws(DocumentException::class)
    fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text, style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)


    }

}
