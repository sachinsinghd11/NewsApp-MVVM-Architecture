package com.sachin_singh_dighan.newsapp.ui.mainscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.databinding.MainActivityItemRowBinding

class MainActivityAdapter(
    private val mainSectionList: ArrayList<MainSection>,
    private val activity: MainActivity,
): RecyclerView.Adapter<MainActivityAdapter.MainSectionViewHolder>(){

    var onItemClickCallback: ((MainSection) -> Unit)? = null

    class MainSectionViewHolder(
        private val binding: MainActivityItemRowBinding,
        private val activity: MainActivity
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(mainSection: MainSection){
            binding.mainSectionElement.text = mainSection.sectionName
            itemView.setOnClickListener { onMenuItemClick(mainSection.sectionName) }
        }
        private fun onMenuItemClick(mainSection: String ){
            activity.onMainSectionItemClick(mainSection)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainSectionViewHolder {
        return MainSectionViewHolder(MainActivityItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false),activity)
    }

    override fun getItemCount(): Int = mainSectionList.size

    override fun onBindViewHolder(holder: MainSectionViewHolder, position: Int) {
        holder.bind(mainSectionList[position])
    }

    fun addData(list: List<MainSection>) {
        mainSectionList.clear()
        mainSectionList.addAll(list)
    }

}