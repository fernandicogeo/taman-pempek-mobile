package com.example.tamanpempek.ui.adapter.admin.users

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tamanpempek.model.UserModel
import java.io.Serializable

class SectionPagerAdapterUserAdmin(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var category1: List<UserModel> = emptyList()
    var category2: List<UserModel> = emptyList()
    override fun createFragment(position: Int): Fragment {
        val fragment = UserFragmentAdmin()
        fragment.arguments = Bundle().apply {
            putInt(UserFragmentAdmin.ARG_POSITION, position)
            putSerializable(UserFragmentAdmin.ARG_CATEGORY1, category1 as Serializable)
            putSerializable(UserFragmentAdmin.ARG_CATEGORY2, category2 as Serializable)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}