package com.sachin_singh_dighan.newsapp.ui.languageselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.R
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.databinding.LanguageSelectionItemRowBinding

class LanguageSelectionAdapter (
    private val languageDataList: ArrayList<LanguageData>,
    private val activity: LanguageSelectionActivity,
): RecyclerView.Adapter<LanguageSelectionAdapter.LanguageSelectionViewHolder>(){

    var onItemClickCallback: ((MainSection) -> Unit)? = null

    class LanguageSelectionViewHolder(
        private val binding: LanguageSelectionItemRowBinding,
        private val activity: LanguageSelectionActivity
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(languageData: LanguageData){
            binding.languageSectionElement.text = languageData.languageName
            itemView.setOnClickListener { onLanguageItemClick(languageData.languageName, languageData.languageCode) }
        }
        private fun onLanguageItemClick(countryName: String, countryCode: String ){
            binding.languageSectionElement.setBackgroundResource(R.color.selected_language)
            activity.onLanguageClick(countryCode)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageSelectionViewHolder {
        return LanguageSelectionViewHolder(LanguageSelectionItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false),activity)
    }

    override fun getItemCount(): Int = languageDataList.size

    override fun onBindViewHolder(holder: LanguageSelectionViewHolder, position: Int) {
        holder.bind(languageDataList[position])
    }

    fun addData(list: List<LanguageData>) {
        languageDataList.addAll(list)
    }

}