package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.MoterModel
import com.alw.temca.Model.PressureModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.unit_moter_list.view.*


class PressureAdapter(
    private var PressureList: ArrayList<PressureModel>,
    private var onClickUnit: InstallationonClickAdapterListener) : RecyclerView.Adapter<PressureAdapter.PressureViewHolder>() {

    class PressureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemList: PressureModel, action: InstallationonClickAdapterListener){
            itemView.textViewUnitMoterList.text = "${itemList.name}"
            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.unit_moter_list, parent, false)
        return PressureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PressureViewHolder, position: Int) {
        val currency = PressureList[position]
        holder.bind(currency, onClickUnit)
    }

    override fun getItemCount(): Int {
        return PressureList.size
    }

}