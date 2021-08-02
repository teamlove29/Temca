package com.alw.temca.Adapter.AdapterInPipe

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.SizeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.type_cable_list.view.*


class SizeCableAdapter(var listSizeCable : ArrayList<SizeCableModel>, var onClickTypeCable: onClickInAdapter) : RecyclerView.Adapter<SizeCableAdapter.SizeViewHolder>(){

    class SizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: SizeCableModel, action: onClickInAdapter){

            itemView.textViewTypeCableList.text = Html.fromHtml(itemList.name!!.replace("mm2","mm<sup><small>2</small></sup>"))
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
        val currency = listSizeCable[position]
        holder.bind(currency,onClickTypeCable)
    }

    override fun getItemCount(): Int {
        return listSizeCable.size
    }
}