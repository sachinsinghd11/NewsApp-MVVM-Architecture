package com.sachin_singh_dighan.newsapp.ui.countryselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.databinding.CountrySelectionItemRowBinding

class CountrySelectionAdapter(
    private val countrySelectionList: ArrayList<CountrySelection>,
    private val activity: CountrySelectionActivity,
): RecyclerView.Adapter<CountrySelectionAdapter.CountrySelectionViewHolder>(){

    var onItemClickCallback: ((MainSection) -> Unit)? = null

    class CountrySelectionViewHolder(
        private val binding: CountrySelectionItemRowBinding,
        private val activity: CountrySelectionActivity
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(countrySelection: CountrySelection){
            binding.countrySectionElement.text = countrySelection.countryName
            itemView.setOnClickListener { onCountryItemClick(countrySelection.countryName, countrySelection.countryCode) }
        }
        private fun onCountryItemClick(countryName: String, countryCode: String ){
            activity.onCountryClick(countryCode)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountrySelectionViewHolder {
        return CountrySelectionViewHolder(CountrySelectionItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false),activity)
    }

    override fun getItemCount(): Int = countrySelectionList.size

    override fun onBindViewHolder(holder: CountrySelectionViewHolder, position: Int) {
        holder.bind(countrySelectionList[position])
    }

    fun addData(list: List<CountrySelection>) {
        countrySelectionList.addAll(list)
    }

}