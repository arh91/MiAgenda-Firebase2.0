package com.castelao.DatosFirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SuppliersAdapter (private val data: List<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<SuppliersAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun accederDatosProveedor(code: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_suppliers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.tvCode.text = item

        holder.itemView.setOnClickListener {
            listener.accederDatosProveedor(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCode: TextView = itemView.findViewById(R.id.tvCode)
    }
}