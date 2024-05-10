package com.sachin_singh_dighan.newsapp.ui.newsources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import com.sachin_singh_dighan.newsapp.databinding.NewSourceItemRowBinding

class NewsSourcesAdapter (
    private val newResourceList: ArrayList<Sources>,
    private val newSourceActivity: NewsSourcesActivity,
): RecyclerView.Adapter<NewsSourcesAdapter.NewSourceViewHolder>() {

    class NewSourceViewHolder(
        private val binding: NewSourceItemRowBinding,
        private val newSourceActivity: NewsSourcesActivity
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sources: Sources) {
            binding.newSourceElement.text = sources.name
            itemView.setOnClickListener { onMenuItemClick(sources) }
        }

        private fun onMenuItemClick(source: Sources) {
            newSourceActivity.onNewSourceItemClick(source)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewSourceViewHolder {
        return NewSourceViewHolder(
            NewSourceItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), newSourceActivity
        )
    }

    override fun getItemCount(): Int = newResourceList.size

    override fun onBindViewHolder(holder: NewSourceViewHolder, position: Int) {
        holder.bind(newResourceList[position])
    }

    fun addData(list: List<Sources>) {
        newResourceList.addAll(list)
    }
}