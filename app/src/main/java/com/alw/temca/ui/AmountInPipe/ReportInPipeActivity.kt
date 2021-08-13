package com.alw.temca.ui.AmountInPipe

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.alw.temca.Common.ApplicationSelectorReceiver
import com.alw.temca.Common.Common
import com.alw.temca.Model.AmountInPipe.ResultPipeToReportModel
import com.alw.temca.R
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_moter_report.*
import kotlinx.android.synthetic.main.activity_report_in_pipe.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ReportInPipeActivity : AppCompatActivity() {

    companion object{
        const val file_name:String = "_result_calculate.pdf"
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_in_pipe)

        val resultInPipe = intent.getParcelableExtra<ResultPipeToReportModel>("resultInPipe")


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        if (resultInPipe != null) {
                            createPDFFile(Common.getAppPath(this@ReportInPipeActivity) + file_name, resultInPipe)
                        }
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                                this@ReportInPipeActivity,
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



        if(resultInPipe != null){
            textViewReslutMainCableTypeInPipeReport.text = resultInPipe.typeCable
            textViewMainResultInstallationInReport.text = Html.fromHtml( resultInPipe.sizeCable.replace("mm2","mm<sup><small>2</small></sup>"))
            textViewMainResultBreakerInReportData.text = resultInPipe.amountCable
            textViewResultConduitSize.text = resultInPipe.maxConduit
//            textViewResultRailSizeInReportPipeSize.text = resultInPipe.maxRails
        }

        btnSendEmailInPipeSize.setOnClickListener {
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
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "รายงานคำนวนชนิดและจำนวนสายในท่อ")
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

    fun createPDFFile(path: String, data: ResultPipeToReportModel?) {
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
            document.isMarginMirroring = true
            // Font setting
            val colorAccent = BaseColor(14, 65, 148, 255)
            val headingFontSize = 12.0f
            val valueFontSize = 12.0f
            val subValueFontSize = 10.0f

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
            var valueStyle = Font(fontName, valueFontSize, Font.NORMAL, colorAccent)

            addNewItemWithLeftAndRight(document, "รายงานคำนวนชนิดและจำนวนสายในท่อ", "", titleStyle, detailStyleTitle)
            addLineSeperator(document)

            addNewItem(document, "ข้อมูลการใช้งาน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)

            addItemAndResult(document, "                ชนิดสายไฟ : ", data!!.typeCable, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดสาย :   ", data.sizeCable, titleStyleTitle, valueStyle)
            addLineSpace(document)

            addItemAndResult(document, "                จำนวนเส้น :   ", data.amountCable, titleStyleTitle, valueStyle)
            addLineSpace(document)

            addNewItem(document, "ผลการคำนวน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)

            addItemAndResult(document, "                ขนาดท่อ                    ", data.maxConduit, titleStyleTitle, valueStyle)
            addItemAndResult(document, "                   (จำนวนสูงสุด)", "", subTitleStyle, valueStyle)
            addLineSpace(document)
//            addItemAndResult(document, "                ขนาดราง                    ", data.maxRails, titleStyleTitle, valueStyle)
//            addItemAndResult(document, "                   (จำนวนสูงสุด)", "", subTitleStyle, valueStyle)
//            addLineSpace(document)

            document.close()

        }catch (e: Exception){
            println("Error createPDFFile : $e")
        }
    }

    @Throws(DocumentException::class)
    fun addItemAndResult(document: Document, textLeft: String, textRight: String, leftStyle: Font, rightStyle: Font){
        val p = Paragraph()
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
            rightStyle: Font,
    ) {
        //add Image
        val d = resources.getDrawable(R.drawable.logo_pdf_temca)
        val bitDw = d as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image: Image = Image.getInstance(stream.toByteArray())
        var chunkTextRight = if (textLeft == "รายงานคำนวนชนิดและจำนวนสายในท่อ"){
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