package com.alw.temca.ui.WireSize

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import com.alw.temca.Common.ApplicationSelectorReceiver
import com.alw.temca.Common.Common
import com.alw.temca.Model.ReportResultWireSize
import com.alw.temca.R
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_pipe_size_report.*
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.activity_wire_size.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class ReportActivity : AppCompatActivity() {
    val file_name:String = "_result_calculate.pdf"
    val MY_REQUEST_CODE = 0
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val resultWire = intent.getParcelableExtra<ReportResultWireSize>("reportWireSize")


        if(resultWire != null){
            textViewResultWireSize.text = Html.fromHtml("${resultWire.cableSize.replace("มม2", "มม")}<sup><small><small>2</small></small></sup>")
            textViewResultWireGroundInReport.text = Html.fromHtml("${resultWire.wireGround.replace("มม2", "มม")}<sup><small><small>2</small></small></sup>")
            textViewResultConduitSize.text = resultWire.condutiSize
            textViewResultPressure.text = resultWire.pressure
        }

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        if (resultWire != null) {
                            createPDFFile(
                                    Common.getAppPath(this@ReportActivity) + file_name, resultWire
                            )
                        }
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                                this@ReportActivity,
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

        btnSendEmailInWireSize.setOnClickListener {
            try {
                val fileWithinMyDir = File(
                        Environment.getExternalStorageDirectory().toString()
                                + File.separator
                                + applicationContext.resources.getString(R.string.app_name)
                                + file_name)

                val uri = FileProvider.getUriForFile(this, this.getPackageName().toString() + ".fileprovider", fileWithinMyDir)
                val sendIntent = Intent(Intent.ACTION_SEND)
                val receiver = Intent(this, ApplicationSelectorReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT)
                sendIntent.type = "*/*"
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "รายงานผลการคำนวณหาขนาดสาย")
                sendIntent.putExtra(Intent.EXTRA_TEXT, "รายงานผลการคำนวณขนาดสายตามไฟล์ที่แนบ...")
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivityForResult(Intent.createChooser(sendIntent, "SHARE", pendingIntent.intentSender), MY_REQUEST_CODE)
            } catch (e: Exception) {
                Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
//                println("Error : $e")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("SS", "onActivityResult: " + requestCode + ", " + resultCode + ", " + (data?.toString()
                ?: "empty intent"))
        if (requestCode == MY_REQUEST_CODE) {
            Toast.makeText(applicationContext, "Success send email",
                    Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext, "failed to send email",
                    Toast.LENGTH_SHORT).show()
        }
        finish() // to end your activity when toast is shown
    }

    fun createPDFFile(path: String, data: ReportResultWireSize?) {
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
            // Font setting
            val colorAccent = BaseColor(14, 65, 148, 255)
            val headingFontSize = 14.0f
            val valueFontSzie = 14.0f
            val SubvalueFontSzie = 14.0f

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
            val titleStyleTitle = Font(fontNameBoldStyle, 14.0f, Font.NORMAL, BaseColor.BLACK)
            val detailStyleTitle = Font(fontNameBoldStyle, 12.0f, Font.NORMAL, colorAccent)
            val titleStyle = Font(fontName, 20.0f, Font.NORMAL, BaseColor.BLACK)
            val headingStyle = Font(fontName, headingFontSize, Font.BOLD, BaseColor.BLACK)
            var valueStyle = Font(fontName, valueFontSzie, Font.NORMAL, colorAccent)
            var SubvalueStyle = Font(fontName, SubvalueFontSzie, Font.NORMAL, colorAccent)


//            //add Image
//            val d = resources.getDrawable(R.drawable.logo_pdf_temca)
//            val bitDw = d as BitmapDrawable
//            val bmp = bitDw.bitmap
//            val stream = ByteArrayOutputStream()
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
////            bmp.scale(200, 500)
//            val image: Image = Image.getInstance(stream.toByteArray())
//
//
//
//            val chunk = Chunk(image, 0F, -40F, true)
//            val p = Paragraph(chunk)
//            p.alignment = Element.ALIGN_RIGHT
//            document.add(p)

//            addNewItem(document, "", Element.ALIGN_RIGHT, titleStyleTitle)
            addNewItemWithLeftAndRight(document, "รายงานคำนวณขนาดสายไฟฟ้าและท่อไฟฟ้า", "", titleStyle, detailStyleTitle)
                addLineSeperator(document)
                addNewItem(document, "ข้อมูลการใช้งาน", Element.ALIGN_LEFT, headingStyle)
                addLineSpace(document)
                addItemAndResult(document, "                ระบบไฟฟ้า : ", data!!.phase, titleStyleTitle, valueStyle)
                addLineSpace(document)
                addItemAndResult(document, "                กลุ่มการติดตั้ง : ", data.installation, titleStyleTitle, valueStyle)
                addLineSpace(document)
                addItemAndResult(document, "                ชนิดสายไฟฟ้า : ", data.cableType, titleStyleTitle, valueStyle)
                addLineSpace(document)
                addItemAndResult(document, "                Circuit Breaker : ", data.breaker, titleStyleTitle, valueStyle)
                addLineSpace(document)
                addItemAndResult(document, "                ระยะสายไฟฟ้า : ", data.distance, titleStyleTitle, valueStyle)
                addLineSpace(document)

            addNewItem(document, "ผลการคำนวน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดสายไฟฟ้าที่เหมาะสม     ", data.cableSize, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดสายดินที่เหมาะสม          ", data.wireGround, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดท่อไฟฟ้า                         ", data.condutiSize, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                แรงดันตก                                 ", data.pressure, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addLineSpace(document)

            addNewItem(document, "* อ้างอิงตามมาตรฐานการติดตั้งทางไฟฟ้า วสท. 2562", Element.ALIGN_LEFT, SubvalueStyle)
            addLineSpace(document)
            addNewItem(document, "** ใช้งานที่อุณหภูมิ 36-40 และเดินสาย 1 กลุ่มวงจร", Element.ALIGN_LEFT, SubvalueStyle)

//                // cableSize
//                addNewItemWithLeftAndRight(document, "ขนาดสายไฟ", data!!.cableSize, titleStyle, headingStyle)
//                addLineSpace(document)
//                //condutiSize
//                addNewItemWithLeftAndRight(document, "ขนาดท่อร้อยสาย(ราง)", data.condutiSize, titleStyle, headingStyle)
//                addLineSpace(document)
//                //pressure
//                addNewItemWithLeftAndRight(document, "แรงดันตก", data.pressure, titleStyle, headingStyle)

            //close
            document.close()

        }catch (e: Exception){
            println("Error : $e")
        }
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
//        bmp.scale(200, 100)
        val image: Image = Image.getInstance(stream.toByteArray())

//        document.add(image)

        var chunkTextRight = if (textLeft == "รายงานคำนวณขนาดสายไฟฟ้าและท่อไฟฟ้า"){
            Chunk(image, 0F, -20F, true)
//            Chunk(textRight, rightStyle)
        }else{
            Chunk(textRight, rightStyle)
        }

//        val chunkTextRight = if (textRight.indexOf("มม2") > 1){
//            Chunk("${textRight.replace("2", "")}\u00B2", rightStyle)
//        }else{
//            Chunk(textRight, rightStyle)
//        }

//        val chunkTextRight =  Chunk(textRight, rightStyle)
        val chunkTextLeft = Chunk(textLeft, leftStyle)


        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        addLineSpace(document)
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

   fun addItemAndResult(document: Document, text1: String, text2: String, style1: Font, style2: Font){
       val glue =  Chunk(VerticalPositionMark())
       val p = Paragraph()
       p.add(Chunk(text1, style1))

       if (text2.indexOf("มม2") > 1){
           p.add(Chunk("${text2.replace("2", "")}\u00B2", style2))
       }else{
           p.add(Chunk(text2, style2))
       }

       document.add(p)

    }

}