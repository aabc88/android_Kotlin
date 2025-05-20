package com.example.ch8.section2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ch8.databinding.ItemBinding

class MyViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
class MyAdapter(val context: Context, val datas: MutableList<ItemModel>?) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder = MyViewHolder(ItemBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val binding = holder.binding
        val model = datas!![position]

        binding.itemTvTitle.text = model.title
        binding.itemTvDesc.text = model.description
        binding.itemTvTime.text = "${model.author} At ${model.publishedAt}"
        Glide.with(context)
            .load(model.urlToImage)
            .into(binding.itemIvImage)

    }

    override fun getItemCount() = datas?.size ?: 0

}