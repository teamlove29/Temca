package com.alw.temca.ui


import android.R.attr.spacing
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.temca.Adapter.InstallationonClickAdapterListener
import com.alw.temca.Adapter.SponsorAdapter
import com.alw.temca.Model.SponsorModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.activity_sponsor.*


class SponsorActivity : AppCompatActivity(), InstallationonClickAdapterListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsor)

        val sponsor1 =  SponsorModel(1, resources.getDrawable(R.drawable.sponser1))
        val sponsor2 =  SponsorModel(2, resources.getDrawable(R.drawable.sponser2))
        val sponsor3 =  SponsorModel(3, resources.getDrawable(R.drawable.sponser3))
        val sponsor4 =  SponsorModel(4, resources.getDrawable(R.drawable.sponser4))
        val sponsor5 =  SponsorModel(5, resources.getDrawable(R.drawable.sponser5))
        val sponsor6 =  SponsorModel(6, resources.getDrawable(R.drawable.sponser6))
        val sponsor7 =  SponsorModel(7, resources.getDrawable(R.drawable.sponser7))
        val sponsor8 =  SponsorModel(8, resources.getDrawable(R.drawable.sponser8))
        val sponsor9 =  SponsorModel(9, resources.getDrawable(R.drawable.sponser9))
        val sponsor10 =  SponsorModel(10, resources.getDrawable(R.drawable.sponser10))
        val sponsor11 =  SponsorModel(11, resources.getDrawable(R.drawable.sponser11))
        val sponsor12 =  SponsorModel(12, resources.getDrawable(R.drawable.sponser12))
        val sponsor13 =  SponsorModel(13, resources.getDrawable(R.drawable.sponser13))
        val sponsor14 =  SponsorModel(14, resources.getDrawable(R.drawable.sponser14))
        val sponsor15 =  SponsorModel(15, resources.getDrawable(R.drawable.sponser15))
        val sponsor16 =  SponsorModel(16, resources.getDrawable(R.drawable.sponser16))
        val sponsor17 =  SponsorModel(17, resources.getDrawable(R.drawable.sponser17))
        val sponsor18 =  SponsorModel(18, resources.getDrawable(R.drawable.sponser18))
        val sponsor19 =  SponsorModel(18, resources.getDrawable(R.drawable.sponser19))
        val sponsor20 =  SponsorModel(20, resources.getDrawable(R.drawable.sponsor20))
        val sponsor21 =  SponsorModel(21, resources.getDrawable(R.drawable.sponsor21))
        val sponsor22 =  SponsorModel(22, resources.getDrawable(R.drawable.sponser22))
        val sponsor23 =  SponsorModel(23, resources.getDrawable(R.drawable.sponser23))
        val sponsor24 =  SponsorModel(24, resources.getDrawable(R.drawable.sponser1))

        val listSponsor = arrayListOf<SponsorModel>(
                sponsor1,
                sponsor2,
                sponsor3,
                sponsor4,
                sponsor5,
                sponsor6,
                sponsor7,
                sponsor8,
                sponsor9,
                sponsor10,
                sponsor11,
                sponsor12,
                sponsor13,
                sponsor14,
                sponsor15,
                sponsor16,
                sponsor17,
                sponsor18,
                sponsor19,
                sponsor20,
                sponsor21,
                sponsor22,
                sponsor23,
                sponsor24,
        )

        val listSponsor2 = ArrayList<SponsorModel>()

        for (i in 0 until 24){
            val uniqueRand = listSponsor.random()
            listSponsor2.add(uniqueRand)
            listSponsor.remove(uniqueRand)
        }

        val checkScape = resources.configuration.orientation

        recyclerViewSponsor.adapter = SponsorAdapter(listSponsor2, this)
        if (checkScape == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerViewSponsor.layoutManager = GridLayoutManager(this, 9)
        } else {
            recyclerViewSponsor.layoutManager = GridLayoutManager(this, 4)
        }
        recyclerViewSponsor.isNestedScrollingEnabled = false
    }

    override fun onClick(postion: Int) {
        val url = "https://www.temcathai.com/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    fun backOnClick(view: View) {finish()}
}