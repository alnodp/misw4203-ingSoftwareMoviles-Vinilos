package com.example.proyectomoviles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.ListItemCollectorBinding
import com.example.proyectomoviles.models.Collector
import com.squareup.picasso.Picasso

class CollectorAdapter() : RecyclerView.Adapter<CollectorAdapter.CollectorViewHolder>(), Filterable {
    class CollectorViewHolder(val viewDataBinding: ListItemCollectorBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_collector
        }
    }

    var collector :List<Collector> = emptyList()
        set(value) {
            field = value
            collectorsFiltered = value
            this.filter
            notifyDataSetChanged()
        }

    var collectorsFiltered: List<Collector> = emptyList()

    private var lastQuery: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val withDataBinding: ListItemCollectorBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT,
            parent,
            false)
        return CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collectorsFiltered[position]
            Picasso.get()
                .load(it.collector!!.image)
                .placeholder(R.drawable.ic_collectors)
                .into(it.ivCollectorImage);
        }

        holder.viewDataBinding.root.setOnClickListener {
            val text = "Navegar a Coleccionista con id " + collectorsFiltered[position].collectorId
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(holder.viewDataBinding.root.context, text, duration)
            toast.show()
        }
    }

    override fun getItemCount(): Int {
        return collectorsFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                lastQuery = constraint?.toString()?.lowercase() ?: ""
                val charString = lastQuery
                if (charString.isEmpty()) collectorsFiltered = collector else {
                    val filteredList = ArrayList<Collector>()
                    collector
                        .filter {
                            (it.name.lowercase().contains(lastQuery))
                        }
                        .forEach { filteredList.add(it) }
                    collectorsFiltered = filteredList
                }
                return FilterResults().apply { values = collectorsFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                collectorsFiltered = if (results?.values == null)
                    emptyList()
                else
                    results.values as List<Collector>
                notifyDataSetChanged()
            }
        }
    }
}