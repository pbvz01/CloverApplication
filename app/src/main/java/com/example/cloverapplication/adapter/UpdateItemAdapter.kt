package com.example.cloverapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cloverapplication.database.entity.UpdateItem
import com.example.cloverapplication.databinding.RecyclerviewItemBinding

class UpdateItemAdapter(
    private val data: List<UpdateItem>
) : RecyclerView.Adapter<UpdateItemAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RecyclerviewItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(updateItem: UpdateItem) {
            binding.itemList = updateItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater = LayoutInflater.from(parent.context)

        val listItemBinding = RecyclerviewItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val updateItem = data[position]
        holder.bind(updateItem)
    }

    override fun getItemCount(): Int = data.size

}