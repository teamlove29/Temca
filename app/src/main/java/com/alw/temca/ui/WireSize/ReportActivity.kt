package com.alw.temca.ui.WireSize

import android.Manifest
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import com.alw.temca.Common.ApplicationSelectorReceiver
import com.alw.temca.Common.Common
import com.alw.temca.Model.ReportReslutPipeSizeModel
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class ReportActivity : AppCompatActivity() {
    val file_name:String = "_result_calculate.pdf"
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val resultWire = intent.getParcelableExtra<ReportResultWireSize>("reportWireSize")

        if(resultWire != null){
            textViewResultWireSize.text = resultWire.cableSize
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
                startActivityForResult(Intent.createChooser(sendIntent, "SHARE", pendingIntent.intentSender), 800)
            } catch (e: Exception) {
                Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
//                println("Error : $e")
            }
        }

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
            val colorAccent = BaseColor(0, 153, 204, 255)
            val headingFontSize = 26.0f
            val valueFontSzie = 26.0f
            val SubvalueFontSzie = 20.0f

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
            val titleStyleTitle = Font(fontNameBoldStyle, 30.0f, Font.NORMAL, BaseColor.BLACK)
            val titleStyle = Font(fontName, 26.0f, Font.NORMAL, BaseColor.BLACK)
            val headingStyle = Font(fontName, headingFontSize, Font.NORMAL, BaseColor.BLACK)
            var valueStyle = Font(fontName, valueFontSzie, Font.NORMAL, BaseColor.BLACK)
            var SubvalueStyle = Font(fontName, SubvalueFontSzie, Font.NORMAL, BaseColor.BLACK)

            val title = "CableSize                                    "
            addNewItemWithLeftAndRight(document, "", title, titleStyle, titleStyleTitle)
                addLineSpace(document)
                addLineSeperator(document)
                addNewItem(document, "รายงานผลการคำนวณสาย", Element.ALIGN_CENTER, titleStyleTitle)
                addLineSpace(document)

                // cableSize
                addNewItemWithLeftAndRight(document, "ขนาดสายไฟ", "${data!!.cableSize}", titleStyle, headingStyle)
                addLineSpace(document)

                    //amount
                    addNewItemWithLeftAndRight(document, "ขนาดท่อร้อยสาย(ราง)", "${data.condutiSize}", titleStyle, headingStyle)

                    //conduit Size
                    addNewItemWithLeftAndRight(document, "แรงดันตก", "${data.pressure}", titleStyle, headingStyle)

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
        val d = resources.getDrawable(R.drawable.temca_logo_mini)
        val bitDw = d as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        bmp.scale(200, 500)
        val image: Image = Image.getInstance(stream.toByteArray())

//        document.add(image)

        var chunkTextLeft = if (textRight == "CableSize                                    "){
            Chunk(image, 25.0F, -10.0F, true)
        }else{
            Chunk(textLeft, leftStyle)
        }

//        val chunkTextLeft = Chunk(textLeft, leftStyle)
        val chunkTextRight = Chunk(textRight, rightStyle)

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

}