package com.example.proyectomoviles.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.ListItemPrizesBinding
import com.example.proyectomoviles.models.Prize

class ArtistPrizesAdapter() : RecyclerView.Adapter<ArtistPrizesAdapter.ArtistPrizesViewHolder>() {

    class ArtistPrizesViewHolder(val viewDataBinding: ListItemPrizesBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_prizes
        }
    }

    var prizes :List<Prize> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistPrizesViewHolder {
        val withDataBinding: ListItemPrizesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistPrizesViewHolder.LAYOUT,
            parent,
            false)
        return ArtistPrizesViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistPrizesViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.prize = prizes[position]
        }
    }

    override fun getItemCount(): Int {
        return prizes.size
    }
}