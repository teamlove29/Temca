package com.alw.temca.Adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Model.RailSizeModel
import com.alw.temca.R
import kotlinx.android.synthetic.main.rail_size_list.view.*


class WireSizeAdapter(var listCircuit : ArrayList<RailSizeModel>) : RecyclerView.Adapter<WireSizeAdapter.WireSizeViewHolder>() {
    class WireSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemList: RailSizeModel){
            itemView.textViewResultWireSizeInList.text = Html.fromHtml("${itemList.wireSize.replace("mm","mm<sup><small><small>2</small></small></sup>")}")
            itemView.textViewResultWireGround.text = Html.fromHtml("${itemList.groundSize} G")
            itemView.textViewResultRailSize.text = "${itemList.railSize} mm"
            itemView.textViewReferenceVoltage.text = "(แรงดันอ้างอิง ${itemList.refPressure})"
            itemView.textResultPreessureInList.text = itemList.resultPressure

            if(itemList.resultPressure == "0.00 V (0.00%)"){
                itemView.textViewPreessureInList.visibility = View.GONE
                itemView.textViewReferenceVoltage.visibility = View.GONE
                itemView.textResultPreessureInList.visibility = View.GONE
            }else{
                itemView.textViewPreessureInList.visibility = View.VISIBLE
                itemView.textViewReferenceVoltage.visibility = View.VISIBLE
                itemView.textResultPreessureInList.visibility = View.VISIBLE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WireSizeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rail_size_list,parent,false)
        return WireSizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WireSizeViewHolder, position: Int) {
        val currency = listCircuit[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return listCircuit.size
    }
}