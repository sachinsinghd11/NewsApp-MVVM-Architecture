package com.sachin_singh_dighan.newsapp.ui.main_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.data.model.main_screen.MainSection
import com.sachin_singh_dighan.newsapp.databinding.MainActivityItemRowBinding

class MainActivityAdapter(
    private val mainSectionList: ArrayList<MainSection>
): RecyclerView.Adapter<MainActivityAdapter.MainSectionViewHolder>(){

    class MainSectionViewHolder(private val binding: MainActivityItemRowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(mainSection: MainSection){
            binding.mainSectionElement.text = mainSection.sectionName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainSectionViewHolder {
        return MainSectionViewHolder(MainActivityItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = mainSectionList.size

    override fun onBindViewHolder(holder: MainSectionViewHolder, position: Int) {
        holder.bind(mainSectionList[position])
    }

    fun addData(list: List<MainSection>) {
        mainSectionList.addAll(list)
    }

}