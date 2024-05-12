package com.sachin_singh_dighan.newsapp.ui.countryselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.databinding.CountrySelectionItemRowBinding
import com.sachin_singh_dighan.newsapp.utils.ItemClickListener

class CountrySelectionAdapter(
    private val countrySelectionList: ArrayList<CountrySelection>,
) : RecyclerView.Adapter<CountrySelectionAdapter.CountrySelectionViewHolder>() {

    lateinit var itemClickListener: ItemClickListener<String>

    class CountrySelectionViewHolder(
        private val binding: CountrySelectionItemRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(countrySelection: CountrySelection, itemClickListener: ItemClickListener<String>) {
            binding.countrySectionElement.text = countrySelection.countryName
            itemView.setOnClickListener {
                itemClickListener(bindingAdapterPosition, countrySelection.countryCode)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountrySelectionViewHolder {
        return CountrySelectionViewHolder(
            CountrySelectionItemRowBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ),
        )
    }

    override fun getItemCount(): Int = countrySelectionList.size

    override fun onBindViewHolder(holder: CountrySelectionViewHolder, position: Int) {
        holder.bind(countrySelectionList[position], itemClickListener)
    }

    fun addData(list: List<CountrySelection>) {
        countrySelectionList.addAll(list)
    }

}