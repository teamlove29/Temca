package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.PhaseModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.phase_list.view.*

interface onClickAdapterListener{
    fun onClick(postion: Int,value:Int)
}

class PhaseAdapter(var PhaseList : ArrayList<PhaseModel>,var onClickPhase:onClickAdapterListener): RecyclerView.Adapter<PhaseAdapter.PhaseViewHolder>() {
    class PhaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: PhaseModel, action: onClickAdapterListener){
            itemView.textViewPhaseList.text = "${itemList.phase} ${itemList.unit}"

            itemView.setOnClickListener {
                action.onClick(adapterPosition,itemList.phase)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.phase_list,parent,false)
        return PhaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhaseViewHolder, position: Int) {
        val currency = PhaseList[position]
        holder.bind(currency,onClickPhase)

    }

    override fun getItemCount(): Int {
        return PhaseList.size
    }
}