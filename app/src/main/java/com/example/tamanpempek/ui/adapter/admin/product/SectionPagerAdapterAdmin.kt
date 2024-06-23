package com.example.tamanpempek.ui.adapter.admin.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.ui.adapter.user.product.ProductFragmentUser
import java.io.Serializable

class SectionPagerAdapterAdmin(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var category1: List<ProductModel> = emptyList()
    var category2: List<ProductModel> = emptyList()
    var category3: List<ProductModel> = emptyList()
    override fun createFragment(position: Int): Fragment {
        val fragment = ProductFragmentAdmin()
        fragment.arguments = Bundle().apply {
            putInt(ProductFragmentAdmin.ARG_POSITION, position)
            putSerializable(ProductFragmentAdmin.ARG_CATEGORY1, category1 as Serializable)
            putSerializable(ProductFragmentAdmin.ARG_CATEGORY2, category2 as Serializable)
            putSerializable(ProductFragmentAdmin.ARG_CATEGORY3, category3 as Serializable)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}