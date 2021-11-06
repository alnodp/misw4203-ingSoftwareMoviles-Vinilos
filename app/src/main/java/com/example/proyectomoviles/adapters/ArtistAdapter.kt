package com.example.proyectomoviles.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Artist
import com.example.proyectomoviles.databinding.ListItemArtistsBinding
import com.squareup.picasso.Picasso

class ArtistAdapter() : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>(){

    class ArtistViewHolder(val viewDataBinding: ListItemArtistsBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_albumes
        }
    }

    var artists :List<Artist> = emptyList()
        set(value) {
            field = value
            artistsFiltered = value
            notifyDataSetChanged()
        }

    var artistsFiltered: List<Artist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val withDataBinding: ListItemArtistsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistViewHolder.LAYOUT,
            parent,
            false)
        return ArtistViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.artist = artistsFiltered[position]
            Picasso.get()
                .load(it.artist!!.image)
                .placeholder(R.drawable.ic_artist)
                .error(R.drawable.ic_artist)
                .into(it.ivArtistImage);
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }


}