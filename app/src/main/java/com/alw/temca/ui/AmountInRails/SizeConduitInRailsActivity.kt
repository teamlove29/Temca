package com.alw.temca.ui.AmountInRails

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.AdapterInRails.SizeConduitAdapter
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.SizeConduitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_size_conduit_in_rails.*
import kotlin.properties.Delegates


class SizeConduitInRailsActivity : AppCompatActivity(),onClickInAdapter {
    private val listConduit = ArrayList<SizeConduitModel>()
    private val listConduit2 = ArrayList<SizeConduitModel>()
    private var startCol by Delegates.notNull<Int>()
    private var endCol by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size_conduit_in_rails)

        listConduit.add(SizeConduitModel("50x75 mm.")) // 0
        listConduit.add(SizeConduitModel("50x100 mm.")) // 1
        listConduit.add(SizeConduitModel("75x100 mm.")) // 2
        listConduit.add(SizeConduitModel("100x100 mm.")) // 3
        listConduit.add(SizeConduitModel("100x150 mm.")) // 4
        listConduit.add(SizeConduitModel("100x200 mm.")) // 5
        listConduit.add(SizeConduitModel("100x250 mm.")) // 6
        listConduit.add(SizeConduitModel("100x300 mm.")) // 7
        listConduit.add(SizeConduitModel("150x300 mm.")) // 8

        val typeCableOfMain = intent.getStringExtra("typeCable").toString()
        val sizeCableOfMain = intent.getStringExtra("sizeCable").toString()

    println("Dasdad ${sizeCableOfMain}")

//        "1 mm2" -> 99
//        "1.5 mm2" -> 99
//        "2.5 mm2" -> 0
//        "4 mm2" -> 0
//        "6 mm2" -> 0
//        "10 mm2" -> 0
//        "16 mm2" -> 0
//        "25 mm2" -> 0
//        "35 mm2" -> 0
//        "50 mm2" -> 1
//        "70 mm2" -> 4
//        "95 mm2" -> 4
//        "120 mm2" -> 6
//        "150 mm2" -> 6
//        "185 mm2" -> 8
//        "240 mm2" -> 8
//        "300 mm2" -> 8
//        "400 mm2" -> 99
//        "500 mm2" -> 99

        if(typeCableOfMain == "IEC01"){
            // เริ่มจาก col ไหน
            startCol = when(sizeCableOfMain){
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" -> 0
                "25 mm2" -> 0
                "35 mm2" -> 0
                "50 mm2" -> 1
                "70 mm2" -> 4
                "95 mm2" -> 4
                "120 mm2" -> 6
                "150 mm2" -> 6
                "185 mm2" -> 8
                "240 mm2" -> 8
                "300 mm2" -> 8
                else -> 99
            }
            endCol = when(sizeCableOfMain){
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 1
                "10 mm2" -> 1
                "16 mm2" -> 3
                "25 mm2" -> 3
                "35 mm2" -> 4
                "50 mm2" -> 6
                "70 mm2" -> 7
                "95 mm2" -> 8
                "120 mm2" -> 8
                "150 mm2" -> 8
                "185 mm2" -> 8
                "240 mm2" -> 8
                "300 mm2" -> 8
                else -> 99
            }
        }
        else if(typeCableOfMain == "NYY 1/C"){
            // เริ่มจาก col ไหน
            startCol = when(sizeCableOfMain){
                "1 mm2" -> 0
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 1
                "6 mm2" -> 2
                "10 mm2" -> 2
                "16 mm2" -> 3
                "25 mm2" -> 4
                "35 mm2" -> 5
                "50 mm2" -> 5
                "70 mm2" -> 6
                "95 mm2" -> 7
                "120 mm2" -> 8
                "150 mm2" -> 8
                "185 mm2" -> 8
                else -> 99
            }
            endCol = when(sizeCableOfMain){
                "1 mm2" -> 3
                "1.5 mm2" -> 3
                "2.5 mm2" -> 4
                "4 mm2" -> 4
                "6 mm2" -> 4
                "10 mm2" -> 5
                "16 mm2" -> 6
                "25 mm2" -> 7
                "35 mm2" -> 8
                "50 mm2" -> 8
                "70 mm2" -> 8
                "95 mm2" -> 8
                "120 mm2" -> 8
                "150 mm2" -> 8
                "185 mm2" -> 8
                else -> 99
            }
        }
        else if(typeCableOfMain == "NYY 2/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1 mm2" -> 0
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" -> 0
                "25 mm2" -> 0
                "35 mm2" -> 0
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1 mm2" -> 4
                "1.5 mm2" -> 4
                "2.5 mm2" -> 5
                "4 mm2" -> 5
                "6 mm2" -> 6
                "10 mm2" -> 7
                "16 mm2" -> 8
                "25 mm2" -> 8
                "35 mm2" -> 8
                else -> 99
            }
        }
        else if(typeCableOfMain == "NYY 3/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1 mm2" -> 0
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" -> 0
                "25 mm2" -> 0
                "35 mm2" -> 0
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1 mm2" -> 4
                "1.5 mm2" -> 4
                "2.5 mm2" -> 5
                "4 mm2" -> 6
                "6 mm2" -> 7
                "10 mm2" -> 8
                "16 mm2" -> 8
                "25 mm2" -> 8
                "35 mm2" -> 8
                else -> 99
            }
        }
        else if(typeCableOfMain == "NYY 4/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1 mm2" -> 0
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" -> 0
                "25 mm2" -> 2
                "35 mm2" -> 2
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1 mm2" -> 5
                "1.5 mm2" -> 5
                "2.5 mm2" -> 6
                "4 mm2" -> 6
                "6 mm2" -> 7
                "10 mm2" -> 8
                "16 mm2" -> 8
                "25 mm2" -> 8
                "35 mm2" -> 8
                else -> 99
            }

        }
        else if(typeCableOfMain == "XLPE 1/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 1
                "10 mm2" -> 1
                "16 mm2" -> 2
                "25 mm2" -> 2
                "35 mm2" -> 3
                "50 mm2" -> 4
                "70 mm2" -> 5
                "95 mm2" -> 5
                "120 mm2" -> 6
                "150 mm2" -> 7
                "185 mm2" -> 8
                "240 mm2" -> 8
                "300 mm2" -> 8
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1.5 mm2" -> 1
                "2.5 mm2" -> 1
                "4 mm2" -> 2
                "6 mm2" -> 2
                "10 mm2" -> 3
                "16 mm2" -> 4
                "25 mm2" -> 4
                "35 mm2" -> 5
                "50 mm2" -> 6
                "70 mm2" -> 7
                "95 mm2" -> 8
                "120 mm2" -> 8
                "150 mm2" -> 8
                "185 mm2" -> 8
                "240 mm2" -> 8
                "300 mm2" -> 8
                else -> 99
            }

        }
        else if(typeCableOfMain == "XLPE 2/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 1
                "10 mm2" -> 0
                "16 mm2" ->0
                "25 mm2" -> 0
                "35 mm2" -> 0
                "50 mm2" -> 0
                "70 mm2" -> 0
                "95 mm2" -> 1
                "120 mm2" -> 2
                "150 mm2" -> 2
                "185 mm2" -> 3
                "240 mm2" -> 4
                "300 mm2" -> 4
                "400 mm2" -> 5
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1.5 mm2" -> 2
                "2.5 mm2" -> 3
                "4 mm2" -> 3
                "6 mm2" -> 4
                "10 mm2" -> 4
                "16 mm2" -> 5
                "25 mm2" -> 6
                "35 mm2" -> 8
                "50 mm2" -> 8
                "70 mm2" -> 8
                "95 mm2" -> 8
                "120 mm2" -> 8
                "150 mm2" -> 8
                "185 mm2" -> 8
                "240 mm2" -> 8
                "300 mm2" -> 8
                "400 mm2" -> 8
                else -> 99
            }

        }
        else if(typeCableOfMain == "XLPE 3/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 1
                "10 mm2" -> 0
                "16 mm2" ->0
                "25 mm2" -> 0
                "35 mm2" -> 0
                "50 mm2" -> 0
                "70 mm2" -> 1
                "95 mm2" -> 1
                "120 mm2" -> 2
                "150 mm2" -> 3
                "185 mm2" -> 3
                "240 mm2" -> 4
                "300 mm2" -> 4
                "400 mm2" -> 5
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1.5 mm2" -> 2
                "2.5 mm2" -> 3
                "4 mm2" -> 3
                "6 mm2" -> 4
                "10 mm2" -> 4
                "16 mm2" -> 5
                "25 mm2" -> 6
                "35 mm2" -> 8
                "50 mm2" -> 8
                "70 mm2" -> 8
                "95 mm2" -> 8
                "120 mm2" -> 8
                "150 mm2" -> 8
                "185 mm2" -> 8
                "240 mm2" -> 8
                "300 mm2" -> 8
                "400 mm2" -> 8
                else -> 99
            }

        }
        else if(typeCableOfMain == "XLPE 4/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" ->0
                "25 mm2" -> 0
                "35 mm2" -> 0
                "50 mm2" -> 0
                "70 mm2" -> 1
                "95 mm2" -> 1
                "120 mm2" -> 2
                "150 mm2" -> 3
                "185 mm2" -> 3
                "240 mm2" -> 4
                "300 mm2" -> 4
                "400 mm2" -> 5
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1.5 mm2" -> 2
                "2.5 mm2" -> 3
                "4 mm2" -> 3
                "6 mm2" -> 4
                "10 mm2" -> 4
                "16 mm2" -> 5
                "25 mm2" -> 6
                "35 mm2" -> 8
                "50 mm2" -> 8
                "70 mm2" -> 8
                "95 mm2" -> 8
                "120 mm2" -> 8
                "150 mm2" -> 8
                "185 mm2" -> 8
                "240 mm2" -> 8
                "300 mm2" -> 8
                "400 mm2" -> 8
                else -> 99
            }

        }
        else if(typeCableOfMain == "VCT 1/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1 mm2" -> 0
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" ->0
                "25 mm2" -> 0
                "35 mm2" -> 0
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1 mm2" -> 2
                "1.5 mm2" -> 3
                "2.5 mm2" -> 3
                "4 mm2" -> 4
                "6 mm2" -> 5
                "10 mm2" -> 6
                "16 mm2" -> 7
                "25 mm2" -> 8
                "35 mm2" -> 8
                else -> 99
            }

        }
        else if(typeCableOfMain == "VCT 2/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1 mm2" -> 0
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" ->0
                "25 mm2" -> 1
                "35 mm2" -> 2
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1 mm2" -> 4
                "1.5 mm2" -> 5
                "2.5 mm2" -> 6
                "4 mm2" -> 7
                "6 mm2" -> 7
                "10 mm2" -> 8
                "16 mm2" -> 8
                "25 mm2" -> 8
                "35 mm2" -> 8
                else -> 99
            }

        }
        else if(typeCableOfMain == "VCT 3/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1 mm2" -> 0
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" ->1
                "25 mm2" -> 1
                "35 mm2" -> 2
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1 mm2" -> 5
                "1.5 mm2" -> 6
                "2.5 mm2" -> 7
                "4 mm2" -> 8
                "6 mm2" -> 8
                "10 mm2" -> 8
                "16 mm2" -> 8
                "25 mm2" -> 8
                "35 mm2" -> 8
                else -> 99
            }

        }
        else if(typeCableOfMain == "VCT 4/C") {
            // เริ่มจาก col ไหน
            startCol = when (sizeCableOfMain) {
                "1 mm2" -> 0
                "1.5 mm2" -> 0
                "2.5 mm2" -> 0
                "4 mm2" -> 0
                "6 mm2" -> 0
                "10 mm2" -> 0
                "16 mm2" ->1
                "25 mm2" -> 1
                "35 mm2" -> 2
                else -> 99
            }
            endCol = when (sizeCableOfMain) {
                "1 mm2" -> 5
                "1.5 mm2" -> 6
                "2.5 mm2" -> 7
                "4 mm2" -> 8
                "6 mm2" -> 8
                "10 mm2" -> 8
                "16 mm2" -> 8
                "25 mm2" -> 8
                "35 mm2" -> 8
                else -> 99
            }

        }

        if (startCol == 99 || endCol == 99){
            Toast.makeText(this,"Warning : ไม่มีค่าสำหรับชนิดสายนี้",Toast.LENGTH_SHORT).show()
            finish()
        }


        for(i in startCol..endCol){
            listConduit2.add(listConduit[i])
        }

        recyclerViewSizeConduit.adapter = SizeConduitAdapter(listConduit2,this)
        recyclerViewSizeConduit.layoutManager = LinearLayoutManager(this)

    }
    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataSizeConduit",listConduit2[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}