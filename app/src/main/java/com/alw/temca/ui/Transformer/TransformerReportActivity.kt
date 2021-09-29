package com.alw.temca.ui.Transformer

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
import android.text.Spannable
import android.text.SpannableString
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TextAppearanceSpan
import android.widget.Toast
import androidx.core.content.FileProvider
import com.alw.temca.Common.ApplicationSelectorReceiver
import com.alw.temca.Common.Common
import com.alw.temca.Model.DataToTransformerReportModel
import com.alw.temca.Model.ReportResultWireSize
import com.alw.temca.R
import com.alw.temca.ui.CurrentRating.ReportInCurrentActivity
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
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.activity_transformer_report.*
import kotlinx.android.synthetic.main.activity_transformer_report.textViewResultConduitSize
import kotlinx.android.synthetic.main.activity_transformer_report.textViewResultConduitSize2
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class TransformerReportActivity : AppCompatActivity() {
    val file_name:String = "_result_calculate.pdf"
    val MY_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transformer_report)

        val DataFromTransformerActivity = intent.getParcelableArrayListExtra<DataToTransformerReportModel>("DataFromTransformerActivity")!!
        var stringFilePath:String = Environment.getExternalStorageDirectory().path + "/Download/${resources.getString(R.string.app_name)}" + ReportInCurrentActivity.file_name

            textViewReslutPressureInTransformerReport.text = DataFromTransformerActivity[0].pressure
            textViewMainResultInstallationInTransformerReport.text = DataFromTransformerActivity[0].installation
            textViewMainResultCableTypeInTransformerReport.text = DataFromTransformerActivity[0].typecable
            textViewResultSizeTransformerInTransformerReport.text = DataFromTransformerActivity[0].sizetransformer
            textViewReslutAmountCableInPipeReport.text = DataFromTransformerActivity[0].resultpowerrating

            textViewResultConduitSize.text = Html.fromHtml(DataFromTransformerActivity[0].resultsizecable.replace("mm", "mm<sup><small><small>2</small></small></sup>"))
            textViewResultConduitSize2.text = "${DataFromTransformerActivity[0].resultrailsize} mm."

            textViewResultRailSizeInTransformerReport.text = Html.fromHtml(DataFromTransformerActivity[1].resultsizecable.replace("mm", "mm<sup><small><small>2</small></small></sup>"))
            textViewResultRailSizeInTransformerReport2.text = "${DataFromTransformerActivity[1].resultrailsize} mm."

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    if (DataFromTransformerActivity.size > 0 ) {
                        createPDFFile(stringFilePath, DataFromTransformerActivity)
                    }
                }
                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    showSettingsDialog()
                    Toast.makeText(
                        this@TransformerReportActivity,
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

        btnSendEmailInTransformerReport.setOnClickListener {
            try {
                val fileWithinMyDir = File(stringFilePath)

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

    fun createPDFFile(path: String, data: ArrayList<DataToTransformerReportModel>) {
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

            addNewItemWithLeftAndRight(document, "รายงานคำนวณขนาดสายหม้อแปลง", "", titleStyle, detailStyleTitle)
            addLineSeperator(document)
            addNewItem(document, "ข้อมูลการใช้งาน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดแรงดัน : ", data[0].pressure, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                กลุ่มการติดตั้ง : ", data[0].installation, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ชนิดสายไฟฟ้า : ", data[0].typecable, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดหม้อแปลง : ", data[0].sizetransformer, titleStyleTitle, valueStyle)
            addLineSpace(document)

            addNewItem(document, "ผลการคำนวน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)
            addItemAndResult(document, "                พิกัดกระแสไฟ       ",         data[0].resultpowerrating, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ผลการคำนวนที่ 1","", titleStyleTitle, valueStyle)
            document.add( Chunk.NEWLINE )
            addItemAndResult(document, "                ขนาดสายไฟ           ",         data[0].resultsizecable.replace("mm", "mm2"), titleStyleTitle, valueStyle)
            document.add( Chunk.NEWLINE )
            addItemAndResult(document, "                รางขนาด            ", "     ${data[0].resultrailsize} mm.", titleStyleTitle, valueStyle)

            addLineSpace(document)
            document.add( Chunk.NEWLINE )

            addItemAndResult(document, "                ผลการคำนวนที่ 2","", titleStyleTitle, valueStyle)
            document.add( Chunk.NEWLINE )
            addItemAndResult(document, "                ขนาดสายไฟ           ",         data[1].resultsizecable.replace("mm", "mm2"), titleStyleTitle, valueStyle)
            document.add( Chunk.NEWLINE )
            addItemAndResult(document, "                รางขนาด            ", "     ${data[1].resultrailsize} mm.", titleStyleTitle, valueStyle)
            addLineSpace(document)

//
//            addNewItem(document, "* อ้างอิงตามมาตรฐานการติดตั้งทางไฟฟ้า วสท. 2562", Element.ALIGN_LEFT, SubvalueStyle)
//            addNewItem(document, "** ใช้งานที่อุณหภูมิ 36-40 C° และเดินสาย 1 กลุ่มวงจร", Element.ALIGN_LEFT, SubvalueStyle)

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
        val d = resources.getDrawable(R.drawable.temca_logo_mini)
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
            if(textRight.indexOf('/') != -1) p.add(Chunk("$s", rightStyle))
            else p.add(Chunk(textRight, rightStyle))
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