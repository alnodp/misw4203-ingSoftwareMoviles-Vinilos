package com.example.proyectomoviles.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Artist
import com.example.proyectomoviles.databinding.ListItemArtistsBinding
import com.example.proyectomoviles.ui.ArtistsFragmentDirections

class ArtistsAdapter() : RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>(), Filterable {

    class ArtistViewHolder(val viewDataBinding: ListItemArtistsBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_artists
        }

        fun bind(artist: Artist) {
            Glide.with(itemView)
                .load(artist.image.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions().placeholder(R.drawable.ic_artist)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_artist)
                ).into(viewDataBinding.ivArtistImage)
        }
    }

    var artists :List<Artist> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
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
        }
        holder.bind(artistsFiltered[position])

        holder.viewDataBinding.root.setOnClickListener {
            val action = ArtistsFragmentDirections.actionArtistsFragmentToArtistFragment(artistsFiltered[position].id)
            // Navigate using that action
            holder.viewDataBinding.root.findNavController().navigate(action)

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

            @SuppressLint("NotifyDataSetChanged")
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