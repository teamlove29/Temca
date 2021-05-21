package com.alw.temca.ui.Moter

import android.Manifest
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Html
import android.text.SpannableString
import android.widget.Toast
import androidx.core.content.FileProvider
import com.alw.temca.Common.ApplicationSelectorReceiver
import com.alw.temca.Common.Common
import com.alw.temca.Model.DataToMoterReportModel
import com.alw.temca.Model.DataToTransformerReportModel
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
import kotlinx.android.synthetic.main.activity_moter_report.*
import kotlinx.android.synthetic.main.activity_transformer_report.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MoterReportActivity : AppCompatActivity() {
    private val file_name:String = "_result_calculate.pdf"
    private val fractionValues = arrayOf("1/2", "1/4", "3/4")
    private val fractionValues2 = arrayOf("\u00BD", "\u00BC", "\u00BE")
    val MY_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moter_report)

        val DataFromMoter = intent.getParcelableArrayListExtra<DataToMoterReportModel>("DataFromMoter")!!

        // Data
        textViewReslutSizeMoterInMoterReport.text = "${DataFromMoter[0].sizemoter} ${DataFromMoter[0].unit}"
        textViewMainResultPhaseInMoterReport.text = DataFromMoter[0].phase
        textViewResultPanternInMoterReport.text = DataFromMoter[0].panternstart
        textViewResultSizeCableInMoterReport.text = DataFromMoter[0].cabletype
        textViewResultAmountDistanceInMoterReport.text = "${DataFromMoter[0].amountdistance}m"
        // Cal
        textViewReslutPowerRatingInMoterReport.text = DataFromMoter[0].resultpowerrate
        textViewReferenceVoltageInMoterReport.text = DataFromMoter[0].resultrefpower
        textViewResultCableSizeInMoterReport.text = Html.fromHtml(DataFromMoter[0].resultsizecable.replace("mm2", "mm<sup><small><small>2</small></small></sup>"))
        textViewResultCableSizeGroudInMoterReport.text = Html.fromHtml(DataFromMoter[0].resultgroudcable.replace("mm2", "mm<sup><small><small>2</small></small></sup>"))
        if(DataFromMoter[0].resultconduit.indexOf('/') != -1){
            for(i in 0..2){
                if(DataFromMoter[0].resultconduit.indexOf(fractionValues[i]) != -1){
                    textViewResultRailSizeInMoterReport.text = DataFromMoter[0].resultconduit.replace(fractionValues[i],fractionValues2[i])
                    break
                }else if(i == 2) textViewResultRailSizeInMoterReport.text = DataFromMoter[0].resultconduit
            }
        }else textViewResultRailSizeInMoterReport.text = DataFromMoter[0].resultconduit
        textViewResultCircuitInMoterReport.text = DataFromMoter[0].resultbreaker
        textViewResultPressureInMoterReport.text = DataFromMoter[0].resultpressure


        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        createPDFFile(
                            Common.getAppPath(this@MoterReportActivity) + file_name, DataFromMoter
                        )
                }
                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    showSettingsDialog()
                    Toast.makeText(
                        this@MoterReportActivity,
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

        btnSendEmailInMoterReport.setOnClickListener {
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
//                startActivityForResult(Intent.createChooser(sendIntent, "SHARE", pendingIntent.intentSender), MY_REQUEST_CODE)
                val chooser = Intent.createChooser(sendIntent, "Share File")
                val resInfoList = this.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(chooser)

            } catch (e: Exception) {
                Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
//                println("Error : $e")
            }
        }
    }

    fun createPDFFile(path: String, data: ArrayList<DataToMoterReportModel>) {
        if(File(path).exists()) File(path).delete()
        try {
            val document = Document(PageSize.A4, 40.0f, 40.0f, 30.0f, 40.0f)
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

//            //add Image
//            val d = resources.getDrawable(R.drawable.logo_pdf_temca)
//            val bitDw = d as BitmapDrawable
//            val bmp = bitDw.bitmap
//            val stream = ByteArrayOutputStream()
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
////            bmp.scale(200, 500)
//            val image: Image = Image.getInstance(stream.toByteArray())
//
//            val chunk = Chunk(image, 0F, -40F, true)
//            val p = Paragraph(chunk)
//            p.alignment = Element.ALIGN_RIGHT
//            document.add(p)
//            addNewItem(document, "", Element.ALIGN_RIGHT, titleStyleTitle)

            addNewItemWithLeftAndRight(document, "รายงานคำนวณขนาดสายหม้อแปลง", "", titleStyle, detailStyleTitle)
            addLineSeperator(document)
            addNewItem(document, "ข้อมูลการใช้งาน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดมอเตอร์ : ", "${data[0].sizemoter} ${data[0].unit}", titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                เฟส : ", data[0].phase, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                รูปแบบสตาร์ท : ", data[0].panternstart, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ชนิดสายไฟ : ", data[0].cabletype, titleStyleTitle, valueStyle)
            addLineSpace(document)

            addItemAndResult(document, "                ระยะทาง : ", "${data[0].amountdistance}m", titleStyleTitle, valueStyle)
            addLineSpace(document)

            addNewItem(document, "ผลการคำนวน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)
            addItemAndResult(document, "                พิกัดกระแสไฟ                 ", data[0].resultpowerrate, titleStyleTitle, valueStyle)
            addItemAndResult(document, "                       ${data[0].resultrefpower}       ", "", subTitleStyle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดสายไฟฟ้า               ", data[0].resultsizecable, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดสายดิน                   ", data[0].resultsizecable, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดท่อร้อยสาย            ", data[0].resultconduit, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดเบรกเกอร์(AT)       ", data[0].resultbreaker, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                แรงดันตก                         ", data[0].resultpressure, titleStyleTitle, valueStyle)
            addLineSpace(document)

//            addNewItem(document, "* อ้างอิงตามมาตรฐานการติดตั้งทางไฟฟ้า วสท. 2562", Element.ALIGN_LEFT, SubvalueStyle)
//            addNewItem(document, "** ใช้งานที่อุณหภูมิ 36-40 C° และเดินสาย 1 กลุ่มวงจร", Element.ALIGN_LEFT, SubvalueStyle)
//            addLineSpace(document)

            //close
            document.close()

        }catch (e: Exception){
            println("Error Create: $e")
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
        val image: Image = Image.getInstance(stream.toByteArray())
        val chunkTextRight = if (textLeft == "รายงานคำนวณขนาดสายหม้อแปลง"){
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
        document.add(Paragraph("  "))
    }

    @Throws(DocumentException::class)
    fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text, style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }

    fun addItemAndResult(document: Document, textLeft: String, textRight: String, leftStyle: Font, rightStyle: Font){

        val glue =  Chunk(VerticalPositionMark())
        val p = Paragraph()
        val s = SpannableString(textRight.trim())

        p.add(Chunk(textLeft, leftStyle))
        if (textRight.indexOf("mm2") > 1){
            p.add(Chunk(textRight.replace("mm2", "mm²"), rightStyle))
        }else{
            if(textRight.indexOf('/') != -1){
                for(i in 0..2){
                    if(textRight.indexOf(fractionValues[i]) != -1){
                        p.add(Chunk(textRight.replace(fractionValues[i],fractionValues2[i]), rightStyle))
                        break
                    }else if(i == 2) p.add(Chunk(textRight, rightStyle))
                }
            }else{
                p.add(Chunk(textRight, rightStyle))
            }
        }
        document.add(p)
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
            openSettings()
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }
}