package com.alw.temca.Adapter.AdapterInRails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.SizeConduitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.type_cable_list.view.*


class SizeConduitAdapter(var listSizeConduit : ArrayList<SizeConduitModel>, var onClickTypeCable:onClickInAdapter) : RecyclerView.Adapter<SizeConduitAdapter.SizeViewHolder>(){
    class SizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: SizeConduitModel, action: onClickInAdapter){
            itemView.textViewTypeCableList.text = itemList.name

            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.type_cable_list,parent,false)
        return SizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val currency = listSizeConduit[position]
        holder.bind(currency,onClickTypeCable)
    }

    override fun getItemCount(): Int {
        return listSizeConduit.size
    }





}