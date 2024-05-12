package com.sachin_singh_dighan.newsapp.ui.languageselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.R
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.databinding.LanguageSelectionItemRowBinding
import com.sachin_singh_dighan.newsapp.utils.ItemClickListener

class LanguageSelectionAdapter(
    private val languageDataList: ArrayList<LanguageData>,
) : RecyclerView.Adapter<LanguageSelectionAdapter.LanguageSelectionViewHolder>() {

    lateinit var itemClickListener: ItemClickListener<String>

    class LanguageSelectionViewHolder(
        private val binding: LanguageSelectionItemRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(languageData: LanguageData, itemClickListener: ItemClickListener<String>) {
            binding.languageSectionElement.text = languageData.languageName
            itemView.setOnClickListener {
                binding.languageSectionElement.setBackgroundResource(R.color.selected_language)
                itemClickListener(bindingAdapterPosition, languageData.languageCode)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageSelectionViewHolder {
        return LanguageSelectionViewHolder(
            LanguageSelectionItemRowBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ),
        )
    }

    override fun getItemCount(): Int = languageDataList.size

    override fun onBindViewHolder(holder: LanguageSelectionViewHolder, position: Int) {
        holder.bind(languageDataList[position], itemClickListener)
    }

    fun addData(list: List<LanguageData>) {
        languageDataList.addAll(list)
    }

}