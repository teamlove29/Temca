package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.startPanternModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.start_pattern_list.view.*

class StartPanternAdapter(private var ItemList : ArrayList<startPanternModel>,
                          private var onClickItem: InstallationonClickAdapterListener): RecyclerView.Adapter<StartPanternAdapter.StartPanternViewHolder>() {

    class StartPanternViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemList: startPanternModel,action : InstallationonClickAdapterListener){
            itemView.textViewStartPantern.text = itemList.name
            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartPanternViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.start_pattern_list,parent,false)
        return StartPanternViewHolder(view)
    }

    override fun onBindViewHolder(holder: StartPanternViewHolder, position: Int) {
        val currency = ItemList[position]
        holder.bind(currency,onClickItem)
    }

    override fun getItemCount(): Int {
        return ItemList.size
    }

}