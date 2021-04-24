package com.alw.temca.Adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.TransformerSizeModal
import com.alw.temca.Model.TransformerSizeModalResult
import com.alw.temca.R
import kotlinx.android.synthetic.main.phase_list.view.*
import kotlinx.android.synthetic.main.result_transformer_list.view.*


class TransformerSizeAdapter(var ItemList : ArrayList<TransformerSizeModal>,
                             var onClick:InstallationonClickAdapterListener): RecyclerView.Adapter<TransformerSizeAdapter.TransformerSizeViewHolder>() {

    class TransformerSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: TransformerSizeModal, action: InstallationonClickAdapterListener){
            itemView.textViewPhaseList.text = itemList.name

            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformerSizeViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.phase_list,parent,false)
        return TransformerSizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransformerSizeViewHolder, position: Int) {
        val currency = ItemList[position]
        holder.bind(currency,onClick)
    }

    override fun getItemCount(): Int {
        return ItemList.size
    }

}


class TransformerSizeAdapterResult(var ItemList : ArrayList<TransformerSizeModalResult> ): RecyclerView.Adapter<TransformerSizeAdapterResult.TransformerSizeViewHolder>() {

    class TransformerSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: TransformerSizeModalResult){

            itemView.TextViewTransformerSizeResult1.text = Html.fromHtml("${itemList.cableSize.replace("mm", "mm<sup><small><small>2</small></small></sup>")}")
            itemView.TextViewTransformerSizeResult3.text = "${itemList.conduitSize}mm."
            itemView.TextViewTransformerSizeResult5.text = "-V(%)"
            itemView.TextViewTransformerSizeResult5.text = itemList.pressureDrop
            itemView.TextViewTransformerSizeResult6.text = "(แรงดันอ้างอิง 230V)"
//            itemView.TextViewTransformerSizeResult6.text = "(แรงดันอ้างอิง ${itemList.referenceVoltage}V)"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformerSizeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_transformer_list,parent,false)
        return TransformerSizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransformerSizeViewHolder, position: Int) {
        val currency = ItemList[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return ItemList.size
    }

}
