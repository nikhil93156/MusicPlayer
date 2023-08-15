package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class Adapter(private val context: Context,private val songslist:ArrayList<AudioModel>): RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val View=LayoutInflater.from(parent.context)
            .inflate(R.layout.musiclist,parent,false)
        return ViewHolder(View)
    }

    override fun getItemCount(): Int {
        return songslist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val AudioModel=songslist[position]
       holder.title.text=AudioModel.title
        holder.duration.text= formatDuration(AudioModel.duration)
        holder.itemView.setOnClickListener{
           val i= Intent(context,Playlist::class.java)
            i.putExtra("index",position)
           ContextCompat.startActivity(context,i,null)
        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title:TextView=itemView.findViewById(R.id.tv)
        val image:ImageView=itemView.findViewById(R.id.icon)
        val duration:TextView=itemView.findViewById(R.id.duration)
    }
}