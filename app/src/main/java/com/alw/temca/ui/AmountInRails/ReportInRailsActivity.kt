package com.alw.temca.ui.AmountInRails

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.alw.temca.Common.ApplicationSelectorReceiver
import com.alw.temca.Common.Common
import com.alw.temca.Model.AmountInRails.ResultRailsToReportModel
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
import kotlinx.android.synthetic.main.activity_report_in_rails.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ReportInRailsActivity : AppCompatActivity() {

    companion object{
        const val file_name:String = "_result_calculate.pdf"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_in_rails)

        val resultInRails = intent.getParcelableExtra<ResultRailsToReportModel>("resultInRails")
        var stringFilePath:String = Environment.getExternalStorageDirectory().path + "/Download/${resources.getString(R.string.app_name)}" + ReportInCurrentActivity.file_name

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        if (resultInRails != null) {
                            createPDFFile(
                                stringFilePath, resultInRails
                            )
                        }
                    }
                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                                this@ReportInRailsActivity,
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

        if (resultInRails != null) {
            if(!resultInRails.status){
                textViewReslutMainCableTypeInPipeReport.text = resultInRails.typeCable
                textViewMainResultInstallationInReport.text = Html.fromHtml(resultInRails.sizeCable.replace("mm2","mm<sup><small>2</small></sup>"))
                textViewMainResultCableTypeInReport.text = resultInRails.sizeRails
                textViewReslutAmountCableInPipeReport.text = resultInRails.amountCable

                amountCableInReport.visibility = View.VISIBLE
                railsInReport.visibility = View.VISIBLE
                railsInReport2.visibility = View.GONE
                amountInReport.visibility = View.GONE
                railsInReport3.visibility = View.GONE
            }else{
                textViewReslutMainCableTypeInPipeReport.text = resultInRails.typeCable
                textViewMainResultInstallationInReport.text =  Html.fromHtml(resultInRails.sizeCable.replace("mm2","mm<sup><small>2</small></sup>"))
                textViewMainResultBreakerInReportData.text = "${resultInRails.amount} เส้น"
                textViewReslutAmountCableInPipeReport2.text = resultInRails.rails
                textViewReslutAmountCableInPipeReport3.text = resultInRails.amountCable


                amountCableInReport.visibility = View.GONE
                railsInReport.visibility = View.GONE
                railsInReport2.visibility = View.VISIBLE
                railsInReport3.visibility = View.VISIBLE
                amountInReport.visibility = View.VISIBLE
            }

        }


        btnSendEmailInPipeSize.setOnClickListener {
            try {
                val fileWithinMyDir = File(stringFilePath)

                val uri = FileProvider.getUriForFile(this, this.packageName.toString() + ".fileprovider", fileWithinMyDir)
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



    fun createPDFFile(path: String, data: ResultRailsToReportModel?) {
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
            val detailStyleTitle = Font(fontNameBoldStyle, 12.0f, Font.NORMAL, colorAccent)
            val titleStyle = Font(fontName, 18.0f, Font.NORMAL, BaseColor.BLACK)
            val headingStyle = Font(fontName, headingFontSize, Font.BOLD, BaseColor.BLACK)
            var valueStyle = Font(fontName, valueFontSize, Font.NORMAL, colorAccent)


            addNewItemWithLeftAndRight(document, "รายงานการคำนวนชนิดและจำนวน", "", titleStyle, detailStyleTitle)
            addNewItemWithLeftAndRight(document, "สายในรางเดินสาย (Wireway)", "", titleStyle, detailStyleTitle)
            addLineSeperator(document)

            addNewItem(document, "ข้อมูลการใช้งาน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)

            addItemAndResult(document, "                ชนิดสายไฟ : ", data!!.typeCable, titleStyleTitle, valueStyle)
            addLineSpace(document)
            addItemAndResult(document, "                ขนาดสาย :   ", data.sizeCable, titleStyleTitle, valueStyle)
            addLineSpace(document)
            if(!data.status){
                addItemAndResult(document, "                ขนาดราง :   ", data.sizeRails, titleStyleTitle, valueStyle)
                addLineSpace(document)
            }else{
                addItemAndResult(document, "                จำนวนสาย :   ", "${data.amount} เส้น", titleStyleTitle, valueStyle)
                addLineSpace(document)
            }


            addNewItem(document, "ผลการคำนวน", Element.ALIGN_LEFT, headingStyle)
            addLineSpace(document)

            if(!data.status){
                addItemAndResult(document, "                จำนวนสายไฟฟ้าสูงสุด               ", data.amountCable, titleStyleTitle, valueStyle)
                addLineSpace(document)
            }else{
                addItemAndResult(document, "                ขนาดราง                                  ", data.rails, titleStyleTitle, valueStyle)
                addLineSpace(document)
                addItemAndResult(document, "                จำนวนสายไฟฟ้าสูงสุด             ", data.amountCable, titleStyleTitle, valueStyle)
                addLineSpace(document)
            }




            //close
            document.close()

        }catch (e: Exception){
            println("Error : $e")
        }
    }

    @Throws(DocumentException::class)
    fun addItemAndResult(document: Document, textLeft: String, textRight: String, leftStyle: Font, rightStyle: Font){
        val glue =  Chunk(VerticalPositionMark())
        val p = Paragraph()
        p.add(Chunk(textLeft, leftStyle))
        if (textRight.indexOf("mm2") > 1){
            p.add(Chunk(textRight.replace("mm2", "mm²"), rightStyle))
        }else{
            p.add(Chunk(textRight, rightStyle))
        }

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
        val d = resources.getDrawable(R.drawable.temca_logo_mini)
        val bitDw = d as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image: Image = Image.getInstance(stream.toByteArray())
        var chunkTextRight = if (textLeft == "รายงานการคำนวนชนิดและจำนวน"){
            Chunk(image, 0F, -35F, true)
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