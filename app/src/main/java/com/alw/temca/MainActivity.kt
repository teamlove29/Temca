package com.alw.temca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alw.temca.ui.PipeSize.PipeSizeActivity
import com.alw.temca.ui.SoonActivity
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
                startActivity(intent)
            }
            buttonPipeSize -> {
                val intent = Intent(this,PipeSizeActivity::class.java)
                startActivity(intent)
            }
            button3 -> {
                val intent = Intent(this,SoonActivity::class.java)
                startActivity(intent)
            }
            button4 -> {
                val intent = Intent(this,SoonActivity::class.java)
                startActivity(intent)
            }
            button5 -> {
                val intent = Intent(this,SoonActivity::class.java)
                startActivity(intent)
            }
            button6 -> {
                val intent = Intent(this,SoonActivity::class.java)
                startActivity(intent)
            }
        }
    }
}