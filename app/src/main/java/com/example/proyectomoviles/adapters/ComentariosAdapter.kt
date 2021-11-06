package com.example.proyectomoviles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.ListItemComentariosBinding
import com.example.proyectomoviles.models.Comment

class ComentariosAdapter() : RecyclerView.Adapter<ComentariosAdapter.CommentViewHolder>() {

    class CommentViewHolder(val viewDataBinding: ListItemComentariosBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_comentarios
        }
    }

    var comments :List<Comment> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val withDataBinding: ListItemComentariosBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CommentViewHolder.LAYOUT,
            parent,
            false)
        return CommentViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.comment = comments[position]
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}