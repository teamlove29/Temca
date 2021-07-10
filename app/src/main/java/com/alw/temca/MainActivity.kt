package com.alw.temca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alw.temca.ui.AmountInPipe.AmountInPipeActivity
import com.alw.temca.ui.AmountInRails.AmountInRailsActivity
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
import com.alw.temca.ui.ElectricalThreePhase.ThreePhaseActivity
import com.alw.temca.ui.Moter.MoterActivity
import com.alw.temca.ui.SoonActivity
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.Transformer.TransformerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun HandleOnClick(view: View) {
        when(view){

            buttonCurrentRating ->{
                val intent = Intent(this,SoonActivity::class.java)
                startActivity(intent)
            }
            buttonTypeAndAmountConduit ->{
                val intent = Intent(this,AmountInPipeActivity::class.java)
                startActivity(intent)
            }
            buttonTypeAndAmountRails ->{
                val intent = Intent(this,AmountInRailsActivity::class.java)
                startActivity(intent)
            }
            buttonOnePhase -> {
                val intent = Intent(this,OnePhaseActivity::class.java)
                startActivity(intent)
            }
            buttonThreePhase -> {
                val intent = Intent(this, ThreePhaseActivity::class.java)
                startActivity(intent)
            }
            buttonTransformer -> {
                val intent = Intent(this,TransformerActivity::class.java)
//                intent.putExtra("txt",buttonTransformer.text)
                startActivity(intent)
            }
            buttonMoter -> {
                val intent = Intent(this,MoterActivity::class.java)
//                intent.putExtra("txt",buttonMoter.text)
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
