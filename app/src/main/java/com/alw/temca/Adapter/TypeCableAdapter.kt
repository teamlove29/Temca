package com.alw.temca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.TypeCableModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.type_cable_list.view.*


class TypeCableAdapter(var TypeCableList : ArrayList<TypeCableModel>, var onClickTypeCable:InstallationonClickAdapterListener): RecyclerView.Adapter<TypeCableAdapter.TypecableViewHolder>() {

    class TypecableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: TypeCableModel, action: InstallationonClickAdapterListener){
            itemView.textViewTypeCableList.text = itemList.name

            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypecableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.type_cable_list,parent,false)
        return TypecableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypecableViewHolder, position: Int) {
        val currency = TypeCableList[position]
        holder.bind(currency,onClickTypeCable)

    }

    override fun getItemCount(): Int {
        return TypeCableList.size
    }
}
