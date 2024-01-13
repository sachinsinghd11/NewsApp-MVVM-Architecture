package com.sachin_singh_dighan.newsapp.ui.language_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.data.model.country_selection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.model.language_selection.LanguageData
import com.sachin_singh_dighan.newsapp.data.model.main_screen.MainSection
import com.sachin_singh_dighan.newsapp.databinding.CountrySelectionItemRowBinding
import com.sachin_singh_dighan.newsapp.databinding.LanguageSelectionItemRowBinding
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionActivity
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionAdapter

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