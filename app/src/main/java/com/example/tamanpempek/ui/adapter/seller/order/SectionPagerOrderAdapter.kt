package com.example.tamanpempek.ui.adapter.seller.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tamanpempek.model.CartModel
import java.io.Serializable

class SectionPagerOrderAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var orderWaiting: List<CartModel> = emptyList()
    var orderSent: List<CartModel> = emptyList()
    var orderFinished: List<CartModel> = emptyList()
    override fun createFragment(position: Int): Fragment {
        val fragment = OrderSellerFragment()
        fragment.arguments = Bundle().apply {
            putInt(OrderSellerFragment.ARG_POSITION, position)
            putSerializable(OrderSellerFragment.ARG_CATEGORY2, orderWaiting as Serializable)
            putSerializable(OrderSellerFragment.ARG_CATEGORY3, orderSent as Serializable)
            putSerializable(OrderSellerFragment.ARG_CATEGORY4, orderFinished as Serializable)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}