package com.example.proyectomoviles.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.ListItemCollectorsBinding
import com.example.proyectomoviles.models.Collector
import com.example.proyectomoviles.ui.CollectorsFragmentDirections

class CollectorsAdapter() : RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>(), Filterable {
    class CollectorViewHolder(val viewDataBinding: ListItemCollectorsBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_collectors
        }
    }

    var collector :List<Collector> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            collectorsFiltered = value
            this.filter
            notifyDataSetChanged()
        }

    var collectorsFiltered: List<Collector> = emptyList()

    private var lastQuery: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val withDataBinding: ListItemCollectorsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT,
            parent,
            false)
        return CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collectorsFiltered[position]
        }

        holder.viewDataBinding.root.setOnClickListener {
            val action = CollectorsFragmentDirections.actionCollectorsFragmentToCollectorFragment(collectorsFiltered[position].collectorId)
            // Navigate using that action
            holder.viewDataBinding.root.findNavController().navigate(action)
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

            @SuppressLint("NotifyDataSetChanged")
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