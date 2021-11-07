package com.example.proyectomoviles.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.databinding.ListItemAlbumesBinding
import com.squareup.picasso.Picasso
import com.example.proyectomoviles.ui.AlbumesFragmentDirections

class AlbumesAdapter() : RecyclerView.Adapter<AlbumesAdapter.AlbumViewHolder>(), Filterable {

    private var lastQuery: String = ""

    class AlbumViewHolder(val viewDataBinding: ListItemAlbumesBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_albumes
        }
    }

    var albums :List<Album> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            albumsFiltered = value
            this.filter
            notifyDataSetChanged()
        }

    var albumsFiltered: List<Album> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val withDataBinding: ListItemAlbumesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumViewHolder.LAYOUT,
            parent,
            false)
        return AlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = albumsFiltered[position]
            Picasso.get()
                .load(it.album!!.cover)
                .placeholder(R.drawable.ic_album)
                .error(R.drawable.ic_artist)
                .into(it.albumItemCoverIv)
        }

        holder.viewDataBinding.root.setOnClickListener {
            val action = AlbumesFragmentDirections.actionAlbumesFragmentToAlbumFragment(albums[position].id)
            // Navigate using that action
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return albumsFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                lastQuery = constraint?.toString()?.lowercase() ?: ""
                val charString = lastQuery
                if (charString.isEmpty()) albumsFiltered = albums else {
                    val filteredList = ArrayList<Album>()
                    albums
                        .filter {
                            (it.name.lowercase().contains(lastQuery))
                        }
                        .forEach { filteredList.add(it) }
                    albumsFiltered = filteredList
                }
                return FilterResults().apply { values = albumsFiltered }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                albumsFiltered = if (results?.values == null)
                    emptyList()
                else
                    results.values as List<Album>
                    notifyDataSetChanged()
            }
        }
    }

}