package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.RailSizeModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.rail_size_list.view.*


class WireSizeAdapter(var listCircuit : ArrayList<RailSizeModel>) : RecyclerView.Adapter<WireSizeAdapter.WireSizeViewHolder>() {
    class WireSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: RailSizeModel){
            itemView.TextViewRailSizeResult.text = itemList.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WireSizeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rail_size_list,parent,false)
        return WireSizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WireSizeViewHolder, position: Int) {
        val currency = listCircuit[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return listCircuit.size
    }
}