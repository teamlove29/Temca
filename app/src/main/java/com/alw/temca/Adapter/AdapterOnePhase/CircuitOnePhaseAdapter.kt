package com.alw.temca.Adapter.AdapterOnePhase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.CircuitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.circuit_list.view.*

class CircuitOnePhaseAdapter(var listCircuit : ArrayList<CircuitModel>, var onClick : onClickInAdapter) : RecyclerView.Adapter<CircuitOnePhaseAdapter.CircuitViewHolder>() {
    class CircuitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: CircuitModel, action: onClickInAdapter){
            itemView.textViewCircuitList.text = itemList.name

            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircuitViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.circuit_list,parent,false)
        return CircuitViewHolder(view)
    }

    override fun onBindViewHolder(holder: CircuitViewHolder, position: Int) {
        val currency = listCircuit[position]
        holder.bind(currency,onClick)


    }

    override fun getItemCount(): Int {
       return listCircuit.size
    }
}