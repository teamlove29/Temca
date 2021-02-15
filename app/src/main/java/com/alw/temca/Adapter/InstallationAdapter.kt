package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.InstallationModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.installation_list.view.*


interface InstallationonClickAdapterListener{
    fun onClick(postion: Int)
}


class InstallationAdapter(var InstallList : ArrayList<InstallationModel>, var onClickInstall:InstallationonClickAdapterListener): RecyclerView.Adapter<InstallationAdapter.InstallViewHolder>() {
    class InstallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: InstallationModel, action: InstallationonClickAdapterListener){
            itemView.textViewInstallation.text = "${itemList.name}"

            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstallViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.installation_list,parent,false)
        return InstallViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstallViewHolder, position: Int) {
        val currency = InstallList[position]
        holder.bind(currency,onClickInstall)

    }

    override fun getItemCount(): Int {
        return InstallList.size
    }
}