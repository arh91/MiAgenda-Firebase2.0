package com.castelao.DatosFirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomersAdapter (private val data: List<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<CustomersAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun accederDatosCliente(code: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_customers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.tvCode.text = item

        holder.itemView.setOnClickListener {
            listener.accederDatosCliente(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCode: TextView = itemView.findViewById(R.id.tvCode)
    }
}