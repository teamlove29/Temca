package com.alw.temca.ui.Transformer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.TransformerSizeAdapter
import com.alw.temca.Model.TransformerSizeModal
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_phase.recyclerViewPhase
import kotlinx.android.synthetic.main.activity_transformer_size.*

class TransformerSizeActivity : AppCompatActivity() , InstallationonClickAdapterListener {
    val TransformerSizeItem = ArrayList<TransformerSizeModal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transformer_size)

        TransformerSizeItem.add(TransformerSizeModal("315 kVA"))
        TransformerSizeItem.add(TransformerSizeModal("400 kVA"))
        TransformerSizeItem.add(TransformerSizeModal("500 kVA"))
        TransformerSizeItem.add(TransformerSizeModal("630 kVA"))
        TransformerSizeItem.add(TransformerSizeModal("800 kVA"))
        TransformerSizeItem.add(TransformerSizeModal("1000 kVA"))
        TransformerSizeItem.add(TransformerSizeModal("1250 kVA"))
        TransformerSizeItem.add(TransformerSizeModal("1600 kVA"))
        TransformerSizeItem.add(TransformerSizeModal("2000 kVA"))


        recyclerViewTransformerSize.adapter = TransformerSizeAdapter(TransformerSizeItem,this)
        recyclerViewTransformerSize.layoutManager = LinearLayoutManager(this)
//        recyclerViewTransformerSize.hasFixedSize()

    }

    override fun onClick(postion: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra("dataTransformerSize",TransformerSizeItem[postion].name)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}