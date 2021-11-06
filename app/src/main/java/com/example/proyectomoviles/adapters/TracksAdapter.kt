package com.example.proyectomoviles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.ListItemTracksBinding
import com.example.proyectomoviles.models.Track

class TracksAdapter() : RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {

    class TrackViewHolder(val viewDataBinding: ListItemTracksBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_tracks
        }
    }

    var tracks :List<Track> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val withDataBinding: ListItemTracksBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            TrackViewHolder.LAYOUT,
            parent,
            false)
        return TrackViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.track = tracks[position]
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}