package com.sachin_singh_dighan.newsapp.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.databinding.TopHeadlineItemLayoutBinding
import com.sachin_singh_dighan.newsapp.utils.ItemClickListener

class NewsListAdapter(
    private val articleList: ArrayList<Any>
) : RecyclerView.Adapter<NewsListAdapter.DataViewHolder>() {

    lateinit var itemClickListener: ItemClickListener<Any>

    class DataViewHolder(private val binding: TopHeadlineItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, itemClickListener: ItemClickListener<Any>) {
            binding.textViewTitle.text = article.title
            binding.textViewDescription.text = article.description
            binding.textViewSource.text = article.source.name
            Glide.with(binding.imageViewBanner.context)
                .load(article.imageUrl)
                .into(binding.imageViewBanner)
            itemView.setOnClickListener {
                itemView.setOnClickListener { itemClickListener(bindingAdapterPosition, article) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            TopHeadlineItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun getItemCount(): Int = articleList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(articleList[position] as Article, itemClickListener)

    fun addData(list: List<Any>) {
        articleList.addAll(list)
    }

    fun replaceData(list: List<Article>) {
        articleList.clear()
        articleList.addAll(list)
    }
}