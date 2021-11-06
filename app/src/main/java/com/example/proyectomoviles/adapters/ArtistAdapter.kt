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
import com.example.proyectomoviles.models.Artist
import com.example.proyectomoviles.databinding.ListItemArtistsBinding
import com.squareup.picasso.Picasso

class ArtistAdapter() : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>(), Filterable {

    class ArtistViewHolder(val viewDataBinding: ListItemArtistsBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_artists
        }
    }

    var artists :List<Artist> = emptyList()
        set(value) {
            field = value
            artistsFiltered = value
            this.filter
            notifyDataSetChanged()
        }

    var artistsFiltered: List<Artist> = emptyList()

    private var lastQuery: String = ""

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
                .into(it.ivArtistImage);
        }

        holder.viewDataBinding.root.setOnClickListener {
            val text = "Navegar a Artista con id " + artistsFiltered[position].id
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(holder.viewDataBinding.root.context, text, duration)
            toast.show()
        }
    }

    override fun getItemCount(): Int {
        return artistsFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                lastQuery = constraint?.toString()?.lowercase() ?: ""
                val charString = lastQuery
                if (charString.isEmpty()) artistsFiltered = artists else {
                    val filteredList = ArrayList<Artist>()
                    artists
                        .filter {
                            (it.name.lowercase().contains(lastQuery))
                        }
                        .forEach { filteredList.add(it) }
                    artistsFiltered = filteredList
                }
                return FilterResults().apply { values = artistsFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                artistsFiltered = if (results?.values == null)
                    emptyList()
                else
                    results.values as List<Artist>
                notifyDataSetChanged()
            }
        }
    }


}