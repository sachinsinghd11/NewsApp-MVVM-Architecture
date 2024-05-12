package com.sachin_singh_dighan.newsapp.ui.mainscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.databinding.MainActivityItemRowBinding
import com.sachin_singh_dighan.newsapp.utils.ItemClickListener

class MainActivityAdapter(
    private val mainSectionList: ArrayList<MainSection>,
) : RecyclerView.Adapter<MainActivityAdapter.MainSectionViewHolder>() {

    lateinit var itemClickListener: ItemClickListener<MainSection>

    class MainSectionViewHolder(
        private val binding: MainActivityItemRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mainSection: MainSection, itemClickListener: ItemClickListener<MainSection>) {
            binding.mainSectionElement.text = mainSection.sectionName
            itemView.setOnClickListener { itemClickListener(bindingAdapterPosition, mainSection) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainSectionViewHolder {
        return MainSectionViewHolder(
            MainActivityItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun getItemCount(): Int = mainSectionList.size

    override fun onBindViewHolder(holder: MainSectionViewHolder, position: Int) {
        holder.bind(mainSectionList[position], itemClickListener)
    }

    fun addData(list: List<MainSection>) {
        mainSectionList.clear()
        mainSectionList.addAll(list)
    }

}