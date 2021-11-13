package com.example.proyectomoviles.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.databinding.ListItemAlbumesBinding
import com.example.proyectomoviles.databinding.ListItemAlbumesContentTabBinding
import com.squareup.picasso.Picasso

class CollectorAlbumsAdapter() : RecyclerView.Adapter<CollectorAlbumsAdapter.CollectorAlbumsViewHolder>() {

    class CollectorAlbumsViewHolder(val viewDataBinding: ListItemAlbumesContentTabBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_albumes_content_tab
        }
    }

    var albums :List<Album> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAlbumsViewHolder {
        val withDataBinding: ListItemAlbumesContentTabBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorAlbumsViewHolder.LAYOUT,
            parent,
            false)
        return CollectorAlbumsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorAlbumsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = albums[position]
            Picasso.get()
                .load(it.album!!.cover)
                .placeholder(R.drawable.ic_album)
                .error(R.drawable.ic_artist)
                .into(it.adCover)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }
}