package com.example.tamanpempek.ui.adapter.seller.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tamanpempek.model.ProductModel
import java.io.Serializable

class SectionPagerAdapterSeller(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var category1: List<ProductModel> = emptyList()
    var category2: List<ProductModel> = emptyList()
    var category3: List<ProductModel> = emptyList()
    override fun createFragment(position: Int): Fragment {
        val fragment = ProductFragmentSeller()
        fragment.arguments = Bundle().apply {
            putInt(ProductFragmentSeller.ARG_POSITION, position)
            putSerializable(ProductFragmentSeller.ARG_CATEGORY1, category1 as Serializable)
            putSerializable(ProductFragmentSeller.ARG_CATEGORY2, category2 as Serializable)
            putSerializable(ProductFragmentSeller.ARG_CATEGORY3, category3 as Serializable)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}