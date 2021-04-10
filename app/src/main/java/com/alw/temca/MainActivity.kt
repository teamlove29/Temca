package com.alw.temca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alw.temca.ui.PipeSize.PipeSizeActivity
import com.alw.temca.ui.SoonActivity
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.Transformer.TransformerActivity
import com.alw.temca.ui.WireSize.WireSizeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun HandleOnClick(view: View) {
        when(view){
            buttonWireSize ->{
                val intent = Intent(this,WireSizeActivity::class.java)
                intent.putExtra("code",true)
                startActivity(intent)
            }
            buttonPipeSize -> {
                val intent = Intent(this,PipeSizeActivity::class.java)
                startActivity(intent)
            }
            buttonTransformer -> {
                val intent = Intent(this,TransformerActivity::class.java)
//                intent.putExtra("txt",buttonTransformer.text)
                startActivity(intent)
            }
            button4 -> {
                val intent = Intent(this,SoonActivity::class.java)
                intent.putExtra("txt",button4.text)
                startActivity(intent)
            }
            button5 -> {
                val intent = Intent(this,SoonActivity::class.java)
                intent.putExtra("txt",button5.text)
                startActivity(intent)
            }
            button6 -> {
                val intent = Intent(this,SoonActivity::class.java)
                intent.putExtra("txt",button6.text)
                startActivity(intent)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            Toast.makeText(this, "SUCCESS !", Toast.LENGTH_LONG).show()
        }
    }


    fun sponsorOnClick(view: View) {
        val intent = Intent(this,SponsorActivity::class.java)
        startActivity(intent)
    }
}
