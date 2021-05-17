package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.SizeMoterModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.size_moter_list.view.*


class SizeMoterAdapter(var listSizeMoter : ArrayList<SizeMoterModel>, var onClickTypeCable:InstallationonClickAdapterListener) : RecyclerView.Adapter<SizeMoterAdapter.SizeMoterViewHolder>(){
    class SizeMoterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: SizeMoterModel, action: InstallationonClickAdapterListener){
            itemView.textViewSizeMoter.text = "${itemList.amount} ${itemList.unit}"
            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeMoterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.size_moter_list,parent,false)
        return SizeMoterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SizeMoterViewHolder, position: Int) {
        val currency = listSizeMoter[position]
        holder.bind(currency,onClickTypeCable)
    }

    override fun getItemCount(): Int {
        return listSizeMoter.size
    }
}