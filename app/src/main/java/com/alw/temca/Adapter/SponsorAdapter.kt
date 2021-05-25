package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.SponsorModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.sponsor_list.view.*


class SponsorAdapter(var listSponsor: ArrayList<SponsorModel>, var onClickTypeCable: InstallationonClickAdapterListener) : RecyclerView.Adapter<SponsorAdapter.sponsorViewHolder>(){
    class sponsorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemList: SponsorModel, action: InstallationonClickAdapterListener){

            if(itemList.index >=15){
                 itemView.textViewSponser.text = "Sponsor ${itemList.index}"
            }else{
                itemView.logoSponsor.setImageDrawable(itemList.image)
            }

            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sponsorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sponsor_list, parent, false)
        return sponsorViewHolder(view)
    }

    override fun onBindViewHolder(holder: sponsorViewHolder, position: Int) {
        val currency = listSponsor[position]
        holder.bind(currency, onClickTypeCable)
    }

    override fun getItemCount(): Int {
        return listSponsor.size
    }
}