package com.coroutines.retrofit.kotlin.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coroutines.retrofit.kotlin.R
import com.coroutines.retrofit.kotlin.model.Posts

class PostsAdapter(private val postList: List<Posts>) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById<View>(R.id.title) as TextView
        var body: TextView = view.findViewById<View>(R.id.body) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (_, _, title, body) = postList[position]
        holder.title.text = title
        holder.body.text = body
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}