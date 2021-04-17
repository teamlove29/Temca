package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.InstallationModelInTransformer
import com.alw.temca.Model.MoterModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.unit_moter_list.view.*


class MoterAdapter(
    private var unitList: ArrayList<MoterModel>,
    private var onClickUnit: InstallationonClickAdapterListener) : RecyclerView.Adapter<MoterAdapter.MotorViewHolder>() {

    class MotorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemList: MoterModel, action: InstallationonClickAdapterListener){
            itemView.textViewUnitMoterList.text = "${itemList.name}"
            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.unit_moter_list, parent, false)
        return MotorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MotorViewHolder, position: Int) {
        val currency = unitList[position]
        holder.bind(currency, onClickUnit)
    }

    override fun getItemCount(): Int {
        return unitList.size
    }


}