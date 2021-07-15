package com.alw.temca.Adapter.AdapterInRailsCable


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.temca.Interfaces.onClickInAdapter
import com.alw.temca.Model.InstallationModel
import com.alw.temca.Model.InstallationModelInTransformer
import com.alw.temca.R
import kotlinx.android.synthetic.main.installation_list.view.*


class InstallationInRailsCableAdapter(private var InstallList: ArrayList<InstallationModel>, private var onClickInstall: onClickInAdapter)
    : RecyclerView.Adapter<InstallationInRailsCableAdapter.InstallViewHolder>() {

    class InstallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemList: InstallationModel, action: onClickInAdapter){
            itemView.imageViewGroup.setImageDrawable(itemList.image)
            itemView.textViewInstallationTitle.text = itemList.title
            itemView.textViewInstallation.text = itemList.des

            itemView.setOnClickListener {
                action.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstallViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.installation_list, parent, false)
        return InstallViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstallViewHolder, position: Int) {
        val currency = InstallList[position]
        holder.bind(currency, onClickInstall)

    }

    override fun getItemCount(): Int {
        return InstallList.size
    }
}

