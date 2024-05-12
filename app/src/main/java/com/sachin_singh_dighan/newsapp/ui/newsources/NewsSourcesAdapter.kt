package com.sachin_singh_dighan.newsapp.ui.newsources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import com.sachin_singh_dighan.newsapp.databinding.NewSourceItemRowBinding
import com.sachin_singh_dighan.newsapp.utils.ItemClickListener

class NewsSourcesAdapter(
    private val newResourceList: ArrayList<Sources>,
) : RecyclerView.Adapter<NewsSourcesAdapter.NewSourceViewHolder>() {

    lateinit var itemClickListener: ItemClickListener<Sources>

    class NewSourceViewHolder(
        private val binding: NewSourceItemRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sources: Sources, itemClickListener: ItemClickListener<Sources>) {
            binding.newSourceElement.text = sources.name
            itemView.setOnClickListener { itemClickListener(bindingAdapterPosition, sources) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewSourceViewHolder {
        return NewSourceViewHolder(
            NewSourceItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun getItemCount(): Int = newResourceList.size

    override fun onBindViewHolder(holder: NewSourceViewHolder, position: Int) {
        holder.bind(newResourceList[position], itemClickListener)
    }

    fun addData(list: List<Sources>) {
        newResourceList.addAll(list)
    }
}