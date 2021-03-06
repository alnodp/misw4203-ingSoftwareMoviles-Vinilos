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
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.databinding.ListItemAlbumesBinding
import com.example.proyectomoviles.ui.AlbumesFragmentDirections

class AlbumesAdapter() : RecyclerView.Adapter<AlbumesAdapter.AlbumViewHolder>(), Filterable {

    private var lastQuery: String = ""

    class AlbumViewHolder(val viewDataBinding: ListItemAlbumesBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_albumes
        }

        fun bind(album: Album) {
            Glide.with(itemView)
                .load(album.cover.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions().placeholder(R.drawable.ic_album)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_artist)
                ).into(viewDataBinding.albumItemCoverIv)
        }
    }

    var albums :List<Album> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            albumsFiltered = value
            notifyDataSetChanged()
            this.filter.filter("")
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
        }
        holder.bind(albumsFiltered[position])

        holder.viewDataBinding.root.setOnClickListener {
            val action = AlbumesFragmentDirections.actionAlbumesFragmentToAlbumFragment(albumsFiltered[position].id)
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
                (if (results?.values == null)
                    emptyList()
                else
                    results.values as List<Album>).also { albumsFiltered = it }
                    notifyDataSetChanged()
            }
        }
    }

}