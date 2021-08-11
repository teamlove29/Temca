package com.alw.temca

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alw.temca.ui.AmountInPipe.AmountInPipeActivity
import com.alw.temca.ui.AmountInRailCable.AmountInRailCableActivity
import com.alw.temca.ui.AmountInRails.AmountInRailsActivity
import com.alw.temca.ui.CurrentRating.CurrentRatingActivity
import com.alw.temca.ui.ElectricalOnePhase.OnePhaseActivity
import com.alw.temca.ui.ElectricalThreePhase.ThreePhaseActivity
import com.alw.temca.ui.Moter.MoterActivity
import com.alw.temca.ui.SponsorActivity
import com.alw.temca.ui.Transformer.TransformerActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

//        try {
//            val pInfo: PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
//            val version = pInfo.versionName
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //        val sdf = SimpleDateFormat("dd/MM/YYYY")
//        val dayOfTheWeek = sdf.format(timestamp)
        val date:Date = Date()
        val timestamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")

//        if(date.time >= 1629565200000 ){
//            Toast.makeText(this, "หมดเวลาการใช้งาน", Toast.LENGTH_SHORT).show()
//            finish()
//        }

    }

    fun HandleOnClick(view: View) {
        when(view){

            buttonCurrentRating -> {
                val intent = Intent(this, CurrentRatingActivity::class.java)
                intent.putExtra("txt", buttonCurrentRating.text)
                startActivity(intent)
            }
            buttonTypeAndAmountConduit -> {
                val intent = Intent(this, AmountInPipeActivity::class.java)
                startActivity(intent)
            }
            buttonTypeAndAmountRails -> {
                val intent = Intent(this, AmountInRailsActivity::class.java)
                startActivity(intent)
            }
            buttonTypeAndAmountRailCable -> {
                val intent = Intent(this, AmountInRailCableActivity::class.java)
                intent.putExtra("txt", buttonTypeAndAmountRailCable.text)
                startActivity(intent)
            }
            buttonOnePhase -> {
                val intent = Intent(this, OnePhaseActivity::class.java)
                startActivity(intent)
            }
            buttonThreePhase -> {
                val intent = Intent(this, ThreePhaseActivity::class.java)
                startActivity(intent)
            }
            buttonMoter -> {
                val intent = Intent(this, MoterActivity::class.java)
                startActivity(intent)
            }
            buttonTransformer -> {
                val intent = Intent(this, TransformerActivity::class.java)
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
        val intent = Intent(this, SponsorActivity::class.java)
        startActivity(intent)
    }

    fun termsOfUseOnClick(view: View) {
        val intent = Intent(this, TermsOfUseActivity::class.java)
        startActivity(intent)
    }

    fun backOnClick(view: View) {}
}
