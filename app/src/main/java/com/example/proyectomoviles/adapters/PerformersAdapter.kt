package com.example.proyectomoviles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.ListItemPerformersBinding
import com.example.proyectomoviles.models.Performer
import com.squareup.picasso.Picasso

class PerformersAdapter() : RecyclerView.Adapter<PerformersAdapter.PerformerViewHolder>() {

    class PerformerViewHolder(val viewDataBinding: ListItemPerformersBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_performers
        }
    }

    var performers :List<Performer> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
        val withDataBinding: ListItemPerformersBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            PerformerViewHolder.LAYOUT,
            parent,
            false)
        return PerformerViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.performer = performers[position]
            Picasso.get()
                .load(it.performer!!.image)
                .placeholder(R.drawable.ic_artist)
                .error(R.drawable.ic_artist)
                .into(it.pdImage);

            it.performer = performers[position]
        }
    }

    override fun getItemCount(): Int {
        return performers.size
    }
}