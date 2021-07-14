package com.alw.temca.Adapter.AdapterCurrentRating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Interfaces.IOnClickAdapterListener
import com.alw.temca.Model.PhaseModel
import com.alw.temca.R
import com.alw.temca.ui.CurrentRating.PhaseInCurrentActivity
import kotlinx.android.synthetic.main.phase_list.view.*

class PhaseCurrentAdapter(
    var PhaseList: ArrayList<PhaseModel>, var onClickPhase: IOnClickAdapterListener): RecyclerView.Adapter<PhaseCurrentAdapter.PhaseViewHolder>() {
    class PhaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: PhaseModel, action: IOnClickAdapterListener){

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