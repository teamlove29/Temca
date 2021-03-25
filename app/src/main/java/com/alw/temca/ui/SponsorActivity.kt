package com.alw.temca.ui


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val sponsor1 =  SponsorModel(1,resources.getDrawable(R.drawable.temca_logo_mini))
        val sponsor2 =  SponsorModel(2,resources.getDrawable(R.drawable.temca_logo))
        val sponsor3 =  SponsorModel(3,resources.getDrawable(R.drawable.temca_logo_2))
        val sponsor4 =  SponsorModel(4,resources.getDrawable(R.drawable.logo_pdf_temca))
        val sponsor5 =  SponsorModel(5,resources.getDrawable(R.drawable.temca_logo_mini))
        val sponsor6 =  SponsorModel(6,resources.getDrawable(R.drawable.temca_logo_mini))
        val sponsor7 =  SponsorModel(7,resources.getDrawable(R.drawable.temca_logo_mini))
        val sponsor8 =  SponsorModel(8,resources.getDrawable(R.drawable.logo_pdf_temca))
        val sponsor9 =  SponsorModel(9,resources.getDrawable(R.drawable.temca_logo_mini))
        val sponsor10 =  SponsorModel(10,resources.getDrawable(R.drawable.logo_pdf_temca))
        val sponsor11 =  SponsorModel(11,resources.getDrawable(R.drawable.temca_logo_mini))
        val sponsor12 =  SponsorModel(12,resources.getDrawable(R.drawable.temca_logo_mini))
//        val test =  SponsorModel(null)
        val listSponsor = arrayListOf<SponsorModel>(sponsor1,sponsor2,sponsor3,sponsor4,sponsor5,sponsor6,sponsor7,sponsor8,sponsor9,sponsor10,sponsor11,sponsor12)
        val listSponsor2 = ArrayList<SponsorModel>()


        for (i in 0 until 12){
            val uniqueRand = listSponsor.random()
            listSponsor2.add(uniqueRand)
            listSponsor.remove(uniqueRand)
        }

        recyclerViewSponsor.adapter = SponsorAdapter(listSponsor2,this)
        recyclerViewSponsor.layoutManager = GridLayoutManager(this,3)



    }


    override fun onClick(postion: Int) {
        val url = "https://www.temcathai.com/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}