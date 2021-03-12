package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.CircuitModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.circuit_list.view.*

class CircuitAdapter(var listCircuit : ArrayList<CircuitModel>,var onClick : InstallationonClickAdapterListener) : RecyclerView.Adapter<CircuitAdapter.CircuitViewHolder>() {
    class CircuitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: CircuitModel, action: InstallationonClickAdapterListener){
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